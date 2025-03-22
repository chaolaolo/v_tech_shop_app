package com.datn.viettech_md_12.data.repository

import com.datn.viettech_md_12.data.model.CartModel
import com.datn.viettech_md_12.data.remote.AppService
import retrofit2.Response


interface AppRepository {
    suspend fun getCart():  Response<CartModel>
}

class NetworkAppRepository(
    private val apiService: AppService
) : AppRepository {
    override suspend fun getCart(): Response<CartModel> = apiService.getCart()
}