package com.ilm.mulga.data.network

import com.ilm.mulga.data.service.AnalysisService
import com.ilm.mulga.BuildConfig
import com.ilm.mulga.data.service.HomeService
import com.ilm.mulga.data.service.TransactionService
import com.ilm.mulga.domain.usecase.GetTokenUseCase
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import okhttp3.MediaType.Companion.toMediaType
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object RetrofitClient : KoinComponent {
    private const val BASE_URL = BuildConfig.BACKEND_HOST

    private val getTokenUseCase: GetTokenUseCase by inject()

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val accessTokenInterceptor = Interceptor { chain ->
        val userToken =getTokenUseCase() ?: ""
        val token = "Bearer $userToken"
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
    val apiAnalysisService: AnalysisService = retrofit.create(AnalysisService::class.java)
}
