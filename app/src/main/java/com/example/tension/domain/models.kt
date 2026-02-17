package com.example.tension.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable



@Serializable
data class AgentCallRS(
    val id: String,
    val message: String,
    val role: String? = null,
    @SerialName("created_at")
    val createdAt: String ? = null
)