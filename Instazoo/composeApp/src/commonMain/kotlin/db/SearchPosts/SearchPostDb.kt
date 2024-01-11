package db.SearchPosts

import data.model.SearchItem
import data.model.toData
import data.model.toDomain
import org.sample.instazoo.db.InstaZooDatabase

class SearchPostDb(private val database: InstaZooDatabase?) {
    fun getAllSearchPosts(): List<SearchItem>? =
        database?.searchPostsQueries?.selectAll()
            ?.executeAsList()?.map {
                it.toDomain()
            }


    fun insertSearchPosts(searchPostsList : List<SearchItem>){
        database?.transaction {
            searchPostsList.forEach {
                database.searchPostsQueries.insertFullPlayerObject(it.toData())
            }
        }
    }
}