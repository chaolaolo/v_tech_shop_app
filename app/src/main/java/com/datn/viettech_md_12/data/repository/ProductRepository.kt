package com.datn.viettech_md_12.data.repository

import com.datn.viettech_md_12.data.interfaces.ProductService
import com.datn.viettech_md_12.data.model.Product
import retrofit2.Response


class ProductRepository(
    private val apiService: ProductService
) {
   suspend fun getProductById(id:Int): Response<Product> = apiService.getProductById(id);
}