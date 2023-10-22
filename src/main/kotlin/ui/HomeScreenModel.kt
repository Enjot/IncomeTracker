package ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.db.SqlDriver
import cafe.adriel.voyager.core.model.ScreenModel
import com.example.Database
import com.example.sqldelight.CategoryQueries
import com.example.sqldelight.Spending
import com.example.sqldelight.SpendingQueries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate


class HomeScreenModel(
    private val driver: SqlDriver
) : ScreenModel {


    private val database = Database(driver)
    private val categoryQueries: CategoryQueries = database.categoryQueries
    private val spendingQueries: SpendingQueries = database.spendingQueries
    
    var currentScreen: MutableState<CurrentScreen> = mutableStateOf(CurrentScreen.ListOfSpending)
    
    lateinit var spendings: Flow<List<Spending>>

    fun insertSpending(name: String, amount: Double) {
        spendingQueries.insert(name, amount, LocalDate.now().toString())
    }

    fun deleteSpending(id: Long){
        spendingQueries.delete(id)
    }
    
    private fun getAllSpendings() {
        spendings = spendingQueries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.IO)
    }
    init {
        getAllSpendings()
    }

}

sealed interface CurrentScreen {
    data object ListOfSpending : CurrentScreen
    data object AddEditSpending : CurrentScreen
    data object ChooseCategory : CurrentScreen
}