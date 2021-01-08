package com.example.delphiatakehometest.Network

import com.example.delphiatakehometest.Models.ExchangeRates
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkClient {
    @GET("/api/latest")
    suspend fun getRatesWithBase(@Query("base") baseCurrency : String) : Response<ExchangeRates>
}