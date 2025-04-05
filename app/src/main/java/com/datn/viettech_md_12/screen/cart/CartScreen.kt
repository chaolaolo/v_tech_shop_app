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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SheetValue
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
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.datn.viettech_md_12.R
import com.datn.viettech_md_12.component.MyTextField
import com.datn.viettech_md_12.data.model.CartModel
import com.datn.viettech_md_12.viewmodel.CartViewModel
import com.datn.viettech_md_12.viewmodel.CartViewModelFactory
import kotlinx.coroutines.launch
import java.text.NumberFormat
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
    val isLoading by cartViewModel.isLoading.collectAsState()

    val selectedItems = remember { mutableStateListOf<String>() }
    val isShowVoucherSheet = remember { mutableStateOf(false) }
    val voucherCode = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        cartViewModel.fetchCart()
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetDragHandle = { BottomSheetDefaults.DragHandle() },
        sheetSwipeEnabled = true,
        sheetContent = {
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .imePadding()
            ) {
                Text(
                    "Voucher Code sheet",
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(Modifier.height(10.dp))
                MyTextField(
                    hint = "Nhập mã giảm giá",
                    value = voucherCode.value,
                    onValueChange = { voucherCode.value = it },
                    isPassword = false
                )
                Spacer(Modifier.height(10.dp))
                MyButton(
                    text = "Áp dụng",
                    onClick = {
                        scope.launch { scaffoldState.bottomSheetState.hide() }
                    },
                    modifier = Modifier,
                    backgroundColor = Color.Black,
                    textColor = Color.White,
                )
                Spacer(Modifier.height(20.dp))
            }
        },
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
            selectedItems = cartProducts
        )
    }
}

@Composable
fun OrderSummary(navController: NavController, selectedItems: List<CartModel.Metadata.CartProduct>) {
    val subtotal = selectedItems.filter { it.isSelected }.sumOf { it.price * it.quantity }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, top = 6.dp)
    ) {
        Text("Thông tin đặt hàng", fontWeight = FontWeight.W600, fontSize = 14.sp, color = Color.Black)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Tổng giá tiền", fontSize = 12.sp, color = Color.Gray)
            Text("VND ${"%.2f".format(subtotal)}", fontSize = 12.sp, color = Color.Gray)
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Phí vận chuyển", fontSize = 12.sp, color = Color.Gray)
            Text("VND 0.00", fontSize = 12.sp, color = Color.Gray)
        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 0.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Tổng thanh toán", fontSize = 16.sp, fontWeight = FontWeight.W500, color = Color.Black)
            Text("VND ${"%.2f".format(subtotal)}", fontSize = 16.sp, fontWeight = FontWeight.W500, color = Color.Black)
        }
        Spacer(Modifier.height(5.dp))
        MyButton(
            text = "Thanh Toán(${selectedItems.count{it.isSelected}})",
            onClick = {navController.navigate("payment_ui") },
            backgroundColor = Color.Black,
            textColor = Color.White,
        )
    }
}//end order summary


