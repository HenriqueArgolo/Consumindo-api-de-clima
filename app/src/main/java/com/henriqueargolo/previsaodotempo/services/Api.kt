package com.henriqueargolo.previsaodotempo.services


import com.henriqueargolo.previsaodotempo.model.Weather
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.json.Json


class Api {
    private val apiKey : String = "47176ac219a9d3d12976d24e068ec7fe"
    suspend fun getWeather(city: String): Flow<Weather> {
        val client = HttpClient{
            install(ContentNegotiation){
                json(Json { ignoreUnknownKeys = true })
            }
            install(HttpCache)
        }
        val result = client
            .get("https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$apiKey")
            .body<Weather>()
        return MutableStateFlow(result)
    }

}