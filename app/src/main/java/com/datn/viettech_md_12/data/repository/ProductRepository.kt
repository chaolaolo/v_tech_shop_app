package com.datn.viettech_md_12.data.repository

import FavoriteListResponse
import FavoriteRequest
import FavoriteResponse
import android.util.Log
import com.datn.viettech_md_12.data.interfaces.ProductService
import com.datn.viettech_md_12.data.model.MatchVariantRequest
import com.datn.viettech_md_12.data.model.MatchVariantResponse
import com.datn.viettech_md_12.data.model.OrderListResponse
import com.datn.viettech_md_12.data.model.OrderModel
import com.datn.viettech_md_12.data.model.ProductByCateModelResponse
import com.datn.viettech_md_12.data.model.ProductDetailResponse
import com.datn.viettech_md_12.data.model.ProductListResponse
import com.datn.viettech_md_12.data.model.SearchResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response


class ProductRepository(
    private val apiService: ProductService
) {
    suspend fun getProductById(id: String): Response<ProductDetailResponse> =
        apiService.getProductById(id)

//    suspend fun getAllProducts(): Response<ProductListResponse> = apiService.getAllProducts()
    fun getAllProductsFlow(): Flow<Response<ProductListResponse>> = flow {
        try {
            val response = apiService.getAllProducts()
            emit(response)
        } catch (e: Exception) {
            emit(
                Response.error(
                    500,
                    "Error: ${e.message}".toResponseBody(null)
                )
            )
        }
    }
    suspend fun addToFavorites(
        favoriteRequest: FavoriteRequest,
        token: String,
        clientId: String,  // Thêm tham số clientId
    ): Response<FavoriteResponse> {
        Log.d(
            "dcm_debug_api_call",
            "Sending Favorite Request: Body = $favoriteRequest, Token = $token, ClientId = $clientId"
        )

        return apiService.addProductToFavorites(
            favoriteRequest,
            token,
            clientId
        )  // Truyền clientId vào đây
    }

//    suspend fun getProductsByCategory(categoryId: String): Response<ProductByCateModelResponse> =
//        apiService.getProductsByCategory(categoryId)

    fun getProductsByCategoryFlow(categoryId: String): Flow<Response<ProductByCateModelResponse>> = flow {
        try {
            val response = apiService.getProductsByCategory(categoryId)
            if (response.isSuccessful) {
                emit(response)
            } else {
                emit(
                    Response.error(
                        response.code(),
                        "API Error: ${response.message()}".toResponseBody(null)
                    )
                )
            }
        } catch (e: Exception) {
            Log.e("ProductRepository", "Error fetching products: ${e.message}")
            emit(
                Response.error(
                    500,
                    "Error: ${e.message}".toResponseBody(null)
                )
            )
        }
    }


    suspend fun getFavoriteProducts(
        token: String,
        clientId: String
    ): Response<FavoriteListResponse> {
        Log.d(
            "dcm_debug_fav",
            "Fetching favorite products with Token: $token and ClientId: $clientId"
        )
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
    suspend fun getUserOrders(
        userId: String,
        token: String,
        clientId: String
    ): Response<OrderListResponse> {
        return apiService.getUserOrders(userId, token, clientId)
    }

    //hien thi chi tiet don hang
    suspend fun getBillById(
        orderId: String,
        token: String,
        clientId: String
    ): Response<OrderModel> {
        return apiService.getBillById(orderId, token, clientId)
    }
    //huy don hang
    suspend fun cancelOrder(
        orderId: String,
        token: String,
        clientId: String
    ): Response<Unit> {
        val body = mapOf("status" to "cancelled")
        return apiService.cancelOrder(orderId, body, token, clientId)
    }


    //matchVariant
    suspend fun matchVariant(
        productId: String,
        attributes: Map<String, String>
    ): MatchVariantResponse {
        val request = MatchVariantRequest(selectedAttributes = attributes)
        // Thêm logging để debug
        Log.d("MatchVariant", "Sending request: productId=$productId, attributes=$attributes")
        return try {
            val response = apiService.matchVariant(
                id = productId,
                request = request,
            )
            Log.d("MatchVariant", "Response received: $response")
            response
        } catch (e: Exception) {
            Log.e("MatchVariant", "Error: ${e.message}")
            throw e
        }
    }

}