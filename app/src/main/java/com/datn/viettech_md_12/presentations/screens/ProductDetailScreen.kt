package com.datn.viettech_md_12.presentations.screens

import MyButton
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.datn.viettech_md_12.R
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ProductDetailScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProductDetailUI()
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProductDetailUI() {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
//            .systemBarsPadding(),

            topBar = {
                TopAppBar(
                    title = { Text(text = "") },
                    modifier = Modifier.padding(end = 16.dp).systemBarsPadding(),
                    backgroundColor = Color.Transparent,
                    elevation = 0.dp,
                    navigationIcon = {
                        IconButton(onClick = { /* nút back */ }) {
                            Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                        }
                    },
                    actions = {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(color = Color.Black)
                        ) {
                            IconButton(onClick = { /* nút back */ }) {
                                Icon(
                                    contentDescription = "favourite icon",
                                    imageVector = Icons.Default.FavoriteBorder,
                                    tint = Color.White
                                )
                            }
                        }
                    },
                )
            },

            bottomBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(top = 0.dp, start = 16.dp, end = 16.dp, bottom = 40.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Bottom
                ) {
                    MyButton(
                        text = "Mua ngay",
                        onClick = { },
                        modifier = Modifier
                            .weight(1f)
                            .border(
                                width = 1.dp,
                                brush = SolidColor(Color(0xFFF4F5FD)),
                                shape = RoundedCornerShape(12.dp)
                            ),
                        backgroundColor = Color.White,
                        textColor = Color.Black,
                    )
                    Spacer(Modifier.width(10.dp))
                    MyButton(
                        text = "Thêm vào giỏ",
                        onClick = {
                            coroutineScope.launch {
                                Log.d("SnackbarDebug", "Snackbar gọi showSnackbar()")
                                snackbarHostState.showSnackbar("Đã thêm sản phẩm vào giỏ hàng thành công.")
                                Log.d("SnackbarDebug", "Snackbar hiển thị")
                            }
                            Log.d("SnackbarDebug", "ProductDetailUI: Add to cart ok")
                        },
                        modifier = Modifier.weight(1f),
                        backgroundColor = Color.Black,
                        textColor = Color.White,
                        vectorIcon = Icons.Default.AddShoppingCart
                    )
                }
            },

            ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_launcher_background),
                        contentDescription = "p detail image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(350.dp)
                            .background(Color.Gray),
                        contentScale = ContentScale.Crop
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(y = 300.dp) // Dịch ngang để tạo hiệu ứng
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.White)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(color = Color.Blue)
                            ) {
                                Text(
                                    "Top Rated",
                                    modifier = Modifier
                                        .padding(6.dp),
                                    color = Color.White,
                                    fontSize = 14.sp
                                )
                            }
                            Spacer(Modifier.width(10.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(color = Color.Green)
                            ) {
                                Text(
                                    "Free Shipping",
                                    modifier = Modifier
                                        .padding(6.dp),
                                    color = Color.White,
                                    fontSize = 14.sp
                                )
                            }
                        }
                        //Tên/giá
                        Spacer(Modifier.height(10.dp))
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Product name here lorem ipsum.........",
                                maxLines = 2,
                                fontSize = 18.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                            Column(
                                horizontalAlignment = Alignment.End
                            ) {
                                Text(
                                    "VND 1 500 000",
                                    fontSize = 16.sp,
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold,
                                )
                                Text(
                                    "VND 2 000 000",
                                    fontSize = 16.sp,
                                    color = Color.Black,
                                    textDecoration = TextDecoration.LineThrough
                                )
                            }
                        }
                        //Đánh giá
                        Spacer(Modifier.height(10.dp))
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Filled.Star,
                                contentDescription = "Star",
                                tint = Color(0xFFFFD700)
                            )
                            Text(
                                text = "4.5 (2,495 reviews)",
                                fontSize = 14.sp, color = Color.Black
                            )
                        }
                        // Description
                        Spacer(Modifier.height(10.dp))
                        Text(
                            text = "Constructed with high-quality silicone material...",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 8.dp)
                        )

                        // chọn màu
                        Spacer(Modifier.height(10.dp))
                        Text(text = "Color", fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 12.dp))
                        Row(modifier = Modifier.padding(top = 4.dp)) {
                            listOf(Color.Black, Color.Gray, Color.Blue, Color.Magenta).forEach { color ->
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(color, CircleShape)
                                        .border(0.dp, Color.LightGray, CircleShape)
                                        .padding(8.dp)
                                        .clickable { /* Handle Color Selection */ }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                        }

                        // Số lượng
                        Spacer(Modifier.height(10.dp))
                        Row(
                            modifier = Modifier
                                .border(
                                    width = 1.dp,
                                    brush = SolidColor(Color(0xFFF4F5FD)),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            IconButton(
                                onClick = {/* if (item.quantity > 1) onQuantityChange(item.id, item.quantity - 1) */ },
                                modifier = Modifier.size(20.dp)
                            ) {
                                Icon(Icons.Default.Remove, contentDescription = "Decrease")
                            }
                            Text("1", modifier = Modifier.padding(horizontal = 14.dp))
                            IconButton(
                                onClick = { /*onQuantityChange(item.id, item.quantity + 1)*/ },
                                modifier = Modifier.size(20.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Increase")
                            }
                        }
                    }

                }
            }//end column
        }//end scaffold
