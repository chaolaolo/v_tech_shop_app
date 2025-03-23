package com.datn.viettech_md_12.data.repository

import com.datn.viettech_md_12.data.interfaces.CartService
import com.datn.viettech_md_12.data.model.CartModel
import retrofit2.Response


class CartRepository(
    private val apiService: CartService
) {
   suspend fun getCart(): Response<CartModel> = apiService.getCart()
}