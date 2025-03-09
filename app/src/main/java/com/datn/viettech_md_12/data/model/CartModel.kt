package com.datn.viettech_md_12.data.model

data class CartModel(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val price: Double,
    val originalPrice: Double,
    var quantity: Int,
    var isSelected: Boolean = true
)

