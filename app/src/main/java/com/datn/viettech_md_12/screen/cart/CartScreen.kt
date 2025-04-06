@file:OptIn(ExperimentalMaterialApi::class)

package com.datn.viettech_md_12.screen.cart

import MyButton
import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material3.RadioButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.datn.viettech_md_12.R
import com.datn.viettech_md_12.component.DashedDivider
import com.datn.viettech_md_12.component.MyTextField
import com.datn.viettech_md_12.component.cart_component.CartItemTile
import com.datn.viettech_md_12.component.cart_component.EmptyCart
import com.datn.viettech_md_12.component.cart_component.OrderSummary
import com.datn.viettech_md_12.component.cart_component.VoucherItem
import com.datn.viettech_md_12.data.model.CartModel
import com.datn.viettech_md_12.data.model.DiscountResponse
import com.datn.viettech_md_12.screen.checkout.formatCurrency
import com.datn.viettech_md_12.viewmodel.CartViewModel
import com.datn.viettech_md_12.viewmodel.CartViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.sql.Timestamp
import java.text.NumberFormat
import java.time.Duration
import java.time.Instant
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CartScreen(
    navController: NavController,
    cartViewModel: CartViewModel = viewModel(factory = CartViewModelFactory(LocalContext.current.applicationContext as Application)),
) {
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Hidden,
            skipHiddenState = false
        )
    )
    val scope = rememberCoroutineScope()
    val cartState by cartViewModel.cartState.collectAsState()
    val discountState by cartViewModel.discountState.collectAsState()
    val selectedVoucherId = remember { mutableStateOf<String?>(null) }
    val isLoading by cartViewModel.isLoading.collectAsState()
    val isDiscountLoading by cartViewModel.isDiscountLoading.collectAsState()

    val listDiscount = discountState?.body()?.metadata ?: emptyList()
    val selectedVoucher = remember { mutableStateOf<DiscountResponse.DiscountModel?>(null) }

    val selectedItems = remember { mutableStateListOf<String>() }
    val isShowVoucherSheet = remember { mutableStateOf(false) }
    val voucherCode = remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        cartViewModel.fetchCart()
        cartViewModel.getListDisCount()
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        sheetPeekHeight = 0.dp,
        sheetDragHandle = { },
        sheetSwipeEnabled = false,
        sheetContainerColor = Color(0xfff4f5fd),
        sheetContent = {
            Column(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 10.dp)
                    .fillMaxWidth()
                    .heightIn(max = LocalConfiguration.current.screenHeightDp.dp * 0.7f)
                    .imePadding()
            ) {
                //Sheet header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                Text(
                    "Mã giảm giá",
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = "Thoát bottomsheet",
                        modifier = Modifier
                            .size(20.dp)
                            .clickable {
                                scope.launch {
                                    scaffoldState.bottomSheetState.hide()
                                }
                            }
                    )
                }
                Spacer(Modifier.height(4.dp))
                HorizontalDivider()
                Spacer(Modifier.height(10.dp))
                //TextField nhập mã
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                MyTextField(
                    hint = "Nhập mã giảm giá",
                    value = voucherCode.value,
                    onValueChange = { voucherCode.value = it },
                    isPassword = false,
                    modifier = Modifier.weight(1f)
                )
                    Spacer(Modifier.width(4.dp))
                    Card(
                        onClick = {
                            val enteredCode = voucherCode.value
                            val matchingVoucher = listDiscount.firstOrNull { it.code == enteredCode }
                            if (matchingVoucher != null) {
                                selectedVoucherId.value = matchingVoucher.id
                                selectedVoucher.value = matchingVoucher
                                scope.launch {
                                    snackbarHostState.showSnackbar("Áp dụng mã thành công!")
                                }
                            } else {
                                scope.launch {
                                    snackbarHostState.showSnackbar("Mã không hợp lệ.")
                                }
                            }
                        },
                        modifier = Modifier
                            .width(100.dp)
                            .height(50.dp),
                        colors = CardDefaults.cardColors(Color(0xFF21D4B4)),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Transparent),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                "Áp dụng",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                            )
                        }
                    } //Card
                }
//                Text(
//                    "Thông báo thành công hoặc thất bại khi bấm nút \"áp dụng\"",
//                    color = Color.Black,
//                    fontSize = 12.sp,
//                    fontWeight = FontWeight.Bold,
//                    modifier = Modifier.padding(start = 5.dp, top = 2.dp)
//                )
                Spacer(Modifier.height(10.dp))
                Text("Voucher dành cho bạn", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                //danh sách mã giảm giá
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xfff4f5fd))
                        .heightIn(max = LocalConfiguration.current.screenHeightDp.dp * 0.7f)
                ) {
                    items(listDiscount, key = { it.id ?: "" }) { discount ->
                        VoucherItem(
                            voucher = discount,
                            selectedVoucher = selectedVoucherId.value == discount.id,
                            onSelectedVoucher = { selectedVoucher ->
                                // Xử lý khi chọn voucher
                                selectedVoucherId.value = selectedVoucher.id
                                voucherCode.value = selectedVoucher.code ?: ""
                            },
                        )
                    }
                    item {
                        Spacer(Modifier.height(10.dp))
                        //Button xác nhận dùng mã
                        MyButton(
                            text = "Xác nhận",
                            onClick = {
                                selectedVoucherId.value?.let { voucherId ->
                                    val selectedVoucherId = listDiscount.firstOrNull { it.id == voucherId }
                                    selectedVoucherId?.let {
//                                        cartViewModel.applyDiscount(it.code ?: "")
                                        selectedVoucher.value = it
                                    }
                                }
                                scope.launch { scaffoldState.bottomSheetState.hide() }
                            },
                            modifier = Modifier,
                            backgroundColor = Color.Black,
                            textColor = Color.White,
                        )
                        Spacer(Modifier.height(10.dp))
                    }
                }

            }
        },
        sheetTonalElevation = 16.dp,
        sheetShadowElevation = 24.dp,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .systemBarsPadding(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Giỏ Hàng", color = Color.Black) },
                colors = TopAppBarColors(
                    containerColor = Color.White,
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
                    TextButton(
                        onClick = {
//                            isShowVoucherSheet.value = true
                            scope.launch { scaffoldState.bottomSheetState.expand() }
                        },
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Text(
                            text = "Mã giảm giá",
                            color = Color(0xFF00C2A8),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                },
                modifier = Modifier.shadow(elevation = 2.dp),
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when {
                isLoading == true -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFF21D4B4))
                    }
                }
                cartState?.body() == null -> {
                EmptyCart()
            }

                else -> {
                    val cartModel = cartState?.body()
                    cartModel?.let { cart ->
                        CartContent(
                            navController = navController,
                            cartProducts = cart.metadata?.cart_products?: emptyList(),
                            selectedItems = selectedItems,
                            cartViewModel = cartViewModel,
                            selectedVoucher = selectedVoucher.value
                        )
                    }
                }
            }
        }
    }//end scaffold
}// end cart UI

