package com.datn.viettech_md_12.screen.checkout.checkout_cart

import MyButton
import android.annotation.SuppressLint
import android.app.Application
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
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
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.datn.viettech_md_12.R
import com.datn.viettech_md_12.component.checkout.CheckoutItemTile
import com.datn.viettech_md_12.data.model.CartModel
import com.datn.viettech_md_12.screen.checkout.PayMethodItem
import com.datn.viettech_md_12.screen.checkout.PaymentMethod
import com.datn.viettech_md_12.screen.checkout.formatCurrency
import com.datn.viettech_md_12.viewmodel.CartViewModel
import com.datn.viettech_md_12.viewmodel.CartViewModelFactory
import com.datn.viettech_md_12.viewmodel.CheckoutViewModel
import com.datn.viettech_md_12.viewmodel.CheckoutViewModelFactory
import com.datn.viettech_md_12.viewmodel.ProductViewModel
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PaymentCartUI(
    navController: NavController,
    discount: String = "",
    checkoutViewModel: CheckoutViewModel = viewModel(factory = CheckoutViewModelFactory(LocalContext.current.applicationContext as Application)),
    cartViewModel: CartViewModel = viewModel(factory = CartViewModelFactory(LocalContext.current.applicationContext as Application)),
) {
    val context = LocalContext.current
    val checkoutState by checkoutViewModel.addressState.collectAsState()
    val gettingAddress by checkoutViewModel.gettingAddress.collectAsState()
    val selectedCartItems by checkoutViewModel.selectedCartItems.collectAsState()
    val isLoadingCartItems by checkoutViewModel.isCheckoutLoading.collectAsState()
    val isCheckoutLoading by checkoutViewModel.isCheckoutLoading.collectAsState()
    val paymentUrl by checkoutViewModel.paymentUrl.collectAsState()

    val quantityState = remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        checkoutViewModel.getAddress()
        checkoutViewModel.getIsSelectedItemInCart()
    }

    LaunchedEffect(paymentUrl) {
        paymentUrl?.let { url ->
            checkoutViewModel._paymentUrl.value = null
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(context, "Không thể mở trình duyệt", Toast.LENGTH_SHORT).show()
            }
        }
    }

    val payOptions =
        listOf(
            PaymentMethod("Thanh toán khi nhận hàng", R.drawable.codpay_img, "tm"),
            PaymentMethod("Thanh toán VNPay", R.drawable.vnpay_img, "vnpay")
        )
    var selectedPayOption by remember { mutableStateOf(payOptions[0]) }

    val discountState by cartViewModel.discountState.collectAsState()
    val listDiscount = discountState?.body()?.data ?: emptyList()
    val selectedVoucher = remember(discount, listDiscount) {
        listDiscount.firstOrNull { it.code.trim() == discount.trim() }
    }
    val subtotal = remember(selectedCartItems) {
        selectedCartItems?.sumOf { it.price * it.quantity } ?: 0.0
    }
    val defaultShippingFee = 35000.0
    val shippingFee = remember(selectedVoucher) {
        if (selectedVoucher?.discountType?.lowercase() == "shipping") {
            if (selectedVoucher?.discountValue == 0.0) {
                0.0
            } else {
                val shippingDiscount = selectedVoucher.discountValue
                (defaultShippingFee - shippingDiscount).coerceAtLeast(0.0)
            }
        } else {
            defaultShippingFee
        }
    }
    val discountPercentage = selectedVoucher?.discountValue ?: 0.0
    val discountAmount = remember(subtotal, discountPercentage) {
        (subtotal * discountPercentage / 100)
    }
    val maxDiscountAmount = selectedVoucher?.maxDiscountAmount ?: Double.MAX_VALUE
    val finalDiscountAmount = remember(discountAmount, maxDiscountAmount) {
        minOf(discountAmount, maxDiscountAmount)
    }
