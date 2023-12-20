package data.ViewModels

import cafe.adriel.voyager.core.model.ScreenModel
import data.DatabaseRepository
import data.DateFilter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class ChartScreenModel(
    repository: DatabaseRepository
) : ScreenModel {

    var filter = MutableStateFlow(DateFilter())

    var currentMonthStatstics = combine(
        repository.tables.spendings, filter
    ) { spending, filter ->
        val mapOfCategories: MutableMap<String, Double> = mutableMapOf()

        spending.forEach {
            if (it.date.startsWith("${filter.selectedYear}-${filter.selectedMonth}")) {
                if (!mapOfCategories.containsKey(it.category)) {
                    mapOfCategories[it.category] = it.amount
                } else {
                    mapOfCategories[it.category] = mapOfCategories[it.category]!! + it.amount
                }
            }
        }

        return@combine mapOfCategories
    }

    fun setFilter(month: Int, year: Int, isFiltered: Boolean = true) {
        filter.value = DateFilter(month, year, isFiltered)
    }
}