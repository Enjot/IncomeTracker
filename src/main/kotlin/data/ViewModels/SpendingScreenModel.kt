package data.ViewModels

import CLEAR_DATABASE_AND_LOAD_PREDEFINED_DATA
import cafe.adriel.voyager.core.model.ScreenModel
import com.example.sqldelight.Category
import com.example.sqldelight.Spending
import data.DatabaseRepository
import data.DateFilter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class SpendingScreenModel(
    private val repository: DatabaseRepository
) : ScreenModel {
    
    val categories = repository.tables.categories.map { list -> 
        list.filter { category -> category.isVisible.toInt() == 1 }
    }
    
    var sortType = MutableStateFlow(SpendingSortType.NAME_INC)
    var filterByDate = MutableStateFlow(DateFilter())
    private var filterByCategory: MutableStateFlow<String> = MutableStateFlow("")

    val spendings = combine(
        repository.tables.spendings, sortType, filterByCategory, filterByDate
    ) { spendings, sortType, categoryFilter, filter ->
        var sortedFilteredSpendings = when (sortType) {
            SpendingSortType.AMOUNT_INC -> spendings.sortedBy { it.amount }
            SpendingSortType.AMOUNT_DEC -> spendings.sortedBy { it.amount }.reversed()
            SpendingSortType.DATE_INC -> spendings.sortedBy { it.date }
            SpendingSortType.DATE_DEC -> spendings.sortedBy { it.date }.reversed()
            SpendingSortType.NAME_INC -> spendings.sortedBy { it.name }
            SpendingSortType.NAME_DEC -> spendings.sortedBy { it.name }.reversed()
        }
        if (categoryFilter != "") {
            sortedFilteredSpendings =
                sortedFilteredSpendings.filter { spending -> spending.category == categoryFilter }
        }
        val singleDigitMonthFilterLogic: (Spending, Int) -> Boolean = { spending, month ->
            spending.date.startsWith("${filter.selectedYear}-0$month")
        }
        return@combine when (filter.isFiltered) {
            false -> sortedFilteredSpendings
            true -> sortedFilteredSpendings.filter { spending ->
                if (filter.selectedMonth in 0..9) {
                    singleDigitMonthFilterLogic(spending, filter.selectedMonth)
                } else {
                    spending.date.startsWith("${filter.selectedYear}-${filter.selectedMonth}")
                }
            }
        }
    }

    fun insert(name: String, amount: Double, category: Category, date: String = LocalDate.now().toString()) {
        repository.insertSpending(name, amount, category, date)
    }

    fun edit(id: Long,name: String,amount: Double){
        repository.editSpending(id, name, amount)
    }

    fun delete(id: Long) = repository.deleteSpending(id)
    fun setSortType(type: SpendingSortType) {
        sortType.value = type
    }

    fun setFilterByDate(month: Int, year: Int, isFiltered: Boolean) {
        filterByDate.value = DateFilter(month, year, isFiltered)
    }

    fun setFilterByCategory(category: String) {
        filterByCategory.value = category
    }

    fun resetDateFilter() {
        filterByDate.value = DateFilter()
    }
    
    init {
        if (CLEAR_DATABASE_AND_LOAD_PREDEFINED_DATA) {
            repository.clearSpendings()
            insert("Buty", 170.0, Category("Moda", 1), "2023-11-16")
            insert("Pasta do zębów", 170.0, Category("Zdrowie", 1), "2023-11-20")
            insert("Red Dead Redemption 2", 149.0, Category("Gry komputerowe", 1), "2023-11-15")
            insert("Kindle Paperwhite 5", 550.0, Category("Elektronika", 1), "2023-12-16")
            insert("Apple iPhone 15 Pro", 5000.0, Category("Elektronika", 1), "2023-11-05")
            insert("Czynsz", 1100.0, Category("Mieszkanie", 1), "2023-11-01")
            insert("Czynsz", 1100.0, Category("Mieszkanie", 1), "2023-12-01")
            insert("Netflix", 56.0, Category("Subskrybcje", 1), "2023-11-25")
            insert("Netflix", 56.0, Category("Subskrybcje", 1), "2023-12-25")
            insert("Tidal", 30.0, Category("Subskrybcje", 1), "2023-11-01")
            insert("Tidal", 30.0, Category("Subskrybcje", 1), "2023-12-01")
            insert("Spotify", 20.0, Category("Subskrybcje", 1), "2023-11-01")
            insert("Spotify", 20.0, Category("Subskrybcje", 1), "2023-12-01")
            insert("Java 17 Masterclass by Tim Buchalka", 54.99, Category("Kursy", 1), "2023-09-17")
            insert("Uber", 23.0, Category("Transport", 1), "2023-11-21")
            insert("Bolt", 19.0, Category("Transport", 1), "2023-12-22")
            insert("Cyberpunk 2077", 100.0, Category("Gry komputerowe", 1), "2023-11-13")
            insert("Elmex", 11.0, Category("Uroda", 1), "2023-11-03")
            insert("Rutinoscorbin", 9.0, Category("Leki", 1), "2023-11-11")
            insert("Eurobiznes", 49.0, Category("Gry planszowe", 1), "2023-11-12")
            insert("Scrable", 70.0, Category("Gry planszowe", 1), "2023-12-12")
            insert("kurtka zimowa", 490.0, Category("Moda", 1), "2023-11-16")
            insert("Bluza Nike", 200.0, Category("Moda", 1), "2023-12-16")
            insert("Tankowanie", 300.0, Category("Utrzymanie auta", 1), "2023-12-12")
            insert("Tankowanie", 300.0, Category("Utrzymanie auta", 1), "2023-11-12")
        }
    }
}

enum class SpendingSortType(val visibleName: String) {
    NAME_INC("od A do Z"),
    NAME_DEC("od Z do A"),
    AMOUNT_INC("od najtańszych"),
    AMOUNT_DEC("od najdroższych"),
    DATE_INC("od najnowszych"),
    DATE_DEC("od najstarszych"),
}