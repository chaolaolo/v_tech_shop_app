package com.datn.viettech_md_12.screen.review

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.datn.viettech_md_12.NetworkHelper
import com.datn.viettech_md_12.ProductViewModelFactory
import com.datn.viettech_md_12.R
import com.datn.viettech_md_12.component.review_component.UpdateReviewDialog
import com.datn.viettech_md_12.data.model.ProductDetailModel
import com.datn.viettech_md_12.viewmodel.ProductViewModel
import com.datn.viettech_md_12.viewmodel.ReviewViewModel
import com.datn.viettech_md_12.viewmodel.ReviewViewModelFactory
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ReviewScreen(navController: NavController) {
    val context = LocalContext.current
    val reviewViewModel: ReviewViewModel = viewModel(factory = ReviewViewModelFactory(context.applicationContext as android.app.Application))
    val productViewModel: ProductViewModel = viewModel(
        factory = ProductViewModelFactory(
            NetworkHelper(LocalContext.current)
        )
    )
    val isLoading by reviewViewModel.isLoading.collectAsState()
    val reviewsByAccount by reviewViewModel.reviewsByAccount.collectAsState()
    var showUpdateDialog by remember { mutableStateOf(false) }
    var selectedReview by remember { mutableStateOf<com.datn.viettech_md_12.data.model.Review?>(null) }

    LaunchedEffect(Unit) {
        reviewViewModel.getReviewsByAccount()
        Log.d("ReviewScreen", "Reviews: $reviewsByAccount") // Log full review data
    }

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Color.White,
                elevation = 4.dp,
                title = {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(end = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Đánh giá của tôi",
                            color = Color.Black
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {navController.navigate("home")
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                }
            )
        },
        bottomBar = {
            Column(
                Modifier
                    .background(Color.White)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Bạn muốn theo dõi đánh giá trước đó của mình? Hãy bổ sung chi tiết để hỗ trợ những người mua hàng khác.",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(reviewsByAccount.size) { index ->
                    val review = reviewsByAccount[index]
                    ReviewItem(
                        review = review,
                        productViewModel = productViewModel,
                        onUpdateClick = {
                                selectedReview = review
                                showUpdateDialog = true
                        }
                    )
                }
            }
            if (showUpdateDialog && selectedReview != null) {
                UpdateReviewDialog(
                    reviewId = selectedReview!!._id,
                    productId = selectedReview!!.product_id,
                    initialRating = selectedReview!!.rating,
                    initialContent = selectedReview!!.contents_review,
                    initialImageUrls = selectedReview!!.images.map { it.url },
                    initialImageIds = selectedReview!!.images.map { it._id },
                    createdAt = selectedReview!!.createdAt,
                    navController = navController,
                    reviewViewModel = reviewViewModel,
                    onDismiss = {
                        showUpdateDialog = false
                        selectedReview = null
                    },
                    onReviewSubmitted = {
                        selectedReview = null // Đóng dialog sau khi review
                    }
                )
            }

        }
    }
}

@Composable
fun ReviewItem(
    review: com.datn.viettech_md_12.data.model.Review,
    productViewModel: ProductViewModel,
    onUpdateClick: () -> Unit
) {
    val context = LocalContext.current
    var localProductDetail by remember { mutableStateOf<ProductDetailModel?>(null) }

    LaunchedEffect(review.product_id) {
        localProductDetail = productViewModel.getProductByIdSuspend(review.product_id)
    }

    // Hiển thị UI sử dụng localProductDetail thay vì productViewModel.productDetail
    Card(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp),
        elevation = 2.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = "http://103.166.184.249:3056/${localProductDetail?.productThumbnail}",
                    contentDescription = "ảnh sản phẩm",
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color(0xFFF4FDFA), RoundedCornerShape(10.dp))
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Fit,
                    placeholder = painterResource(R.drawable.logo),
                    error = painterResource(R.drawable.error_img),
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = localProductDetail?.productName ?: "Đang tải...",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        maxLines = 1
                    )
                    Text(
                        text = "Giá: ${formatPrice(localProductDetail?.productPrice)}",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    RatingBar(rating = review.rating)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = formatDate(review.createdAt),
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            if (isReviewWithin2Days(review.createdAt)) {
                UpdateReviewButton(onClick = onUpdateClick)

            }
        }
    }
}


@Composable
fun RatingBar(rating: Int) {
    Row {
        repeat(rating) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "Star",
                tint = Color(0xFFFFC107),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

fun formatDate(dateString: String?): String {
    return try {
        if (dateString.isNullOrEmpty()) return ""
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        parser.timeZone = TimeZone.getTimeZone("UTC")
        val date = parser.parse(dateString)
        val formatter = SimpleDateFormat("MMM dd, yyyy h:mma", Locale.getDefault())
        formatter.format(date!!)
    } catch (e: Exception) {
        ""
    }
}

fun formatPrice(price: Double?): String {
    if (price == null) return "Không rõ"
    val formatter = DecimalFormat("#,###")
    return "${formatter.format(price)} ₫"
}

fun isReviewWithin2Days(dateString: String?): Boolean {
    return try {
        if (dateString.isNullOrEmpty()) return false
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        parser.timeZone = TimeZone.getTimeZone("UTC")
        val reviewDate = parser.parse(dateString)

        val now = Date()
        val diff = now.time - (reviewDate?.time ?: 0L)
        val diffInDays = diff / (1000 * 60 * 60 * 24)

        diffInDays < 2
    } catch (e: Exception) {
        false
    }
}
@Composable
fun UpdateReviewButton(onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, Color(0xFFFF5722)), // Màu cam (giống logo app của bạn)
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = Color(0xFFFF5722)
        )
    ) {
        Icon(
            imageVector = Icons.Default.RateReview,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = "Cập nhật đánh giá",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
