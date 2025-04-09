package com.datn.viettech_md_12.data.repository

import FavoriteListResponse
import FavoriteRequest
import FavoriteResponse
import android.util.Log
import com.datn.viettech_md_12.data.interfaces.ProductService
import com.datn.viettech_md_12.data.model.OrderListResponse
import com.datn.viettech_md_12.data.model.OrderModel
import com.datn.viettech_md_12.data.model.Product
import com.datn.viettech_md_12.data.model.ProductByCateModelResponse
import com.datn.viettech_md_12.data.model.ProductListResponse
import com.datn.viettech_md_12.data.model.ProductResponse
import com.datn.viettech_md_12.data.model.SearchResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

    suspend fun getProductsByCategory(categoryId: String): Response<ProductByCateModelResponse> =
        apiService.getProductsByCategory(categoryId)

    suspend fun getFavoriteProducts(
        token: String,
        clientId: String
    ): Response<FavoriteListResponse> {
        Log.d("dcm_debug_fav", "Fetching favorite products with Token: $token and ClientId: $clientId")
        return apiService.getFavoriteProducts(token, clientId)
    }
    suspend fun removeFromFavorites(
        productId: String,
        token: String,
        clientId: String,
        apiKey: String
    ): Response<Void> {
        return apiService.removeProductFromFavorites(productId, token, clientId, apiKey)
    }

    suspend fun searchProducts(query: String): Response<SearchResponse> {
        return withContext(Dispatchers.IO) {
            apiService.searchProducts(query)
        }
    }
    //hien thi don hang
    suspend fun getUserOrders(userId: String,token: String, clientId: String): Response<OrderListResponse> {
        return apiService.getUserOrders(userId,token, clientId)
    }
    //hien thi chi tiet don hang
    suspend fun getBillById(orderId : String, token: String,clientId: String) :Response<OrderModel>{
        return apiService.getBillById(orderId,token, clientId)
    }

}