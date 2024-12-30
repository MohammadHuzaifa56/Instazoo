package di

import DatabaseDriverFactory
import data.remote.InstazooAPI
import data.repository.HomeRepository
import data.repository.HomeRepositoryImpl
import data.repository.profile.ProfileRepository
import data.repository.profile.ProfileRepositoryImpl
import data.repository.reels.ReelsRepository
import data.repository.reels.ReelsRepositoryImpl
import data.repository.search.SearchRepository
import data.repository.search.SearchRepositoryImpl
import db.FeedPosts.HomeScreenDb
import db.ReelsData.ReelsDataDb
import db.SearchPosts.SearchPostDb
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.KotlinxSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import org.sample.instazoo.db.InstaZooDatabase
import presentation.home.HomeScreenViewModel
import presentation.profile.ProfileScreenViewModel
import presentation.reels.ReelsViewModel
import presentation.search.SearchViewModel

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

    single {
        ReelsViewModel(get())
    }

    single<ReelsRepository> {
        ReelsRepositoryImpl(get(), get())
    }

    single<HttpClient> {
        HttpClient {
            install(ContentNegotiation) {
                register(ContentType.Application.Json, KotlinxSerializationConverter(Json {
                    prettyPrint = true
                    ignoreUnknownKeys = true
                    explicitNulls = false
                }))
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

    single {
        ReelsDataDb(get())
    }
}