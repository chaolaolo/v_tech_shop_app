import com.google.gson.annotations.SerializedName

data class FavoriteResponse(
    val success: Boolean,
    val favorite: Favorite // Đối tượng "favorite" trả về sau khi thêm sản phẩm thành công
)

data class Favorite(
    @SerializedName("user")
    val user: String,
    @SerializedName("product")
    val product: String,
    @SerializedName("_id")
    val _id: String,
)

data class FavoriteRequest(
    @SerializedName("productId") val productId: String
)