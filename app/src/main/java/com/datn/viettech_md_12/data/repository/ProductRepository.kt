package com.datn.viettech_md_12.data.repository

import com.datn.viettech_md_12.data.interfaces.ProductService
import com.datn.viettech_md_12.data.model.ProductListResponse
import com.datn.viettech_md_12.data.model.ProductResponse
import retrofit2.Response


class ProductRepository(
    private val apiService: ProductService
) {
    suspend fun getProductById(id: String): Response<ProductResponse> =
        apiService.getProductById(id);

    suspend fun getAllProducts(): Response<ProductListResponse> = apiService.getAllProducts()

}