// Tổng thanh toán
    val total = remember(subtotal, shippingFee, finalDiscountAmount) {
        subtotal + shippingFee - finalDiscountAmount
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val showOutOfStockDialog = remember { mutableStateOf(false) }
    Scaffold(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .systemBarsPadding(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Thanh Toán",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                },
                colors = TopAppBarColors(
                    containerColor = Color.White,
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
                modifier = Modifier.shadow(elevation = 2.dp),
            )
        },
        snackbarHost = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 10.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                SnackbarHost(
                    hostState = snackbarHostState,
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
        },
    )
    { innerPadding ->
        Log.d("discount", "discount: $discount")
        Log.d("discount", "selectedVoucher: $selectedVoucher")
        Log.d("discount", "Available vouchers: ${listDiscount.map { it.code }}")
        Log.d("discount", "subtotal: $subtotal")
        Log.d("discount", "discountPercentage: $discountPercentage")
        Log.d("discount", "discountAmount: $discountAmount")
        Log.d("discount", "maxDiscountAmount: $maxDiscountAmount")
        Log.d("discount", "finalDiscountAmount: $finalDiscountAmount")
        Log.d("discount", "total: $total")
        Log.d("discount", "Discount State: $discountState")
        if (gettingAddress) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.2f))
            ) {
                CircularProgressIndicator(
                    color = Color(0xFF21D4B4),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .background(Color.White),
                verticalArrangement = Arrangement.Top,
            ) {
                //Địa chỉ
                Spacer(Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(horizontal = 16.dp)
                        .clickable {
                            navController.navigate("address_screen")
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Outlined.LocationOn,
                        contentDescription = "icon địa chỉ",
                        tint = Color.Red
                    )
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 2.dp)
                    ) {
                        val addressData = checkoutState?.body()?.data
                        Row {
                            Text(
                                if (addressData?.full_name.isNullOrEmpty()) "Chưa có tên" else addressData?.full_name
                                    ?: "Chưa có tên",
                                color = Color.Black,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W600,
                            )
                            Spacer(Modifier.width(2.dp))
                            Text(
                                "(${if (addressData?.phone.isNullOrEmpty()) "Chưa có số điện thoại" else addressData?.phone ?: "Chưa có số điện thoại"})",
                                color = Color.Black,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W600,
                            )
                        }
                        Text(
                            if (addressData?.address.isNullOrEmpty()) "Chưa có địa chỉ" else addressData?.address
                                ?: "Chưa có địa chỉ",
                            color = Color(0xFF1A1A1A),
                            fontSize = 14.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = "icon xem địa chỉ",
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(Modifier.height(4.dp))
                HorizontalDivider(
                    Modifier
                        .height(2.dp)
                        .background(Color(0xfff4f5fd)),
                    color = Color(0xfff4f5fd)
                )
                //Chọn phương thức thanh toán
                Spacer(Modifier.height(4.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(start = 16.dp, end = 16.dp, bottom = 4.dp)
                ) {
                    Text(
                        "Phương thức thanh toán",
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W500,
                    )
                    payOptions.forEach { method ->
                        PayMethodItem(
                            text = method.displayName,
                            imageRes = method.imageRes,
                            selected = (method == selectedPayOption),
                            onSelected = { selectedPayOption = method },
                        )
                    }
                }
                HorizontalDivider(
                    Modifier
                        .height(1.dp),
                    color = Color(0xfff4f5fd)
                )
                //Danh sách sản phẩm sẽ thanh toán
                if (isLoadingCartItems) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color(0xFF21D4B4))
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                            .background(Color(0xfff4f5fd))
                            .padding(start = 16.dp, end = 16.dp, top = 4.dp)
                            .nestedScroll(rememberNestedScrollInteropConnection())
                    ) {
                        items(selectedCartItems ?: emptyList()) { item ->
                            CheckoutCartItemTile(
                                product = item,
                                cartViewModel = cartViewModel,
                                checkoutViewModel = checkoutViewModel,
                                snackbarHostState = snackbarHostState,
                            )
                        }
                    }
                }
                //Tóm tắt đơn hàng
                Spacer(
                    Modifier
                        .fillMaxHeight(1f)
                        .background(Color.Red)
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .background(Color.White)
                ) {
                    Spacer(Modifier.height(2.dp))
                    Text(
                        "Tóm tắt đơn hàng",
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W600,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(
                            "Tổng phụ",
                            color = Color.Black,
                            fontSize = 14.sp,
                        )
                        Text(
                            "${formatCurrency(subtotal)}₫",
                            color = Color.Black,
                            fontSize = 14.sp,
                        )
                    }
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(
                            "Vận chuyển",
                            color = Color.Black,
                            fontSize = 14.sp,
                        )
                        Text(
                            "${formatCurrency(shippingFee)}₫",
                            color = if (selectedVoucher?.discountType?.lowercase() == "shipping") Color(0xFF00C2A8) else Color.Black,
                            fontSize = 14.sp,
                        )
                    }
                    if (finalDiscountAmount > 0) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(
                                "Giảm giá (${discountPercentage.toInt()}%)",
                                fontSize = 14.sp,
                                color = Color(0xFF00C2A8),
                            )
                            Text(
                                "-${formatCurrency(finalDiscountAmount)}₫",
                                fontSize = 14.sp,
                                color = Color(0xFF00C2A8),
                            )
                        }
                    }
                    Spacer(Modifier.height(4.dp))
                    HorizontalDivider(Modifier.height(0.5.dp), color = Color.LightGray)
                    Spacer(Modifier.height(4.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Tổng (${selectedCartItems?.size} mặt hàng)",
                            color = Color.Black,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W600
                        )
                        Text(
                            "${formatCurrency(total)}₫",
                            color = Color.Black,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W600,
                        )
                    }
                    MyButton(
                        text = "Đặt hàng",
                        onClick = {
                            checkoutViewModel._isCheckoutLoading.value = true
                            val addressData = checkoutState?.body()?.data
                            val address = addressData?.address?.trim() ?: ""
                            val phone = addressData?.phone?.trim() ?: ""
                            val name = addressData?.full_name?.trim() ?: ""
                            Log.d("PaymentUI", "address: $address, phone: $phone, name:$name ")
                            if (selectedCartItems?.any { it.isSelected && it.stock == 0 } == true) {
                                showOutOfStockDialog.value = true
                            } else {
                                when {
                                    address.isNullOrBlank() || address == "null" -> {
                                        Toast.makeText(
                                            context,
                                            "Vui lòng thiết lập địa chỉ giao hàng",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        checkoutViewModel._isCheckoutLoading.value = false
                                    }

                                    phone.isNullOrBlank() || address == "null" -> {
                                        Toast.makeText(
                                            context,
                                            "Vui lòng thiết lập số điện thoại",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        checkoutViewModel._isCheckoutLoading.value = false
                                    }

                                    name.isNullOrBlank() || address == "null" -> {
                                        Toast.makeText(
                                            context,
                                            "Vui lòng thiết lập Họ tên",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        checkoutViewModel._isCheckoutLoading.value = false
                                    }

                                    !phone.matches(Regex("^[0-9]{10,11}\$")) -> {
                                        Toast.makeText(
                                            context,
                                            "Số điện thoại không hợp lệ (phải có 10 số)",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        checkoutViewModel._isCheckoutLoading.value = false
                                    }

                                    else -> {
                                    checkoutViewModel.checkout(
                                        address = address,
                                        phone_number = phone,
                                        receiver_name = name,
                                        payment_method = selectedPayOption.apiValue,
                                        discount_code = discount,
                                    )
                                    Log.d(
                                        "PaymentUI",
                                        "address: $address, phone: $phone, name:$name, payment_method ${selectedPayOption.apiValue}"
                                    )
//                                    navController.navigate("order_successfully")
                                    Log.d("PaymentUI", "selectedPayOption.apiValue: $paymentUrl")
                                    if (selectedPayOption.apiValue != "vnpay") {
                                        navController.navigate("order_successfully") {
                                            popUpTo("cart") {
                                                inclusive = false
                                            }
                                            launchSingleTop = true
                                        }
                                    }
                                    }
                                }
                            }
                        },
                        modifier = Modifier.padding(vertical = 10.dp),
                        backgroundColor = Color(0xFF00C2A8),
                        textColor = Color.White,
                        enabled = selectedCartItems?.isNotEmpty()
                    )
                }
            }
        }
        if (showOutOfStockDialog.value) {
            AlertDialog(
                onDismissRequest = {
                    showOutOfStockDialog.value = false
                },
                title = {
                    Text(text = "Thông báo", fontWeight = FontWeight.Bold)
                },
                text = {
                    Text("Xin lỗi một số sản phẩm bạn chọn đã hết hàng. Vui lòng kiểm tra lại!")
                },
                confirmButton = {
                    TextButton(onClick = { showOutOfStockDialog.value = false }) {
                        Text("Đã hiểu", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
        if (isCheckoutLoading) {
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
                        Text("Vui lòng chờ...", color = Color.Black)
                    }
                }
            }
        }
    }
//    }
}


@Composable
fun CheckoutCartItemTile(
    product: CartModel.Metadata.CartProduct,
    cartViewModel: CartViewModel,
    checkoutViewModel: CheckoutViewModel,
    snackbarHostState: SnackbarHostState,
) {
    val imageUrl = if (product.image.startsWith("http")) {
        product.image
    } else {
        "http://103.166.184.249:3056/${product.image.replace("\\", "/")}"
    }
    Log.d("CheckoutItemTile", "Loading image from URL: $imageUrl")

    val itemPrice = product.price
    val itemPriceFormatted = NumberFormat.getNumberInstance(Locale("vi", "VN")).format(itemPrice)
    var currentQuantity by remember { mutableIntStateOf(product.quantity) }
    LaunchedEffect(product.quantity) {
        currentQuantity = product.quantity
    }
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
                            product.variant_details?.values?.joinToString(", ") { it.value } ?: ""
                        if (!variantValues.isNullOrEmpty()) {
                            Text(
                                variantValues,
                                fontSize = 12.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.W500,
                                lineHeight = 14.sp
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
                                if (currentQuantity > 1) {
                                    val newQuantity = currentQuantity - 1
                                    currentQuantity = newQuantity
                                    coroutineScope.launch {
                                        cartViewModel.updateProductQuantity(
                                            productId = product.productId,
                                            variantId = product.detailsVariantId ?: "",
                                            newQuantity = newQuantity,
                                        )
                                        // Sau khi cập nhật xong, refresh lại danh sách
                                        checkoutViewModel.refreshSelectedItems()
                                    }
                                }
                            },
                            modifier = Modifier.size(18.dp),
                            enabled = currentQuantity > 1
                        ) {
                            Icon(
                                Icons.Default.Remove, contentDescription = "Decrease",
                                tint = if (currentQuantity > 1) Color.Black else Color.Gray
                            )
                        }
                        Text(
                            if (product?.stock == 0) "0" else "$currentQuantity",
                            modifier = Modifier.padding(horizontal = 12.dp),
                            color = Color.Black
                        )
                        IconButton(
                            onClick = {
                                if (currentQuantity < (product.stock ?: Int.MAX_VALUE)) {
                                    val newQuantity = currentQuantity + 1
                                    currentQuantity = newQuantity
                                    coroutineScope.launch {
                                        cartViewModel.updateProductQuantity(
                                            productId = product.productId,
                                            variantId = product.detailsVariantId ?: "",
                                            newQuantity = newQuantity,
                                        )
                                        checkoutViewModel.refreshSelectedItems()
                                    }
                                } else if (product.stock == 1) {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Số lượng sản phẩm này chỉ còn ${product?.stock} trong kho")
                                    }
                                }
                            },
                            modifier = Modifier.size(18.dp),
                            enabled = currentQuantity < (product.stock ?: Int.MAX_VALUE)
                        ) {
                            Icon(
                                Icons.Default.Add, contentDescription = "Increase",
                                tint = if (currentQuantity < (product.stock ?: Int.MAX_VALUE)) Color.Black else Color.Gray
                            )
                        }
                    }
                }
            }
        }
        HorizontalDivider(
            thickness = 0.3.dp,
            color = Color.Gray
        )
        if (product.stock == 0) {
            Text(
                "Sản phẩm này đã hết hàng", color = Color.Red, fontSize = 12.sp, textAlign = TextAlign.End, modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.End)
                    .padding(horizontal = 16.dp)
            )
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(end = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    "Số lượng ${currentQuantity}, tổng cộng ",
//             textAlign = TextAlign.End,
                    fontSize = 12.sp,
                    color = Color.Black
                )
                Text(
                    "${
                        NumberFormat.getNumberInstance(Locale("vi", "VN"))
                            .format(currentQuantity * itemPrice)
                    }₫",
//             textAlign = TextAlign.End,
                    fontSize = 12.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.W600
                )
            }
        }
    }
}