package data.repository.search

import data.model.SearchItem
import data.remote.Resource
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun getFeedPosts(): Flow<Resource<List<SearchItem>>>
}