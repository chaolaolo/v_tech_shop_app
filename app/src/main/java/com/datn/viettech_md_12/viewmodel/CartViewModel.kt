package com.datn.viettech_md_12.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datn.viettech_md_12.data.model.CartModel
import com.datn.viettech_md_12.data.remote.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response


class CartViewModel : ViewModel() {
    private val cartRepository = ApiClient.cartRepository

    private val _cartState = MutableStateFlow<Response<CartModel>?>(null)
    val cartState: StateFlow<Response<CartModel>?> get() = _cartState

    private val _updateCartState = MutableStateFlow<Response<CartModel>?>(null)
    val updateCartState: StateFlow<Response<CartModel>?> get() = _updateCartState

    fun fetchCart(token:String, userId:String, userIdQuery:String) {
        viewModelScope.launch {
            try {
                val response = cartRepository.getCart(
                    token = token,
                    userId = userId,
                    userIdQuery=userIdQuery
                )
                _cartState.value = response
                Log.d("CartViewModel", "Fetch Cart Success: ${response.body()}")
            } catch (e: Exception) {
                Log.e("CartViewModel", "Fetch Cart Error: ${e.message}", e)
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