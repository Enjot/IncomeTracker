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


    var allSpendings = spendingQueries.selectAll()
        .asFlow()
        .mapToList(Dispatchers.IO)

    var spendingSort: MutableStateFlow<SpendingSort> = MutableStateFlow(SpendingSort.NameInc)
    
    
    var sortedSpendings = allSpendings.combine(spendingSort) { spending, sortType ->

        val currentSpendingSort = when(sortType) {
            SpendingSort.AmountInc -> spending.sortedBy { it.amount }
            SpendingSort.AmountDec -> spending.sortedBy { it.amount }.reversed()
            SpendingSort.DateInc -> spending.sortedBy { it.date}
            SpendingSort.DateDec -> spending.sortedBy { it.date }.reversed()
            SpendingSort.NameInc -> spending.sortedBy { it.name }
            SpendingSort.NameDec -> spending.sortedBy { it.name }.reversed()
        }
        return@combine currentSpendingSort
    }

    fun selectSortType(type: SpendingSort) {
        spendingSort.value = type
    }


    var allCategories = categoryQueries.selectAll()
        .asFlow()
        .mapToList(Dispatchers.IO)

    var categories = allSpendings.combine(allCategories) { spending, category ->
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

        return@combine mapOfCategories.entries.toList().sortedBy { it.key.uppercase() }
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
        insertSpending("Buty", 170.0, Category("Moda", 0))
        insertSpending("Pasta do zębów", 170.0, Category("Zdrowie", 0))
        insertSpending("Cyberpunk 2077", 156.99, Category("Gry komputerowe", 0))
        insertSpending("AMD Ryzen 7700X", 1459.0, Category("Części do komputera", 0))
        insertSpending("Kindle Paperwhite 5", 550.0, Category("Elektronika", 0))
        insertSpending("Apple iPhone 15 Pro Max 1TB", 9599.0, Category("Elektronika", 0))
        insertSpending("Tesla", 864.0, Category("Akcje giełdowe", 0))
        insertSpending("Mieszkanie", 2700.0, Category("Mieszkanie", 0))
        insertSpending("4pak piwa", 14.0, Category("Alkohol", 0))
        insertSpending("Netflix", 56.0, Category("Subskrybcje", 0))
        insertSpending("Tidal", 19.99, Category("Subskrybcje", 0))
        insertSpending("Java 17 Masterclass by Tim Buchalka", 54.99, Category("Kursy", 0))
        insertSpending("Uber", 23.0, Category("Transport", 0))
        insertSpending("Bolt", 19.0, Category("Transport", 0))
        
    }
}

enum class SpendingSort{
    AmountInc, AmountDec, DateInc, DateDec, NameInc, NameDec
}
