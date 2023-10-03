package ui

import app.cash.sqldelight.db.SqlDriver
import cafe.adriel.voyager.core.model.ScreenModel
import com.example.Database
import com.example.sqldelight.CategoryQueries
import com.example.sqldelight.SpendingQueries
import java.time.LocalDate


class HomeScreenModel(
    private val driver: SqlDriver
) : ScreenModel {


    private val database = Database(driver)
    private val categoryQueries: CategoryQueries = database.categoryQueries
    private val spendingQueries: SpendingQueries = database.spendingQueries

    fun insert() {
        categoryQueries.insert("Żywność")
        spendingQueries.insert("Nowe Adidasy",200.0,LocalDate.now().toString())
    }
}
