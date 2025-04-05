package com.datn.viettech_md_12.data.model

data class Review(
    val _id: String,
    val account_id: String,
    val username: String,
    val avatar: String,
    val product_id: String,
    val contents_review: String,
    val createdAt: String,
    val updatedAt: String,
    val images: List<Image>,
    val rating: Int // Thêm trường rating kiểu Int

)

data class Image(
    val _id: String,
    val url: String
)

data class ImageUploadResponse(
    val success: Boolean,
    val data: Image
)
data class ReviewResponse(
    val success: Boolean,
    val data: List<Review>
)

data class AddReviewRequest(
    val product_id: String,
    val contents_review: String,
    val images: List<Image>,
    val rating: Int
)
data class UpdateReviewRequest(
    val contents_review: String,
    val images: List<Image>
)