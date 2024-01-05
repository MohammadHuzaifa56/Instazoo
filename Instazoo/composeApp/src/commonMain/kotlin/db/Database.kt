package db

import DatabaseDriverFactory
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import data.model.SearchItem
import data.model.toData
import data.model.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.sample.instazoo.db.InstaZooDatabase

class Database(databaseDriverFactory: DatabaseDriverFactory) {

    private val database = databaseDriverFactory.createDriver()?.let { InstaZooDatabase(it) }

    fun getAllSearchPosts(): List<SearchItem>? =
        database?.searchPostsQueries?.selectAll()
            ?.executeAsList()?.map {
                it.toDomain()
            }


    fun insertSearchPosts(searchPostsList : List<SearchItem>){
        database?.transaction {
            searchPostsList.forEach {
                database?.searchPostsQueries?.insertFullPlayerObject(it.toData())
            }
        }
    }
}