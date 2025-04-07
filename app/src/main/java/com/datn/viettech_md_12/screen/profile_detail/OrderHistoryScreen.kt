package com.datn.viettech_md_12.screen.profile_detail


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.datn.viettech_md_12.R
import com.datn.viettech_md_12.data.model.OrderItem
import com.datn.viettech_md_12.data.model.OrderModel
import com.datn.viettech_md_12.viewmodel.ProductViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OrderHistoryScreen(navController: NavController,viewModel: ProductViewModel) {
    val context = LocalContext.current
    val orders = viewModel.orders.collectAsState()

    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        viewModel.getUserOrders(context)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
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
                text = stringResource(R.string.order_history),
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        DividerItemOrder()
        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(50.dp)
                .background(Color(0xffF4F5FD), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                backgroundColor = Color.Transparent,
                contentColor = Color.Black,
                modifier = Modifier.fillMaxWidth(),
                indicator = {}, // Ẩn đường underline mặc định
            ) {
                listOf("Đang thực hiện", "Hoàn thành").forEachIndexed { index, title ->
                    val isSelected = pagerState.currentPage == index
                    Tab(
                        selected = isSelected,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        modifier = Modifier
                            .padding(4.dp)
                            .clip(RoundedCornerShape(12.dp)) // Bo góc từng Tab
                            .background(if (isSelected) Color.Black else Color.Transparent) // Đổi màu khi được chọn
                            .padding(vertical = 8.dp, horizontal = 16.dp) // Padding bên trong
                    ) {
                        Text(
                            text = title,
                            color = if (isSelected) Color.White else Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
        HorizontalPager(
            count = 2,
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            when (page) {
                0 -> OngoingOrdersScreen(orders.value)
                1 -> CompletedOrdersScreen()
            }
        }
    }
}

@Composable
fun OngoingOrdersScreen(orderList: List<OrderModel>) {
    if (orderList.isEmpty()) {
        EmptyOrderScreen()
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(orderList) { order ->
                OrderCard(order)
            }
        }
    }
}


@Composable
fun CompletedOrdersScreen() {
    EmptyOrderScreen()
}

@Composable
fun OrderCard(order: OrderModel) {
    val BASE_URL = "http://103.166.184.249:3056/"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .background(Color(0xFFE63946), shape = RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Text(
                text = "Trạng thái: ${order.status}",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Lặp qua các sản phẩm trong đơn hàng
        order.products.forEach { product ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = BASE_URL + product.image,
                    contentDescription = "Product Image",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.LightGray),
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = product.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "VND ${product.price}", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
            Spacer(modifier = Modifier.height(8.dp)) // Thêm khoảng cách giữa các sản phẩm
        }
    }
}




@Composable
fun EmptyOrderScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.img_empty_order),
            contentDescription = null,
            modifier = Modifier.size(250.dp)
        )
        Text(
            text = "Không có đơn hàng đang diễn ra",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = "Hiện tại chúng tôi không có đơn hàng nào đang hoạt động. Hãy thoải mái khám phá sản phẩm của chúng tôi và đặt hàng mới.",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {

                },
                modifier = Modifier
                    .width(380.dp)
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Khám phá danh mục", color = Color.White)
            }
        }

    }
}

@Composable
fun DividerItemOrder() {
    Divider(
        color = Color(0xffF4F5FD),
        thickness = 1.dp,
    )
}

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_7)
@Composable
fun OrderHistoryScreenPreview() {
//    OrderHistoryScreen(navController = NavController(context = LocalContext.current))
}