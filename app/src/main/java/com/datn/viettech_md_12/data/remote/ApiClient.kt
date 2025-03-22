package com.datn.viettech_md_12.data.remote

import com.datn.viettech_md_12.data.repository.AppRepository
import com.datn.viettech_md_12.data.repository.NetworkAppRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val appRepository: AppRepository
}


class DefaultAppContainer : AppContainer {
    private val baseUrl = " http://103.166.184.249:3056/v1/api/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    private val retrofitService: AppService by lazy {
        retrofit.create(AppService::class.java)
    }

    override val appRepository: AppRepository by lazy {
        NetworkAppRepository(retrofitService)
    }
}