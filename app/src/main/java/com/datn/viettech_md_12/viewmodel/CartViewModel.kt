package com.datn.viettech_md_12.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.datn.viettech_md_12.data.model.CartModel
import com.datn.viettech_md_12.data.model.DiscountResponse
import com.datn.viettech_md_12.data.remote.ApiClient
import com.datn.viettech_md_12.data.remote.ApiClient.checkoutService
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class CartViewModel(application: Application) : ViewModel() {
    private val cartRepository = ApiClient.cartRepository

    private val _cartState = MutableStateFlow<Response<CartModel>?>(null)
    val cartState: StateFlow<Response<CartModel>?> get() = _cartState

    private val _updateCartState = MutableStateFlow<Response<CartModel>?>(null)
    val updateCartState: StateFlow<Response<CartModel>?> get() = _updateCartState

    private val _updateIsSelectedState = MutableStateFlow<Response<CartModel>?>(null)
    val updateIsSelectedState: StateFlow<Response<CartModel>?> get() = _updateIsSelectedState

    private val _deleteCartItemState = MutableStateFlow<Response<Unit>?>(null)
    val deleteCartItemState: StateFlow<Response<Unit>?> get() = _deleteCartItemState

    private val _discountState = MutableStateFlow<Response<DiscountResponse>?>(null)
    val discountState: StateFlow<Response<DiscountResponse>?> get() = _discountState

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading
    private val _isDiscountLoading = MutableStateFlow(true)
    val isDiscountLoading: StateFlow<Boolean> = _isDiscountLoading

    private val sharedPreferences = application.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    private val token: String? = sharedPreferences.getString("accessToken", null)
    private val userId: String? = sharedPreferences.getString("clientId", null)

    init {
        fetchCart()
        getListDisCount()
    }

    //Get cart
    fun fetchCart() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = cartRepository.getCart(
                    token = token?:"",
                    userId = userId?:"",
                    userIdQuery = userId?:""
                )
                if (response.isSuccessful) {
                    _cartState.value = response
                    response.body()?.let {
                        Log.d("fetchCart", "Fetch Cart Success: ${it}")
                    }
                } else {
                    Log.e("fetchCart", "Fetch Cart Failed: ${response.code()} - ${response.message()}")
                }
            } catch (e: UnknownHostException) {
                Log.e("fetchCart", "Lỗi mạng: Không thể kết nối với máy chủ")
            } catch (e: SocketTimeoutException) {
                Log.e("fetchCart", "Lỗi mạng: Đã hết thời gian chờ")
            } catch (e: HttpException) {
                Log.e("fetchCart", "Lỗi HTTP: ${e.message()}")
            } catch (e: JsonSyntaxException) {
                Log.e("fetchCart", "Lỗi dữ liệu: Invalid JSON response")
            } catch (e: Exception) {
                Log.e("fetchCart", "Lỗi chung: ${e.message}", e)
            } finally {
                _isLoading.value = false // Kết thúc trạng thái loading
            }
        }
    }

    //Update cart item quantity
    fun updateProductQuantity(
        productId: String,
        variantId: String,
        newQuantity: Int,
    ) {
//        updateLocalCartState(productId, newQuantity)
        viewModelScope.launch {
//            _isLoading.value = true
            try {
                val response = cartRepository.updateCartItem(
                    token = token ?: "",
                    userId = userId ?: "",
                    productId = productId,
                    detailsVariantId = variantId,
                    newQuantity = newQuantity,
                )

                if (response.isSuccessful) {
//                    fetchCart()
                    checkoutService.getIsSelectedItemInCart(
                        token = token ?: "",
                        userId = userId ?: "",
                        userIdQuery = userId ?: ""
                    )
                    _updateCartState.value = response
                    updateLocalCartState(productId, newQuantity)
                    Log.d("updateProductQuantity", "Update quantity success")
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("updateProductQuantity", "Update quantity failed: $errorMsg")
                }
            } catch (e: Exception) {
                Log.e("updateProductQuantity", "updateProductQuantity: $e")
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

    // Update isSelected status for cart item
    fun updateIsSelected(
        productId: String,
        detailsVariantId: String?,
        isSelected: Boolean,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                val response = cartRepository.updateIsSelected(
                    token = token ?: "",
                    userId = userId ?: "",
                    productId = productId,
                    detailsVariantId = detailsVariantId?:"",
                    isSelected = isSelected
                )


                if (response.isSuccessful) {
                    // Update the local cart state with the new isSelected value
                    _updateIsSelectedState.value = response
                    updateLocalCartItemSelection(productId, detailsVariantId, isSelected)
//                    fetchCart()
                    onSuccess()
                    Log.d("updateIsSelected", "Update isSelected success")
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("updateIsSelected", "Update isSelected failed: $errorMsg")
                    onError(errorMsg)
                }
            } catch (e: UnknownHostException) {
                val errorMsg = "Lỗi mạng: Không thể kết nối với máy chủ"
                Log.e("updateIsSelected", errorMsg, e)
                onError(errorMsg)
            } catch (e: SocketTimeoutException) {
                val errorMsg = "Lỗi mạng: Đã hết thời gian chờ"
                Log.e("updateIsSelected", errorMsg, e)
                onError(errorMsg)
            } catch (e: HttpException) {
                val errorMsg = "Lỗi HTTP: ${e.message()}"
                Log.e("updateIsSelected", errorMsg, e)
                onError(errorMsg)
            } catch (e: JsonSyntaxException) {
                val errorMsg = "Lỗi dữ liệu: Invalid JSON response"
                Log.e("updateIsSelected", errorMsg, e)
                onError(errorMsg)
            } catch (e: Exception) {
                val errorMsg = e.message ?: "Lỗi không xác định"
                Log.e("updateIsSelected", errorMsg, e)
                onError(errorMsg)
            }
        }
    }

    // Helper method to update the local cart state with new isSelected value
    private fun updateLocalCartItemSelection(productId: String, variantId: String?, isSelected: Boolean) {
        val currentCart = _cartState.value?.body()
        currentCart?.let { cart ->
            val updatedProducts = cart.metadata.cart_products.map { product ->
                if (product.productId == productId && (variantId == null || product.detailsVariantId == variantId)) {
                    product.copy(isSelected = isSelected)
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

    //Delete cart item
    fun deleteCartItem(
        productId: String,
        detailsVariantId: String,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {},
    ) {
        viewModelScope.launch {
//            _isLoading.value = true
            Log.d("CartViewModel", "token $token")
            Log.d("CartViewModel", "client id $userId")
            try {
                val response = cartRepository.deleteCartItem(
                    token = token?:"",
                    userId = userId?:"",
                    productId = productId,
                    detailsVariantId = detailsVariantId
                )
                _deleteCartItemState.value = response
                if (response.isSuccessful) {
                    // Cập nhật lại giỏ hàng sau khi xóa thành công
                    fetchCart()
                    onSuccess()
                    Log.d("deleteCartItem", "Delete item success")
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("deleteCartItem", "Delete item failed: $errorMsg")
                    onError(errorMsg)
                }
            } catch (e: UnknownHostException) {
                val errorMsg = "Lỗi mạng: Không thể kết nối với máy chủ"
                Log.e("deleteCartItem", errorMsg, e)
                onError(errorMsg)
            } catch (e: SocketTimeoutException) {
                val errorMsg = "Lỗi mạng: Đã hết thời gian chờ"
                Log.e("deleteCartItem", errorMsg, e)
                onError(errorMsg)
            } catch (e: HttpException) {
                val errorMsg = "Lỗi HTTP: ${e.message()}"
                Log.e("deleteCartItem", errorMsg, e)
                onError(errorMsg)
            } catch (e: JsonSyntaxException) {
                val errorMsg = "Lỗi dữ liệu: Invalid JSON response"
                Log.e("deleteCartItem", errorMsg, e)
                onError(errorMsg)
            } catch (e: Exception) {
                val errorMsg = e.message ?: "Lỗi không xác định"
                Log.e("deleteCartItem", errorMsg, e)
                onError(errorMsg)
            } finally {
                _isLoading.value = false
            }
        }
    }

    //Get list DisCount
    fun getListDisCount() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = cartRepository.getDiscount(
                    token = token?:"",
                    userId = userId?:"",
                )
                if (response.isSuccessful) {
                    val body = response.body()
                    val currentDate = LocalDate.now()
                    val formatter = DateTimeFormatter.ISO_DATE

                    val filteredMetadata = body?.data?.filter { discount ->
                        val endDateStr = discount.endDate ?: discount.expirationDate
                        endDateStr?.let {
                            try {
                                val endDate = LocalDate.parse(it.substring(0, 10), formatter)
                                !endDate.isBefore(currentDate) // Chưa hết hạn
                            } catch (e: Exception) {
                                true // Nếu không parse được thì vẫn giữ lại
                            }
                        } ?: true // Nếu không có ngày thì giữ lại (giả định là chưa hết hạn)
                    } ?: emptyList()
                    val filteredResponse = body?.copy(data = filteredMetadata)
                    _discountState.value = Response.success(filteredResponse)

                    response.body()?.let {
                        Log.d("getListDisCount", "Fetch Discount Success: $filteredMetadata")
                    }
                } else {
                    Log.e("getListDisCount", "Fetch Discount Failed: ${response.code()} - ${response.message()}")
                }
            } catch (e: UnknownHostException) {
                Log.e("getListDisCount", "Lỗi mạng: Không thể kết nối với máy chủ")
            } catch (e: SocketTimeoutException) {
                Log.e("getListDisCount", "Lỗi mạng: Đã hết thời gian chờ")
            } catch (e: HttpException) {
                Log.e("getListDisCount", "Lỗi HTTP: ${e.message()}")
            } catch (e: JsonSyntaxException) {
                Log.e("getListDisCount", "Lỗi dữ liệu: Invalid JSON response")
            } catch (e: Exception) {
                Log.e("getListDisCount", "Lỗi chung: ${e.message}", e)
            } finally {
                _isLoading.value = false // Kết thúc trạng thái loading
            }
        }
    }

}


class CartViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
            return CartViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}