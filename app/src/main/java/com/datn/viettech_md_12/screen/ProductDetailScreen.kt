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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.datn.viettech_md_12.R
import com.datn.viettech_md_12.viewmodel.CartViewModel
import com.datn.viettech_md_12.viewmodel.ProductViewModel
import com.datn.viettech_md_12.viewmodel.ReviewViewModel
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProductDetailScreen(
    navController: NavController,
    productId: String,
    viewModel: ProductViewModel= viewModel(),
    reviewViewModel: ReviewViewModel = viewModel()
) {
    LaunchedEffect(productId) {
        viewModel.getProductById(productId)
        reviewViewModel.getReviewsByProduct(productId)
    }
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val product by viewModel.product.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var isFavorite by remember { mutableStateOf(false) }

    var isExpanded by remember { mutableStateOf(false) }
    var showMoreVisible by remember { mutableStateOf(false) }
    val textLayoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }
    val reviews by reviewViewModel.reviews.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var selectedImageUrl by remember { mutableStateOf("") }

    val price = product?.productPrice ?:0.0
    val itemPriceFormatted = NumberFormat.getNumberInstance(Locale("vi", "VN")).format(price)

    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            CircularProgressIndicator(
                color = Color(0xFF21D4B4),
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
                                        .size(30.dp)
                                ) {
                                    IconButton(onClick = { isFavorite = !isFavorite }) {
                                        Icon(
                                            contentDescription = "favourite icon",
                                            imageVector = if (!isFavorite) Icons.Default.FavoriteBorder else Icons.Default.Favorite,
                                            tint = if (!isFavorite) Color.White else Color.Red,
                                            modifier = Modifier
                                                .size(20.dp)
                                        )
                                    }
                                }
                            },
                        )
                    },

                    ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                                .padding(bottom = 20.dp)
                                .background(Color(0xFFF4FDFA)),
                        ) {
                            AsyncImage(
                            model = "http://103.166.184.249:3056/${product?.productThumbnail}",
                                contentDescription = "p detail image",
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentScale = ContentScale.Crop,
                                placeholder = painterResource(R.drawable.logo),
                                error = painterResource(R.drawable.error_img)
                            )
                        }
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
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.Start
                                    ) {
                                        Text(
                                            "${product!!.productStock} còn hàng",
                                            fontSize = 12.sp,
                                            color = Color.Gray,
                                        )
                                        Text(
                                        "${product?.productName}",
                                        maxLines = 2,
                                        fontSize = 16.sp,
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                    }
                                        Text(
                                            "$itemPriceFormatted₫",
                                            fontSize = 14.sp,
                                            color = Color.Black,
                                            fontWeight = FontWeight.Bold,
                                        )

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
                                FlowRow(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.Start),
//                                    mainAxisAlignment = MainAxisAlignment.Center,
                                ) {
                                    Text(
                                    text = "${product?.productDescription}",
                                        fontSize = 12.sp,
                                    color = Color.Gray,
                                        modifier = Modifier.padding(top = 8.dp),
                                        maxLines = if (isExpanded) Int.MAX_VALUE else 5,
                                        overflow = TextOverflow.Ellipsis,
                                        onTextLayout = { layoutResult ->
                                            textLayoutResult.value = layoutResult
                                            showMoreVisible = layoutResult.hasVisualOverflow && !isExpanded
                                        }
                                )
                                    if (showMoreVisible) {
                                        Text(text = "... Xem thêm",
                                            fontSize = 12.sp,
                                            color = Color(0xFF21D4B4),
                                            fontWeight = FontWeight.Medium,
                                            modifier = Modifier
                                                .padding(top = 4.dp)
                                                .clickable { isExpanded = true })
                                    } else if (isExpanded) {
                                        Text(text = "Thu gọn",
                                            fontSize = 12.sp,
                                            color = Color(0xFF21D4B4),
                                            fontWeight = FontWeight.Medium,
                                            modifier = Modifier
                                                .padding(top = 4.dp)
                                                .clickable { isExpanded = false })
                                    }
                                }

                                // chọn màu
                                Spacer(Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Bottom
                                ) {
                                    Column {
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
                                    }

                                // Số lượng
//                                Spacer(Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier
                                        .border(
                                            width = 1.dp, brush = SolidColor(Color(0xFFF4F5FD)), shape = RoundedCornerShape(8.dp)
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
                                }


                                Spacer(Modifier.height(8.dp))
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
                                                width = 1.dp, brush = SolidColor(Color(0xFFF4F5FD)), shape = RoundedCornerShape(12.dp)
                                            ),
                                        backgroundColor = Color.White,
                                        textColor = Color.Black,
                                    )
                                    Spacer(Modifier.width(10.dp))
                                    MyButton(
                                        text = "Thêm vào giỏ",
                                        onClick = {
                                            Log.d("ProductDetailScreen", "productId: " + productId)
                                            product?.let { product ->
                                                Log.d("ProductDetailScreen", "product.id: " + product.id)
                                                viewModel.addProductToCart(
                                                    productId = product.id,
                                                    variantId = "",
                                                    quantity = 1,
                                                    context = context,
                                                    onSuccess = {
                                                        coroutineScope.launch {
                                                            snackbarHostState.showSnackbar("Đã thêm sản phẩm vào giỏ hàng thành công.")
                                                        }
                                                    },
                                                    onError = {
                                                        coroutineScope.launch {
                                                            snackbarHostState.showSnackbar("Thêm sản phẩm vào giỏ hàng thất bại.")
                                                        }
                                                    }
                                                )
                                            }
//                                            coroutineScope.launch {
//                                                Log.d("SnackbarDebug", "Snackbar gọi showSnackbar()")
//                                                snackbarHostState.showSnackbar("Đã thêm sản phẩm vào giỏ hàng thành công.")
//                                                Log.d("SnackbarDebug", "Snackbar hiển thị")
//                                            }
//                                            Log.d("SnackbarDebug", "ProductDetailUI: Add to cart ok")
                                        },
                                        modifier = Modifier.weight(1f),
                                        backgroundColor = Color.Black,
                                        textColor = Color.White,
                                        vectorIcon = Icons.Default.AddShoppingCart
                                    )
                                }
                                // review màn
                                if (reviews.isEmpty()) {
                                    // Nếu không có review, hiển thị một thông báo
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(300.dp)
                                            .wrapContentHeight(align = Alignment.CenterVertically),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "Chưa có đánh giá nào cho sản phẩm này.",
                                            fontSize = 16.sp,
                                            color = Color.Gray,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                } else {
                                    LazyColumn(modifier = Modifier.height(300.dp)) {
                                        items(reviews) { review ->
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(8.dp)
                                            ) {
                                                // Avatar của người đánh giá
                                                val avatarUrl = review.avatar.replace(
                                                    "http://localhost:",
                                                    "http://103.166.184.249:"
                                                )
                                                AsyncImage(
                                                    model = avatarUrl,
                                                    contentDescription = "Avatar",
                                                    modifier = Modifier
                                                        .size(40.dp)
                                                        .clip(CircleShape)
                                                        .background(Color.LightGray),
                                                    contentScale = ContentScale.Crop
                                                )

                                                Spacer(modifier = Modifier.width(8.dp))

                                                Column(modifier = Modifier.weight(1f)) {
                                                    // Tên người đánh giá
                                                    Text(
                                                        text = review.username,
                                                        fontWeight = FontWeight.Bold
                                                    )

                                                    // Xếp hạng sao
                                                    Row {
                                                        repeat(5) { index ->
                                                            Icon(
                                                                Icons.Filled.Star,
                                                                contentDescription = "Star",
                                                                tint = if (index < review.rating) Color(
                                                                    0xFFFFD700
                                                                ) else Color.Gray,
                                                                modifier = Modifier.size(16.dp)
                                                            )
                                                        }
                                                    }

                                                    // Nội dung đánh giá
                                                    Text(
                                                        text = review.contents_review,
                                                        fontSize = 14.sp
                                                    )

                                                    // Ảnh đính kèm trong review (nếu có)
                                                    if (review.images.isNotEmpty()) {
                                                        LazyRow(
                                                            horizontalArrangement = Arrangement.spacedBy(
                                                                8.dp
                                                            ),
                                                            modifier = Modifier.fillMaxWidth()
                                                        ) {
                                                            items(review.images) { image ->
                                                                val fixedUrl = image.url.replace(
                                                                    "http://localhost:",
                                                                    "http://103.166.184.249:"
                                                                )

                                                                // Hiển thị ảnh trong 1 hàng ngang
                                                                AsyncImage(
                                                                    model = fixedUrl,
                                                                    contentDescription = "Review Image",
                                                                    modifier = Modifier
                                                                        .size(100.dp)
                                                                        .clip(RoundedCornerShape(8.dp))
                                                                        .background(Color.Gray)
                                                                        .clickable {
                                                                            // Khi ấn vào ảnh, mở dialog với ảnh lớn
                                                                            selectedImageUrl =
                                                                                fixedUrl
                                                                            showDialog = true
                                                                        },
                                                                    contentScale = ContentScale.Crop
                                                                )
                                                            }
                                                        }

                                                        Spacer(modifier = Modifier.height(8.dp)) // Khoảng cách giữa các ảnh
                                                    }

                                                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                                                }
                                            }
                                        }
                                    }

                                    // Hiển thị Dialog khi showDialog là true
                                    if (showDialog) {
                                        ShowImageDialog(imageUrl = selectedImageUrl) {
                                            showDialog = false // Đóng dialog khi ấn ra ngoài
                                        }
                                    }
                                }
                            }

                        }
                    }
                }
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(start = 16.dp, end = 16.dp, top = 10.dp)
                        .systemBarsPadding()
                        .background(Color.Transparent),
                ) { data ->
                    // Custom Snackbar with white background and rounded corners
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF464646))
                            .border(
                                width = 1.dp, color = Color(0xFF00C4B4), shape = RoundedCornerShape(12.dp)
                            )
                            .padding(10.dp)
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
                                    "Đã thêm vào giỏ hàng!", color = Color.White, fontSize = 14.sp
                                )
                            }

                            TextButton(onClick = { navController.navigate("cart") }) {
                                Text(
                                    "Xem giỏ hàng",
                                    color = Color(0xFF00C4B4),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }

        }
    }
}
@Composable
fun ShowImageDialog(imageUrl: String, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        // Sử dụng Surface để tạo khung cho ảnh
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.Black.copy(alpha = 0.9f),
            modifier = Modifier
                .fillMaxWidth(0.9f) // Khung chiếm 90% chiều rộng màn hình
                .wrapContentHeight() // Chiều cao sẽ tự động thay đổi theo ảnh
        ) {
            Box(contentAlignment = Alignment.Center) {
                // AsyncImage để tải ảnh từ URL
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Full Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight() // Cho phép ảnh có chiều cao tự động theo kích thước của ảnh
                        .padding(16.dp), // Padding xung quanh ảnh để không bị chặt vào khung
                    contentScale = ContentScale.Fit // Đảm bảo ảnh không bị cắt và tỷ lệ đúng
                )
            }
        }
    }
}