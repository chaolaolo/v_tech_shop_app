package com.datn.viettech_md_12.screen

import MyButton
import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.datn.viettech_md_12.R
import com.datn.viettech_md_12.viewmodel.ProductViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProductDetailScreen(navController: NavController, productId: String, viewModel: ProductViewModel= viewModel()) {
    LaunchedEffect(productId) {
        viewModel.getProductById(productId)
    }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val product by viewModel.product.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            product?.let {
                Scaffold(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
//            .systemBarsPadding(),

                    topBar = {
                        TopAppBar(
                            title = { Text(text = "") },
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .systemBarsPadding()
                                .shadow(elevation = 0.dp),
                            colors = TopAppBarColors(
                                containerColor = Color.Transparent,
                                scrolledContainerColor = Color.Transparent,
                                navigationIconContentColor = Color.Black,
                                titleContentColor = Color.Black,
                                actionIconContentColor = Color.Transparent
                            ),
                            navigationIcon = {
                                IconButton(onClick = { navController.popBackStack() }) {
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

                    ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        AsyncImage(
                            model = "http://103.166.184.249:3056/${product?.productThumbnail}",
                            contentDescription = "p detail image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(350.dp)
                                .background(Color(0xFFF4FDFA))
                            ,
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(R.drawable.logo),
                            error = painterResource(R.drawable.ic_logo)
                        )
                        Log.d("zzzzzzzzzzzzzz", "productThumbnail: ${product?.productThumbnail}")
//                            Image(
//                                painter = painterResource(R.drawable.ic_launcher_background),
//                                contentDescription = "p detail image",
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .height(350.dp)
//                                    .background(Color.Gray),
//                                contentScale = ContentScale.Crop
//                            )
                        Card(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 250.dp), // Đẩy nội dung lên che hình ảnh
                            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
//                            backgroundColor = Color.White,
//                            elevation = 8.dp
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Color.White)
                                    .padding(16.dp)
                                    .verticalScroll(rememberScrollState()),
//                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(color = Color(0xFF1F8BDA))
                                    ) {
                                        Text(
                                            "Top Rated",
                                            modifier = Modifier.padding(4.dp),
                                            color = Color.White,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Spacer(Modifier.width(10.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(color = Color(0xFF08E488))
                                    ) {
                                        Text(
                                            "Free Shipping",
                                            modifier = Modifier.padding(4.dp),
                                            color = Color.White,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
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
                                        "${product?.productName}",
                                        maxLines = 2,
                                        fontSize = 16.sp,
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Column(
                                        horizontalAlignment = Alignment.End
                                    ) {
                                        Text(
                                            "${product?.productPrice}",
                                            fontSize = 14.sp,
                                            color = Color.Black,
                                            fontWeight = FontWeight.Bold,
                                        )
                                        Text(
                                            "${product?.productPrice}",
                                            fontSize = 14.sp,
                                            color = Color.Gray,
                                            textDecoration = TextDecoration.LineThrough
                                        )
                                    }
                                }
                                //Đánh giá
                                Spacer(Modifier.height(4.dp))
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
                                        fontSize = 12.sp,
                                        color = Color.Black
                                    )
                                }
                                // Description
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    text = "${product?.productDescription}",
                                    fontSize = 12.sp,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(top = 8.dp),
                                )

                                // chọn màu
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    text = "Color",
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(top = 10.dp)
                                )
                                Row(modifier = Modifier.padding(top = 4.dp)) {
                                    listOf(
                                        Color.Black, Color.Gray, Color.Blue, Color.Magenta
                                    ).forEach { color ->
                                        Box(
                                            modifier = Modifier
                                                .size(32.dp)
                                                .background(color, CircleShape)
                                                .border(0.dp, Color.LightGray, CircleShape)
                                                .padding(8.dp)
                                                .clickable { /* Handle Color Selection */ })
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
                                        .padding(horizontal = 6.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    IconButton(
                                        onClick = {/* if (item.quantity > 1) onQuantityChange(item.id, item.quantity - 1) */ },
                                        modifier = Modifier.size(16.dp)
                                    ) {
                                        Icon(Icons.Default.Remove, contentDescription = "Decrease")
                                    }
                                    Text("1", modifier = Modifier.padding(horizontal = 12.dp))
                                    IconButton(
                                        onClick = { /*onQuantityChange(item.id, item.quantity + 1)*/ },
                                        modifier = Modifier.size(16.dp)
                                    ) {
                                        Icon(Icons.Default.Add, contentDescription = "Increase")
                                    }
                                }

                                Spacer(Modifier.weight(1f))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(),
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
                            }

                        }
                    }
                }
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(start = 16.dp, end = 16.dp)
                        .systemBarsPadding()
                        .background(Color.Transparent),
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
                                    "Đã thêm vào giỏ hàng!", color = Color.Black
                                )
                            }

                            TextButton(onClick = { navController.navigate("cart") }) {
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
    }
}
