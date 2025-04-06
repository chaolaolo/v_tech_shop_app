package com.datn.viettech_md_12.component.cart_component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.datn.viettech_md_12.R
import com.datn.viettech_md_12.data.model.CartModel
import com.datn.viettech_md_12.viewmodel.CartViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CartItemTile(
    product: CartModel.Metadata.CartProduct,
    isSelected: Boolean,
    onSelectionChange: (Boolean) -> Unit,
    onDelete: (String, String) -> Unit,
    navController: NavController,
    cartViewModel: CartViewModel,
) {
    val swipeableState = rememberSwipeableState(initialValue = 0)
    val swipeThreshold = 250f
    val anchors = mapOf(0f to 0, -swipeThreshold to 1)
    val quantityState = remember { mutableStateOf(product.quantity) }
    // Xử lý khi detailsVariantId null thì dùng productId
    val variantIdToUse = product.detailsVariantId ?: product.productId
    val imageUrl = if (product.image.startsWith("http")) {
        product.image
    } else {
        "http://103.166.184.249:3056/${product.image.replace("\\", "/")}"
    }
    Log.d("lol", "Loading image from URL: $imageUrl")

    var localIsSelected by remember { mutableStateOf(isSelected) }
    val itemPrice = product.price
    val itemPriceFormatted = NumberFormat.getNumberInstance(Locale("vi", "VN")).format(itemPrice)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.5f) },
                orientation = Orientation.Horizontal
            )
            .background(if (swipeableState.offset.value < -swipeThreshold / 2) Color.Red else Color.White)
    ) {
        //nút xóa sp khỏi giỏ hàng
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 10.dp)
                .clickable {
                    onDelete(product.productId, variantIdToUse)
                    cartViewModel.deleteCartItem(
                        productId = product.productId,
                        detailsVariantId = product.detailsVariantId ?: "",
                        onSuccess = {
                            // Có thể thêm thông báo thành công
                            Log.d("CartItemTile", "Deleting productId: ${product.productId}, variantId: ${product.detailsVariantId}")
                            Log.d("CartItemTile", "Xóa sản phẩm thành công")
                        },
                        onError = { error ->
                            Log.e("CartItemTile", "Lỗi khi xóa sản phẩm: $error")
                            // Có thể hiển thị Snackbar thông báo lỗi
                        }
                    )
                    Log.d("CartScreen", "ondelete: clicked")
                },
            contentAlignment = Alignment.CenterEnd
        ) {
            Row(
                modifier = Modifier
                    .height(80.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Xóa",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.White
                )
            }
        }

        // nội dung của item
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(swipeableState.offset.value.toInt(), 0) }
                .background(Color.White)
                .clickable {
                    navController.navigate("product_detail/${product.productId}") // Chuyển đến chi tiết sản phẩm
                },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .background(Color.Transparent)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.ic_launcher_background),
                error = painterResource(R.drawable.error_img),
                onError = { Log.e("lol", "Failed to load image: $imageUrl") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(product.name, fontSize = 12.sp, fontWeight = FontWeight.W600, maxLines = 2, overflow = TextOverflow.Ellipsis, lineHeight = 12.sp, color = Color.Black)
                Text(product.variant?.sku ?: "", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.W500, maxLines = 2, overflow = TextOverflow.Ellipsis, lineHeight = 1.sp)
                Text("$itemPriceFormatted₫", fontSize = 10.sp, fontWeight = FontWeight.W500, lineHeight = 1.sp, color = Color.Black)
                Row(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            brush = SolidColor(Color(0xFFF4F5FD)),
                            shape = RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 4.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = {
                            if (quantityState.value > 1) {
                                quantityState.value -= 1
                                cartViewModel.updateProductQuantity(
                                    productId = product.productId,
                                    variantId = product.detailsVariantId ?: "",
                                    newQuantity = quantityState.value,
                                )
                            }
                        },
                        modifier = Modifier.size(16.dp)
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Decrease")
                    }
                    Text("${quantityState.value}", fontSize = 12.sp, modifier = Modifier.padding(horizontal = 12.dp), color = Color.Black)
                    IconButton(
                        onClick = {
                            quantityState.value += 1
                            cartViewModel.updateProductQuantity(
                                productId = product.productId,
                                variantId =  product.detailsVariantId ?: "",
                                newQuantity = quantityState.value,
                            )
                        },
                        modifier = Modifier.size(16.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Increase")
                    }
                }
            }
            Spacer(Modifier.width(4.dp))
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(20.dp)
                    .padding(end = 4.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = { isChecked ->
                        onSelectionChange(isChecked)
                        cartViewModel.updateIsSelected(
                            productId = product.productId,
                            detailsVariantId = product.detailsVariantId,
                            isSelected = isChecked,
                            onSuccess = {
                                onSelectionChange(isChecked)
                                // Update local UI state via callback
                                Log.d("CartItemTile", "Updated selection status: $isChecked for product: ${product.productId}")
                            },
                            onError = { error ->
                                onSelectionChange(!isChecked)
                                Log.e("CartItemTile", "Failed to update selection: $error")
                                // You could show an error message here if needed
                            }
                        )
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFF21D4B4),
                        uncheckedColor = Color.Gray,
                        checkmarkColor = Color.White,
                        disabledCheckedColor = Color.LightGray, // vô hiệu hóa và được chọn
                        disabledUncheckedColor = Color.LightGray // vô hiệu hóa và không được chọn
                    ),
                )
            }
        }
    }
}