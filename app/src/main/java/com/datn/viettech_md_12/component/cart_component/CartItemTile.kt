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
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch
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
    onDeletingStateChange: (Boolean) -> Unit,
    snackbarHostState:SnackbarHostState
) {
    val swipeableState = rememberSwipeableState(initialValue = 0)
    val swipeThreshold = 250f
    val anchors = mapOf(0f to 0, -swipeThreshold to 1)
    val quantityState = remember { mutableStateOf(product.quantity) }
    val coroutineScope = rememberCoroutineScope()
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
                state = swipeableState, anchors = anchors, thresholds = { _, _ -> FractionalThreshold(0.5f) }, orientation = Orientation.Horizontal
            )
            .background(
                if (swipeableState.offset.value < -swipeThreshold / 2) Color.Red else Color.Transparent, shape = RoundedCornerShape(
                    topStart = 20.dp, bottomStart = 20.dp, topEnd = 10.dp, bottomEnd = 10.dp
                )
            )
            .clip(RoundedCornerShape(10.dp))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxHeight()
                .width(100.dp)
                .background(Color.Transparent)
                .align(Alignment.CenterEnd)
                .padding(end = 10.dp)
                .clickable {
                    onDeletingStateChange(true)
                    onDelete(product.productId, variantIdToUse)
                    cartViewModel.deleteCartItem(productId = product.productId, detailsVariantId = product.detailsVariantId ?: "", onSuccess = {
                        // Có thể thêm thông báo thành công
                        onDeletingStateChange(false)
                        Log.d("CartItemTile", "Deleting productId: ${product.productId}, variantId: ${product.detailsVariantId}")
                        Log.d("CartItemTile", "Xóa sản phẩm thành công")
                    }, onError = { error ->
                        onDeletingStateChange(false)
                        Log.e("CartItemTile", "Lỗi khi xóa sản phẩm: $error")
                        // Có thể hiển thị Snackbar thông báo lỗi
                    })
                    Log.d("CartItemTile", "ondelete: clicked")
                },

            ) {
            Text(
                text = "Xóa", color = Color.White, fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                Icons.Default.Delete, contentDescription = "Delete", tint = Color.White
            )
        }

        // nội dung của item
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(swipeableState.offset.value.toInt(), 0) }
                .background(Color.White)
                .padding(horizontal = 4.dp, vertical = 4.dp)
                .clickable {
                    navController.navigate("product_detail/${product.productId}") // Chuyển đến chi tiết sản phẩm
                },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "cart item image",
                modifier = Modifier
                    .size(80.dp)
                    .background(Color(0xFFF4F4F4), RoundedCornerShape(10.dp))
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.logo),
                error = painterResource(R.drawable.error_img),
                onError = { Log.e("CartItemTile", "Failed to load image: $imageUrl") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(product.name, fontSize = 14.sp, fontWeight = FontWeight.W600, maxLines = 2, overflow = TextOverflow.Ellipsis, lineHeight = 16.sp, color = Color.Black)
                // Hiển thị danh sách các value của biến thể, cách nhau bởi dấu phẩy
                val variantValues = product.variant_details?.values?.joinToString(", ") { it.value } ?: ""
                if(!variantValues.isNullOrEmpty()){
                    Text(variantValues, fontSize = 13.sp, color = Color.Gray,maxLines = 2, overflow = TextOverflow.Ellipsis, lineHeight = 14.sp)
                }
                Text("$itemPriceFormatted₫", fontSize = 13.sp, color = Color.Black)
              Row(
                  verticalAlignment = Alignment.CenterVertically
              ) {
                  Row(
                      modifier = Modifier
                          .border(
                              width = 1.dp, brush = SolidColor(Color(0xFFF4F5FD)), shape = RoundedCornerShape(6.dp)
                          )
                          .padding(horizontal = 8.dp, vertical = 4.dp),
                      verticalAlignment = Alignment.CenterVertically,
                      horizontalArrangement = Arrangement.SpaceBetween
                  ) {
                      IconButton(
                          onClick = {
                              if (quantityState.value > 1) {
                                  quantityState.value -= 1
                                  coroutineScope.launch {
                                      cartViewModel.updateProductQuantity(
                                          productId = product.productId,
                                          variantId = product.detailsVariantId ?: "",
                                          newQuantity = quantityState.value,
                                      )
                                  }
                              }
                          },
                          modifier = Modifier.size(20.dp),
                          enabled = quantityState.value > 1
                      ) {
                          Icon(Icons.Default.Remove, contentDescription = "Decrease",
                              tint = if (quantityState.value > 1) Color.Black else Color.Gray)
                      }
                      Text(if(product?.stock == 0) "0" else "${quantityState.value}", fontSize = 13.sp, modifier = Modifier.padding(horizontal = 12.dp), color = Color.Black)
                      IconButton(
                          onClick = {
                              if (quantityState.value < (product?.stock ?: Int.MAX_VALUE)) {
                                  quantityState.value += 1
                                  coroutineScope.launch {
                                      cartViewModel.updateProductQuantity(
                                          productId = product.productId,
                                          variantId = product.detailsVariantId ?: "",
                                          newQuantity = quantityState.value,
                                      )
                                  }
                              }else if (product!!.stock == 1) {
                                  coroutineScope.launch {
                                      snackbarHostState.showSnackbar("Số lượng sản phẩm này chỉ còn ${product?.stock} trong kho")
                                  }
                              }
                          },
                          modifier = Modifier.size(20.dp),
                          enabled = quantityState.value < (product?.stock ?: Int.MAX_VALUE)
                      ) {
                          Icon(Icons.Default.Add, contentDescription = "Increase",
                              tint = if (quantityState.value < (product?.stock ?: Int.MAX_VALUE)) Color.Black else Color.Gray
                          )
                      }
                  }
                  if(product?.stock == 0) {
                      Spacer(Modifier.width(8.dp))
                      Text("Hết hàng", color = Color.Red, fontSize = 13.sp)
                  }
              }
            }
            Spacer(Modifier.width(4.dp))
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(20.dp)
                    .padding(end = 6.dp),
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