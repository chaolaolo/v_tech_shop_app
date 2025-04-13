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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Snackbar
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
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.window.Dialog
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
import com.datn.viettech_md_12.component.cart_component.VoucherBottomSheetContent
import com.datn.viettech_md_12.component.cart_component.VoucherItem
import com.datn.viettech_md_12.data.model.CartModel
import com.datn.viettech_md_12.data.model.DiscountResponse
import com.datn.viettech_md_12.screen.checkout.formatCurrency
import com.datn.viettech_md_12.viewmodel.CartViewModel
import com.datn.viettech_md_12.viewmodel.CartViewModelFactory
import kotlinx.coroutines.CoroutineScope
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

    val listDiscount = discountState?.body()?.data ?: emptyList()
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
        sheetPeekHeight = 0.dp,
        sheetDragHandle = { },
        sheetSwipeEnabled = false,
        sheetContainerColor = Color(0xfff4f5fd),
        sheetContent = {
            VoucherBottomSheetContent(
                scaffoldState = scaffoldState,
                snackbarHostState = snackbarHostState,
                listDiscount = listDiscount,
                selectedVoucherId = selectedVoucherId,
                selectedVoucher = selectedVoucher,
                voucherCode = voucherCode,
                scope = scope
            )
        },
        sheetTonalElevation = 16.dp,
        sheetShadowElevation = 24.dp,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .systemBarsPadding(),
        topBar = {
            SmallTopAppBar(
                title = {
                        Text(text = "Giỏ Hàng", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.Black)
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
                            fontSize = 16.sp,
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
                EmptyCart(navController)
            }

                else -> {
                    val cartModel = cartState?.body()
                    cartModel?.let { cart ->
                        CartContent(
                            navController = navController,
                            cartProducts = cart.metadata?.cart_products?: emptyList(),
                            selectedItems = selectedItems,
                            cartViewModel = cartViewModel,
                            selectedVoucher = selectedVoucher.value,
                            snackbarHostState = snackbarHostState
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
    snackbarHostState: SnackbarHostState
) {
    var isDeleting by remember { mutableStateOf(false) }
    // Hiển thị dialog loading khi đang xóa
    if (isDeleting) {
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
                    Text("Đang xóa...", color = Color.Black)
                }
            }
        }
    }
    Column(Modifier.fillMaxSize()) {
        if(cartProducts.isEmpty()){
            EmptyCart(navController)
        }else{
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .background(Color(0xfff4f5fd))
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
                        onDelete = { _, _ ->
                            selectedItems.remove(itemKey)
                        },
                        navController,
                        cartViewModel = cartViewModel,
                        onDeletingStateChange = { deleting-> isDeleting = deleting },
                        snackbarHostState = snackbarHostState
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
}

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