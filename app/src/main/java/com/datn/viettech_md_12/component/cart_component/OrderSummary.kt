package com.datn.viettech_md_12.component.cart_component

import MyButton
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.datn.viettech_md_12.data.model.CartModel
import com.datn.viettech_md_12.data.model.DiscountResponse
import com.datn.viettech_md_12.screen.checkout.formatCurrency
@Composable
fun OrderSummary(
    navController: NavController,
    selectedItems: List<CartModel.Metadata.CartProduct>,
    selectedVoucher: DiscountResponse.DiscountModel? = null,
) {
    val subtotal = selectedItems.filter { it.isSelected }.sumOf { it.price * it.quantity }
    val shippingFee = remember(selectedItems) {
        if (selectedItems.any { it.isSelected }) 35000.0 else 0.0
    }
    val discount = remember { 0.0 }
    val discountPercentage = selectedVoucher?.discountValue ?: 0.0
    val discountAmount = remember(subtotal, discountPercentage) {
        (subtotal * discountPercentage / 100)
    }
    val maxDiscountAmount = selectedVoucher?.maxDiscountAmount ?: Double.MAX_VALUE
    val finalDiscountAmount = remember(discountAmount, maxDiscountAmount) {
        minOf(discountAmount, maxDiscountAmount)
    }
    val total = remember(subtotal, shippingFee, finalDiscountAmount) {
        subtotal + shippingFee - finalDiscountAmount
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, top = 6.dp)
    ) {
        Text("Thông tin đặt hàng", fontWeight = FontWeight.W600, fontSize = 14.sp, color = Color.Black)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Tổng giá tiền", fontSize = 12.sp, color = Color.Gray)
            Text("${"%.2f".format(subtotal)}₫", fontSize = 12.sp, color = Color.Gray)
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Phí vận chuyển", fontSize = 12.sp, color = Color.Gray)
            Text("${formatCurrency(shippingFee)}₫", fontSize = 12.sp, color = Color.Gray)
        }
        if (finalDiscountAmount > 0) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Giảm giá (${discountPercentage.toInt()}%)", fontSize = 12.sp, color = Color(0xFF00C2A8))
                Text("-${formatCurrency(finalDiscountAmount)}₫", fontSize = 12.sp, color = Color(0xFF00C2A8))
            }
        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 0.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Tổng thanh toán", fontSize = 16.sp, fontWeight = FontWeight.W500, color = Color.Black)
            Text("${formatCurrency(total)}₫", fontSize = 16.sp, fontWeight = FontWeight.W500, color = Color.Black)
        }
        Spacer(Modifier.height(5.dp))
        MyButton(
            text = "Thanh Toán(${selectedItems.count{it.isSelected}})",
            onClick = {
//                navController.navigate("payment_ui")
                navController.navigate("payment_ui/${selectedVoucher?.code ?: ""}") // Chuyển đến chi tiết sản phẩm
                      },
            backgroundColor = Color.Black,
            textColor = Color.White,
        )
    }
}//end order summary

