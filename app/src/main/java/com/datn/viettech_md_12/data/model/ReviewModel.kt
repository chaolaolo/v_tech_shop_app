package com.datn.viettech_md_12.data.model

// Review chứa thông tin về đánh giá sản phẩm, bao gồm ảnh và rating
data class Review(
    val _id: String,
    val account_id: String,
    val username: String,
    val avatar: String,
    val product_id: String,
    val contents_review: String,
    val createdAt: String,
    val updatedAt: String,
    val images: List<Image>,  // Danh sách ảnh đi kèm đánh giá
    val rating: Int  // Đánh giá sao
)

// Thông tin thống kê đánh giá sản phẩm (tổng số đánh giá và điểm trung bình)
data class ReviewStats(
    val totalReviews: Int,     // Tổng số đánh giá
    val averageRating: Float  // Điểm đánh giá trung bình
)

// Thông tin ảnh với các thuộc tính như ID, tên tệp, đường dẫn tệp,...
data class Image(
    val id: String,
    val fileName: String,
    val filePath: String,
    val url: String,
    val fileSize: Long,
    val fileType: String,
)

// Phản hồi sau khi upload ảnh, trả về ảnh vừa tải lên
data class ImageUploadResponse(
    val success: Boolean,
    val data: Image  // Vì mỗi lần chỉ trả về một ảnh
)


// Phản hồi từ API chứa thông tin các đánh giá
data class ReviewResponse(
    val success: Boolean,
    val data: List<Review>  // Danh sách các đánh giá
)

// Phản hồi từ API chứa thông tin các đánh giá
data class ReviewResponseAddUp(
    val success: Boolean,
    val data: Review
)

// Phản hồi từ API chứa thông tin các đánh giá
data class ReviewStatsResponse(
    val success: Boolean,
    val data: ReviewStats  // Danh sách các đánh giá
)

// Yêu cầu thêm review, chỉ truyền vào ID của ảnh và rating
data class AddReviewRequest(
    val account_id: String,          // ID của tài khoản
    val product_id: String,          // ID của sản phẩm
    val contents_review: String,     // Nội dung đánh giá
    val image_ids: List<String>,     // Danh sách ID của ảnh
    val rating: Int                  // Đánh giá sao
)


// Yêu cầu cập nhật review, chỉ truyền vào nội dung và danh sách ảnh mới
data class UpdateReviewRequest(
    val contents_review: String,  // Nội dung mới của đánh giá
    val imageIds: List<String>,  // Danh sách ID của ảnh
)
