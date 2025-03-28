package com.datn.viettech_md_12.data.interfaces

import com.datn.viettech_md_12.data.model.CartModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PUT
import retrofit2.http.Query

interface CartService {
    @Headers(
//        "authorization: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI2N2NjMGY4YzAyZTM5ZWJlOWY3YjYwZDUiLCJ1c2VybmFtZSI6ImN1c3RvbWVyMDMiLCJpYXQiOjE3NDMxNDE0MDksImV4cCI6MTc0MzMxNDIwOX0.76hCWIOH1suWcgIz5zZiG40HLEXQanojSsM3EBRl7x0",
        "x-api-key: c244dcd1532c91ab98a1c028e4f24f81457cdb2ac83e2ca422d36046fec84233589a4b51eda05e24d8871f73653708e3b13cf6dd1415a6330eaf6707217ef683",
//        "x-client-id: 67cc0f8c02e39ebe9f7b60d5"
    )
    @GET("cart/")
    suspend fun getCart(
        @Header("authorization") token: String,
        @Header("x-client-id") userId: String,
        @Query("userId") userIdQuery: String
    ): Response<CartModel>

    @PUT("cart/")
    suspend fun updateCart(
        @Header("authorization") token: String,
        @Header("x-client-id") userId: String,
        @Body cart: CartModel
    ): Response<CartModel>

}