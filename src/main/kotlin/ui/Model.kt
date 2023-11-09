package ui

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.db.SqlDriver
import cafe.adriel.voyager.core.model.ScreenModel
import com.example.Database
import com.example.sqldelight.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import java.time.LocalDate

class HomeScreenModel(
    driver: SqlDriver
) : ScreenModel {

    private val database = Database(driver)
    private val categoryQueries = database.categoryQueries
    private val spendingQueries = database.spendingQueries

    private var allSpendings = spendingQueries.selectAll()
        .asFlow()
        .mapToList(Dispatchers.IO)
    var allCategories = categoryQueries.selectAll()
        .asFlow()
        .mapToList(Dispatchers.IO)
    
    var spendingSortType: MutableStateFlow<SpendingSortType> = MutableStateFlow(SpendingSortType.NAME_INC)
    var categorySortType: MutableStateFlow<CategorySortType> = MutableStateFlow(CategorySortType.NAME_INC)
    
    private var categoryFilter: MutableStateFlow<String> = MutableStateFlow("")

    var sortedSpendings = allSpendings.combine(spendingSortType) { spending, sortType ->
        return@combine when (sortType) {
            SpendingSortType.AMOUNT_INC -> spending.sortedBy { it.amount }
            SpendingSortType.AMOUNT_DEC -> spending.sortedBy { it.amount }.reversed()
            SpendingSortType.DATE_INC -> spending.sortedBy { it.date }
            SpendingSortType.DATE_DEC -> spending.sortedBy { it.date }.reversed()
            SpendingSortType.NAME_INC -> spending.sortedBy { it.name }
            SpendingSortType.NAME_DEC -> spending.sortedBy { it.name }.reversed()
        }
    }.combine(categoryFilter) { spendingList, filter ->
        return@combine if (filter == "") {
            spendingList
        } else {
            spendingList.filter { spending -> spending.category == filter }
        }
    }

    fun selectSortedCategory(category: String) {
        categoryFilter.value = category
    }

    fun setSpendingSortType(type: SpendingSortType) {
        spendingSortType.value = type
    }

    fun setCategorySortType(type: CategorySortType) {
        categorySortType.value = type
    }
    
    var sortedCategories = allSpendings.combine(allCategories) { spending, category ->
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
        return@combine mapOfCategories.entries.toList()
    }.combine(categorySortType) {categories, sortType  ->
        return@combine when (sortType) {
            CategorySortType.NAME_INC -> categories.sortedBy { it.key.uppercase() }
            CategorySortType.NAME_DEC -> categories.sortedBy { it.key.uppercase() }.reversed()
            CategorySortType.SUM_INC -> categories.sortedBy { it.value.first }
            CategorySortType.SUM_DEC -> categories.sortedBy { it.value.first }.reversed()
            CategorySortType.AMOUNT_INC -> categories.sortedBy { it.value.second }
            CategorySortType.AMOUNT_DEC -> categories.sortedBy { it.value.second }.reversed()
        }
    }


    fun insertSpending(name: String, amount: Double, category: Category) =
        spendingQueries.insert(name, amount, LocalDate.now().toString(), category.name)

    fun deleteSpending(id: Long) = spendingQueries.delete(id)

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

    fun setHiddenCategory(name: String) = categoryQueries.setHidden(name)


    init {

        // temporary init categories to test functionality
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
        insertCategory("Utrzymanie auta")
        insertCategory("Alkohol")
        insertCategory("Papierosy")
        insertCategory("Gry komputerowe")
        insertCategory("Remonty")
        insertCategory("Mieszkanie")
        insertCategory("Części do komputera")
        insertCategory("Akcje giełdowe")
        insertCategory("Czesne")
        insertCategory("Chemia")
        insertCategory("Remonty")
        insertCategory("Mieszkanie")
        insertCategory("Zdrowie")

        // temporary init spendings to test functionality
//        insertSpending("Buty", 170.0, Category("Moda", 0))
//        insertSpending("Pasta do zębów", 170.0, Category("Zdrowie", 0))
//        insertSpending("Cyberpunk 2077", 156.99, Category("Gry komputerowe", 0))
//        insertSpending("AMD Ryzen 7700X", 1459.0, Category("Części do komputera", 0))
//        insertSpending("Kindle Paperwhite 5", 550.0, Category("Elektronika", 0))
//        insertSpending("Apple iPhone 15 Pro Max 1TB", 9599.0, Category("Elektronika", 0))
//        insertSpending("Tesla", 864.0, Category("Akcje giełdowe", 0))
//        insertSpending("Mieszkanie", 2700.0, Category("Mieszkanie", 0))
//        insertSpending("4pak piwa", 14.0, Category("Alkohol", 0))
//        insertSpending("Netflix", 56.0, Category("Subskrybcje", 0))
//        insertSpending("Tidal", 19.99, Category("Subskrybcje", 0))
//        insertSpending("Java 17 Masterclass by Tim Buchalka", 54.99, Category("Kursy", 0))
//        insertSpending("Uber", 23.0, Category("Transport", 0))
//        insertSpending("Bolt", 19.0, Category("Transport", 0))

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
