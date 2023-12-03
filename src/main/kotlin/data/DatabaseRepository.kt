package data

import com.example.sqldelight.Category
import com.example.sqldelight.LimitOfCategory
import com.example.sqldelight.Spending
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface DatabaseRepository {

    val tables: DatabaseTables

    fun insertSpending(name: String, amount: Double, category: Category, date: String)

    fun deleteSpending(id: Long)

    fun editSpending(id: Long, name: String, amount: Double)

    fun insertCategory(name: String)

    fun deleteCategory(name: String)

    fun insertLimit(category: Category, amount: Double, date: String)

    fun deleteLimit(name: String, date: String)

    fun editLimit(name: String,amount: Double, date: String)

    fun clearSpendings()
    fun clearCategories()
    fun clearLimits()

    fun alreadyExist(name: String): Boolean
    fun setVisible(name: String)
}

data class DatabaseTables(
    var spendings: Flow<List<Spending>>,
    var categories: Flow<List<Category>>,
    var limits: Flow<List<LimitOfCategory>>
)

data class DateFilter(
    var selectedMonth: Int = LocalDate.now().month.value,
    var selectedYear: Int = LocalDate.now().year,
    var isFiltered: Boolean = false,
)