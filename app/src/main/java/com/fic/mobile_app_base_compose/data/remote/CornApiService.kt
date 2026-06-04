package com.fic.mobile_app_base_compose.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface CornApiService {
    // Usamos el endpoint de Alpha Vantage para el precio del Maíz (Commodity)
    @GET("query?function=CORN&interval=monthly&apikey=demo") 
    suspend fun getCornPrice(): CornPriceResponse

    companion object {
        private const val BASE_URL = "https://www.alphavantage.co/"

        fun create(): CornApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CornApiService::class.java)
        }
    }
}
