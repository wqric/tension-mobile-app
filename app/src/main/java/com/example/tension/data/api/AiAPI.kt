package com.example.tension.data.api

import com.example.tension.BuildConfig
import com.example.tension.domain.AgentCallRS
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.koin.core.annotation.Single

@Single
class AiAPI(private val client: HttpClient) {

    companion object {
        const val BASE_URL = "https://agent.timeweb.cloud/api/v1/cloud-ai/agents"
        const val AGENT_ID = "e0335005-3525-447e-992d-49fe41436cbe"
    }


    suspend fun callAgent(req: AgentCallRQ): AgentCallRS {
        return client.post("$BASE_URL/$AGENT_ID/call") {
            contentType(ContentType.Application.Json)
            bearerAuth(BuildConfig.AI_TOKEN)
            setBody(req)
        }.body<AgentCallRS>()
    }
}