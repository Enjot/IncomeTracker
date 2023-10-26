package ui

import androidx.compose.runtime.mutableStateOf
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.db.SqlDriver
import cafe.adriel.voyager.core.model.ScreenModel
import com.example.Database
import com.example.sqldelight.Category
import com.example.sqldelight.CategoryQueries
import com.example.sqldelight.Spending
import com.example.sqldelight.SpendingQueries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.LocalDate


class HomeScreenModel(
    driver: SqlDriver
) : ScreenModel {
    
    private val database = Database(driver)
    private val categoryQueries: CategoryQueries = database.categoryQueries
    private val spendingQueries: SpendingQueries = database.spendingQueries

    var currentScreen = mutableStateOf<CurrentScreen>(CurrentScreen.ListOfSpending)

    var allSpendings: Flow<List<Spending>> = spendingQueries.selectAll()
        .asFlow()
        .mapToList(Dispatchers.IO)

    var allCategories: Flow<List<Category>> = categoryQueries.selectAll()
        .asFlow()
        .mapToList(Dispatchers.IO)

    var categories: Flow<List<MutableMap.MutableEntry<String, Pair<Int, Double>>>> =
        allSpendings.combine(allCategories) { spending, category ->

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
                if (!mapOfCategories.containsKey(it.name) && it.isDeleted.toInt() == 0)
                    mapOfCategories[it.name] = Pair(0, 0.00)
            }
            return@combine mapOfCategories.entries.toList<MutableMap.MutableEntry<String, Pair<Int, Double>>>()
                .sortedBy<MutableMap.MutableEntry<String, Pair<Int, Double>>, String> { it.key.uppercase() }
        }
    
    fun insertSpending(
        name: String,
        amount: Double,
        category: Category
    ) {
        spendingQueries.insert(
            name,
            amount,
            LocalDate.now().toString(),
            category.name
        )
    }

    fun deleteSpending(id: Long) {
        spendingQueries.delete(id)
    }

    fun insertCategory(name: String) {
        try {
            categoryQueries.insert(name)
        } catch (_: Exception) {
            // error name: org.sqlite.SQLiteException
        }
    }

    private fun deleteCategory(name: String) {
        categoryQueries.delete(name)
    }

    init {
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
        
        insertSpending("Pościel", 130.0, Category("Dom i ogród", 0))
    }
}

sealed interface CurrentScreen {
    data object ListOfSpending : CurrentScreen
    data object AddEditSpending : CurrentScreen
    data object ChooseCategory : CurrentScreen
}