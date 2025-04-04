package com.datn.viettech_md_12.data.repository

import com.datn.viettech_md_12.data.interfaces.ReviewService
import com.datn.viettech_md_12.data.model.*
import okhttp3.MultipartBody
import java.io.IOException

class ReviewRepository(private val reviewService: ReviewService) {

    // Thêm một review mới
    suspend fun addReview(
        token: String,
        clientId: String,
        productId: String,
        contentsReview: String,
        images: List<Image>,
        rating: Int,
    ): Result<ReviewResponse> {
        return try {
            val request = AddReviewRequest(productId, contentsReview, images, rating)
            val response = reviewService.addReview(request, token, clientId)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(IOException("Empty response body"))
            } else {
                Result.failure(IOException("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(IOException("Failed to add review: ${e.localizedMessage}", e))
        }
    }

    // Cập nhật review
    suspend fun updateReview(
        token: String,
        clientId: String,
        reviewId: String,
        contentsReview: String,
        images: List<Image>
    ): Result<ReviewResponse> {
        return try {
            val request = UpdateReviewRequest(contentsReview, images)
            val response = reviewService.updateReview(reviewId, request, token, clientId)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(IOException("Empty response body"))
            } else {
                Result.failure(IOException("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(IOException("Failed to update review: ${e.localizedMessage}", e))
        }
    }

    // Upload ảnh
    suspend fun uploadImage(file: MultipartBody.Part): Result<Image> {
        return try {
            val response = reviewService.uploadImage(file)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it.data)
                } ?: Result.failure(IOException("Empty response body"))
            } else {
                Result.failure(IOException("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(IOException("Failed to upload image: ${e.localizedMessage}", e))
        }
    }

    // Lấy danh sách review cho sản phẩm
    suspend fun getReviewsByProduct(productId: String): Result<ReviewResponse> {
        return try {
            val response = reviewService.getReviewsByProduct(productId)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(IOException("Empty response body"))
            } else {
                Result.failure(IOException("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(IOException("Failed to fetch reviews: ${e.localizedMessage}", e))
        }
    }
}
