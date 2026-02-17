package com.example.tension.presentation.viewmodels

import android.util.Log
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tension.domain.CommonUseCase
import com.example.tension.presentation.models.User
import com.example.tension.presentation.models.UserStats
import com.example.tension.presentation.models.Workout
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import java.time.LocalDate


@KoinViewModel
class MainVM(private val useCase: CommonUseCase) : ViewModel() {
    val user = mutableStateOf<User?>(null)
    val emailState = mutableStateOf("")
    val passwordState = mutableStateOf("")
    val passwordRepeatState = mutableStateOf("")
    val userStats = mutableStateOf<UserStats?>(null)
    val userWorkouts = mutableStateOf<List<Workout>?>(null)

    val currentWorkout = derivedStateOf {
        val todayString = LocalDate.now().toString() // "2026-02-08"
        userWorkouts.value?.find { workout ->
            workout.date.take(10) == todayString
        }
    }
    val errorState = mutableStateOf("")

    init {
        getProfile()
    }

    fun updateUser(
        name: String,
        weight: Double,
        height: Double,
        aim: Int,
        difficult: Int
    ) = viewModelScope.launch {
        val res = useCase.updateProfile(

                name = name,
                weight = weight.toFloat(),
                height = height.toFloat(),
                aim = aim,
                difficult = difficult
        )

        res.fold(
            onSuccess = {
                Log.d("user", "Profile updated: $it")
                getData()
            },
            onFailure = {
                Log.d("er", "Update failed: $it")
            }
        )
    }

    fun logout() = viewModelScope.launch {
        user.value = null
        useCase.logout()
    }

    fun getData() = viewModelScope.launch {
        val stats = useCase.getUserStats()
        stats.fold(
            onSuccess = {
                userStats.value = it
                Log.d("user", it.toString())
            },
            onFailure = {
                Log.d("er", it.toString())
            }
        )
        val workouts = useCase.getUserWorkouts()
        workouts.fold(
            onSuccess = {
                userWorkouts.value = it
                Log.d("user", it.toString())
            },
            onFailure = {
                Log.d("er", it.toString())
            }
        )
    }

    fun completeCurrentWorkout() = viewModelScope.launch {
        val workout = currentWorkout.value ?: return@launch

        val res = useCase.markWorkoutDone(
            workout.id,
            workout.date.take(10)
        )
        res.fold(
            onSuccess = {
                Log.d("user", it.toString())
                userWorkouts.value = userWorkouts.value?.map { w ->
                    if (w.id == workout.id) {
                        w.copy(isDone = true)
                    } else {
                        w
                    }
                }
            },
            onFailure = {
                Log.d("er", it.toString())
            }
        )
    }

    fun getProfile() = viewModelScope.launch {
        val res = useCase.getProfile()
        res.fold(
            onSuccess = {
                user.value = it
                Log.d("user", it.toString())
            },
            onFailure = {
                Log.d("er", it.toString())
            }
        )
    }

    fun reg() = viewModelScope.launch {
        val res = useCase.register(emailState.value, passwordState.value)
        res.fold(
            onSuccess = {
                user.value = it
                getData()
                Log.d("user", it.toString())
            },
            onFailure = {
                Log.d("er", it.toString())
                errorState.value = "Ошибка авторизации"
            }
        )
    }

    fun login() = viewModelScope.launch {
        val res = useCase.login(emailState.value, passwordState.value)
        res.fold(
            onSuccess = {
                user.value = it
                getData()
                Log.d("user", it.toString())
            },
            onFailure = {
                Log.d("er", it.toString())
                if ("401" in it.toString()) {
                    errorState.value = "Неверные данные"
                } else
                    errorState.value = "Неизвестная ошибка"
            }
        )
    }
}