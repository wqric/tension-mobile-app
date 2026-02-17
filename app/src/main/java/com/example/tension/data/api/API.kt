package com.example.tension.data.api

import com.example.tension.presentation.models.MessageResponse
import com.example.tension.presentation.models.User
import com.example.tension.presentation.models.UserStats
import com.example.tension.presentation.models.Workout
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.koin.core.annotation.Single

@Single
class API(private val client: HttpClient) {
    companion object {
        const val BASE_URL = "http://85.198.83.158:8080"
    }

    // Войти в аккаунт
    suspend fun login(req: LoginRQ): User {
        return client.post("$BASE_URL/login") {
            contentType(ContentType.Application.Json)
            setBody(req)
        }.body<User>()
    }


    // Создаьб аккаунт
    suspend fun register(req: RegistrationRQ): User {
        return client.post("$BASE_URL/register") {
            contentType(ContentType.Application.Json)
            setBody(req)
        }.body<User>()
    }

    // --- ПРОФИЛЬ ---

    suspend fun getProfile(token: String): User {
        return client.get("$BASE_URL/api/profile") {
            bearerAuth(token)
        }.body<User>()
    }

    suspend fun updateProfile(token: String, req: UpdateProfileRQ): User {
        return client.patch("$BASE_URL/api/profile/update") {
            contentType(ContentType.Application.Json)
            bearerAuth(token)
            setBody(req)
        }.body<User>()
    }

    // --- ТРЕНИРОВКИ ---

    // Получить список всех назначенных тренировок
    suspend fun getUserWorkouts(token: String): List<Workout> {
        return client.get("$BASE_URL/api/workouts") {
            bearerAuth(token)
        }.body<List<Workout>>()
    }

    // Получить статистику пользователя
    suspend fun getUserStats(token: String): UserStats {
        return client.get("$BASE_URL/api/stats") {
            bearerAuth(token)
        }.body<UserStats>()
    }

    // Сгенерировать новый план трениовок
    suspend fun generateWorkoutPlan(token: String): List<Workout> {
        return client.post("$BASE_URL/api/workouts/generate") {
            bearerAuth(token)
        }.body<List<Workout>>()
    }

    // Пометить тренировку выполненной
    suspend fun markWorkoutDone(token: String, workoutId: Int, date: String): MessageResponse {
        return client.patch("$BASE_URL/api/workouts/complete") {
            contentType(ContentType.Application.Json)
            bearerAuth(token)
            setBody(MarkWorkoutDoneRQ(workoutId, date))
        }.body<MessageResponse>()
    }
}