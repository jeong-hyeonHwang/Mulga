package com.ilm.mulga.data.network

import android.util.Log
import com.ilm.mulga.BuildConfig
import com.ilm.mulga.data.datasource.local.UserLocalDataSource
import com.ilm.mulga.data.repository.AuthRepositoryImpl
import com.ilm.mulga.data.service.AnalysisService
import com.ilm.mulga.data.service.HomeService
import com.ilm.mulga.data.service.TransactionService
import com.ilm.mulga.data.service.UserService
import com.ilm.mulga.domain.usecase.GetTokenUseCase
import com.ilm.mulga.util.handler.GlobalErrorHandler
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.Retrofit
import java.io.IOException

object RetrofitClient : KoinComponent {
    private const val BASE_URL = BuildConfig.BACKEND_HOST
    private const val TOKEN_EXPIRED_ERROR = "COMMON_1005"  // 토큰 만료 에러 코드

    private val getTokenUseCase: GetTokenUseCase by inject()
    private val userLocalDataSource: UserLocalDataSource by inject()
    private val authRepository: AuthRepositoryImpl by inject()

    private val jsonParser = Json { ignoreUnknownKeys = true }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val accessTokenInterceptor = Interceptor { chain ->
        val userToken = getTokenUseCase() ?: ""
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
        var response = chain.proceed(request)

        // 이미 재시도된 요청인지 확인
        if (request.header("Error-Retry") != null) {
            return@Interceptor response
        }

        // 응답 본문을 문자열로 읽습니다.
        val responseBodyString = response.body?.string()
        var errorCode: String? = null

        // 응답 본문이 null이 아니면 에러 필드 체크
        if (!responseBodyString.isNullOrEmpty()) {
            try {
                // JSON 파싱
                val jsonElement = jsonParser.parseToJsonElement(responseBodyString)
                if (jsonElement is JsonObject && jsonElement.containsKey("code")) {
                    // "code" 필드가 존재하면 에러로 판단하고, 해당 에러 메시지를 저장
                    errorCode = jsonElement["code"]?.jsonPrimitive?.content

                    // 토큰 만료 에러(COMMON_1005) 처리
                    if (errorCode == TOKEN_EXPIRED_ERROR && !request.url.encodedPath.contains("/auth/")) {
                        Log.d("RetrofitClient", "토큰 만료 감지: COMMON_1005")

                        // 토큰 갱신 시도
                        val newToken = runBlocking {
                            try {
                                val result = authRepository.getCurrentUserToken()
                                if (result.isSuccess) {
                                    result.getOrNull()
                                } else {
                                    null
                                }
                            } catch (e: Exception) {
                                Log.e("RetrofitClient", "토큰 갱신 실패", e)
                                null
                            }
                        }

                        // 토큰 갱신 성공 시 원래 요청 재시도
                        if (newToken != null) {
                            response.close() // 기존 응답 닫기

                            Log.d("RetrofitClient", "토큰 갱신 성공, 요청 재시도")
                            val newRequest = request.newBuilder()
                                .header("Authorization", "Bearer $newToken")
                                .header("Error-Retry", "true") // 재시도 표시
                                .build()
                            return@Interceptor chain.proceed(newRequest)
                        } else {
                            // 토큰 갱신 실패 시 전역 에러 핸들러에 알림
                            GlobalErrorHandler.emitError("AUTH_REFRESH_FAILED")
                        }
                    } else if (errorCode != null) {
                        // 다른 에러 코드는 전역 에러 핸들러로 전달
                        GlobalErrorHandler.emitError(errorCode)
                    }
                }
            } catch (e: Exception) {
                // JSON 파싱 실패 시 추가 로깅 처리
                Log.e("RetrofitClient", "JSON 파싱 실패", e)
                e.printStackTrace()
            }
        }

        // 응답 본문을 새로 생성해서 리턴 (이미 소모되었으므로)
        val newResponseBody = responseBodyString?.toResponseBody(response.body?.contentType())
        response.newBuilder().body(newResponseBody).build()
    }

    private val okHttpClient = OkHttpClient.Builder()
        .authenticator(TokenAuthenticator(userLocalDataSource, authRepository))
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
    val userService: UserService = retrofit.create(UserService::class.java)
    val apiAnalysisService: AnalysisService = retrofit.create(AnalysisService::class.java)
}