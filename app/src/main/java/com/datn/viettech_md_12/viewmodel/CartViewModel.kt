package com.datn.viettech_md_12.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datn.viettech_md_12.data.model.CartModel
import com.datn.viettech_md_12.data.model.UpdateCartRequest
import com.datn.viettech_md_12.data.remote.ApiClient
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException


class CartViewModel : ViewModel() {
    private val cartRepository = ApiClient.cartRepository

    private val _cartState = MutableStateFlow<Response<CartModel>?>(null)
    val cartState: StateFlow<Response<CartModel>?> get() = _cartState

    private val _updateCartState = MutableStateFlow<Response<CartModel>?>(null)
    val updateCartState: StateFlow<Response<CartModel>?> get() = _updateCartState

    private val _deleteCartItemState = MutableStateFlow<Response<Unit>?>(null)
    val deleteCartItemState: StateFlow<Response<Unit>?> get() = _deleteCartItemState

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val token =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI2N2NjMGY4YzAyZTM5ZWJlOWY3YjYwZDUiLCJ1c2VybmFtZSI6ImN1c3RvbWVyMDMiLCJpYXQiOjE3NDMzMDM0MDMsImV4cCI6MTc0MzQ3NjIwM30.HFBLyvuTOwmavvIToqR4Ofa-aEUk0RbtHbXXpvdehhQ"
    private val userId = "67cc0f8c02e39ebe9f7b60d5"

    fun fetchCart() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = cartRepository.getCart(
                    token = token,
                    userId = userId,
                    userIdQuery = userId
                )
                if (response.isSuccessful) {
                    _cartState.value = response
                    response.body()?.let {
                        Log.d("dm", "Fetch Cart Success: ${it}")
                    }
                } else {
                    Log.e("dm", "Fetch Cart Failed: ${response.code()} - ${response.message()}")
                }
            } catch (e: UnknownHostException) {
                Log.e("dm_error", "Lỗi mạng: Không thể kết nối với máy chủ")
            } catch (e: SocketTimeoutException) {
                Log.e("dm_error", "Lỗi mạng: Đã hết thời gian chờ")
            } catch (e: HttpException) {
                Log.e("dm_error", "Lỗi HTTP: ${e.message()}")
            } catch (e: JsonSyntaxException) {
                Log.e("dm_error", "Lỗi dữ liệu: Invalid JSON response")
            } catch (e: Exception) {
                Log.e("dm_error", "Lỗi chung: ${e.message}", e)
            } finally {
                _isLoading.value = false // Kết thúc trạng thái loading
            }
        }
    }

    fun updateProductQuantity(
        productId: String,
        variantId: String,
        newQuantity: Int,
    ) {
        updateLocalCartState(productId, newQuantity)
        viewModelScope.launch {
//            _isLoading.value = true
            try {
                val response = cartRepository.updateCartItem(
                    token = token,
                    userId = userId,
                    productId = productId,
                    variantId = variantId,
                    newQuantity = newQuantity
                )

                _updateCartState.value = response

                if (response.isSuccessful) {
                    // Update local state first for better UX
//                    updateLocalCartState(productId, newQuantity)
                    // Then refresh from server
//                    fetchCart()
                    Log.d("CartViewModel", "Update quantity success")
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("CartViewModel", "Update quantity failed: $errorMsg")
                }
            } catch (e: Exception) {
                Log.e("CartViewModel", "updateProductQuantity: $e", )
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun updateLocalCartState(productId: String, newQuantity: Int) {
        val currentCart = _cartState.value?.body()
        currentCart?.let { cart ->
            val updatedProducts = cart.metadata.cart_products.map { product ->
                if (product.productId == productId) {
                    product.copy(quantity = newQuantity)
                } else {
                    product
                }
            }
            val updatedCart = cart.copy(
                metadata = cart.metadata.copy(
                    cart_products = updatedProducts
                )
            )
            _cartState.value = Response.success(updatedCart)
        }
    }

    fun deleteCartItem(
        productId: String,
        variantId: String,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = cartRepository.deleteCartItem(
                    token = token,
                    userId = userId,
                    productId = productId,
                    variantId = variantId
                )

                _deleteCartItemState.value = response

                if (response.isSuccessful) {
                    // Cập nhật lại giỏ hàng sau khi xóa thành công
                    fetchCart()
                    onSuccess()
                    Log.d("CartViewModel", "Delete item success")
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("CartViewModel", "Delete item failed: $errorMsg")
                    onError(errorMsg)
                }
            } catch (e: UnknownHostException) {
                val errorMsg = "Lỗi mạng: Không thể kết nối với máy chủ"
                Log.e("CartViewModel", errorMsg, e)
                onError(errorMsg)
            } catch (e: SocketTimeoutException) {
                val errorMsg = "Lỗi mạng: Đã hết thời gian chờ"
                Log.e("CartViewModel", errorMsg, e)
                onError(errorMsg)
            } catch (e: HttpException) {
                val errorMsg = "Lỗi HTTP: ${e.message()}"
                Log.e("CartViewModel", errorMsg, e)
                onError(errorMsg)
            } catch (e: JsonSyntaxException) {
                val errorMsg = "Lỗi dữ liệu: Invalid JSON response"
                Log.e("CartViewModel", errorMsg, e)
                onError(errorMsg)
            } catch (e: Exception) {
                val errorMsg = e.message ?: "Lỗi không xác định"
                Log.e("CartViewModel", errorMsg, e)
                onError(errorMsg)
            } finally {
                _isLoading.value = false
            }
        }
    }

}