package com.ilm.mulga.data.network

import android.util.Log
import androidx.compose.ui.res.stringResource
import com.ilm.mulga.data.service.HomeService
import com.ilm.mulga.BuildConfig
import com.ilm.mulga.R
import com.ilm.mulga.data.service.TransactionService
import com.ilm.mulga.domain.repository.local.UserLocalRepository
import com.ilm.mulga.domain.usecase.GetTokenUseCase
import com.ilm.mulga.util.handler.GlobalErrorHandler
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object RetrofitClient : KoinComponent {
    private const val BASE_URL = BuildConfig.BACKEND_HOST

    private val getTokenUseCase: GetTokenUseCase by inject()

    private val jsonParser = Json { ignoreUnknownKeys = true }

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

    private val errorInterceptor = Interceptor { chain ->
        val request = chain.request()
        val response = chain.proceed(request)

        // 응답 본문을 문자열로 읽습니다.
        val responseBodyString = response.body?.string()

        // 응답 본문이 null이 아니면 에러 필드 체크
        if (!responseBodyString.isNullOrEmpty()) {
            try {
                // JSON 파싱
                val jsonElement = jsonParser.parseToJsonElement(responseBodyString)
                if (jsonElement is JsonObject && jsonElement.containsKey("code")) {
                    // "code" 필드가 존재하면 에러로 판단하고, 해당 에러 메시지를 전역 에러 핸들러로 전달
                    val errorCode = jsonElement["code"]?.jsonPrimitive?.content ?: "UNKNOWN"
                    GlobalErrorHandler.emitError(errorCode)
                }
            } catch (e: Exception) {
                // JSON 파싱 실패 시 추가 로깅 처리 가능
                e.printStackTrace()
            }
        }

        // 응답 본문을 새로 생성해서 리턴 (이미 소모되었으므로)
        val newResponseBody = responseBodyString?.toResponseBody(response.body?.contentType())
        response.newBuilder().body(newResponseBody).build()
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(accessTokenInterceptor)
        .addInterceptor(acceptHeaderInterceptor)
        .addInterceptor(loggingInterceptor)
        .addInterceptor(errorInterceptor)
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