@Composable
fun CartContent(
    navController: NavController,
    cartProducts: List<CartModel.Metadata.CartProduct>,
    selectedItems: MutableList<String>,
    cartViewModel: CartViewModel,
    selectedVoucher: DiscountResponse.DiscountModel? = null,
) {
    Column(Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .background(Color.White)
                .padding(horizontal = 10.dp)
        ) {
            items(cartProducts, key =  {it.detailsVariantId ?: it.productId}) { item ->
                val itemKey = item.detailsVariantId ?: item.productId
                CartItemTile(
                    product = item,
                    isSelected = item.isSelected,
                    onSelectionChange = { selected ->
//                        item.isSelected = selected
                        if (selected) {
                            if (!selectedItems.contains(itemKey)) {
                                selectedItems.add(itemKey)
                            }
                        } else {
                            selectedItems.remove(itemKey)
                        }
                    },
                    onDelete = { _, _  ->
                        selectedItems.remove(itemKey)
                    },
                    navController,
                    cartViewModel = cartViewModel,
                )
            }
        }
        val selectedCartItems = cartProducts.filter {
            val itemKey = it.detailsVariantId ?: it.productId
            selectedItems.contains(itemKey) }
        OrderSummary(
            navController = navController,
            selectedItems = cartProducts,
            selectedVoucher = selectedVoucher
        )
    }
}


@Composable
fun OrderSummary(
    navController: NavController,
    selectedItems: List<CartModel.Metadata.CartProduct>,
    selectedVoucher: DiscountResponse.DiscountModel? = null,
) {
    val subtotal = selectedItems.filter { it.isSelected }.sumOf { it.price * it.quantity }
    val shippingFee = remember(selectedItems) {
        if (selectedItems.any { it.isSelected }) 35000.0 else 0.0
    }
    val discount = remember { 0.0 }
    val discountPercentage = selectedVoucher?.discountValue ?: 0.0
    val discountAmount = remember(subtotal, discountPercentage) {
        (subtotal * discountPercentage / 100)
    }
    val maxDiscountAmount = selectedVoucher?.maxDiscountAmount ?: Double.MAX_VALUE
    val finalDiscountAmount = remember(discountAmount, maxDiscountAmount) {
        minOf(discountAmount, maxDiscountAmount)
    }
    val total = remember(subtotal, shippingFee, finalDiscountAmount) {
        subtotal + shippingFee - finalDiscountAmount
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, top = 6.dp)
    ) {
        Text("Thông tin đặt hàng", fontWeight = FontWeight.W600, fontSize = 14.sp, color = Color.Black)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Tổng giá tiền", fontSize = 12.sp, color = Color.Gray)
            Text("${"%.2f".format(subtotal)}₫", fontSize = 12.sp, color = Color.Gray)
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Phí vận chuyển", fontSize = 12.sp, color = Color.Gray)
            Text("${formatCurrency(shippingFee)}₫", fontSize = 12.sp, color = Color.Gray)
        }
        if (finalDiscountAmount > 0) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Giảm giá (${discountPercentage.toInt()}%)", fontSize = 12.sp, color = Color(0xFF00C2A8))
                Text("-${formatCurrency(finalDiscountAmount)}₫", fontSize = 12.sp, color = Color(0xFF00C2A8))
            }
        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 0.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Tổng thanh toán", fontSize = 16.sp, fontWeight = FontWeight.W500, color = Color.Black)
            Text("${formatCurrency(total)}₫", fontSize = 16.sp, fontWeight = FontWeight.W500, color = Color.Black)
        }
        Spacer(Modifier.height(5.dp))
        MyButton(
            text = "Thanh Toán(${selectedItems.count{it.isSelected}})",
            onClick = {
                navController.navigate("payment_ui/${selectedVoucher?.code ?: ""}") // Chuyển đến chi tiết sản phẩm
                      },
            backgroundColor = Color.Black,
            textColor = Color.White,
        )
    }
}//end order summary




fun formatCurrency(amount: Double): String {
    return "%,.0f".format(amount).replace(",", ".")
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showSystemUi = true)
@Composable
fun CartPreview() {
    CartScreen(
        navController = rememberNavController()
    )
}

