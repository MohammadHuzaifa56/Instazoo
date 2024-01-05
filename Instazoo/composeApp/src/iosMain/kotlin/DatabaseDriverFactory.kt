import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import org.sample.instazoo.db.InstaZooDatabase

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver? {
        return NativeSqliteDriver(
            schema = InstaZooDatabase.Schema,
            name = "InstaZooDatabase.db"
        )
    }
}