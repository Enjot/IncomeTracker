package ui

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.db.SqlDriver
import cafe.adriel.voyager.core.model.ScreenModel
import com.example.Database
import com.example.sqldelight.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import java.time.LocalDate
import java.time.YearMonth

class HomeScreenModel(
    driver: SqlDriver
) : ScreenModel {
    /* ---------- SQL DELIGHT ---------- */
    private val database = Database(driver)
    private val categoryQueries = database.categoryQueries
    private val spendingQueries = database.spendingQueries
    private val limitQueries = database.limitQueries

    /* ---------- STATE ---------- */
    private var _spendings = spendingQueries.selectAll().asFlow().mapToList(Dispatchers.IO)
    private var _categories = categoryQueries.selectAll().asFlow().mapToList(Dispatchers.IO)
    val categories: Flow<List<Category>>
        get() = _categories
    private var _limits = limitQueries.selectAll().asFlow().mapToList(Dispatchers.IO)
    
    var sortCategory = MutableStateFlow(CategorySortType.NAME_INC)

    var filterLimitByDate = MutableStateFlow(DateFilter())
    var statisticFilter = MutableStateFlow(DateFilter())
    
    var categoriesSummary = combine(_spendings, _categories, sortCategory) { spending, category, sortType ->
        val mapOfCategories: MutableMap<String, Pair<Int, Double>> = mutableMapOf()
        spending.forEach {
            if (!mapOfCategories.containsKey(it.category)) {
                mapOfCategories[it.category] = Pair(1, it.amount)
            } else {
                mapOfCategories[it.category] = Pair(
                    mapOfCategories.getValue(it.category).first + 1,
                    mapOfCategories.getValue(it.category).second + it.amount
                )
            }
        }
        category.forEach {
            if (!mapOfCategories.containsKey(it.name) && it.isVisible.toInt() == 1)
                mapOfCategories[it.name] = Pair(0, 0.00)
        }
        val listOfCategories = mapOfCategories.entries.toList()
        return@combine when (sortType) {
            CategorySortType.NAME_INC -> listOfCategories.sortedBy { it.key.uppercase() }
            CategorySortType.NAME_DEC -> listOfCategories.sortedBy { it.key.uppercase() }.reversed()
            CategorySortType.SUM_INC -> listOfCategories.sortedBy { it.value.first }
            CategorySortType.SUM_DEC -> listOfCategories.sortedBy { it.value.first }.reversed()
            CategorySortType.AMOUNT_INC -> listOfCategories.sortedBy { it.value.second }
            CategorySortType.AMOUNT_DEC -> listOfCategories.sortedBy { it.value.second }.reversed()
        }
    }

    var limits = combine(_spendings, _limits, filterLimitByDate) { spendings, limits, filter ->
        val listOfCurrentLimits: MutableList<CurrentLimit> = mutableListOf()
        limits.forEach { limit ->
            var currentAmount = 0.0
            spendings.forEach { spending ->
                if (spending.category == limit.category && spending.date.startsWith(limit.date)) {
                    currentAmount += spending.amount
                }
            }
            listOfCurrentLimits.add(
                CurrentLimit(limit.category, limit.date, currentAmount, limit.limitAmount)
            )
        }
        val singleDigitMonthFilterLogic: (CurrentLimit, Int) -> Boolean = { limit, month ->
            limit.currentDate.startsWith("${filter.selectedYear}-0$month")
        }
        return@combine when (filter.isFiltered) {
            false -> listOfCurrentLimits
            true -> listOfCurrentLimits.filter { limit ->
                if (filter.selectedMonth in 0..9) {
                    singleDigitMonthFilterLogic(limit, filter.selectedMonth)
                } else {
                    limit.currentDate.startsWith("${filter.selectedYear}-${filter.selectedMonth}")
                }
            }
        }
    }
    
    /* ---------- CATEGORY SCREEN FUNCTIONS ---------- */
    fun insertCategory(name: String) {
        if (categoryQueries.alreadyExist(name).executeAsOne()) {
            categoryQueries.setVisible(name)
        } else {
            try {
                categoryQueries.insert(name)
            } catch (_: Throwable) {
                // SQLiteException
            }
        }
    }

    fun deleteCategory(name: String) = categoryQueries.setHidden(name)
    fun setCategorySortType(type: CategorySortType) {
        sortCategory.value = type
    }

    /* ---------- LIMIT SCREEN FUNCTIONS ---------- */
    fun insertLimit(category: Category, amount: Double, date: String = YearMonth.from(LocalDate.now()).toString()) =
        limitQueries.insert(category.name, date, amount)

    fun setFilterLimitByDate(month: Int, year: Int, isFiltered: Boolean = true) {
        filterLimitByDate.value = DateFilter(month, year, isFiltered)
    }

    /* ---------- CHART SCREEN FUNCTIONS ---------- */
    var currentMonthStatstics = combine(_spendings, statisticFilter) { spending, filter ->
        val mapOfCategories: MutableMap<String, Double> = mutableMapOf()

        spending.forEach {
            if (it.date.startsWith("${filter.selectedYear}-${filter.selectedMonth}")) {
                if (!mapOfCategories.containsKey(it.category)) {
                    mapOfCategories[it.category] = it.amount
                } else {
                    mapOfCategories[it.category] = mapOfCategories[it.category]!! + it.amount
                }
            }
        }

        return@combine mapOfCategories
    }

    fun setChartByDate(month: Int, year: Int, isFiltered: Boolean = true) {
        statisticFilter.value = DateFilter(month, year, isFiltered)
    }

    init {
        spendingQueries.deleteAll()
        categoryQueries.deleteAll()
        limitQueries.deleteAll()
        insertCategory("Produkty spożywcze")
        insertCategory("Transport")
        insertCategory("Rachunki")
        insertCategory("Elektronika")
        insertCategory("Kursy")
        insertCategory("Leki")
        insertCategory("Moda")
        insertCategory("Subskrybcje")
        insertCategory("Uroda")
        insertCategory("Karma")
        insertCategory("Dom i ogród")
        insertCategory("Fast foody")
        insertCategory("Utrzymanie auta")
        insertCategory("Gry komputerowe")
        insertCategory("Gry planszowe")
        insertCategory("Remonty")
        insertCategory("Mieszkanie")
        insertCategory("Części do komputera")
        insertCategory("Akcje giełdowe")
        insertCategory("Czesne")
        insertCategory("Chemia")
        insertCategory("Remonty")
        insertCategory("Płyty CD")
        insertCategory("Mieszkanie")
        insertCategory("Zdrowie")
        insertLimit(Category("Subskrybcje", 1), 100.0, "2023-11")
        insertLimit(Category("Subskrybcje", 1), 50.0, "2023-10")
        insertLimit(Category("Elektronika", 1), 300.0, "2023-11")
        insertLimit(Category("Transport", 1), 200.0, "2023-11")
        insertLimit(Category("Gry komputerowe", 1), 300.0, "2023-09")
        insertLimit(Category("Płyty CD", 1), 250.0, "2023-09")
        insertLimit(Category("Części do komputera", 1), 7000.0, "2023-07")
    }
}




enum class CategorySortType(val sortType: String) {
    // sortType names are temporary
    NAME_INC("od A do Z"),
    NAME_DEC("od Z do A"),
    SUM_INC("ilość w górę"),
    SUM_DEC("ilość w dół"),
    AMOUNT_INC("suma w górę"),
    AMOUNT_DEC("suma w dół"),
}

data class CurrentLimit(
    var categoryName: String,
    var currentDate: String,
    var currentAmount: Double,
    var limitAmount: Double
)

data class DateFilter(
    var selectedMonth: Int = LocalDate.now().month.value,
    var selectedYear: Int = LocalDate.now().year,
    var isFiltered: Boolean = false,
)
