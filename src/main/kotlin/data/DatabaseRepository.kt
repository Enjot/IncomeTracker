package data

import com.example.sqldelight.Category
import com.example.sqldelight.LimitOfCategory
import com.example.sqldelight.Spending
import kotlinx.coroutines.flow.Flow

interface DatabaseRepository {

    val tables: DatabaseTables

    fun insertSpending(name: String, amount: Double, category: Category, date: String)

    fun deleteSpending(id: Long)

    fun insertCategory(name: String)

    fun deleteCategory(name: String)

    fun insertLimit(category: Category, amount: Double, date: String)

    fun deleteLimit(id: Long)

    fun clearSpendings()
    fun clearCategories()
    fun clearLimits()
}

data class DatabaseTables(
    var spendings: Flow<List<Spending>>,
    var categories: Flow<List<Category>>,
    var limits: Flow<List<LimitOfCategory>>
)