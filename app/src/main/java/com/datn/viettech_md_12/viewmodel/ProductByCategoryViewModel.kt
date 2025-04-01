package com.datn.viettech_md_12.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datn.viettech_md_12.data.model.ProductByCateModel
import com.datn.viettech_md_12.data.remote.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductByCategoryViewModel : ViewModel() {
    private val _products = MutableStateFlow<List<ProductByCateModel>>(emptyList())
    val products: StateFlow<List<ProductByCateModel>> = _products

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val productRepository = ApiClient.productRepository

    fun fetchProductsByCategory(categoryId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = productRepository.getProductsByCategory(categoryId)

                // Log mã trạng thái và dữ liệu trả về
                Log.d("ProductByCategoryViewModel", "Response Code: ${response.code()}")
                Log.d("ProductByCategoryViewModel", "Response Body: ${response.body()}")

                if (response.isSuccessful) {
                    // Trực tiếp sử dụng List<ProductByCateModel> từ metadata
                    _products.value = response.body()?.metadata ?: emptyList()
                } else {
                    Log.e("ProductByCategoryViewModel", "Error: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("ProductByCategoryViewModel", "Exception: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}
