package ui.spendingscreen

import cafe.adriel.voyager.core.model.ScreenModel
import com.example.sqldelight.Category
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
    ) { spendings, sortType, categoryFilter, dateFilter ->
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
        return@combine when (dateFilter.isFiltered) {
            false -> sortedFilteredSpendings
            true -> sortedFilteredSpendings.filter {
                it.date.startsWith("${dateFilter.selectedYear}-${dateFilter.selectedMonth}")
            }
        }
    }

    fun insert(name: String, amount: Double, category: Category, date: String = LocalDate.now().toString()) {
        repository.insertSpending(name, amount, category, date)
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
        repository.clearSpendings()
        insert("Buty", 170.0, Category("Moda", 1), "2023-11-16")
        insert("Pasta do zębów", 170.0, Category("Zdrowie", 1), "2023-11-20")
        insert("Cyberpunk 2077", 156.99, Category("Gry komputerowe", 1), "2023-07-16")
        insert("Red Dead Redemption 2", 149.0, Category("Gry komputerowe", 1), "2023-08-15")
        insert("AMD Ryzen 7700X", 1459.0, Category("Części do komputera", 1), "2023-07-16")
        insert("Kindle Paperwhite 5", 550.0, Category("Elektronika", 1), "2023-07-16")
        insert("Apple iPhone 15 Pro Max 1TB", 9599.0, Category("Elektronika", 1), "2023-11-05")
        insert("Tesla", 864.0, Category("Akcje giełdowe", 1), "2023-10-05")
        insert("Mieszkanie", 2700.0, Category("Mieszkanie", 1), "2023-10-01")
        insert("Netflix", 56.0, Category("Subskrybcje", 1), "2023-08-25")
        insert("Tidal", 30.0, Category("Subskrybcje", 1), "2023-11-01")
        insert("Spotify", 20.0, Category("Subskrybcje", 1), "2023-10-01")
        insert("Java 17 Masterclass by Tim Buchalka", 54.99, Category("Kursy", 1), "2023-09-17")
        insert("Uber", 23.0, Category("Transport", 1), "2023-09-21")
        insert("Bolt", 19.0, Category("Transport", 1), "2023-09-22")
        insert("RP Points", 100.0, Category("Gry komputerowe", 1), "2023-11-13")
        insert("Elmex", 11.0, Category("Uroda", 1), "2023-11-03")
        insert("Rutinoscorbin", 9.0, Category("Leki", 1), "2023-11-11")
        insert("Eurobiznes", 49.0, Category("Gry planszowe", 1), "2023-09-12")
        insert("Kendrick Lamar - Mr. Morale & the Big Steppers", 53.0, Category("Płyty CD", 1), "2023-08-22")
        insert("J. Cole - The Off-Season", 113.0, Category("Płyty CD", 1), "2023-09-30")
        insert("Eminem - Kamikaze", 78.0, Category("Płyty CD", 1), "2023-09-01")
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