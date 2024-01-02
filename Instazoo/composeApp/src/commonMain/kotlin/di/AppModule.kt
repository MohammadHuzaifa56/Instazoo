package di

import data.remote.InstazooAPI
import data.repository.HomeRepository
import data.repository.HomeRepositoryImpl
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import presentation.home.HomeScreenViewModel

fun appModule() = module {
    single<HomeScreenViewModel> {
        HomeScreenViewModel()
    }

    single<HomeRepository> {
        HomeRepositoryImpl(get())
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