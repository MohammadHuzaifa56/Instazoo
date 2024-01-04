package data.repository.search

import data.model.SearchItem
import data.remote.Resource

interface SearchRepository {
    suspend fun getFeedPosts(): Resource<List<SearchItem>>
}