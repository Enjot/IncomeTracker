package ui

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.db.SqlDriver
import cafe.adriel.voyager.core.model.ScreenModel
import com.example.Database
import com.example.sqldelight.CategoryQueries
import com.example.sqldelight.SpendingQueries
import kotlinx.coroutines.Dispatchers
import java.time.LocalDate


class HomeScreenModel(
    private val driver: SqlDriver
) : ScreenModel {


    private val database = Database(driver)
    private val categoryQueries: CategoryQueries = database.categoryQueries
    private val spendingQueries: SpendingQueries = database.spendingQueries

    val spendings = spendingQueries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.IO)
    fun insertSpending(name: String, amount: Double) {
        spendingQueries.insert(name, amount, LocalDate.now().toString())
    }

    fun deleteSpending(id: Long){
        spendingQueries.delete(id)
    }


}
