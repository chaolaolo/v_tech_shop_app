package com.datn.viettech_md_12.component.checkout

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.datn.viettech_md_12.R
import com.datn.viettech_md_12.data.model.CartModel
import com.datn.viettech_md_12.viewmodel.CartViewModel
import com.datn.viettech_md_12.viewmodel.CheckoutViewModel
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CheckoutItemTile(
    product: CartModel.Metadata.CartProduct,
    cartViewModel: CartViewModel,
    checkoutViewModel: CheckoutViewModel,
    onQuantityChange: (Int) -> Unit
) {
    val imageUrl = if (product.image.startsWith("http")) {
        product.image
    } else {
        "http://103.166.184.249:3056/${product.image.replace("\\", "/")}"
    }
    Log.d("CheckoutItemTile", "Loading image from URL: $imageUrl")

    val itemPrice = product.price
    val itemPriceFormatted = NumberFormat.getNumberInstance(Locale("vi", "VN")).format(itemPrice)
    val quantityState = remember { mutableStateOf(product.quantity) }
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .padding(bottom = 4.dp)
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .padding(bottom = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(start = 4.dp, end = 6.dp, top = 4.dp, bottom = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .background(Color(0xFFF4F4F4), RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.ic_launcher_background),
                error = painterResource(R.drawable.error_img),
                onError = { Log.e("CheckoutItemTile", "Failed to load image: $imageUrl") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            product.name,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.W600,
                            color = Color.Black,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                        val variantValues =
                            product.variant?.values?.joinToString(", ") { it.value } ?: ""
                        if (!variantValues.isNullOrEmpty()) {
                            Text(
                                variantValues,
                                fontSize = 12.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.W500,
                            )
                        }
                        Text("$itemPriceFormatted₫", fontSize = 12.sp, fontWeight = FontWeight.W500, color = Color.Black)
                    }
                    Row(
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                brush = SolidColor(Color(0xFFF4F5FD)),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(
                            onClick = {
//                            if (product.quantity > 1) onQuantityChange(product.productId, product.quantity - 1)
                                if (quantityState.value > 1) {
                                    quantityState.value -= 1
                                    onQuantityChange(quantityState.value)
                                    coroutineScope.launch {
                                        cartViewModel.updateProductQuantity(
                                            productId = product.productId,
                                            variantId = product.detailsVariantId ?: "",
                                            newQuantity = quantityState.value,
                                        )
                                        // Sau khi cập nhật xong, refresh lại danh sách
                                        checkoutViewModel.refreshSelectedItems()
                                    }
                                }
                            },
                            modifier = Modifier.size(18.dp)
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "Decrease", tint = Color.Black)
                        }
                        androidx.compose.material.Text(
                            "${quantityState.value}",
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )
                        IconButton(
                            onClick = {
//                            onQuantityChange(product.productId, product.quantity + 1)
                                quantityState.value += 1
                                onQuantityChange(quantityState.value)
                                coroutineScope.launch {
                                    cartViewModel.updateProductQuantity(
                                        productId = product.productId,
                                        variantId = product.detailsVariantId ?: "",
                                        newQuantity = quantityState.value,
                                    )

                                    checkoutViewModel.refreshSelectedItems()
                                }
                            },
                            modifier = Modifier.size(18.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Increase", tint = Color.Black)
                        }
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(end = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                "Số lượng ${quantityState.value}, tổng cộng ",
//             textAlign = TextAlign.End,
                fontSize = 12.sp,
                color = Color.Black
            )
            Text(
                "${
                    NumberFormat.getNumberInstance(Locale("vi", "VN"))
                        .format(quantityState.value * itemPrice)
                }₫",
//             textAlign = TextAlign.End,
                fontSize = 12.sp,
                color = Color.Black,
                fontWeight = FontWeight.W600
            )
        }
    }
}