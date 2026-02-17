package com.example.tension.data.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
data class WorkoutCompleteResponse(
    val message: String,
    val status: String? = null,
    @SerialName("workout_id")
    val workoutID: Int
)
@Serializable
data class AgentCallRQ(
    val message: String,
    @SerialName("parent_message_id")
    val parentMessageId: String? = null
)

