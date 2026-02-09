package com.example.tension.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import io.ktor.client.plugins.ResponseException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class MainRep(private val dataStore: DataStore<Preferences>, private val api: Api) {
    companion object {
        val TOKEN = stringPreferencesKey("token")
    }

    suspend fun setToken(token: String) {
        dataStore.edit { it[TOKEN] = token }
    }

    suspend fun forgetToken() {
        dataStore.edit { it[TOKEN] = "" }
    }

    suspend fun login(email: String, password: String): Result<User> {
        return safeCall { api.login(LoginRQ(email = email, password = password)) }
    }

    suspend fun register(email: String, password: String): Result<User> {
        return safeCall { api.register(RegistrationRQ(email = email, password = password)) }
    }

    suspend fun getUserStats(): Result<UserStats>  {
        val token = dataStore.data.map { it[TOKEN] ?: "" }.first()
        return safeCall { api.getUserStats(token) }
    }

    suspend fun getProfile(): Result<User> {
        val token = dataStore.data.map { it[TOKEN] ?: "" }.first()
        return safeCall { api.getProfile(token) }
    }

    suspend fun updateProfile(req: UpdateProfileRQ): Result<User> {
        val token = dataStore.data.map { it[TOKEN] ?: "" }.first()
        return safeCall { api.updateProfile(token, req) }
    }

    suspend fun getMyWorkouts(): Result<List<Workout>> {
        val token = dataStore.data.map { it[TOKEN] ?: "" }.first()
        return safeCall { api.getMyWorkouts(token) }
    }

    suspend fun generateWorkouts(): Result<List<Workout>> {
        val token = dataStore.data.map { it[TOKEN] ?: "" }.first()
        return safeCall { api.generateWorkoutPlan(token) }
    }


    suspend fun markWorkoutDone(workoutId: Int, date: String): Result<WorkoutCompleteResponse> {
        val token = dataStore.data.map { it[TOKEN] ?: "" }.first()
        return safeCall { api.markWorkoutDone(token, workoutId, date) }
    }
    private suspend fun <T> safeCall(block: suspend () -> T): Result<T> {
        return try {
            Result.success(block())
        } catch (e: ResponseException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}