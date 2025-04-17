package com.datn.viettech_md_12.screen

import MyButton
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.datn.viettech_md_12.R
import com.datn.viettech_md_12.component.product_detail_components.ProductStockNotifyDialog
import com.datn.viettech_md_12.component.product_detail_components.toColor
import com.datn.viettech_md_12.component.review_component.AddReviewDialog
import com.datn.viettech_md_12.component.review_component.UpdateReviewDialog
import com.datn.viettech_md_12.data.model.Image
import com.datn.viettech_md_12.data.model.Review
import com.datn.viettech_md_12.screen.authentication.LoginScreen
import com.datn.viettech_md_12.screen.authentication.RegisterScreen
import com.datn.viettech_md_12.data.remote.ApiClient
import com.datn.viettech_md_12.viewmodel.CartViewModel
import com.datn.viettech_md_12.viewmodel.CartViewModelFactory
import com.datn.viettech_md_12.viewmodel.ProductViewModel
import com.datn.viettech_md_12.viewmodel.ReviewViewModel
import com.datn.viettech_md_12.viewmodel.ReviewViewModelFactory
import com.google.accompanist.flowlayout.FlowRow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@Composable
fun ProductDetailScreen(
    navController: NavController,
    productId: String,
    viewModel: ProductViewModel = viewModel(),
) {
    val context = LocalContext.current.applicationContext as Application

    // Khởi tạo ReviewViewModel với factory
    val reviewViewModel: ReviewViewModel = viewModel(
        factory = ReviewViewModelFactory(context)
    )
    val contextToCheckLogin = LocalContext.current
    LaunchedEffect(productId) {
        viewModel.getProductById(productId)
        reviewViewModel.getReviewsByProduct(productId)
        reviewViewModel.getReviewStats(productId)
    }
    val snackbarHostState = remember { SnackbarHostState() }
    val simpleSnackbarHostState = remember { SnackbarHostState() }
    val bottomsheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Hidden, skipHiddenState = false
        )
    )
    val bottomSheetType by viewModel.bottomSheetType.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val product by viewModel.product.collectAsState()
    val productResponse by viewModel.productResponse.collectAsState()
    val attributes = productResponse?.attributes
    val variants = productResponse?.variants
    val defaultVariant = productResponse?.defaultVariant
    val isLoading by viewModel.isLoading.collectAsState()
    var isAddingToCart by remember { mutableStateOf(false) }
    var quantity by remember { mutableStateOf(1) }

    var isExpanded by remember { mutableStateOf(false) }
    var showMoreVisible by remember { mutableStateOf(false) }
    val textLayoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }
    val reviews by reviewViewModel.reviews.collectAsState()
    val reviewStats by reviewViewModel.reviewStats.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var showAddReviewDialog by remember { mutableStateOf(false) }
    var selectedImageUrl by remember { mutableStateOf("") }
    var showLoginDialog by remember { mutableStateOf(false) }
    var showCheckStockDialog by remember { mutableStateOf(false) }

    val currentAccountId = reviewViewModel.getCurrentUserId()
    var showUpdateDialog by remember { mutableStateOf(false) }
    var selectedReview by remember { mutableStateOf<Review?>(null) }
    val price = product?.productPrice ?:0.0
    val itemPriceFormatted = NumberFormat.getNumberInstance(Locale("vi", "VN")).format(price)


    var isFavorite by remember { mutableStateOf(false) }
    val sharedPreferences =
        context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE) //lay trang thai da luu tru
    if (product != null) {
        if (sharedPreferences != null) {
            isFavorite = sharedPreferences.getBoolean(productId, false)
        }
    }

    // Thêm state cho selected attributes và valid options
    val selectedAttributes = remember { mutableStateMapOf<String, String>() }
    val validOptions = remember { mutableStateOf<Map<String, Set<String>>>(emptyMap()) }

    // Lấy danh sách attributes của sản phẩm
    val productAttributes = attributes?.filter { attr ->
        product?.attributeIds?.contains(attr._id) == true
    } ?: emptyList()

    // Hàm để lọc các options hợp lệ
    fun updateValidOptions(selected: Map<String, String>) {
        val currentOptions = viewModel.filterValidOptions(selected)
        validOptions.value = currentOptions
    }
    //lấy giá của variant
    val matchedVariantId by viewModel.matchedVariantId.collectAsState()
    val matchedVariantPrice by viewModel.matchedVariantPrice.collectAsState()
    // Khi selectedAttributes thay đổi, cập nhật validOptions
    LaunchedEffect(selectedAttributes) {
        delay(50)
        validOptions.value = viewModel.filterValidOptions(selectedAttributes)
        if (selectedAttributes.size == productAttributes.size) {
            viewModel.matchVariant(productId, selectedAttributes)
        }
        updateValidOptions(selectedAttributes)
    }


    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.2f))
                    .align(Alignment.Center)
            ) {
                CircularProgressIndicator(
                    color = Color(0xFF21D4B4),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else {
            product?.let {
                BottomSheetScaffold(
                    scaffoldState = bottomsheetScaffoldState,
                    sheetPeekHeight = 0.dp,
                    sheetDragHandle = { },
                    sheetSwipeEnabled = false,
                    sheetContainerColor = Color(0xfff4f5fd),
                    sheetContent = {
                        Column(
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp, top = 10.dp)
                                .fillMaxWidth()
                                .heightIn(max = LocalConfiguration.current.screenHeightDp.dp * 0.7f, min = LocalConfiguration.current.screenHeightDp.dp * 0.5f)
                                .imePadding()
                                .verticalScroll(rememberScrollState())
                        ) {
                            //Hiện thông tin sp
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(end = 10.dp)
                                    .background(color = Color(0xfff4f5fd)),
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Box(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        //ảnh
                                        AsyncImage(
//                                            model = "https://cdn.tgdd.vn/Products/Images/5698/326091/asus-aio-a3402wvak-i3-wpc080w-thumb-49-600x600.jpg",
                                            model = "http://103.166.184.249:3056/${product?.productThumbnail}",
                                            contentDescription = "ảnh sản phẩm",
                                            modifier = Modifier
                                                .size(80.dp)
                                                .background(Color(0xFFF4FDFA), RoundedCornerShape(10.dp))
                                                .clip(RoundedCornerShape(10.dp)),
                                            contentScale = ContentScale.Fit,
                                            placeholder = painterResource(R.drawable.logo),
                                            error = painterResource(R.drawable.error_img),
                                            // onError = { Log.e("CartItemTile", "Failed to load image: $imageUrl") }
                                        )
                                        Spacer(Modifier.width(4.dp))
                                        //nội dung sản phẩm
                                        Column {
                                            Text(
                                                buildString {
                                                    // 1: giá sp có variant được chọn
                                                    if (matchedVariantId != null && matchedVariantPrice != null) {
                                                        append(NumberFormat.getNumberInstance(Locale("vi", "VN")).format(matchedVariantPrice))
                                                    }
                                                    // 2: giá default variant
//                                                    else if (defaultVariant?.price != null) {
//                                                        append(NumberFormat.getNumberInstance(Locale("vi", "VN")).format(defaultVariant.price))
//                                                    }
                                                    // giá sp gốc
                                                    else {
                                                        append(NumberFormat.getNumberInstance(Locale("vi", "VN")).format(product?.productPrice ?: 0))
                                                    }
                                                    append(" ₫")
                                                },
//                                                "${NumberFormat.getNumberInstance(Locale("vi", "VN")).format(defaultVariant?.price ?: product?.productPrice ?: 0)} ₫",
                                                color = Color(0xFF21D4B4), fontSize = 16.sp, fontWeight = FontWeight.Bold
                                            )
                                            Text(product!!.productName, color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                            Text(
                                                buildString {
                                                    if (selectedAttributes.isNotEmpty()) {
                                                        append(selectedAttributes.values.joinToString(", "))
                                                    }
                                                },
                                                color = Color.DarkGray, fontSize = 12.sp, maxLines = 3, overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }
                                }
                                Icon(Icons.Filled.Close, contentDescription = "Thoát bottomsheet", modifier = Modifier
                                    .size(20.dp)
                                    .clickable {
                                        coroutineScope.launch {
                                            bottomsheetScaffoldState.bottomSheetState.hide()
                                        }
                                    })
                            }
                            Spacer(Modifier.height(8.dp))
                            HorizontalDivider()
                            Spacer(Modifier.height(8.dp))
                            productAttributes.forEach { attribute ->
                            Column(
                                Modifier
                                    .fillMaxWidth()
                                    .background(Color.Transparent)
                            ) {
                                Text(text = attribute.name, color = Color.DarkGray, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                FlowRow(
                                    mainAxisSpacing = 4.dp,
                                ) {
                                    // Lấy danh sách giá trị hợp lệ cho attribute hiện tại
                                    attribute.values.forEach { value ->
                                        val isOptionValid = validOptions.value[attribute.name]?.contains(value) ?: true
                                        // Nếu đã chọn giá trị này thì luôn enable
                                        val isSelected = selectedAttributes[attribute.name] == value
                                        val variantInStock = variants?.any { variant ->
                                            variant.variantDetails.any { detail ->
                                                attributes?.find { it._id == detail.variantId }?.name == attribute.name &&
                                                        detail.value == value
                                            } && variant.stock > 0
                                        } ?: true
                                        val enabled =  (isOptionValid || isSelected) && variantInStock
//                                        val enabled = isOptionValid || isSelected
                                        FilterChip(
                                            selected = selectedAttributes[attribute.name] == value,
                                            onClick = {
                                                if (enabled) {
                                                    // Tạo bản sao của selectedAttributes để tránh mutation trực tiếp
                                                    val newSelected = selectedAttributes.toMutableMap().apply {
                                                        this[attribute.name] = value
                                                    }
                                                    // Cập nhật selectedAttributes
                                                    selectedAttributes.clear()
                                                    selectedAttributes.putAll(newSelected)
                                                    // Cập nhật valid options ngay lập tức
                                                    updateValidOptions(newSelected)
                                                    // Gọi matchVariant khi có đủ selectedAttributes
                                                    if (newSelected.size == productAttributes.size) {
                                                        viewModel.matchVariant(productId, newSelected)
                                                    }
                                                }
                                            },
                                            label = { Text(value) },
                                            colors = FilterChipDefaults.filterChipColors(
                                                selectedContainerColor = Color(0xFF21D4B4).copy(alpha = 0.2f),
                                                selectedLabelColor = Color(0xFF21D4B4),
                                                disabledContainerColor = Color.LightGray.copy(alpha = 0.2f),
                                                disabledLabelColor = Color.Gray
                                            ),
                                            enabled = enabled
                                        )
                                    }
                                }
                            }
                            }
                            // Chọn số lượng
                            Spacer(Modifier.height(20.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.Transparent),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Số lượng", color = Color.Black, fontSize = 14.sp)
                                Box(
                                    modifier = Modifier
                                        .clip(shape = RoundedCornerShape(8.dp))
                                        .background(Color.White)
                                        .border(
                                            width = 1.dp, brush = SolidColor(Color(0xFFCACACA)), shape = RoundedCornerShape(8.dp)
                                        )

                                ) {
                                    Row(
                                        modifier = Modifier
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                            .background(Color.Transparent),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        IconButton(
                                            onClick = {
                                                if (quantity > 1) {
                                                    quantity--
                                                }
                                            },
                                            modifier = Modifier.size(20.dp),
                                            enabled = quantity > 1
                                        ) {
                                            Icon(
                                                Icons.Default.Remove, contentDescription = "Decrease",
                                                tint = if (quantity > 1) Color.Black else Color.Gray
                                            )
                                        }
                                        Text("$quantity", modifier = Modifier.padding(horizontal = 14.dp), color = Color.Black)
                                        IconButton(
                                            onClick = {
                                                if (quantity < (product?.productStock ?: Int.MAX_VALUE)) {
                                                    quantity++
                                                } else if (product!!.productStock == 1) {
                                                    coroutineScope.launch {
                                                        simpleSnackbarHostState.showSnackbar("Số lượng sản phẩm này chỉ còn ${product?.productStock} trong kho")
                                                    }
                                                }
                                            },
                                            modifier = Modifier.size(20.dp),
                                            enabled = quantity < (product?.productStock ?: Int.MAX_VALUE)
                                        ) {
                                            Icon(
                                                Icons.Default.Add, contentDescription = "Increase",
                                                tint = if (quantity < (product?.productStock ?: Int.MAX_VALUE)) Color.Black else Color.Gray
                                            )
                                        }
                                    }
                                }
                            }
                            Spacer(Modifier.weight(1f))
                            Spacer(Modifier.height(8.dp))
                            HorizontalDivider()
                            Spacer(Modifier.height(8.dp))
                            // nút bấm
                            when (bottomSheetType) {
                                "buy_now" -> {
                                    MyButton(
                                        text = "Mua ngay",
                                        onClick = {
                                            if (selectedAttributes.size == productAttributes.size) {
                                                viewModel.matchedVariantId.value?.let { variantId ->
                                                    val selectedVariant = variants?.find { it.id == variantId }
                                                    if (selectedVariant?.stock ?: 0 > 0) {
                                                        navController.navigate("payment_ui/product/${product?.id}/$quantity?variantId=$variantId")
                                                    } else {
                                                        coroutineScope.launch {
                                                            simpleSnackbarHostState.showSnackbar("Sản phẩm này đã hết hàng")
                                                        }
                                                    }
                                                } ?: run {
                                                    coroutineScope.launch {
                                                        simpleSnackbarHostState.showSnackbar("Không tìm thấy phiên bản sản phẩm phù hợp")
                                                    }
                                                }
                                            } else {
                                                coroutineScope.launch {
                                                    simpleSnackbarHostState.showSnackbar("Vui lòng chọn đầy đủ các thuộc tính sản phẩm")
                                                }
                                            }
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        backgroundColor = Color(0xFF21D4B4),
                                        textColor = Color.White,
                                        enabled = selectedAttributes.size == productAttributes.size,
                                    )
                                }

                                "add_to_cart" -> {
                                    MyButton(
                                        text = "Thêm vào giỏ hàng",
                                        onClick = {
                                            if (selectedAttributes.size == productAttributes.size) {
                                                viewModel.matchedVariantId.value?.let { variantId ->
                                                    val selectedVariant = variants?.find { it.id == variantId }
                                                    if (selectedVariant?.stock ?: 0 > 0) {
                                                        isAddingToCart = true
                                                        product?.let { product ->
                                                            viewModel.addProductToCart(
                                                                productId = product.id,
                                                                variantId = variantId,
                                                                quantity = quantity,
                                                                context = contextToCheckLogin,
                                                                onSuccess = {
                                                                    isAddingToCart = false
                                                                    coroutineScope.launch {
                                                                        bottomsheetScaffoldState.bottomSheetState.hide()
                                                                        snackbarHostState.showSnackbar("Đã thêm sản phẩm vào giỏ hàng thành công.")
                                                                    }
                                                                },
                                                                onError = {
                                                                    isAddingToCart = false
                                                                    coroutineScope.launch {
                                                                        simpleSnackbarHostState.showSnackbar("Có lỗi xảy ra khi thêm sản phẩm này vào giỏ hàng.")
                                                                    }
                                                                }
                                                            )
                                                        }
                                                    } else {
                                                        coroutineScope.launch {
                                                            simpleSnackbarHostState.showSnackbar("Sản phẩm này đã hết hàng")
                                                        }
                                                    }
                                                } ?: run {
                                                    coroutineScope.launch {
                                                        simpleSnackbarHostState.showSnackbar("Không tìm thấy phiên bản sản phẩm phù hợp")
                                                    }
                                                }
                                            } else {
                                                coroutineScope.launch {
                                                    simpleSnackbarHostState.showSnackbar("Vui lòng chọn đầy đủ các thuộc tính sản phẩm")
                                                }
                                            }
                                        },
                                        modifier = Modifier,
                                        backgroundColor = Color(0xFF21D4B4),
                                        textColor = Color.White,
                                        enabled = selectedAttributes.size == productAttributes.size,
                                    )
                                }
                            }
                            Spacer(Modifier.height(12.dp))
                        }
                    },
                    sheetTonalElevation = 16.dp,
                    sheetShadowElevation = 24.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .windowInsetsPadding(WindowInsets(0, 0, 0, 0)),
                    snackbarHost = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 10.dp), contentAlignment = Alignment.TopCenter
                        ) {
                            SnackbarHost(
                                hostState = simpleSnackbarHostState,
                                snackbar = { data ->
                                    Snackbar(
                                        modifier = Modifier.padding(8.dp),
                                        action = {
                                            TextButton(
                                                onClick = { data.dismiss() },
                                                colors = ButtonDefaults.textButtonColors(
                                                    contentColor = Color.White
                                                )
                                            ) {
                                                Icon(Icons.Default.Close, contentDescription = "Đóng")
                                            }
                                        }
                                    ) {
                                        Text(data.visuals.message, fontWeight = FontWeight.Bold)
                                    }
                                }
                            )
                        }
                    }
                ) {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Blue)
                    ) {
                        // ảnh sản phẩm chi tiết
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(350.dp)
                                .padding(bottom = 20.dp)
                                .background(Color(0xFFF4FDFA)),
                        ) {
                            AsyncImage(
                                model = "http://103.166.184.249:3056/${product?.productThumbnail}",
                                contentDescription = "p detail image",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp),
                                contentScale = ContentScale.Fit,
                                placeholder = painterResource(R.drawable.logo),
                                error = painterResource(R.drawable.error_img)
                            )
                        }
                        TopAppBar(
                            title = { },
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .background(Color.Transparent)
                                .zIndex(1f)
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
                                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                                }
                            },
                            actions = {
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(color = Color.Black)
                                        .size(30.dp)
                                ) {
                                    IconButton(
                                        onClick = {
                                            isFavorite = !isFavorite
                                            if (isFavorite) {
                                                val productId = product?.id
                                                if (productId != null) {
                                                    viewModel.addToFavorites(productId, context)
                                                }
                                            } else {
                                                val favoriteId = product?.id
                                                if (favoriteId != null) {
                                                    viewModel.removeFromFavorites(favoriteId, context)
                                                }
                                            }
                                        },
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(8.dp)
                                            .size(40.dp)
                                    ) {
                                        Icon(
                                            contentDescription = "favourite icon",
                                            imageVector = if (!isFavorite) Icons.Default.FavoriteBorder else Icons.Default.Favorite,
                                            tint = if (!isFavorite) Color.White else Color.Red,
                                            modifier = Modifier
                                                .size(36.dp)
                                        )
                                    }
                                }
                            },
                        )
                        Log.d("zzzzzzzzzzzzzz", "productThumbnail: ${product?.productThumbnail}")
                        Card(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 300.dp)
                                .shadow(elevation = 4.dp, shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)), // Đẩy nội dung lên che hình ảnh
                            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                                    .background(Color.White)
                                    .padding(16.dp)
                                    .verticalScroll(rememberScrollState()),
                            ) {
                                Row {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(color = Color(0xFF0090EE))
                                            .padding(horizontal = 4.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            "Top Rated",
                                            modifier = Modifier,
                                            color = Color.White,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Spacer(Modifier.width(10.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(color = Color(0xFF009B79))
                                            .padding(horizontal = 4.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            "Free Shipping",
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
                                        modifier = Modifier.weight(1f),
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

                                    // Kiểm tra xem reviewStats có dữ liệu hay không
                                    if (reviewStats != null) {
                                        val totalReviews =
                                            reviewStats?.getOrNull()?.totalReviews ?: 0
                                        val averageRating =
                                            reviewStats?.getOrNull()?.averageRating ?: 0f

                                        Text(
                                            text = "${averageRating} (${totalReviews} reviews)",
                                            fontSize = 12.sp,
                                            color = Color.Black
                                        )
                                    } else {
                                        // Hiển thị thông báo hoặc xử lý khi không có dữ liệu
                                        Text(
                                            text = "Đang tải dữ liệu đánh giá...",
                                            fontSize = 12.sp,
                                            color = Color.Gray
                                        )
                                    }
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
                                        Text(
                                            text = "... Xem thêm",
                                            fontSize = 12.sp,
                                            color = Color(0xFF21D4B4),
                                            fontWeight = FontWeight.Medium,
                                            modifier = Modifier
                                                .padding(top = 4.dp)
                                                .clickable { isExpanded = true })
                                    } else if (isExpanded) {
                                        Text(
                                            text = "Thu gọn",
                                            fontSize = 12.sp,
                                            color = Color(0xFF21D4B4),
                                            fontWeight = FontWeight.Medium,
                                            modifier = Modifier
                                                .padding(top = 4.dp)
                                                .clickable { isExpanded = false })
                                    }
                                }

                                // chọn màu, số lượng
                                Spacer(Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Bottom
                                ) {
                                    val hasColorAttribute = remember(attributes) {
                                        attributes?.any {it.name.equals("Color", ignoreCase = true)} ?: false
                                    }
                                    Log.d("hasColorAttribute", "hasColorAttribute: $hasColorAttribute")
                                    Log.d("hasColorAttribute", "product: $product")
                                    Log.d("hasColorAttribute", "attributes: $attributes")
                                    Log.d("hasColorAttribute", "variants: $variants")
                                    Log.d("hasColorAttribute", "default_variant: $defaultVariant")
                                    if(hasColorAttribute){
                                        Column {
                                            Text(
                                                text = "Color",
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(top = 10.dp)
                                            )
                                            Row(modifier = Modifier.padding(top = 4.dp)) {
                                                val colorAttribute = attributes?.first { it.name.equals("Color", ignoreCase = true) }
                                                colorAttribute?.values?.forEach { colorValue ->
                                                    val color = colorValue.toColor()
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
                                    }
                                // Số lượng
                                Row(
                                    modifier = Modifier
                                        .border(
                                            width = 1.dp, brush = SolidColor(Color(0xFFF4F5FD)), shape = RoundedCornerShape(8.dp)
                                        )
                                        .padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    IconButton(
                                        onClick = {
                                            if (quantity > 1) {
                                                quantity--
                                            }
                                        },
                                        modifier = Modifier.size(20.dp),
                                        enabled = quantity > 1
                                    ) {
                                        Icon(
                                            Icons.Default.Remove, contentDescription = "Decrease",
                                            tint = if (quantity > 1) Color.Black else Color.Gray
                                        )
                                    }
                                    Text("$quantity", modifier = Modifier.padding(horizontal = 14.dp), color = Color.Black)
                                    IconButton(
                                        onClick = {
                                            if (quantity < (product?.productStock ?: Int.MAX_VALUE)) {
                                                quantity++
                                            }else if (product!!.productStock == 1) {
                                                coroutineScope.launch {
                                                    simpleSnackbarHostState.showSnackbar("Số lượng sản phẩm này chỉ còn ${product?.productStock} trong kho")
                                                }
                                            }
                                        },
                                        modifier = Modifier.size(20.dp),
                                        enabled = quantity < (product?.productStock ?: Int.MAX_VALUE)
                                    ) {
                                        Icon(
                                            Icons.Default.Add, contentDescription = "Increase",
                                            tint = if (quantity < (product?.productStock ?: Int.MAX_VALUE)) Color.Black else Color.Gray
                                        )
                                    }
                                }
                                }


                                Spacer(Modifier.height(8.dp))
                                //add review
                                if (showAddReviewDialog) {
                                    AddReviewDialog(
                                        productId = productId,
                                        reviewViewModel = reviewViewModel,
                                        onDismiss = { showAddReviewDialog = false }
                                    )
                                }

                                Button(onClick = { showAddReviewDialog = true }) {
                                    Icon(Icons.Default.RateReview, contentDescription = null)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Thêm đánh giá")
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
                                                                        .size(90.dp)
                                                                        .clip(RoundedCornerShape(8.dp))
                                                                        .background(Color.Gray)
                                                                        .clickable {
                                                                            // Khi ấn vào ảnh, mở dialog với ảnh lớn
                                                                            selectedImageUrl = fixedUrl
                                                                            showDialog = true
                                                                        },
                                                                    contentScale = ContentScale.Crop
                                                                )
                                                            }
                                                        }

                                                        Spacer(modifier = Modifier.height(8.dp)) // Khoảng cách giữa các ảnh
                                                    }
                                                    // 👇 Thêm nút Cập nhật nếu đúng tài khoản
                                                    if (review.account_id == currentAccountId) {
                                                        Button(
                                                            onClick = {
                                                                selectedReview = review
                                                                showUpdateDialog = true
                                                            },
                                                            colors = ButtonDefaults.buttonColors(
                                                                containerColor = Color(0xFF1976D2)
                                                            ),
                                                            shape = RoundedCornerShape(10.dp),
                                                            contentPadding = PaddingValues(
                                                                horizontal = 8.dp,
                                                                vertical = 4.dp
                                                            ),
                                                            modifier = Modifier
                                                                .padding(top = 4.dp)
                                                                .height(32.dp) // Chiều cao nhỏ hơn
                                                        ) {
                                                            Icon(
                                                                imageVector = Icons.Default.Edit, // Hoặc giữ Send nếu bạn thích
                                                                contentDescription = null,
                                                                tint = Color.White,
                                                                modifier = Modifier.size(16.dp) // Icon nhỏ lại
                                                            )
                                                            Spacer(modifier = Modifier.width(4.dp))
                                                            Text(
                                                                "Cập nhật",
                                                                color = Color.White,
                                                                fontSize = 12.sp // Font nhỏ hơn
                                                            )
                                                        }
                                                    }
                                                        if (showUpdateDialog && selectedReview != null) {
                                                        UpdateReviewDialog(
                                                            reviewId = selectedReview!!._id,
                                                            productId = productId,
                                                            initialRating = selectedReview!!.rating,
                                                            initialContent = selectedReview!!.contents_review,
                                                            initialImageUrls = selectedReview!!.images.map { it.url }, // Nếu là List<Image>
                                                            initialImageIds = selectedReview!!.images.map { it._id }, // Nếu là List<Image>
                                                            reviewViewModel = reviewViewModel,
                                                            onDismiss = {
                                                                showUpdateDialog = false
                                                                selectedReview = null
                                                            }

                                                        )
                                                            Log.d("UPDATE_REVIEW", "selectedReview: $selectedReview")
                                                            Log.d("UPDATE_REVIEW", "imageIds: ${selectedReview!!.images.map { it._id }}")
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
                        BottomAppBar(
                            modifier = Modifier
                                .zIndex(1f)
                                .align(Alignment.BottomCenter)
                                .background(Color(0xfff4f5fd))
                                .shadow(elevation = 4.dp),
                        ) {
                            //nút bấm mua ngay, thêm vào giỏ hàng
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .background(Color.Transparent),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                MyButton(
                                    text = "Mua ngay",
                                    onClick = {
                                        //check bat dang nhap hoac dang ki moi cho su dung
                                        val token = contextToCheckLogin?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                                            ?.getString("accessToken", "")
                                        val isLoggedIn = !token.isNullOrEmpty()

                                        if (!isLoggedIn) {
                                            showLoginDialog = true
                                        } else {
                                            if (product!!.productStock == 0) {
                                                showCheckStockDialog = true
                                            } else if (quantity <= 0) {
                                                coroutineScope.launch {
                                                    simpleSnackbarHostState.showSnackbar("Số lượng phải lớn hơn 0")
                                                }
                                            } else if (productResponse?.variants?.size!! >= 1) {
                                                viewModel.setBottomSheetType("buy_now")
                                                coroutineScope.launch { bottomsheetScaffoldState.bottomSheetState.expand() }
                                            } else {
                                                showCheckStockDialog = false
                                                navController.navigate("payment_ui/product/${product?.id}/$quantity") // Chuyển đến màn thanh toán
                                                Log.d("ProductDetailScreen", "sl: $quantity ")
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .border(
                                            width = 1.dp, brush = SolidColor(Color(0xFF21D4B4)), shape = RoundedCornerShape(12.dp)
                                        ),
                                    backgroundColor = Color(0xFFF4FDFA),
                                    textColor = Color.Black,
                                )
                                Spacer(Modifier.width(10.dp))
                                MyButton(
                                    text = "Thêm vào giỏ",
                                    onClick = {
                                        Log.d("ProductDetailScreen", "productId: " + productId)
                                        //check bat dang nhap hoac dang ki moi cho su dung
                                        val token = contextToCheckLogin?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                                            ?.getString("accessToken", "")
                                        val isLoggedIn = !token.isNullOrEmpty()

                                        if (!isLoggedIn) {
                                            showLoginDialog = true
                                        } else if (product!!.productStock == 0) {
                                            showCheckStockDialog = true
                                        } else if (productResponse?.variants?.size!! >= 1) {
                                            viewModel.setBottomSheetType("add_to_cart")
                                            coroutineScope.launch { bottomsheetScaffoldState.bottomSheetState.expand() }
                                        } else {
                                            isAddingToCart = true
                                            showCheckStockDialog = false
                                            product?.let { product ->
                                                Log.d("ProductDetailScreen", "product.id: " + product.id)
                                                viewModel.addProductToCart(productId = product.id, variantId = "", quantity = quantity, context = contextToCheckLogin, onSuccess = {
                                                    isAddingToCart = false
                                                    coroutineScope.launch {
                                                        snackbarHostState.showSnackbar("Đã thêm sản phẩm vào giỏ hàng thành công.")
                                                    }
                                                }, onError = {
                                                    isAddingToCart = false
                                                    coroutineScope.launch {
                                                        simpleSnackbarHostState.showSnackbar("Có lỗi xảy ra khi thêm sản phẩm này vào giỏ hàng.")
                                                    }
                                                })
                                            }
                                        }
                                    },
                                    modifier = Modifier.weight(1f),
                                    backgroundColor = Color.Black,
                                    textColor = Color.White,
                                    vectorIcon = Icons.Default.AddShoppingCart
                                )
                                // Custom Dialog yêu cầu đăng nhập
                                if (showLoginDialog) {
                                    Dialog(onDismissRequest = { showLoginDialog = false }) {
                                        Card(
                                            shape = RoundedCornerShape(16.dp),
                                            elevation = CardDefaults.cardElevation(8.dp),
                                            colors = CardDefaults.cardColors(containerColor = Color.White),
                                            modifier = Modifier.width(300.dp)
                                        ) {
                                            Column(
                                                modifier = Modifier.padding(20.dp),
                                                horizontalAlignment = Alignment.Start
                                            ) {
                                                Text(
                                                    text = "Bạn cần đăng nhập",
                                                    fontSize = 18.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.Black
                                                )
                                                Spacer(modifier = Modifier.height(10.dp))
                                                Text(
                                                    text = "Vui lòng đăng nhập hoặc tạo tài khoản để thực hiện hành động này.",
                                                    fontSize = 14.sp,
                                                    color = Color.Gray
                                                )
                                                Spacer(modifier = Modifier.height(20.dp))
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.End
                                                ) {
                                                    TextButton(onClick = {
                                                        showLoginDialog = false
                                                        val intent = Intent(contextToCheckLogin, RegisterScreen::class.java)
                                                        contextToCheckLogin?.startActivity(intent)
                                                    }) {
                                                        Text("Tạo tài khoản mới")
                                                    }
                                                    TextButton(onClick = {
                                                        showLoginDialog = false
                                                        val intent = Intent(contextToCheckLogin, LoginScreen::class.java)
                                                        contextToCheckLogin?.startActivity(intent)
                                                    }) {
                                                        Text("Đăng nhập")
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                //thông báo stock đã hết
                                if (showCheckStockDialog) {
                                    ProductStockNotifyDialog(
                                        onDismiss = { showCheckStockDialog = false },
                                        navController = navController
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
        // Thêm vào phần Box chính, cùng cấp với các dialog khác
        if (isAddingToCart) {
            Dialog(onDismissRequest = {}) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(150.dp)
                        .background(Color.White, shape = RoundedCornerShape(8.dp))
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = Color(0xFF21D4B4))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Đang thêm...", color = Color.Black)
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

@Composable
fun AddReviewDialog(
    productId: String,
    reviewViewModel: ReviewViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var rating by remember { mutableStateOf(0) }
    var content by remember { mutableStateOf("") }

    val addReviewResult by reviewViewModel.addReviewResult.collectAsState()
    val userReviewStatus by reviewViewModel.userReviewStatus.collectAsState()

    var showConfirmDialog by remember { mutableStateOf(false) }

    // Kiểm tra xem người dùng đã đánh giá sản phẩm chưa
    LaunchedEffect(productId) {
        reviewViewModel.checkUserReviewStatus(productId)
    }

    // Nếu người dùng đã có đánh giá, hiển thị thông báo và không cho phép thêm review
    if (userReviewStatus) {
        Toast.makeText(context, "Bạn đã thêm đánh giá cho sản phẩm này!", Toast.LENGTH_SHORT).show()
        return
    }

    // Sau khi submit review thành công
    LaunchedEffect(addReviewResult) {
        addReviewResult?.onSuccess {
            Toast.makeText(context, "Gửi đánh giá thành công!", Toast.LENGTH_SHORT).show()
            reviewViewModel.getReviewsByProduct(productId)
            onDismiss()
        }?.onFailure {
            Log.e("ADD_REVIEW", "Error: ${it.message}")
            Toast.makeText(context, "Gửi đánh giá thất bại!", Toast.LENGTH_SHORT).show()
            onDismiss()
        }
    }

    // Dialog xác nhận gửi
    if (showConfirmDialog) {
        ConfirmDialog(
            onConfirm = {
                showConfirmDialog = false

                coroutineScope.launch {
                    if (content.isBlank() || rating == 0) {
                        Toast.makeText(context, "Vui lòng nhập đầy đủ nội dung và số sao", Toast.LENGTH_SHORT).show()
                        return@launch
                    }

                    // Gửi review với ảnh ID cố định
                    reviewViewModel.addReviewWithFixedImageId(
                        productId = productId,
                        contentsReview = content,
                        rating = rating
                    )
                }
            },
            onDismiss = { showConfirmDialog = false }
        )
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Thêm đánh giá", fontWeight = FontWeight.Bold, fontSize = 18.sp)

                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(5) { index ->
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = if (index < rating) Color(0xFFFFD700) else Color.Gray,
                            modifier = Modifier
                                .size(32.dp)
                                .clickable { rating = index + 1 }
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("$rating sao")
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Nội dung đánh giá") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 4
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Hủy")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    TextButton(
                        onClick = {
                            if (content.isBlank() || rating == 0) {
                                Toast.makeText(context, "Nhập đầy đủ nội dung và số sao", Toast.LENGTH_SHORT).show()
                            } else {
                                showConfirmDialog = true
                            }
                        },
                    ) {
                        Text("Gửi")
                    }
                }
            }
        }
    }
}

// Dialog xác nhận
@Composable
fun ConfirmDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Xác nhận gửi đánh giá") },
        text = { Text("Bạn có chắc chắn muốn gửi đánh giá này không?") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Đồng ý")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy")
            }
        }
    )
}


