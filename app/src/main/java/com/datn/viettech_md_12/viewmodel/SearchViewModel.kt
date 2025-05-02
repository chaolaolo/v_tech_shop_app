package com.datn.viettech_md_12.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datn.viettech_md_12.common.SortOption
import com.datn.viettech_md_12.data.model.ProductModel
import com.datn.viettech_md_12.data.remote.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel: ViewModel() {
    private val repository = ApiClient.productRepository
    private val _searchResults = MutableStateFlow<List<ProductModel>>(emptyList())
    val searchResults: StateFlow<List<ProductModel>> = _searchResults

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun searchProducts(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val result = repository.searchProducts(query)
                if (result.isSuccessful) {
                    result.body()?.let {
                        _searchResults.value = it.products
                    }
                } else {
                    Log.e("ProductViewModel", "Error: ${result.code()} ${result.message()}")
                }
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage ?: "Lỗi không xác định"
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun clearSearchResults() {
        _searchResults.value = emptyList()
    }

    fun sortSearchResults(option: SortOption) {
//        val sortedList = when (option) {
//            SortOption.PRICE_ASC -> _searchResults.value.sortedBy { it.price }
//            SortOption.PRICE_DESC -> _searchResults.value.sortedByDescending { it.price }
//            SortOption.AZ -> _searchResults.value.sortedBy { it.name }
//            SortOption.ZA -> _searchResults.value.sortedByDescending { it.name }
//        }
//        _searchResults.value = sortedList
    }
}
