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
        images: List<Image>, // Danh sách ảnh đầy đủ
        rating: Int
    ): Result<ReviewResponse> {
        return try {
            // Chỉ truyền vào các ID của ảnh thay vì toàn bộ ảnh
            val request = AddReviewRequest(productId, contentsReview, images.map { it.id }, rating)
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
        images: List<Image> // Danh sách ảnh đầy đủ
    ): Result<ReviewResponse> {
        return try {
            // Chuyển List<Image> thành List<String> chỉ chứa ID của ảnh
            val imageIds = images.map { it.id } // Lấy ID của ảnh

            val request = UpdateReviewRequest(contentsReview, imageIds) // Truyền vào ID ảnh
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

    // Upload multiple images
    suspend fun uploadImages(files: List<MultipartBody.Part>): Result<List<Image>> {
        val images = mutableListOf<Image>()

        for (file in files) {
            try {
                val result = reviewService.uploadImage(file)

                // Kiểm tra nếu upload thành công
                if (result.isSuccessful) {
                    result.body()?.let {
                        images.addAll(it.data) // Thêm tất cả các ảnh vào danh sách nếu upload thành công
                    } ?: return Result.failure(IOException("Empty response body"))
                } else {
                    // Trả về lỗi nếu phản hồi từ server không thành công
                    return Result.failure(IOException("Error: ${result.code()} ${result.message()}"))
                }
            } catch (e: Exception) {
                // Bắt các ngoại lệ có thể xảy ra trong quá trình upload ảnh
                return Result.failure(IOException("Failed to upload image: ${e.localizedMessage}", e))
            }
        }

        // Trả về kết quả thành công với danh sách các ảnh đã upload
        return Result.success(images)
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

    }    // Lấy danh sách review cho sản phẩm
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
            Result.failure(IOException("Failed to thong ke reviews: ${e.localizedMessage}", e))
        }

    }
}
