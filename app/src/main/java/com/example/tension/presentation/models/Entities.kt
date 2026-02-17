package com.example.tension.presentation.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


enum class Difficulty(val value: Int, val text: String) {
    BEGINNER(0, "Новичок"),
    INTERMEDIATE(1, "Средний уровень"),
    ADVANCED(2, "Продвинутый");


}

enum class Aim(val value: Int, val text: String) {
    LOSE_WEIGHT(0, "Похудение"),
    MASS(1, "Набор массы"),
    MAINTENANCE(2, "Поддержание формы");


}

@Serializable
data class User(
    val email: String,
    val name: String,
    val aim: Int,
    val difficult: Int,
    val weight: Double,
    val height: Double,
    val token: String
)

@Serializable
data class Exercise(
    @SerialName("ID")
    val id: Int,
    val title: String,
    val reps: String,
    val sets: Int,
    val rest: Int
)

@Serializable
data class Workout(
    @SerialName("workout_id")
    val id: Int,
    val title: String,
    val desc: String,
    @SerialName("scheduled_date")
    val date: String,
    @SerialName("is_done")
    var isDone: Boolean,
    val exercises: List<Exercise>? = emptyList()
)





@Serializable
data class UserStats(
    @SerialName("total_workouts") val totalWorkouts: Int,
    @SerialName("completion_rate") val completionRate: Double,
    @SerialName("total_exercises") val totalExercises: Int,
    @SerialName("current_streak") val currentStreak: Int,
    @SerialName("favorite_workout") val favoriteWorkout: String
)
@Serializable
data class MessageResponse(val message: String, val status: String? = null)

data class ChatUiMessage(
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)