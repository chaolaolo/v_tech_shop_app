package com.datn.viettech_md_12.data.remote

import com.datn.viettech_md_12.data.interfaces.CartService
import com.datn.viettech_md_12.data.interfaces.ProductService
import com.datn.viettech_md_12.data.repository.CartRepository
import com.datn.viettech_md_12.data.repository.ProductRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiClient {
    private const val BASE_URL = "http://103.166.184.249:3056/v1/api/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    val cartService: CartService by lazy {
        retrofit.create(CartService::class.java)
    }

    val productService: ProductService by lazy {
        retrofit.create(ProductService::class.java)
    }

    val cartRepository: CartRepository by lazy {
        CartRepository(cartService)
    }

    val productRepository: ProductRepository by lazy {
        ProductRepository(productService)
    }
}