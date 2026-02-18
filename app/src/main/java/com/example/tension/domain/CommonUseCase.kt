package com.example.tension.domain

import com.example.tension.data.reps.InternalDataRep
import com.example.tension.data.reps.NetworkDataRep
import com.example.tension.presentation.models.MessageResponse
import com.example.tension.presentation.models.User
import com.example.tension.presentation.models.UserStats
import com.example.tension.presentation.models.Workout
import org.koin.core.annotation.Single
import kotlin.onSuccess

@Single
class CommonUseCase(
    private val networkDataRep: NetworkDataRep,
    private val internalDataRep: InternalDataRep
) {

    suspend fun logout() {
        internalDataRep.forgetToken()
    }
    suspend fun login(email: String, password: String): Result<User> {
        val res = networkDataRep.login(email, password)
        res.onSuccess { internalDataRep.setToken(it.token) }
        return res
    }

    suspend fun register(email: String, password: String): Result<User> {
        val res =networkDataRep.register(email, password)
        res.onSuccess { internalDataRep.setToken(it.token) }
        return res
    }

    suspend fun getUserStats(): Result<UserStats> {
        val token = internalDataRep.getToken()
        return networkDataRep.getUserStats(token)
    }

    suspend fun getProfile(): Result<User> {
        val token = internalDataRep.getToken()
        val res = networkDataRep.getProfile(token)
        res.onSuccess { internalDataRep.setToken(it.token) }
        return res

    }

    suspend fun updateProfile(
        name: String? = null,
        weight: Float? = null,
        height: Float? = null,
        aim: Int? = null,
        difficult: Int? = null
    ): Result<User> {
        val token = internalDataRep.getToken()
        return networkDataRep.updateProfile(
            token = token,
            name = name,
            weight = weight,
            height = height,
            aim = aim,
            difficult = difficult
        )
    }

    suspend fun getUserWorkouts(): Result<List<Workout>> {
        val token = internalDataRep.getToken()
        return networkDataRep.getUserWorkouts(token)
    }

    suspend fun generateWorkouts(): Result<List<Workout>> {
        val token = internalDataRep.getToken()
        return networkDataRep.generateWorkouts(token)
    }

    suspend fun markWorkoutDone(workoutId: Int, date: String): Result<MessageResponse> {
        val token = internalDataRep.getToken()
        return networkDataRep.markWorkoutDone(token, workoutId, date)
    }
}

