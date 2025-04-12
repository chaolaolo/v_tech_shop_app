package com.datn.viettech_md_12.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.datn.viettech_md_12.data.model.*
import com.datn.viettech_md_12.data.remote.ApiClient
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class ReviewViewModel(application: Application) : ViewModel() {

    private val _repository = ApiClient.reviewRepository
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _addReviewResult = MutableStateFlow<Result<BaseResponse<ReviewResponseAddUp>>?>(null)
    val addReviewResult: StateFlow<Result<BaseResponse<ReviewResponseAddUp>>?> = _addReviewResult

    private val _reviews = MutableStateFlow<List<Review>>(emptyList())
    val reviews: StateFlow<List<Review>> = _reviews

    private val _reviewStats = MutableStateFlow<Result<ReviewStats>?>(null)
    val reviewStats: StateFlow<Result<ReviewStats>?> = _reviewStats

    private val _updateReviewResult = MutableStateFlow<Result<BaseResponse<ReviewResponseAddUp>>?>(null)
    val updateReviewResult: StateFlow<Result<BaseResponse<ReviewResponseAddUp>>?> = _updateReviewResult

    private val _userReviewStatus = MutableStateFlow<Boolean>(false)
    val userReviewStatus: StateFlow<Boolean> = _userReviewStatus

    private val sharedPreferences =
        application.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    fun addReview(
        productId: String,
        contentsReview: String,
        rating: Int,
        images: List<String>
    ) {
        val token = sharedPreferences.getString("accessToken", "") ?: ""
        val clientId = sharedPreferences.getString("clientId", "") ?: ""

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = _repository.addReview(
                    token = token,
                    clientId = clientId,
                    accountId = clientId,
                    productId = productId,
                    contentsReview = contentsReview,
                    rating = rating,
                    imageIds = images
                )
                _addReviewResult.value = result
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateReview(reviewId: String, contentsReview: String,rating: Int, images: List<String>) {
        val token = sharedPreferences.getString("accessToken", "") ?: ""
        val clientId = sharedPreferences.getString("clientId", "") ?: ""

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = _repository.updateReview(
                    token = token,
                    clientId = clientId,
                    reviewId = reviewId,
                    contentsReview = contentsReview,
                    rating = rating,
                    imageIds = images,
                    )
                _updateReviewResult.value = result
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getReviewsByProduct(productId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = _repository.getReviewsByProduct(productId)
                if (result.isSuccess) {
                    _reviews.value = result.getOrNull()?.data ?: emptyList()
                } else {
                    Log.e("GET_REVIEWS", "Lỗi: ${result.exceptionOrNull()?.message}")
                }
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun checkUserReviewStatus(productId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = _repository.getReviewsByProduct(productId)
                if (result.isSuccess) {
                    // Kiểm tra nếu người dùng đã có review cho sản phẩm này
                    val userId = sharedPreferences.getString("clientId", "")
                    val reviews = result.getOrNull()?.data ?: emptyList()
                    val userHasReviewed = reviews.any { it.account_id == userId }
                    _userReviewStatus.value = userHasReviewed
                } else {
                    _userReviewStatus.value = false
                }
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun getCurrentUserId(): String {
        return sharedPreferences.getString("clientId", "") ?: ""
    }

    fun getReviewStats(productId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = _repository.getReviewStats(productId)
                _reviewStats.value = result.map { it.data }
            } finally {
                _isLoading.value = false
            }
        }
    }
}

class ReviewViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReviewViewModel::class.java)) {
            return ReviewViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
