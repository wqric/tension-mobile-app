package com.example.tension.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


enum class Difficulty(val value: Int, val text: String) {
    BEGINNER(0, "Новичок"),
    INTERMEDIATE(1, "Средний уровень"),
    ADVANCED(2, "Продвинутый");

    companion object {
        fun fromInt(id: Int) = entries.find { it.value == id } ?: BEGINNER
    }
}

enum class Aim(val value: Int, val text: String) {
    LOSE_WEIGHT(0, "Похудение"),
    MASS(1, "Набор массы"),
    MAINTENANCE(2, "Поддержание формы");

    companion object {
        fun fromInt(id: Int) = entries.find { it.value == id } ?: MAINTENANCE
    }
}

@Serializable
data class User(
    val email: String,
    val name: String,
    val aim: Int,         // Цель: 0 - Похудение, 1 - Масса и т.д.
    val difficult: Int,   // Сложность: 0 - Новичок, 1 - Продвинутый
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
data class RegistrationRQ(
    val email: String,
    val password: String,
)

@Serializable
data class LoginRQ(
    val email: String,
    val password: String
)

@Serializable
data class UpdateProfileRQ(
    val name: String? = null,
    val lastname: String? = null,
    val weight: Float? = null,
    val height: Float? = null,
    val aim: Int? = null,
    val difficult: Int? = null
)

@Serializable
data class MarkWorkoutDoneRQ(
    @SerialName("workout_id")
    val workoutId: Int,
    val date: String
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
data class WorkoutCompleteResponse(
    val message: String,
    val status: String? = null,
    @SerialName("workout_id")
    val workoutID: Int
)

@Serializable
data class MessageResponse(val message: String, val status: String? = null)