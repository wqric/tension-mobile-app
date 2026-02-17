package com.example.tension.data.reps

import com.example.tension.data.api.AiAPI
import com.example.tension.data.api.API
import com.example.tension.data.api.AgentCallRQ
import com.example.tension.data.api.LoginRQ
import com.example.tension.data.api.RegistrationRQ
import com.example.tension.data.api.UpdateProfileRQ
import com.example.tension.domain.AgentCallRS
import com.example.tension.presentation.models.MessageResponse
import com.example.tension.presentation.models.User
import com.example.tension.presentation.models.UserStats
import com.example.tension.presentation.models.Workout
import io.ktor.client.plugins.ResponseException
import org.koin.core.annotation.Single

@Single
class NetworkDataRep(private val api: API, private val aiApi: AiAPI) {
    suspend fun login(email: String, password: String): Result<User> {
        val req = LoginRQ(email = email, password = password)
        return safeCall { api.login(req) }
    }

    suspend fun register(email: String, password: String): Result<User> {
        val req = RegistrationRQ(email = email, password = password)
        return safeCall { api.register(req) }
    }

    suspend fun getUserStats(token: String): Result<UserStats> {
        return safeCall { api.getUserStats(token) }
    }

    suspend fun getProfile(token: String): Result<User> {
        return safeCall { api.getProfile(token) }
    }

    suspend fun updateProfile(
        token: String,
        name: String? = null,
        weight: Float? = null,
        height: Float? = null,
        aim: Int? = null,
        difficult: Int? = null
    ): Result<User> {
        val req = UpdateProfileRQ(
            name = name,
            weight = weight,
            height = height,
            aim = aim,
            difficult = difficult
        )
        return safeCall { api.updateProfile(token, req) }
    }

    suspend fun getUserWorkouts(token: String): Result<List<Workout>> {
        return safeCall { api.getUserWorkouts(token) }
    }

    suspend fun generateWorkouts(token: String): Result<List<Workout>> {
        return safeCall { api.generateWorkoutPlan(token) }
    }


    suspend fun markWorkoutDone(token: String,workoutId: Int, date: String): Result<MessageResponse> {
        return safeCall { api.markWorkoutDone(token, workoutId, date) }
    }

    suspend fun callAgent(message: String, parentMessageId: String?): Result<AgentCallRS>{
        val req = AgentCallRQ(
            message = message,
            parentMessageId = parentMessageId
        )
        return safeCall { aiApi.callAgent(req) }
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