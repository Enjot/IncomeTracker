package ui.limitscreen

import cafe.adriel.voyager.core.model.ScreenModel
import com.example.sqldelight.Category
import data.DatabaseRepository
import data.DateFilter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import java.time.LocalDate
import java.time.YearMonth

class LimitScreenModel(
    private val repository: DatabaseRepository
) : ScreenModel {
    private var _limits = repository.tables.limits
    val categories = repository.tables.categories

    var filter = MutableStateFlow(DateFilter())

    var limits = combine(repository.tables.spendings, _limits, filter) { spendings, limits, filter ->
        val listOfCurrentLimits: MutableList<CurrentLimit> = mutableListOf()
        limits.forEach { limit ->
            var currentAmount = 0.0
            spendings.forEach { spending ->
                if (spending.category == limit.category && spending.date.startsWith(limit.date)) {
                    currentAmount += spending.amount
                }
            }
            listOfCurrentLimits.add(
                CurrentLimit(limit.category, limit.date, currentAmount, limit.limitAmount)
            )
        }
        val singleDigitMonthFilterLogic: (CurrentLimit, Int) -> Boolean = { limit, month ->
            limit.currentDate.startsWith("${filter.selectedYear}-0$month")
        }
        return@combine when (filter.isFiltered) {
            false -> listOfCurrentLimits
            true -> listOfCurrentLimits.filter { limit ->
                if (filter.selectedMonth in 0..9) {
                    singleDigitMonthFilterLogic(limit, filter.selectedMonth)
                } else {
                    limit.currentDate.startsWith("${filter.selectedYear}-${filter.selectedMonth}")
                }
            }
        }
    }

    fun insert(category: Category, amount: Double, date: String = YearMonth.from(LocalDate.now()).toString()){
        repository.insertLimit(category, amount, date)
    }

    fun setFilter(month: Int, year: Int, isFiltered: Boolean){
        filter.value = DateFilter(month,year,isFiltered)
    }


    init {
        repository.clearLimits()

        insert(Category("Subskrybcje", 1), 100.0, "2023-11")
        insert(Category("Subskrybcje", 1), 50.0, "2023-10")
        insert(Category("Elektronika", 1), 300.0, "2023-11")
        insert(Category("Transport", 1), 200.0, "2023-11")
        insert(Category("Gry komputerowe", 1), 300.0, "2023-09")
        insert(Category("Płyty CD", 1), 250.0, "2023-09")
        insert(Category("Części do komputera", 1), 7000.0, "2023-07")
    }
}

data class CurrentLimit(
    val categoryName: String,
    val currentDate: String,
    val currentAmount: Double,
    val limitAmount: Double
)
