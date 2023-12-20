package data.ViewModels

import CLEAR_DATABASE_AND_LOAD_PREDEFINED_DATA
import cafe.adriel.voyager.core.model.ScreenModel
import data.DatabaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class CategoryScreenModel(
    private val repository: DatabaseRepository
) : ScreenModel {
    
    val categories = repository.tables.categories

    var sortType = MutableStateFlow(CategorySortType.NAME_INC)

    var categoriesSummary = combine(repository.tables.spendings,
        repository.tables.categories,
        sortType
    ) { spending, category, sortType ->
        val mapOfCategories: MutableMap<String, Pair<Int, Double>> = mutableMapOf()
        spending.forEach {
            if (!mapOfCategories.containsKey(it.category)) {
                mapOfCategories[it.category] = Pair(1, it.amount)
            } else {
                mapOfCategories[it.category] = Pair(
                    mapOfCategories.getValue(it.category).first + 1,
                    mapOfCategories.getValue(it.category).second + it.amount
                )
            }
        }
        category.forEach {
            if (!mapOfCategories.containsKey(it.name) && it.isVisible.toInt() == 1)
                mapOfCategories[it.name] = Pair(0, 0.00)
        }
        val listOfCategories = mapOfCategories.entries.toList()
        return@combine when (sortType) {
            CategorySortType.NAME_INC -> listOfCategories.sortedBy { it.key.uppercase() }
            CategorySortType.NAME_DEC -> listOfCategories.sortedBy { it.key.uppercase() }.reversed()
            CategorySortType.SUM_INC -> listOfCategories.sortedBy { it.value.first }
            CategorySortType.SUM_DEC -> listOfCategories.sortedBy { it.value.first }.reversed()
            CategorySortType.AMOUNT_INC -> listOfCategories.sortedBy { it.value.second }
            CategorySortType.AMOUNT_DEC -> listOfCategories.sortedBy { it.value.second }.reversed()
        }
    }

    fun insert(name: String){
        if (repository.alreadyExist(name)) {
            repository.setVisible(name)
        } else {
            try {
                repository.insertCategory(name)
            } catch (_: Throwable) {
                // SQLiteException
            }
        }
    }

    fun delete(name: String) = repository.deleteCategory(name)

    fun setSortType(type: CategorySortType){
        sortType.value = type
    }

    init {
        if (CLEAR_DATABASE_AND_LOAD_PREDEFINED_DATA) {
            repository.clearCategories()
            insert("Produkty spożywcze")
            insert("Transport")
            insert("Rachunki")
            insert("Elektronika")
            insert("Kursy")
            insert("Leki")
            insert("Moda")
            insert("Subskrybcje")
            insert("Uroda")
            insert("Karma")
            insert("Dom i ogród")
            insert("Fast foody")
            insert("Utrzymanie auta")
            insert("Gry komputerowe")
            insert("Gry planszowe")
            insert("Remonty")
            insert("Mieszkanie")
            insert("Czesne")
            insert("Chemia")
            insert("Remonty")
            insert("Mieszkanie")
            insert("Zdrowie")
        }
    }
}

enum class CategorySortType(val visibleName: String) {
    NAME_INC("od A do Z"),
    NAME_DEC("od Z do A"),
    SUM_INC("ilość w górę"),
    SUM_DEC("ilość w dół"),
    AMOUNT_INC("od najtańszych"),
    AMOUNT_DEC("od najdroższych"),
}


