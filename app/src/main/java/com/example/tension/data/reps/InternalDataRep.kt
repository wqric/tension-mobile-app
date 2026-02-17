package com.example.tension.data.reps

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class InternalDataRep(private val dataStore: DataStore<Preferences>) {
    companion object {
        val TOKEN = stringPreferencesKey("token")
        val AI_LAST_MESSAGE_ID = stringPreferencesKey("ai message")
    }

    suspend fun getMessageId(): String? {
        return dataStore.data.map { it[AI_LAST_MESSAGE_ID]?.takeIf { id -> id.isNotBlank() } }.first()
    }
    suspend fun setMessageId(id: String) {
        dataStore.edit { it[AI_LAST_MESSAGE_ID] = id }
    }

    suspend fun forgetMessageId() {
        dataStore.edit { it[AI_LAST_MESSAGE_ID] = "" }
    }

    suspend fun getToken(): String {
        return dataStore.data.map { it[TOKEN] ?: "" }.first()
    }


    suspend fun setToken(token: String) {
        dataStore.edit { it[TOKEN] = token }
    }

    suspend fun forgetToken() {
        dataStore.edit { it[TOKEN] = "" }
    }
}