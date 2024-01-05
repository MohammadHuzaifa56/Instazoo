import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import org.sample.instazoo.MyApplication
import org.sample.instazoo.db.InstaZooDatabase

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver? {
        return MyApplication.context?.let {
            AndroidSqliteDriver(
                schema = InstaZooDatabase.Schema,
                context = it,
                name = "InstaZooDatabase.db"
            )
        }
    }
}