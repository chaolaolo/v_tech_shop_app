package com.datn.viettech_md_12.data.interfaces

import com.datn.viettech_md_12.data.model.CartModel
import com.datn.viettech_md_12.data.model.Product
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProductService {
    @GET("/shop/product/{id}")
    suspend fun getProductById(@Path("id") id: Int): Response<Product>

    @PUT
    suspend fun updateCart(cartModel: CartModel): CartModel
}