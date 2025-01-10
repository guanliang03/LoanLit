package com.example.loanapp.data

import com.example.loanapp.network.PrinterApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

interface AppContainer {
    val printerAppRepository: PrinterAppRepository
}

class DefaultAppContainer : AppContainer {
    private val baseUrl = "http://157.245.204.192:3000"
    val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)  // Set connection timeout
        .readTimeout(30, TimeUnit.SECONDS)     // Set read timeout
        .writeTimeout(60, TimeUnit.SECONDS)    // Set write timeout
        .build()

    /**
     * Use the Retrofit builder to build a retrofit object using a kotlinx.serialization converter
     */
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .client(client)
        .baseUrl(baseUrl)
        .build()

    /**
     * Retrofit service object for creating api calls
     */
    private val retrofitService: PrinterApiService by lazy {
        retrofit.create(PrinterApiService::class.java)
    }

    /**
     * DI implementation for Mars photos repository
     */
    override val printerAppRepository: PrinterAppRepository by lazy {
        NetworkPrinterAppRepository(retrofitService)
    }
}