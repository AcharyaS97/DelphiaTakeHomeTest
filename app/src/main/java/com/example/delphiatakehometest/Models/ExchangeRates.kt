package com.example.delphiatakehometest.Models

import com.example.delphiatakehometest.Models.Currency

/*
    The response of the call to RatesAPI is deserialized into this object through Gson
 */
data class ExchangeRates(val rates : List<Currency>)