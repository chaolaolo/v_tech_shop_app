package com.datn.viettech_md_12.data.repository

import FavoriteListResponse
import FavoriteRequest
import FavoriteResponse
import android.util.Log
import com.datn.viettech_md_12.data.interfaces.ProductService
import com.datn.viettech_md_12.data.model.ProductListResponse
import com.datn.viettech_md_12.data.model.ProductModel
import com.datn.viettech_md_12.data.model.ProductResponse
import retrofit2.Response


class ProductRepository(
    private val apiService: ProductService
) {
    suspend fun getProductById(id: String): Response<ProductResponse> =
        apiService.getProductById(id);

    suspend fun getAllProducts(): Response<ProductListResponse> = apiService.getAllProducts()
    suspend fun addToFavorites(
        favoriteRequest: FavoriteRequest,
        token: String,
        clientId: String,  // Thêm tham số clientId
    ): Response<FavoriteResponse> {
        Log.d("dcm_debug_api_call", "Sending Favorite Request: Body = $favoriteRequest, Token = $token, ClientId = $clientId")

        return apiService.addProductToFavorites(favoriteRequest, token, clientId)  // Truyền clientId vào đây
    }

    suspend fun getFavoriteProducts(
        token: String,
        clientId: String
    ): Response<FavoriteListResponse> {
        Log.d("dcm_debug_fav", "Fetching favorite products with Token: $token and ClientId: $clientId")
        return apiService.getFavoriteProducts(token, clientId)
    }
}