package com.datn.viettech_md_12.data.model


import com.google.gson.annotations.SerializedName

/**
{
  "image_ids": [],
  "_id": "67cdd20838591fcf41a06e58",
  "product_name": "Sony A7 IV Mirrorless Camera 2",
  "product_thumbnail": "uploads\\1742124754520.webp",
  "product_description": "High-performance mirrorless camera with 33MP sensor.",
  "product_price": 2499.99,
  "product_stock": 12,
  "category": {
    "_id": "67c9c927de7f8ec2801605b6",
    "name": "Camera",
    "parent_category": null,
    "attributes_template": [
      "resolution",
      "zoom",
      "lens_type",
      "battery_life",
      "video_resolution"
    ]
  },
  "product_attributes": {
    "resolution": "33MP",
    "zoom": "Optical Zoom 10x",
    "lens_type": "Full-frame",
    "battery_life": "600 shots",
    "video_resolution": "4K 60fps"
  },
  "product_ratingsAverage": 4.5,
  "isDraft": true,
  "isPulished": false,
  "variations": [
    {
      "variant_name": "Color",
      "variant_value": "Black",
      "price": 2499.99,
      "stock": 12,
      "sku": "SONY-A7IV-BLACK",
      "_id": "67cdd20838591fcf41a06e59"
    }
  ],
  "product_slug": "sony-a7-iv-mirrorless-camera"
}
*/
data class ProductModel(
    @SerializedName("category")
    val category: Category,
    @SerializedName("_id")
    val id: String,
    @SerializedName("image_ids")
    val imageIds: List<Any>,
    @SerializedName("isDraft")
    val isDraft: Boolean,
    @SerializedName("isPulished")
    val isPublished: Boolean,
//    @SerializedName("product_attributes")
//    val productAttributes: ProductAttributes,
    @SerializedName("attributes")  //Lở thêm
    val attributes: Attributes, //Lở thêm
    @SerializedName("attributeIds")
    val attributeIds: List<String>,
    @SerializedName("product_description")
    val productDescription: String,
    @SerializedName("product_name")
    val productName: String,
    @SerializedName("product_price")
    val productPrice: Double,
    @SerializedName("product_ratingsAverage")
    val productRatingsAverage: Double,
    @SerializedName("product_slug")
    val productSlug: String,
    @SerializedName("product_stock")
    val productStock: Int,
    @SerializedName("product_thumbnail")
    val productThumbnail: String,
//    @SerializedName("variations")
//    val variations: List<Variation>,
    @SerializedName("variants") //Lở thêm
    val variants: List<Variation>,//Lở thêm
    @SerializedName("default_variant") //Lở thêm
    val default_variant: Variation //Lở thêm
) {
    data class Category(
        @SerializedName("attributes_template")
        val attributesTemplate: List<String>,
        @SerializedName("_id")
        val id: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("parent_category")
        val parentCategory: Any,
        @SerializedName("thumbnail")
        val thumbnail: String,
    )

    data class Attributes(
        @SerializedName("_id")
        val _id: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("values")
        val values: List<String>,
        @SerializedName("video_resolution")
        val videoResolution: String,
        @SerializedName("zoom")
        val zoom: String
    )

    data class Variation(
        @SerializedName("_id")
        val id: String,
        @SerializedName("productId")
        val productId: String,
        @SerializedName("variantDetails")
        val variantDetails: List<VariationDetail>,
        @SerializedName("price")
        val price: Double,
        @SerializedName("stock")
        val stock: Int,
        @SerializedName("sku")
        val sku: String,
    ){
        data class VariationDetail(
            @SerializedName("_id")
            val _id: String,
            @SerializedName("variantId")
            val variantId: String,
            @SerializedName("value")
            val value: String,
        )

    }

}
data class ProductResponse(
    val success: Boolean,
    val product: ProductModel
)
data class ProductListResponse(
    @SerializedName("products")
    val products: List<ProductModel>, // Danh sach san pham
)
data class SearchResponse(
    val success: Boolean,
    val products: List<ProductModel>
)
