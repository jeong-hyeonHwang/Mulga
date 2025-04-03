package com.example.mulga.data.network

import com.example.mulga.BuildConfig
import com.example.mulga.data.service.HomeService
import com.example.mulga.data.service.TransactionService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import okhttp3.MediaType.Companion.toMediaType

object RetrofitClient {
    private const val BASE_URL = BuildConfig.BACKEND_HOST

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val accessTokenInterceptor = Interceptor { chain ->
        val token = "Bearer " + BuildConfig.TEST_ACCESS_TOKEN
        val request = chain.request().newBuilder()
            .addHeader("Authorization", token)
            .build()
        chain.proceed(request)
    }

    private val acceptHeaderInterceptor = Interceptor { chain ->
        val original = chain.request()
        val request = original.newBuilder()
            .addHeader("Accept", "application/json")
            .build()
        chain.proceed(request)
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(accessTokenInterceptor)
        .addInterceptor(acceptHeaderInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()

    private val json = Json { ignoreUnknownKeys = true }

    @OptIn(ExperimentalSerializationApi::class)
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    val transactionService: TransactionService = retrofit.create(TransactionService::class.java)
    val homeService: HomeService = retrofit.create(HomeService::class.java)
}
