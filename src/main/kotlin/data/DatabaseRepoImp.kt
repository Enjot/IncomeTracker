package data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.example.Database
import com.example.sqldelight.Category
import kotlinx.coroutines.Dispatchers
import java.io.File

class DatabaseRepoImp() : DatabaseRepository {

    private val driver: SqlDriver = JdbcSqliteDriver("jdbc:sqlite:database.db")

    private val database = Database(driver)

    override val tables = DatabaseTables(
        database.spendingQueries.selectAll().asFlow().mapToList(Dispatchers.IO),
        database.categoryQueries.selectAll().asFlow().mapToList(Dispatchers.IO),
        database.limitQueries.selectAll().asFlow().mapToList(Dispatchers.IO)
    )

    override fun insertSpending(name: String, amount: Double, category: Category, date: String) =
        database.spendingQueries.insert(name, amount, date, category.name)

    override fun deleteSpending(id: Long) = database.spendingQueries.delete(id)

    override fun insertCategory(name: String) = database.categoryQueries.insert(name)

    override fun deleteCategory(name: String) = database.categoryQueries.setHidden(name)

    override fun insertLimit(category: Category, amount: Double, date: String) =
        database.limitQueries.insert(category.name, date, amount)

    override fun deleteLimit(id: Long) = database.limitQueries.delete(id)
    override fun clearSpendings() = database.spendingQueries.deleteAll()

    override fun clearCategories() = database.categoryQueries.deleteAll()

    override fun clearLimits() = database.limitQueries.deleteAll()
    
    init {
        if (!File("database.db").exists()) {
            Database.Schema.create(driver)
        }
    }
}