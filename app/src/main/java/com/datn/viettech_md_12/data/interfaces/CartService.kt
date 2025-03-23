package com.datn.viettech_md_12.data.interfaces

import com.datn.viettech_md_12.data.model.CartModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PUT

interface CartService {
    @GET("cart/")
    suspend fun getCart(): Response<CartModel>

    @PUT
    suspend fun updateCart(cartModel: CartModel): CartModel
}