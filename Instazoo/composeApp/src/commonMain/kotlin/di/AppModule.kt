package di

import DatabaseDriverFactory
import app.cash.sqldelight.Transacter
import data.remote.InstazooAPI
import data.repository.HomeRepository
import data.repository.HomeRepositoryImpl
import data.repository.profile.ProfileRepository
import data.repository.profile.ProfileRepositoryImpl
import data.repository.search.SearchRepository
import data.repository.search.SearchRepositoryImpl
import db.FeedPosts.HomeScreenDb
import db.SearchPosts.SearchPostDb
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import org.koin.dsl.module
import org.sample.instazoo.db.InstaZooDatabase
import presentation.home.HomeScreenViewModel
import presentation.profile.ProfileScreenViewModel
import presentation.search.SearchViewModel
import kotlin.math.sin

fun appModule() = module {

    single {
        DatabaseDriverFactory()
    }

    single<HomeScreenViewModel> {
        HomeScreenViewModel()
    }

    single<ProfileScreenViewModel> {
        ProfileScreenViewModel()
    }

    single<HomeRepository> {
        HomeRepositoryImpl(get(),get())
    }

    single<ProfileRepository> {
        ProfileRepositoryImpl(get())
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

    single {
        InstazooAPI(get())
    }

    single {
        val factory: DatabaseDriverFactory = get()
        factory.createDriver()?.let { InstaZooDatabase(it) }
    }

    single {
        SearchPostDb(get())
    }

    single {
        HomeScreenDb(get())
    }
}