package com.datn.viettech_md_12.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datn.viettech_md_12.data.model.CartModel
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
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

//    fun fetchCart(token:String, userId:String, userIdQuery:String) {
//        viewModelScope.launch {
//            try {
//                val response = cartRepository.getCart(
//                    token = token,
//                    userId = userId,
//                    userIdQuery=userIdQuery
//                )
//                _cartState.value = response
//                Log.d("CartViewModel", "Fetch Cart Success: ${response.body()}")
//            } catch (e: Exception) {
//                Log.e("CartViewModel", "Fetch Cart Error: ${e.message}", e)
//            }
//        }
//    }

    fun fetchCart(token: String, userId: String, userIdQuery: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = cartRepository.getCart(
                    token = token,
                    userId = userId,
                    userIdQuery = userIdQuery
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




    fun updateCart(token: String, userId: String, cart: CartModel) {
        viewModelScope.launch {
            try {
                val response = cartRepository.updateCart(token, userId, cart)
                _updateCartState.value = response
                if (response.isSuccessful) {
                    Log.d("CartViewModel", "Update Cart Success: ${response.body()}")
                } else {
                    Log.e("CartViewModel", "Update Cart Failed: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("CartViewModel", "Update Cart Error: ${e.message}", e)
            }
        }
    }

    fun updateProductQuantity(productId: String, newQuantity: Int) {
        val currentCartResponse = _cartState.value
        val currentCart = currentCartResponse?.body()

        if (currentCart != null) {
            val updatedCart = currentCart.copy(
                metadata = currentCart.metadata.copy(
                    cart_products = currentCart.metadata.cart_products.map { product ->
                        if (product.productId == productId) {
                            product.copy(quantity = newQuantity)
                        } else {
                            product
                        }
                    }
                )
            )

            // Tạo một Response mới với body được cập nhật
            _cartState.value = Response.success(updatedCart)
        }
    }



}