//            Box(
//                modifier = Modifier.fillMaxSize(),
//                contentAlignment = Alignment.TopCenter // Hiển thị snackbar ở trên cùng
//            ) {
//        SnackbarHost(snackbarHostState) { data ->
//            Snackbar(
//                action = {
//                    TextButton(onClick = { /* Xử lý khi bấm View Cart */ }) {
//                        Text("Xem giỏ hàng", color = Color.Cyan)
//                    }
//                },
//                modifier = Modifier
//                    .background(Color.Red)
//                    .align(Alignment.TopCenter)
//                    .padding(top = 16.dp, start = 16.dp, end = 16.dp),
//            ) {
////                    Text(data.visuals.message)
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Icon(
//                        imageVector = Icons.Default.CheckCircle,
//                        contentDescription = "Success",
//                        tint = Color(0xFF00C4B4),
//                        modifier = Modifier.size(24.dp)
//                    )
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Text("đã thêm vào giỏ hàng!")
//                }
////                    }
//            }
//        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(start = 16.dp, end = 16.dp).systemBarsPadding(),
        ) { data ->
            // Custom Snackbar with white background and rounded corners
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .border(
                        width = 1.dp,
                        color = Color(0xFFEEEEEE),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Success",
                            tint = Color(0xFF00C4B4),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Đã thêm vào giỏ hàng!",
                            color = Color.Black
                        )
                    }

                    TextButton(onClick = { data.performAction() }) {
                        Text(
                            "Xem giỏ hàng",
                            color = Color(0xFF00C4B4),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

//Hiện thông báo ở trên bằng AnimatedVisibility
//fun ProductDetailUI() {
//    var showNotification by remember { mutableStateOf(false) }
//    val coroutineScope = rememberCoroutineScope()
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        // Thanh thông báo xuất hiện từ trên xuống
//        AnimatedVisibility(
//            visible = showNotification,
//            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
//            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = 16.dp)
//                .align(Alignment.TopCenter)
//        ) {
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 16.dp),
//                shape = RoundedCornerShape(8.dp),
//                backgroundColor = Color.White,
//                elevation = 8.dp
//            ) {
//                Row(
//                    modifier = Modifier.padding(16.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.CheckCircle,
//                        contentDescription = "Success",
//                        tint = Color(0xFF00C4B4),
//                        modifier = Modifier.size(24.dp)
//                    )
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Text("The product has been added to your cart", fontSize = 14.sp)
//                    Spacer(modifier = Modifier.weight(1f))
//                    TextButton(onClick = { /* Điều hướng đến giỏ hàng */ }) {
//                        Text("View Cart", color = Color(0xFF00C4B4))
//                    }
//                }
//            }
//        }
//
//        // Nút Add To Cart
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(bottom = 32.dp),
//            verticalArrangement = Arrangement.Bottom,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Row(
//                horizontalArrangement = Arrangement.spacedBy(16.dp),
//                modifier = Modifier.padding(16.dp)
//            ) {
//                OutlinedButton(onClick = { /* Xử lý mua ngay */ }) {
//                    Text("Buy Now")
//                }
//                Button(
//                    onClick = {
//                        showNotification = true
//                        // Ẩn thông báo sau 3 giây
//                        coroutineScope.launch {
//                            kotlinx.coroutines.delay(3000) // Ẩn thông báo sau 3 giây
//                            showNotification = false
//                        }
//                    },
//                    colors = ButtonDefaults.buttonColors(Color.Black)
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.ShoppingCart,
//                        contentDescription = "Cart",
//                        tint = Color.White
//                    )
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Text("Add To Cart", color = Color.White)
//                }
//            }
//        }
//    }
//}


@Preview(showSystemUi = true)
@Composable
fun ProductDetailPreview() {
    ProductDetailUI()
}