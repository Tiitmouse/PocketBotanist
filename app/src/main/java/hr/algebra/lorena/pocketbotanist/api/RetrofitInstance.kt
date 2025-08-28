package hr.algebra.lorena.pocketbotanist.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://68b025d33b8db1ae9c02e880.mockapi.io/"

    val api: PlantApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PlantApiService::class.java)
    }
}