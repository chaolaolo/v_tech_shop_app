package com.datn.viettech_md_12.data.repository

import com.datn.viettech_md_12.data.interfaces.CartService
import com.datn.viettech_md_12.data.model.CartModel
import com.datn.viettech_md_12.data.remote.ApiClient.cartService
import retrofit2.Response


class CartRepository(
    private val apiService: CartService
) {
    //   suspend fun getCart(): Response<CartModel> = apiService.getCart()
    suspend fun getCart(token:String, userId:String, userIdQuery:String): Response<CartModel> {
        return cartService.getCart(
            token = token,
            userId = userId,
            userIdQuery = userIdQuery
        )
    }

    suspend fun updateCart(token: String, userId: String, cart: CartModel): Response<CartModel> {
        return cartService.updateCart(token, userId, cart)
    }
}