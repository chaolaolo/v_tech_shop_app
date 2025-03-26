package com.datn.viettech_md_12.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datn.viettech_md_12.R
import com.datn.viettech_md_12.data.model.Category
import com.datn.viettech_md_12.data.model.CategoryModel
import com.datn.viettech_md_12.data.remote.ApiClient
import com.datn.viettech_md_12.data.repository.CategoryRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CategoryViewModel() : ViewModel() {
    private val _repository = ApiClient.categoryRepository

    private val _categories = MutableStateFlow<List<CategoryModel>>(emptyList())
    val categories: StateFlow<List<CategoryModel>> = _categories

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        fetchCategories() // ✅ Gọi API ngay khi ViewModel được tạo
    }

    fun fetchCategories() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = _repository.getCategories()
            Log.d("CategoryViewModel", "Data: $result")
            if (result != null) {
                _categories.value = result
            }
            _isLoading.value = false
        }
    }
}