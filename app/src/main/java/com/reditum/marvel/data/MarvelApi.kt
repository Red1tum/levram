package com.reditum.marvel.data

import com.reditum.marvel.BuildConfig
import com.reditum.marvel.ui.theme.PrimaryColors
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.URLProtocol
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json
import java.security.MessageDigest

const val REQUEST_LIMIT = 10

@Serializable
data class MarvelResponse(
    val data: Data
)

@Serializable
data class Data(val results: List<Character>)

@Serializable
data class Character(
    val id: Int,
    val name: String,
    var description: String,
    val thumbnail: Thumbnail,
    @Transient var colors: PrimaryColors? = null
)

@Serializable
data class Thumbnail(
    val path: String,
    val extension: String
) {
    fun getUrl(): String = "$path.$extension"
}

object MarvelApi {
    private const val publicKey = "c6d2683bc9729bcc96c058669db70798"
    private const val privateKey = BuildConfig.apiKey
    private const val timestamp = "1"

    private const val NOT_AVAILABLE = "image_not_available"

    private val client = createClient()

    private fun createClient() = HttpClient(OkHttp) {
        expectSuccess = true

        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    encodeDefaults = true
                }
            )
        }

        defaultRequest {
            url {
                protocol = URLProtocol.HTTPS
                host = "gateway.marvel.com"
                path("v1/public/")
                parameters.append("ts", timestamp)
                parameters.append("apikey", publicKey)
                parameters.append("hash", getHash())
            }

        }
    }

    suspend fun getHeroes(offset: Int): Result<List<Character>> = runCatching {
        val res = client.get("characters") {
            parameter("limit", REQUEST_LIMIT)
            parameter("offset", offset)
            parameter("orderBy", "name")
        }.body<MarvelResponse>()

        res.data.results.filter { !it.thumbnail.path.endsWith(NOT_AVAILABLE) }
    }

    suspend fun getHero(id: Int): Result<Character> = runCatching {
        val res = client.get("characters/$id").body<MarvelResponse>()
        res.data.results.first()
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun getHash(): String {
        val md5 = MessageDigest.getInstance("MD5")
        return md5.digest("$timestamp$privateKey$publicKey".toByteArray()).toHexString()
    }
}