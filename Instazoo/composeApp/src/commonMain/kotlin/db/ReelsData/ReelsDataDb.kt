package db.ReelsData

import data.model.ReelsItem
import data.model.toData
import data.model.toDomain
import org.sample.instazoo.db.InstaZooDatabase

class ReelsDataDb(private val database: InstaZooDatabase?) {
    fun getAllReels(): List<ReelsItem>? =
        database?.reelsDataQueries?.selectAllReels()
            ?.executeAsList()?.map {
                it.toDomain()
            }


    fun insertReels(reelsList: List<ReelsItem>) {
        database?.transaction {
            reelsList.forEach {
                database.reelsDataQueries.insertReelsItem(it.toData())
            }
        }
    }
}