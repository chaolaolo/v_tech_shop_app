package com.datn.viettech_md_12.data.repository

import com.datn.viettech_md_12.data.interfaces.ReviewService
import com.datn.viettech_md_12.data.model.*
import okhttp3.MultipartBody
import java.io.IOException

class ReviewRepository(private val reviewService: ReviewService) {

    suspend fun uploadImages(files: List<MultipartBody.Part>): Result<List<Image>> {
        val uploadedImages = mutableListOf<Image>()
        val failedUploads = mutableListOf<String>()

        for (filePart in files) {
            try {
                val response = reviewService.uploadImage(filePart)
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null && result.success) {
                        uploadedImages.add(result.data)
                    } else {
                        failedUploads.add("Failed to upload image")
                    }
                } else {
                    failedUploads.add("Upload failed: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                failedUploads.add("Failed to upload image: ${e.localizedMessage}")
            }
        }

        return if (uploadedImages.isNotEmpty()) {
            Result.success(uploadedImages)
        } else {
            Result.failure(IOException("Failed to upload some or all images: ${failedUploads.joinToString(", ")}"))
        }
    }

    // Thêm mới review (chỉ nhận List<Image> đã được upload thành công)
    suspend fun addReview(
        token: String,
        clientId: String,  // 👈 Tách riêng clientId
        accountId: String,
        productId: String,
        contentsReview: String,
        uploadedImages: List<Image>,  // 👈 Ảnh đã được upload
        rating: Int
    ): Result<ReviewResponse> {
        return try {
            val request = AddReviewRequest(
                account_id = accountId,
                product_id = productId,
                contents_review = contentsReview,
                image_ids = uploadedImages.map { it.id }, // lấy ID ảnh
                rating = rating
            )
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
        uploadedImages: List<Image> // ảnh đã upload
    ): Result<ReviewResponse> {
        return try {
            val imageIds = uploadedImages.map { it.id }
            val request = UpdateReviewRequest(contents_review = contentsReview, imageIds = imageIds)
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

    // Lấy danh sách review
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

    // Lấy thống kê review
    suspend fun getReviewStats(productId: String): Result<ReviewStatsResponse> {
        return try {
            val response = reviewService.getReviewStats(productId)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(IOException("Empty response body"))
            } else {
                Result.failure(IOException("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(IOException("Failed to fetch review stats: ${e.localizedMessage}", e))
        }
    }
}
