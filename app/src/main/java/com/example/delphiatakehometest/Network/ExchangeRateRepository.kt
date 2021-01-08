package com.example.delphiatakehometest.Network

import com.example.delphiatakehometest.Models.ExchangeRates
import com.example.delphiatakehometest.Models.ExchangeRateResponse
import javax.inject.Inject

/*
    This Repository class is an interface for all means of data access.
    It exposes a method getRate that makes a network call to the API and returns the result
 */
class ExchangeRateRepository @Inject constructor(private val service : NetworkClient){

    suspend fun getRate(symbol: String) : ExchangeRateResponse {

        try{
            val response = service.getRatesWithBase(symbol)

            if (response.isSuccessful){

                val body = response.body()
                val currencyListResponse = body?.rates;
                val sortedList = currencyListResponse?.sortedWith(compareBy{it.symbol})

                return ExchangeRateResponse.success(ExchangeRates(sortedList!!))
            }
            return ExchangeRateResponse.error("${response.code()}, ${response.message()}")
        }
        catch (e: Exception){
            return ExchangeRateResponse.error(e.message ?: e.toString())
        }

    }




}