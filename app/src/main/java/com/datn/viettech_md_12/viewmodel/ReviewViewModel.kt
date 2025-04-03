package com.datn.viettech_md_12.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datn.viettech_md_12.data.model.*
import com.datn.viettech_md_12.data.remote.ApiClient
import com.datn.viettech_md_12.data.remote.ApiClient.reviewRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class ReviewViewModel: ViewModel() {
    private val _repository= ApiClient.reviewRepository
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    private val _reviews = MutableStateFlow<List<Review>>(emptyList())
    val reviews: StateFlow<List<Review>> = _reviews

    private val _addReviewResult = MutableStateFlow<Result<ReviewResponse>?>(null)
    val addReviewResult: StateFlow<Result<ReviewResponse>?> = _addReviewResult.asStateFlow()

    private val _updateReviewResult = MutableStateFlow<Result<ReviewResponse>?>(null)
    val updateReviewResult: StateFlow<Result<ReviewResponse>?> = _updateReviewResult.asStateFlow()

    private val _uploadImageResult = MutableStateFlow<Result<Image>?>(null)
    val uploadImageResult: StateFlow<Result<Image>?> = _uploadImageResult.asStateFlow()

    private val _reviewsByProductResult = MutableStateFlow<Result<ReviewResponse>?>(null)
    val reviewsByProductResult: StateFlow<Result<ReviewResponse>?> = _reviewsByProductResult.asStateFlow()

    fun addReview(
        token: String,
        clientId: String,
        productId: String,
        contentsReview: String,
        images: List<Image>,
        rating:Int,
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = reviewRepository.addReview(token, clientId, productId, contentsReview, images,rating)
            _addReviewResult.value = result
            _isLoading.value = false
        }
    }

    fun updateReview(
        token: String,
        clientId: String,
        reviewId: String,
        contentsReview: String,
        images: List<Image>
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = reviewRepository.updateReview(token, clientId, reviewId, contentsReview, images)
            _updateReviewResult.value = result
            _isLoading.value = false
        }
    }

    fun uploadImage(file: MultipartBody.Part) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = reviewRepository.uploadImage(file)
            _uploadImageResult.value = result
            _isLoading.value = false
        }
    }

    fun getReviewsByProduct(productId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = reviewRepository.getReviewsByProduct(productId)

            if (result.isSuccess) {
                val reviewResponse = result.getOrNull()
                _reviews.value = reviewResponse?.data ?: emptyList() // Cập nhật danh sách đánh giá
            }

            _isLoading.value = false
        }
    }
}
