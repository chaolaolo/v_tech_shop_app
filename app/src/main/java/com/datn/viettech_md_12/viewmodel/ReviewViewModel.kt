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
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isUploading = MutableStateFlow(false)
    val isUploading: StateFlow<Boolean> get() = _isUploading

    private val _reviews = MutableStateFlow<List<Review>>(emptyList())
    val reviews: StateFlow<List<Review>> = _reviews.asStateFlow()

    private val _reviewStats = MutableStateFlow<Result<ReviewStats>?>(null)
    val reviewStats: StateFlow<Result<ReviewStats>?> = _reviewStats.asStateFlow()

    private val _addReviewResult = MutableStateFlow<Result<ReviewResponse>?>(null)
    val addReviewResult: StateFlow<Result<ReviewResponse>?> = _addReviewResult.asStateFlow()

    private val _updateReviewResult = MutableStateFlow<Result<ReviewResponse>?>(null)
    val updateReviewResult: StateFlow<Result<ReviewResponse>?> = _updateReviewResult.asStateFlow()

    private val _uploadImagesResult = MutableStateFlow(Result.success(emptyList<Image>()))
    val uploadImagesResult: StateFlow<Result<List<Image>>> = _uploadImagesResult.asStateFlow()

    private val sharedPreferences =
        application.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    // ✅ Flow kết hợp: upload ảnh rồi mới thêm review
    fun uploadImagesAndAddReview(
        imageParts: List<MultipartBody.Part>,  // Không cần sử dụng nữa nếu không upload ảnh
        productId: String,
        contentsReview: String,
        rating: Int
    ) {
        val token = sharedPreferences.getString("accessToken", "") ?: ""
        val clientId = sharedPreferences.getString("clientId", "") ?: ""

        viewModelScope.launch {
            _isUploading.value = true
            _isLoading.value = true

            try {
                // Không upload ảnh nữa, dùng ảnh mặc định
                val fixedImageId = "67f2043a4c6573cb98bd844f"  // ID ảnh mặc định

                // Gửi review với ảnh mặc định
                val addReviewResult = _repository.addReview(
                    token = token,
                    clientId = clientId,
                    accountId = clientId,
                    productId = productId,
                    contentsReview = contentsReview,
                    uploadedImages = emptyList(),  // Không gửi ảnh nữa
                    rating = rating
                )

                _addReviewResult.value = addReviewResult
            } catch (e: Exception) {
                Log.e("UPLOAD_ADD_REVIEW", "Lỗi: ${e.message}")
                _addReviewResult.value = Result.failure(e)
                _isLoading.value = false
            } finally {
                _isUploading.value = false
                _isLoading.value = false
            }
        }
    }

    // ✅ Dùng nếu chỉ cập nhật review (ảnh đã được upload sẵn)
    fun updateReview(reviewId: String, contentsReview: String, images: List<Image>) {
        val token = sharedPreferences.getString("accessToken", "") ?: ""
        val clientId = sharedPreferences.getString("clientId", "") ?: ""

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = _repository.updateReview(token, clientId, reviewId, contentsReview, images)
                _updateReviewResult.value = result
            } finally {
                _isLoading.value = false
            }
        }
    }

    // ✅ Dùng nếu bạn chỉ muốn upload ảnh riêng
    fun uploadImages(files: List<MultipartBody.Part>) {
        viewModelScope.launch {
            _isUploading.value = true
            try {
                val result = _repository.uploadImages(files)
                _uploadImagesResult.value = result
                if (result.isSuccess) {
                    Log.d("UPLOAD_IMAGES", "Upload thành công: ${result.getOrNull()}")
                } else {
                    Log.e("UPLOAD_IMAGES", "Upload thất bại: ${result.exceptionOrNull()?.message}")
                }
            } catch (e: Exception) {
                Log.e("UPLOAD_IMAGES", "Lỗi upload: ${e.message}")
                _uploadImagesResult.value = Result.failure(e)
            } finally {
                _isUploading.value = false
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
