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

    var sortSpending = MutableStateFlow(SpendingSortType.NAME_INC)
    var filterSpendingByDate = MutableStateFlow(DateFilter())
    private var filterSpendingByCategory: MutableStateFlow<String> = MutableStateFlow("")

    var sortCategory = MutableStateFlow(CategorySortType.NAME_INC)

    var filterLimitByDate = MutableStateFlow(DateFilter())

    var spendings = combine(
        _spendings, sortSpending, filterSpendingByCategory, filterSpendingByDate
    ) { spendings, sortType, categoryFilter, dateFilter ->
        var sortedFilteredSpendings = when (sortType) {
            SpendingSortType.AMOUNT_INC -> spendings.sortedBy { it.amount }
            SpendingSortType.AMOUNT_DEC -> spendings.sortedBy { it.amount }.reversed()
            SpendingSortType.DATE_INC -> spendings.sortedBy { it.date }
            SpendingSortType.DATE_DEC -> spendings.sortedBy { it.date }.reversed()
            SpendingSortType.NAME_INC -> spendings.sortedBy { it.name }
            SpendingSortType.NAME_DEC -> spendings.sortedBy { it.name }.reversed()
        }
        if (categoryFilter != "") {
            sortedFilteredSpendings =
                sortedFilteredSpendings.filter { spending -> spending.category == categoryFilter }
        }
        return@combine when (dateFilter.isFiltered) {
            false -> sortedFilteredSpendings
            true -> sortedFilteredSpendings.filter {
                it.date.startsWith("${dateFilter.selectedYear}-${dateFilter.selectedMonth}")
            }
        }
    }

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

    /* ---------- SPENDING SCREEN FUNCTIONS ---------- */
    fun insertSpending(name: String, amount: Double, category: Category, date: String = LocalDate.now().toString()) =
        spendingQueries.insert(name, amount, date, category.name)

    fun deleteSpending(id: Long) = spendingQueries.delete(id)
    fun setSpendingSortType(type: SpendingSortType) {
        sortSpending.value = type
    }

    fun setFilterSpendingByDate(month: Int, year: Int, isFiltered: Boolean) {
        filterSpendingByDate.value = DateFilter(month, year, isFiltered)
    }

    fun setSpendingFilterByCategory(category: String) {
        filterSpendingByCategory.value = category
    }

    fun resetSpendingDateFilter() {
        filterSpendingByDate.value = DateFilter()
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
        insertSpending("Buty", 170.0, Category("Moda", 1), "2023-11-16")
        insertSpending("Pasta do zębów", 170.0, Category("Zdrowie", 1), "2023-11-20")
        insertSpending("Cyberpunk 2077", 156.99, Category("Gry komputerowe", 1), "2023-07-16")
        insertSpending("Red Dead Redemption 2", 149.0, Category("Gry komputerowe", 1), "2023-08-15")
        insertSpending("AMD Ryzen 7700X", 1459.0, Category("Części do komputera", 1), "2023-07-16")
        insertSpending("Kindle Paperwhite 5", 550.0, Category("Elektronika", 1), "2023-07-16")
        insertSpending("Apple iPhone 15 Pro Max 1TB", 9599.0, Category("Elektronika", 1), "2023-11-05")
        insertSpending("Tesla", 864.0, Category("Akcje giełdowe", 1), "2023-10-05")
        insertSpending("Mieszkanie", 2700.0, Category("Mieszkanie", 1), "2023-10-01")
        insertSpending("Netflix", 56.0, Category("Subskrybcje", 1), "2023-08-25")
        insertSpending("Tidal", 30.0, Category("Subskrybcje", 1), "2023-11-01")
        insertSpending("Spotify", 20.0, Category("Subskrybcje", 1), "2023-10-01")
        insertSpending("Java 17 Masterclass by Tim Buchalka", 54.99, Category("Kursy", 1), "2023-09-17")
        insertSpending("Uber", 23.0, Category("Transport", 1), "2023-09-21")
        insertSpending("Bolt", 19.0, Category("Transport", 1), "2023-09-22")
        insertSpending("RP Points", 100.0, Category("Gry komputerowe", 1), "2023-11-13")
        insertSpending("Elmex", 11.0, Category("Uroda", 1), "2023-11-03")
        insertSpending("Rutinoscorbin", 9.0, Category("Leki", 1), "2023-11-11")
        insertSpending("Eurobiznes", 49.0, Category("Gry planszowe", 1), "2023-09-12")
        insertSpending("Kendrick Lamar - Mr. Morale & the Big Steppers", 53.0, Category("Płyty CD", 1), "2023-08-22")
        insertSpending("J. Cole - The Off-Season", 113.0, Category("Płyty CD", 1), "2023-09-30")
        insertSpending("Eminem - Kamikaze", 78.0, Category("Płyty CD", 1), "2023-09-01")
        insertLimit(Category("Subskrybcje", 1), 100.0, "2023-11")
        insertLimit(Category("Subskrybcje", 1), 50.0, "2023-10")
        insertLimit(Category("Elektronika", 1), 300.0, "2023-11")
        insertLimit(Category("Transport", 1), 200.0, "2023-11")
        insertLimit(Category("Gry komputerowe", 1), 300.0, "2023-09")
        insertLimit(Category("Płyty CD", 1), 250.0, "2023-09")
        insertLimit(Category("Części do komputera", 1), 7000.0, "2023-07")
    }
}


enum class SpendingSortType(val sortType: String) {
    NAME_INC("od A do Z"),
    NAME_DEC("od Z do A"),
    AMOUNT_INC("od najtańszych"),
    AMOUNT_DEC("od najdroższych"),
    DATE_INC("od najnowszych"),
    DATE_DEC("od najstarszych"),

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
