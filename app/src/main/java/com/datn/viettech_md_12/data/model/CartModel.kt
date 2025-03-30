package com.datn.viettech_md_12.data.model

import com.google.gson.annotations.SerializedName

data class CartMode(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val price: Double,
    val originalPrice: Double,
    var quantity: Int,
    var isSelected: Boolean = true
)


data class CartModel(
    @SerializedName("message") val message: String,
    @SerializedName("statusCode") val statusCode: Int,
    @SerializedName("metadata") val metadata: Metadata
)

data class Metadata(
    @SerializedName("_id") val _id: String,
    @SerializedName("cart_state") val cart_state: String,
    @SerializedName("cart_products") val cart_products: List<CartProduct>,
    @SerializedName("cart_count_product") val cart_count_product: Int,
    @SerializedName("cart_userId") val cart_userId: String,
) {
    data class CartProduct(
        @SerializedName("productId") val productId: String,
        @SerializedName("name") val name: String,
        @SerializedName("price") val price: Double,
        @SerializedName("image") val image: String,
        @SerializedName("quantity") val quantity: Int,
        @SerializedName("variant") val variant: ProductVariant,
    )

    data class ProductVariant(
        @SerializedName("variantId") val variantId: String,
        @SerializedName("variant_name") val variant_name: String,
        @SerializedName("variant_value") val variant_value: String,
        @SerializedName("sku") val sku: String,
    )
}


// Thêm data class cho request body
data class DeleteCartItemRequest(
    @SerializedName("userId") val userId: String,
    @SerializedName("variantId") val variantId: String,
    @SerializedName("productId") val productId: String,
)

data class UpdateCartRequest(
    @SerializedName("userId") val userId: String,
    @SerializedName("product") val product: CartProduct,
) {
    data class CartProduct(
        @SerializedName("productId") val productId: String,
        @SerializedName("variantId") val variantId: String,
        @SerializedName("quantity") val quantity: Int,
    )
//    {
//        data class ProductVariant(
//            @SerializedName("variantId") val variantId: String? = null,
//            @SerializedName("variant_name") val variantName: String? = null,
//            @SerializedName("variant_value") val variantValue: String? = null,
//            @SerializedName("sku") val sku: String? = null
//        )
//    }
}