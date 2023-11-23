package ui.categoryscreen

import cafe.adriel.voyager.core.model.ScreenModel
import data.DatabaseRepository

class CategoryScreenModel(
    repository: DatabaseRepository
) : ScreenModel {
    
    val categories = repository.tables.categories
}