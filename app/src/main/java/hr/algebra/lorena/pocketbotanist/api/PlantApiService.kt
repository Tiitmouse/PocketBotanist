package hr.algebra.lorena.pocketbotanist.api

import retrofit2.http.GET

interface PlantApiService {
    @GET("/plants")
    suspend fun getPlants(): List<ApiPlant>
}