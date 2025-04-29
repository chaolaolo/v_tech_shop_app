package com.datn.viettech_md_12.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datn.viettech_md_12.NetworkHelper
import com.datn.viettech_md_12.R
import com.datn.viettech_md_12.data.model.Category
import com.datn.viettech_md_12.data.model.CategoryModel
import com.datn.viettech_md_12.data.remote.ApiClient
import com.datn.viettech_md_12.data.repository.CategoryRepository
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class CategoryViewModel(private val networkHelper: NetworkHelper) : ViewModel() {
    private val _repository = ApiClient.categoryRepository

    private val _categories = MutableStateFlow<List<CategoryModel>>(emptyList())
    val categories: StateFlow<List<CategoryModel>> = _categories

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        if (networkHelper.isNetworkConnected()) {
            fetchCategories()
        } else {
            Log.d("CategoryViewModel", "Không có kết nối mạng.")
            _isLoading.value = false
        }
    }

    fun fetchCategories() {
        viewModelScope.launch {
            try {
            _isLoading.value = true
            val result = _repository.getCategories()
            Log.d("CategoryViewModel", "Data: $result")
            if (result != null) {
                _categories.value = result
            }
            _isLoading.value = false
            } catch (e: UnknownHostException) {
                Log.e("fetchCategories", "Lỗi mạng: Không thể kết nối với máy chủ")
            } catch (e: SocketTimeoutException) {
                Log.e("fetchCategories", "Lỗi mạng: Đã hết thời gian chờ")
            } catch (e: HttpException) {
                Log.e("fetchCategories", "Lỗi HTTP: ${e.message}")
            } catch (e: JsonSyntaxException) {
                Log.e("fetchCategories", "Lỗi dữ liệu: Invalid JSON response")
            } catch (e: Exception) {
                Log.e("fetchCategories", "Lỗi chung: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}