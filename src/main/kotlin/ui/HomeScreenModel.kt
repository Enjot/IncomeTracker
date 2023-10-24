package ui

import androidx.compose.runtime.MutableState
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
import java.time.LocalDate


class HomeScreenModel(
    driver: SqlDriver
) : ScreenModel {


    private val database = Database(driver)
    private val categoryQueries: CategoryQueries = database.categoryQueries
    private val spendingQueries: SpendingQueries = database.spendingQueries
    
    var currentScreen = mutableStateOf<CurrentScreen>(CurrentScreen.ListOfSpending)
    
    lateinit var spendings: Flow<List<Spending>>
    lateinit var categories: Flow<List<Category>>

    var selectedCategory: MutableState<Category?> = mutableStateOf(null)
    
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

    fun deleteSpending(id: Long){
        spendingQueries.delete(id)
    }
    
    private fun getAllSpendings() {
        spendings = spendingQueries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.IO)
    }

    private fun insertCategory(name: String) {
        try {
            categoryQueries.insert(name)
        } catch (_: Exception) {
            // error name: org.sqlite.SQLiteException
        }
    }
    
    private fun deleteCategory(name: String){
        categoryQueries.delete(name)
    }

    private fun getAllCategories() {
        categories = categoryQueries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.IO)
    }

    init {
        getAllSpendings()
        getAllCategories()
        
        insertCategory("żywność")
        insertCategory("transport")
        insertCategory("rachunki")
    }


}

sealed interface CurrentScreen {
    data object ListOfSpending : CurrentScreen
    data object AddEditSpending : CurrentScreen
    data object ChooseCategory : CurrentScreen
}