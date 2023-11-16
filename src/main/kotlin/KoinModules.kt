
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.example.Database
import org.koin.dsl.module
import ui.HomeScreenModel
import java.io.File

val homeModule = module {
    val driver: SqlDriver = JdbcSqliteDriver("jdbc:sqlite:database.db")
    if (!File("database.db").exists()) {
        Database.Schema.create(driver)
    }
    factory { HomeScreenModel(driver) }
}