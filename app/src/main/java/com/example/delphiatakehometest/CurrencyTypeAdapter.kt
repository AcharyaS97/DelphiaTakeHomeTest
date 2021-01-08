package com.example.delphiatakehometest

import com.example.delphiatakehometest.Models.Currency
import com.example.delphiatakehometest.Models.ExchangeRates
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.math.RoundingMode
import java.text.DecimalFormat

/*
    This class overrides the TypeAdapter class from the Gson library
    and is used to deserialize the response from the Rates API into
    a ExchangeRates object
 */
class CurrencyTypeAdapter : TypeAdapter<ExchangeRates>() {
    override fun write(out: JsonWriter?, value: ExchangeRates?) {
        //Don't need to implement this because we're not converting into json
    }

    //Used by Gson to read json into ExchangeRates object
    override fun read(reader: JsonReader?): ExchangeRates {
        reader?.beginObject()

        //Arbitrarily deciding to format exchange rates to 4 decimal places
        val decimalFormat = DecimalFormat("#.####")
        decimalFormat.roundingMode = RoundingMode.CEILING
        val currencyList = ArrayList<Currency>()

        while (reader?.hasNext()!!){

            //Deciding only to deserialize the rates object in the response and not worry base or dates members since they aren't used in the app
            when(reader.nextName()){
                "base" -> reader.skipValue()
                "date" -> reader.skipValue()
                "rates" -> {

                    reader.beginObject()

                    while (reader.peek() != JsonToken.END_OBJECT){
                        val symbol = reader.nextName();
                        val rateString = reader.nextDouble()
                        val formattedDoubleString = decimalFormat.format(rateString)
                        val currency = Currency(symbol!!,formattedDoubleString)
                        currencyList.add(currency)
                    }
                    reader.endObject()
                }

            }

        }
        reader.endObject()
        return ExchangeRates(currencyList)
    }
}