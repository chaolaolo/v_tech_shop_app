package com.datn.viettech_md_12.component.product_detail_components

import androidx.compose.ui.graphics.Color


fun String.toColor(): Color {
    return when (this.trim().lowercase()) {
        "space black", "black" -> Color.Black
        "silver", "gray" -> Color.Gray
        "gold" -> Color(0xFFD4AF37)
        "deep blue", "blue" -> Color.Blue
        else -> Color.Gray
    }
}