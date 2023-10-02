package ui

import app.cash.sqldelight.db.SqlDriver
import cafe.adriel.voyager.core.model.ScreenModel
import com.example.Database
import com.example.sqldelight.PlayerQueries

class HomeScreenModel(
    private val driver: SqlDriver
) : ScreenModel {


    val database = Database(driver)
    val playerQueries: PlayerQueries = database.playerQueries
    
    fun insert(number: Int, name: String) {
        playerQueries.insert(player_number = number.toLong(), "XD")
    }
}
