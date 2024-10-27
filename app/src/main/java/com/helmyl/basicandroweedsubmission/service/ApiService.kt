package com.helmyl.basicandroweedsubmission.service

import com.google.gson.GsonBuilder
import com.helmyl.basicandroweedsubmission.model.PostResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

interface ApiService {
    @GET("posts")
    suspend fun getPosts(): PostResponse
}

object RetrofitInstance {
    private const val BASE_URL = "https://dummyjson.com/"

    private val client = OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }).connectTimeout(15, TimeUnit.SECONDS).readTimeout(15, TimeUnit.SECONDS).build()

    private val gson = GsonBuilder().setLenient().create()

    private val retrofit by lazy {
        Retrofit.Builder().baseUrl(BASE_URL).client(client)
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
    }

    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
