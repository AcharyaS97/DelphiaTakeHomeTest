package com.example.delphiatakehometest.DI

import com.example.delphiatakehometest.CurrencyTypeAdapter
import com.example.delphiatakehometest.Network.NetworkClient
import com.example.delphiatakehometest.Models.ExchangeRates
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class ApplicationModule {

    @Provides
    fun provideCurrencyAdapter() = CurrencyTypeAdapter()

    @Provides
    fun provideGson(currencyTypeAdapter: CurrencyTypeAdapter): Gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().registerTypeAdapter(
        ExchangeRates::class.java,currencyTypeAdapter).create()

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl("https://api.ratesapi.io")
            .build()

    @Provides
    @Singleton
    fun provideRetrofitService(retrofit: Retrofit): NetworkClient = retrofit.create(NetworkClient::class.java)

}