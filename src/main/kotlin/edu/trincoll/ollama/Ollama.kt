
package edu.trincoll.ollama

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class OllamaRequest(
    val model: String,
    val prompt: String,
    val stream: Boolean,
)

@Serializable
data class OllamaResponse(
    val model: String,
    val response: String,
)

suspend fun main() {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 60000000 // Set the request timeout to 60 seconds
        }
    }

    try {
        val response = client.post("http://localhost:11434/api/generate") {
            contentType(ContentType.Application.Json)
            setBody(OllamaRequest("llama3.2", "What do you think about having a unicurrency all around the world in 300 words?", false))
        }

        println("Response status: ${response.status.value}")

        if (response.status.isSuccess()) {
            val ollamaResponse = response.body<OllamaResponse>()
            println("Response body: $ollamaResponse")
        } else {
            println("Request failed with status: ${response.status.value}")
        }
    } catch (e: Exception) {
        println("An error occurred: ${e.message}")
        e.printStackTrace()
    } finally {
        client.close()
    }
}
