package com.example.tension.domain

import com.example.tension.data.reps.InternalDataRep
import com.example.tension.data.reps.NetworkDataRep
import kotlinx.coroutines.runBlocking
import org.koin.core.annotation.Single

@Single
class AiAgentUseCase(private val networkRep: NetworkDataRep, private val internalRep: InternalDataRep) {
    init {
        runBlocking {
            internalRep.forgetMessageId()
        }
    }
    suspend fun execute(message: String): Result<String> {
        val token = internalRep.getToken()
        if (token.isBlank()) {
            return Result.failure(Exception("Пользователь не авторизован"))
        }

        val lastMessageId = internalRep.getMessageId()

        val result = networkRep.callAgent(
            message = message,
            parentMessageId = lastMessageId
        )

        result.onSuccess { response ->
            internalRep.setMessageId(response.id)
        }

        return result.map { it.message }
    }

    suspend fun resetChat() {
        internalRep.forgetMessageId()
    }
}