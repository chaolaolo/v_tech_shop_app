package com.datn.viettech_md_12.data.remote
import com.datn.viettech_md_12.data.model.CartModel
import retrofit2.http.GET
import retrofit2.http.PUT

interface CartApiService {
    @GET("cart/add")
    suspend fun getCart(): CartModel

    @PUT
    suspend fun updateCart(cartModel: CartModel): CartModel
}