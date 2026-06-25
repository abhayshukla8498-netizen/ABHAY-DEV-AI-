package com.example.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit
import com.example.BuildConfig

interface GeminiService {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GenerateContentRequest
    ): GeminiResponse
}

object GeminiClient {
    val moshi: Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://generativelanguage.googleapis.com/")
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val service: GeminiService = retrofit.create(GeminiService::class.java)

    fun getApiKey(): String {
        return BuildConfig.GEMINI_API_KEY
    }

    fun parseGeneratedProject(jsonString: String): GeneratedProjectResult? {
        val cleanJson = jsonString.trim()
            .removePrefix("```json")
            .removePrefix("```")
            .removeSuffix("```")
            .trim()
        return try {
            val adapter = moshi.adapter(GeneratedProjectResult::class.java)
            adapter.fromJson(cleanJson)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    fun projectResultToJson(result: GeneratedProjectResult): String {
        return try {
            val adapter = moshi.adapter(GeneratedProjectResult::class.java)
            adapter.toJson(result)
        } catch (e: Exception) {
            ""
        }
    }
}
