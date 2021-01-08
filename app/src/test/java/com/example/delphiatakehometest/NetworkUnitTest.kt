package com.example.delphiatakehometest

import com.example.delphiatakehometest.Models.ExchangeRates

import com.example.delphiatakehometest.Network.NetworkClient
import com.google.gson.GsonBuilder

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations
import java.net.HttpURLConnection
import org.junit.Assert.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
class NetworkUnitTest {

    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    private lateinit var mockWebServer : MockWebServer

    lateinit var retrofitClient : NetworkClient

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {

        Dispatchers.setMain(testCoroutineDispatcher)
        MockitoAnnotations.initMocks(this)
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val okHttpClient = OkHttpClient
            .Builder()
            .build()

        retrofitClient = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().registerTypeAdapter(ExchangeRates::class.java,CurrencyTypeAdapter()).create()))
            .client(okHttpClient)
            .build()
            .create(NetworkClient::class.java)
    }


    @Test
    fun `read success json file`(){
        val reader = MockResponseFileReader("test_success_response.json")
        assertNotNull(reader.content)
    }

    @Test
    fun `Test Valid Network Call`(){

        //Mock a response object from the MockWebServer from the success json file
        val mockResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(MockResponseFileReader("test_success_response.json").content)
        mockWebServer.enqueue(mockResponse)

        //Use NetworkClient object to make an API call same as it is done in the app for a sample currency input of CAD
        val actualResponse = runBlocking { retrofitClient.getRatesWithBase("CAD") }

        assertEquals(
            mockResponse.toString().contains("200"),
            actualResponse.code().toString().contains("200")
        )
    }

    @Test
    fun `Test Invalid Network Call`(){

        //Mock a response object from the MockWebServer from the failure json file
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST)
            .setBody(MockResponseFileReader("test_failure_response.json").content)
        mockWebServer.enqueue(response)

        // Act
        val actualResponse = runBlocking { retrofitClient.getRatesWithBase("sdf") }

        // Assert
        assertEquals(
            response.toString().contains("400"),
            actualResponse.code().toString().contains("400")
        )
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        Dispatchers.resetMain()
    }

}