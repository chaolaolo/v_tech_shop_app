package com.datn.viettech_md_12.data.model

import com.google.gson.annotations.SerializedName

data class CategoryModel(
    @SerializedName("_id") val id: String,
    @SerializedName("attributes_template") val attributesTemplate: List<String>,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("name") val name: String,
    @SerializedName("parent_category") val parentCategory: Any?,
    @SerializedName("updatedAt") val updatedAt: String
)


data class CategoryListResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("categories") val categories: List<CategoryModel>
)