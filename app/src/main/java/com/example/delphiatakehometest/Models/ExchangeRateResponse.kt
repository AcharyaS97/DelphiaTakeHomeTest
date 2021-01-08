package com.example.delphiatakehometest.Models

/*
    This class is used to encapsulate the result of the network request and provide additional information if there was an error
 */
data class ExchangeRateResponse(val status: Status, val data: ExchangeRates?, val message : String?){
    enum class Status{
        SUCCESS,ERROR
    }

    //Static constructors for failure and success cases
    companion object {
        fun success(data: ExchangeRates) : ExchangeRateResponse {
            return ExchangeRateResponse(Status.SUCCESS,data,null)
        }

        fun error(message : String, data: ExchangeRates? = null) : ExchangeRateResponse {
            return ExchangeRateResponse(Status.ERROR,data,message)
        }
    }

}