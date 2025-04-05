package com.datn.viettech_md_12.data.model

import com.google.gson.annotations.SerializedName

data class CheckoutModel(
    @SerializedName("message") val message: String,
    @SerializedName("code") val statusCode: Int,
    @SerializedName("status") val status: String,
    @SerializedName("data") val metadata: AddressData
)

data class AddressModel(
    @SerializedName("message") val message: String,
    @SerializedName("code") val statusCode: Int,
    @SerializedName("status") val status: String,
    @SerializedName("data") val data: AddressData
)

data class AddressData(
    @SerializedName("_id") val _id: String,
    @SerializedName("username") val username: String,
    @SerializedName("full_name") val full_name: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("address") val address: String,
    @SerializedName("email") val email: String,
    @SerializedName("status") val status: String,
    @SerializedName("role") val role: String,
)

data class UpdateAddressRequest(
    @SerializedName("full_name") val full_name: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("address") val address: String,

)
