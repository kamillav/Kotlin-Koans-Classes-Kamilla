
package edu.trincoll.jsonplaceholder

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class BlogPost(
    //val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)

class JsonPlaceholderService {
    private val baseUrl = "https://jsonplaceholder.typicode.com"

    private fun createHttpClient(): HttpClient {
        return HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }
    }

    suspend fun getPosts(): List<BlogPost> =
        createHttpClient().use { client ->
            client.get("$baseUrl/posts") {
                accept(ContentType.Application.Json)
            }.body()
        }

    suspend fun getPost(index: Int): HttpResponse =
        createHttpClient().use { client ->
            client.get("$baseUrl/posts/$index") {
                accept(ContentType.Application.Json)
            }
        }

    suspend fun insertPost(post: BlogPost): HttpResponse =
        createHttpClient().use { client ->
            client.post("$baseUrl/posts") {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                setBody(post)
            }
        }

    suspend fun updatePost(post: BlogPost): BlogPost =
        createHttpClient().use { client ->
            client.put("$baseUrl/posts/${post.id}") {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                setBody(post)
            }.body()
        }

    suspend fun deletePost(index: Int): HttpResponse =
        createHttpClient().use { client ->
            client.delete("$baseUrl/posts/$index")
        }
}
