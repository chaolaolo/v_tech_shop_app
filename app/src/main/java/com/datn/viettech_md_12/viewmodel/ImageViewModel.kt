package com.datn.viettech_md_12.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datn.viettech_md_12.data.model.ImageModel
import com.datn.viettech_md_12.data.remote.ApiClient
import com.datn.viettech_md_12.data.repository.ImageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class ImageViewModel : ViewModel() {
    private val repository = ApiClient.imageRepository

    private val _uploadResult = MutableStateFlow<ImageModel?>(null)
    val uploadResult: StateFlow<ImageModel?> = _uploadResult.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Log tag
    private val TAG = "ImageViewModel"

    fun uploadImage(image: MultipartBody.Part) {
        viewModelScope.launch {
            _isLoading.value = true
            Log.d(TAG, "Starting image upload...")

            try {
                Log.d(TAG, "Uploading image: ${image.body?.contentLength()} bytes")

                val response = repository.uploadImage(image)

                if (response.isSuccessful && response.body() != null) {
                    _uploadResult.value = response.body()
                    Log.d(TAG, "Image uploaded successfully: ${response.body()}")
                } else {
                    _error.value = "Upload failed: ${response.message()}"
                    Log.e(TAG, "Upload failed: ${response.message()}")
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
                Log.e(TAG, "Error during upload: ${e.message}", e)
            } finally {
                _isLoading.value = false
                Log.d(TAG, "Image upload process finished.")
            }
        }
    }
}
