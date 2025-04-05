package com.datn.viettech_md_12.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.datn.viettech_md_12.data.model.*
import com.datn.viettech_md_12.data.remote.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class ReviewViewModel(application: Application) : AndroidViewModel(application) {

    private val _repository = ApiClient.reviewRepository
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _reviews = MutableStateFlow<List<Review>>(emptyList())
    val reviews: StateFlow<List<Review>> = _reviews
    private val _reviewStats = MutableStateFlow<Result<ReviewStats>?>(null)
    val reviewStats: StateFlow<Result<ReviewStats>?> = _reviewStats.asStateFlow()


    private val _addReviewResult = MutableStateFlow<Result<ReviewResponse>?>(null)
    val addReviewResult: StateFlow<Result<ReviewResponse>?> = _addReviewResult.asStateFlow()

    private val _updateReviewResult = MutableStateFlow<Result<ReviewResponse>?>(null)
    val updateReviewResult: StateFlow<Result<ReviewResponse>?> = _updateReviewResult.asStateFlow()

    private val _uploadImagesResult =
        MutableStateFlow<Result<List<Image>>>(Result.success(emptyList()))
    val uploadImagesResult: StateFlow<Result<List<Image>>> = _uploadImagesResult.asStateFlow()

    private val _reviewsByProductResult = MutableStateFlow<Result<ReviewResponse>?>(null)
    val reviewsByProductResult: StateFlow<Result<ReviewResponse>?> =
        _reviewsByProductResult.asStateFlow()

    private val sharedPreferences =
        application.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    private val token: String? = sharedPreferences.getString("accessToken", null)
    private val userId: String? = sharedPreferences.getString("clientId", null)

    // Add review
    fun addReview(
        productId: String,
        contentsReview: String,
        images: List<Image>,
        rating: Int
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = _repository.addReview(
                token ?: "",
                userId ?: "",
                productId,
                contentsReview,
                images,
                rating
            )
            _addReviewResult.value = result
            _isLoading.value = false
        }
    }

    // Update review
    fun updateReview(
        reviewId: String,
        contentsReview: String,
        images: List<Image>
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = _repository.updateReview(
                token ?: "",
                userId ?: "",
                reviewId,
                contentsReview,
                images
            )
            _updateReviewResult.value = result
            _isLoading.value = false
        }
    }

    // Upload images
    fun uploadImages(files: List<MultipartBody.Part>) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = _repository.uploadImages(files)
            _uploadImagesResult.value = result
            _isLoading.value = false
        }
    }

    // Get reviews by product
    fun getReviewsByProduct(productId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = _repository.getReviewsByProduct(productId)

            if (result.isSuccess) {
                val reviewResponse = result.getOrNull()
                _reviews.value = reviewResponse?.data ?: emptyList() // Cập nhật danh sách đánh giá
            }

            _isLoading.value = false
        }
    }

    fun getReviewStats(productId: String) {
        viewModelScope.launch {
            _isLoading.value = true

            val result = _repository.getReviewStats(productId)

            // map từ Result<ReviewStatsResponse> → Result<ReviewStats>
            val mappedResult = result.map { it.data }

            _reviewStats.value = mappedResult

            _isLoading.value = false
        }
    }
}


    class ReviewViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReviewViewModel::class.java)) {
            return ReviewViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
