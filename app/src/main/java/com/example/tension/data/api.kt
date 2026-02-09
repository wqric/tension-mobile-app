package com.example.tension.data

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
class Api(private val client: HttpClient) {
    private val BASE_URL = "http://85.198.83.158:8080"

    suspend fun login(req: LoginRQ): User {
        return client.post("$BASE_URL/login") {
            contentType(ContentType.Application.Json)
            setBody(req)
        }.body<User>()
    }

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
    suspend fun getMyWorkouts(token: String): List<Workout> {
        return client.get("$BASE_URL/api/workouts") {
            bearerAuth(token)
        }.body<List<Workout>>()
    }

    suspend fun getUserStats(token: String): UserStats {
        return client.get("$BASE_URL/api/stats") {
            bearerAuth(token)
        }.body<UserStats>()
    }

    // Сгенерировать новый план (POST)
    suspend fun generateWorkoutPlan(token: String): List<Workout> {
        return client.post("$BASE_URL/api/workouts/generate") {
            bearerAuth(token)
        }.body<List<Workout>>()
    }

    // Пометить тренировку выполненной (PATCH)
    suspend fun markWorkoutDone(token: String, workoutId: Int, date: String): WorkoutCompleteResponse {
        return client.patch("$BASE_URL/api/workouts/complete") {
            contentType(ContentType.Application.Json)
            bearerAuth(token)
            // Создаем анонимный объект для Body, если нет отдельного класса
            setBody(MarkWorkoutDoneRQ(workoutId, date))
        }.body<WorkoutCompleteResponse>()
    }
}