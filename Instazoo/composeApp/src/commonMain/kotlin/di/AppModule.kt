package di

import DatabaseDriverFactory
import data.remote.InstazooAPI
import data.repository.HomeRepository
import data.repository.HomeRepositoryImpl
import data.repository.search.SearchRepository
import data.repository.search.SearchRepositoryImpl
import db.Database
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import org.koin.dsl.module
import presentation.home.HomeScreenViewModel
import presentation.search.SearchViewModel

fun appModule() = module {

    single {
        Database(
            databaseDriverFactory = get()
        )
    }

    single {
        DatabaseDriverFactory()
    }

    single<HomeScreenViewModel> {
        HomeScreenViewModel()
    }

    single<HomeRepository> {
        HomeRepositoryImpl(get())
    }

    single<SearchRepository> {
        SearchRepositoryImpl(get(), get())
    }

    single<SearchViewModel> {
        SearchViewModel(get())
    }


    single<HttpClient> {
        HttpClient {
            install(ContentNegotiation) {
                json()
            }
        }
    }

    single{
        InstazooAPI(get())
    }
}