package com.datn.viettech_md_12.screen.profile_detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.datn.viettech_md_12.R
import com.datn.viettech_md_12.data.model.OrderModel
import com.datn.viettech_md_12.data.model.OrderProduct
import com.datn.viettech_md_12.viewmodel.ProductViewModel
import java.text.NumberFormat
import java.util.Locale

// ... (imports giữ nguyên)

@Composable
fun OrderDetailScreen(
    orderId: String,
    navController: NavController,
    viewModel: ProductViewModel = viewModel()
) {
    val context = LocalContext.current
    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
    val order by viewModel.selectedOrder.collectAsState()
    val BASE_URL = "http://103.166.184.249:3056/"

    LaunchedEffect(orderId) {
        viewModel.getOrderById(context, orderId)
    }

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color(0xFFF5F5F5))
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = Color.Black
                )
            }
            Text(
                text = stringResource(R.string.order_infomation),
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }

        order?.let { currentOrder ->
            Card(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF1ABF79))
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Đơn hàng đã hoàn thành",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Thông tin vận chuyển",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "SPX Express: SPXVN04485138241B",
                            fontSize = 13.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🚚", fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = currentOrder.status ?: "",
                                    color = Color(0xFF1ABF79),
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "${currentOrder.createdAt ?: ""} - ${currentOrder.updatedAt ?: ""}",
                                    fontSize = 13.sp,
                                    color = Color.Black
                                )
                            }
                        }
                    }

                    Divider(color = Color.LightGray, thickness = 1.dp)

                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Địa chỉ nhận hàng",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row {
                            Text(text = "📍", fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = "${currentOrder.receiver_name ?: ""} - ${currentOrder.phone_number ?: ""}",
                                    fontSize = 14.sp,
                                    color = Color.Black
                                )
                                Text(
                                    text = currentOrder.address ?: "",
                                    fontSize = 13.sp,
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }
            }

            // Sản phẩm
            Card(
                modifier = Modifier.padding(12.dp),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(Modifier.background(Color.White)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .background(Color.Red, shape = RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("Yêu thích", color = Color.White, fontSize = 10.sp)
                        }
                        Spacer(Modifier.width(6.dp))
                        Text("VietTech", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.Black)
                    }

                    Divider()

                    val firstProduct = currentOrder.products?.firstOrNull()
                    if (firstProduct != null) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            AsyncImage(
                                model = "$BASE_URL${firstProduct.image}",
                                contentDescription = null,
                                modifier = Modifier
                                    .height(80.dp)
                                    .width(80.dp)
                                    .background(Color.LightGray, RoundedCornerShape(8.dp))
                            )

                            Spacer(Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = firstProduct.name ?: "",
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 14.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = Color.Black
                                )
                                Spacer(Modifier.height(6.dp))
                                Text(
                                    text = "x${firstProduct.quantity}",
                                    fontSize = 13.sp,
                                    color = Color.Black
                                )
                            }

                            Text(
                                text = formatter.format(firstProduct.price ?: 0.0),
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp
                            )
                        }
                    }

                    Divider()

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Thành tiền:", fontSize = 14.sp, color = Color.Black)
                        Text(
                            formatter.format(currentOrder.total ?: 0.0),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp, color = Color.Black
                        )
                    }
                }
            }

            // Hỗ trợ
            Card(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                shape = RoundedCornerShape(8.dp), elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Bạn cần hỗ trợ?", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
                        Spacer(Modifier.height(12.dp))
                        Text("• Gửi yêu cầu Trả hàng/Hoàn tiền", fontSize = 13.sp,color = Color.Black)
                        Spacer(Modifier.height(4.dp))
                        Text("• Liên hệ Shop", fontSize = 13.sp,color = Color.Black)
                        Spacer(Modifier.height(4.dp))
                        Text("• Trung tâm hỗ trợ", fontSize = 13.sp,color = Color.Black)
                    }
                }
            }

            // Mã đơn hàng
            Card(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                shape = RoundedCornerShape(8.dp), elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Mã đơn hàng: ${currentOrder.order_code ?: ""}", fontSize = 13.sp,color = Color.Black)
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(
                    onClick = { /* TODO */ },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 6.dp)
                ) {
                    Text("Xem Đánh giá")
                }
                Button(
                    onClick = { /* TODO */ },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 6.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Mua lại", color = Color.White)
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}


@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_7)
@Composable
fun Detail() {
//    OrderDetailScreen()
}
