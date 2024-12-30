package data.repository.reels

import data.model.ReelsItem
import data.remote.Resource
import kotlinx.coroutines.flow.Flow

interface ReelsRepository {
    suspend fun getReelsData(): Flow<Resource<List<ReelsItem>>>
}