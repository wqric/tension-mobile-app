package com.example.tension.common

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.Preferences
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
@Module
@ComponentScan("com.example.tension")
class ModuleDI {
    @Single
    fun provideDataStore(context: Context): DataStore<Preferences> {
        return context.dataStore
    }

    @Single
    fun provideKtor(): HttpClient {
        return HttpClient(CIO) {
            expectSuccess = true
            install(ContentNegotiation) {
                json(Json{
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
        }
    }
}