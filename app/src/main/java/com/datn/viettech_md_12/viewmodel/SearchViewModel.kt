package com.datn.viettech_md_12.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datn.viettech_md_12.common.SortOption
import com.datn.viettech_md_12.data.model.ProductModel
import com.datn.viettech_md_12.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: ProductRepository // Inject repository here
) : ViewModel() {
    private val _searchResults = MutableStateFlow<List<ProductModel>>(emptyList())
    val searchResults: StateFlow<List<ProductModel>> = _searchResults

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _shouldCloseBottomSheet = MutableStateFlow(false)
    val shouldCloseBottomSheet: StateFlow<Boolean> = _shouldCloseBottomSheet

    private var currentSortOption: SortOption? = null
    private var currentQuery: String = ""

    private val _searchHistory = MutableStateFlow<List<String>>(emptyList())
    val searchHistory: StateFlow<List<String>> = _searchHistory
    fun searchProducts(query: String, sortOption: SortOption? = currentSortOption) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            currentQuery = query // 💡 Lưu lại query mới nhất
            currentSortOption = sortOption // 💡 Lưu lại sortOption mới nhất
            try {
                val sortQuery = sortOption?.let {
                    when (it) {
                        SortOption.PRICE_ASC -> "price_asc"
                        SortOption.PRICE_DESC -> "price_desc"
                        SortOption.AZ -> "name_asc"
                        SortOption.ZA -> "name_desc"
                    }
                }

                // Collect the cold flow and update the hot state
                repository.searchProducts(query, sortQuery).collect { result ->
                    if (result.isSuccessful) {
                        result.body()?.let {
                            _searchResults.value = it.products
                        }
                    } else {
                        Log.e("SearchViewModel", "Error: ${result.code()} ${result.message()}")
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage ?: "Lỗi không xác định"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun applySort(sortOption: SortOption?) {
        currentSortOption = sortOption
        searchProducts(query = currentQuery, sortOption = sortOption) // ✅ Sử dụng currentQuery
    }

    fun clearSearchResults() {
        _searchResults.value = emptyList()
        currentQuery = "" // ✅ reset currentQuery khi xoá
    }

    fun onBottomSheetClosed() {
        _shouldCloseBottomSheet.value = false
    }

    fun saveToHistory(query: String) {
        if (query.isNotBlank()) {
            _searchHistory.value = listOf(query) + _searchHistory.value.filterNot { it == query }.take(10)
        }
    }

    fun clearSearchHistory() {
        _searchHistory.value = emptyList()
    }
}