@Composable
fun CartItemTile(
    product: CartModel.Metadata.CartProduct,
    isSelected: Boolean,
    onSelectionChange: (Boolean) -> Unit,
    onDelete: (String, String) -> Unit,
    navController: NavController,
    cartViewModel: CartViewModel,
) {
    val swipeableState = rememberSwipeableState(initialValue = 0)
    val swipeThreshold = 250f
    val anchors = mapOf(0f to 0, -swipeThreshold to 1)
    val quantityState = remember { mutableStateOf(product.quantity) }
    // Xử lý khi detailsVariantId null thì dùng productId
    val variantIdToUse = product.detailsVariantId ?: product.productId
    val imageUrl = if (product.image.startsWith("http")) {
        product.image
    } else {
        "http://103.166.184.249:3056/${product.image.replace("\\", "/")}"
    }
    Log.d("lol", "Loading image from URL: $imageUrl")

    var localIsSelected by remember { mutableStateOf(isSelected) }
    val itemPrice = product.price
    val itemPriceFormatted = NumberFormat.getNumberInstance(Locale("vi", "VN")).format(itemPrice)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.5f) },
                orientation = Orientation.Horizontal
            )
            .background(if (swipeableState.offset.value < -swipeThreshold / 2) Color.Red else Color.White)
    ) {
        //nút xóa sp khỏi giỏ hàng
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 10.dp)
                .clickable {
                    onDelete(product.productId, variantIdToUse)
                    cartViewModel.deleteCartItem(
                        productId = product.productId,
                        detailsVariantId = product.detailsVariantId ?: "",
                        onSuccess = {
                            // Có thể thêm thông báo thành công
                            Log.d("CartItemTile", "Deleting productId: ${product.productId}, variantId: ${product.detailsVariantId}")
                            Log.d("CartItemTile", "Xóa sản phẩm thành công")
                        },
                        onError = { error ->
                            Log.e("CartItemTile", "Lỗi khi xóa sản phẩm: $error")
                            // Có thể hiển thị Snackbar thông báo lỗi
                        }
                    )
                    Log.d("CartScreen", "ondelete: clicked")
                },
            contentAlignment = Alignment.CenterEnd
        ) {
            Row(
                modifier = Modifier
                    .height(80.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Xóa",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.White
                )
            }
        }

        // nội dung của item
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(swipeableState.offset.value.toInt(), 0) }
                .background(Color.White)
                .clickable {
                    navController.navigate("product_detail/${product.productId}") // Chuyển đến chi tiết sản phẩm
                },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .background(Color.Transparent)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.ic_launcher_background),
                error = painterResource(R.drawable.error_img),
                onError = { Log.e("lol", "Failed to load image: $imageUrl") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(product.name, fontSize = 12.sp, fontWeight = FontWeight.W600, maxLines = 2, overflow = TextOverflow.Ellipsis, lineHeight = 12.sp, color = Color.Black)
                Text(product.variant?.sku ?: "", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.W500, maxLines = 2, overflow = TextOverflow.Ellipsis, lineHeight = 1.sp)
                Text("VND $itemPriceFormatted", fontSize = 10.sp, fontWeight = FontWeight.W500, lineHeight = 1.sp, color = Color.Black)
                Row(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            brush = SolidColor(Color(0xFFF4F5FD)),
                            shape = RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 4.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = {
                            if (quantityState.value > 1) {
                                quantityState.value -= 1
                                cartViewModel.updateProductQuantity(
                                    productId = product.productId,
                                    variantId = product.detailsVariantId ?: "",
                                    newQuantity = quantityState.value,
                                )
                            }
                        },
                        modifier = Modifier.size(16.dp)
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Decrease")
                    }
                    Text("${quantityState.value}", fontSize = 12.sp, modifier = Modifier.padding(horizontal = 12.dp), color = Color.Black)
                    IconButton(
                        onClick = {
                            quantityState.value += 1
                            cartViewModel.updateProductQuantity(
                                productId = product.productId,
                                variantId =  product.detailsVariantId ?: "",
                                newQuantity = quantityState.value,
                            )
                        },
                        modifier = Modifier.size(16.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Increase")
                    }
                }
            }
            Spacer(Modifier.width(4.dp))
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(20.dp)
                    .padding(end = 4.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = { isChecked ->
                        onSelectionChange(isChecked)
                        cartViewModel.updateIsSelected(
                            productId = product.productId,
                            detailsVariantId = product.detailsVariantId,
                            isSelected = isChecked,
                            onSuccess = {
                                onSelectionChange(isChecked)
                                // Update local UI state via callback
                                Log.d("CartItemTile", "Updated selection status: $isChecked for product: ${product.productId}")
                            },
                            onError = { error ->
                                onSelectionChange(!isChecked)
                                Log.e("CartItemTile", "Failed to update selection: $error")
                                // You could show an error message here if needed
                            }
                        )
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFF21D4B4),
                        uncheckedColor = Color.Gray,
                        checkmarkColor = Color.White,
                        disabledCheckedColor = Color.LightGray, // vô hiệu hóa và được chọn
                        disabledUncheckedColor = Color.LightGray // vô hiệu hóa và không được chọn
                    ),
                )
            }
        }
    }
}


@Composable
fun EmptyCart() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(horizontal = 20.dp)
            .systemBarsPadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //ảnh giỏ hàng
        Spacer(Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .size(340.dp)
                .clip(shape = RoundedCornerShape(16.dp))
                .background(color = Color(0xFFF4FDFA)),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                modifier = Modifier
                    .size(200.dp),
                painter = painterResource(R.drawable.empty_cart),
                contentDescription = "empty cart image",
                contentScale = ContentScale.Crop
            )
        }
        Spacer(Modifier.height(20.dp))
        //Text
        Text(
            "Giỏ hàng đang trống",
            color = Color.Black,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(10.dp))
        Text(
            "Có vẻ như bạn chưa thêm bất kỳ sản phẩm nào vào giỏ hàng. Hãy tiếp tục và khám phá các danh mục hàng đầu.",
            color = Color.Gray,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(20.dp))
        //Button
        MyButton(
            text = "Khám phá",
            onClick = { },
            modifier = Modifier,
            backgroundColor = Color.Black,
            textColor = Color.White,
        )
    }
}// end empty cart

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showSystemUi = true)
@Composable
fun CartPreview() {
    CartScreen(
        navController = rememberNavController()
    )
}

