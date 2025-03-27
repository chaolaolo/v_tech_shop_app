package com.datn.viettech_md_12.screen.cart

import MyButton
import android.annotation.SuppressLint
import android.widget.Space
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.datn.viettech_md_12.R
import com.datn.viettech_md_12.component.MyTextField
import com.datn.viettech_md_12.data.model.CartModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CartScreen(navController: NavController) {
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Hidden,
            skipHiddenState = false
        )
    )
    val scope = rememberCoroutineScope()

    val cartItems = remember {
        mutableStateListOf(
            CartModel(
                1,
                "Loop Silicone Strong Magnetic Watch",
                "https://mihanoi.vn/wp-content/uploads/2024/09/dong-ho-do-huyet-ap-thong-minh-fruit-health-6.jpg",
                15.25,
                20.00,
                1
            ),
            CartModel(
                2,
                "M6 Smart watch IP67 Waterproof",
                "https://gomhang.vn/wp-content/uploads/2022/01/m6-138.webp",
                12.00,
                18.00,
                1
            ),
            CartModel(
                3,
                "Loop Silicone Strong Magnetic Watch",
                "https://i5.walmartimages.com/seo/YuiYuKa-Magnetic-Loop-Strap-Silicone-Band-Compatible-Apple-watch-band-45mm-44mm-Ultra-49mm-40mm-41mm-38mm-42mm-Women-Men-Strong-Magnet-Closure-Bracel_c0c1391f-6af4-4b66-8cca-a77c27428b5a.db0a2fbc84d23aebf027a6dd56bab110.jpeg?odnHeight=612&odnWidth=612&odnBg=FFFFFF",
                15.25,
                20.00,
                1
            ),
            CartModel(
                4,
                "M6 Smart watch IP67 Waterproof",
                "https://gomhang.vn/wp-content/uploads/2022/01/m6-138.webp",
                12.00,
                18.00,
                1
            ),
            CartModel(
                5,
                "Loop Silicone Strong Magnetic Watch",
                "https://i5.walmartimages.com/seo/YuiYuKa-Magnetic-Loop-Strap-Silicone-Band-Compatible-Apple-watch-band-45mm-44mm-Ultra-49mm-40mm-41mm-38mm-42mm-Women-Men-Strong-Magnet-Closure-Bracel_c0c1391f-6af4-4b66-8cca-a77c27428b5a.db0a2fbc84d23aebf027a6dd56bab110.jpeg?odnHeight=612&odnWidth=612&odnBg=FFFFFF",
                15.25,
                20.00,
                1
            ),
            CartModel(
                6,
                "M6 Smart watch IP67 Waterproof",
                "https://gomhang.vn/wp-content/uploads/2022/01/m6-138.webp",
                12.00,
                18.00,
                1
            ),
            CartModel(
                7,
                "Loop Silicone Strong Magnetic Watch",
                "https://i5.walmartimages.com/seo/YuiYuKa-Magnetic-Loop-Strap-Silicone-Band-Compatible-Apple-watch-band-45mm-44mm-Ultra-49mm-40mm-41mm-38mm-42mm-Women-Men-Strong-Magnet-Closure-Bracel_c0c1391f-6af4-4b66-8cca-a77c27428b5a.db0a2fbc84d23aebf027a6dd56bab110.jpeg?odnHeight=612&odnWidth=612&odnBg=FFFFFF",
                15.25,
                20.00,
                1
            ),
            CartModel(
                8,
                "M6 Smart watch IP67 Waterproof",
                "https://gomhang.vn/wp-content/uploads/2022/01/m6-138.webp",
                12.00,
                18.00,
                1
            ),
        )
    }
    val selectedItems = remember { mutableStateListOf<Int>() }
    val isShowVoucherSheet = remember { mutableStateOf(false) }
    val voucherCode = remember { mutableStateOf("") }
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

//    ) { }
//    Scaffold(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .systemBarsPadding(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Giỏ Hàng") },
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
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .background(Color.White)
                    .padding(horizontal = 10.dp)
            ) {
                items(cartItems) { item ->
                    CartItemTile(
                        item,
                        selectedItems.contains(item.id),
                        onSelectionChange = { selected ->
                            if (selected) {
                                if (!selectedItems.contains(item.id)) {
                                    selectedItems.add(item.id)
                                }
                            } else {
                                selectedItems.remove(item.id)
                            }
                        },
                        onQuantityChange = { id, newQuantity ->
                            val index = cartItems.indexOfFirst { it.id == id }
                            if (index != -1) {
                                cartItems[index] = cartItems[index].copy(
                                    quantity = newQuantity,
                                )
                            }
                        },
                        onDelete = { id ->
                            selectedItems.remove(id)
                            cartItems.removeAll { it.id == id }
                        },
                        navController
                    )
                }
            }


            //Hiện EmptyCart nếu không có item nào
            if (cartItems.size == 0) {
                EmptyCart()
            }

            //
            val selectedCartItems = cartItems.filter { selectedItems.contains(it.id) }
            OrderSummary(
                navController = navController,
                selectedItems = selectedCartItems
            )

        }//end column

//        if (isShowVoucherSheet.value) {
//            // Hiển thị ModalBottomSheet
//            ModalBottomSheet(
//                onDismissRequest = { isShowVoucherSheet.value = false }, // Đóng
//                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
//                containerColor = Color.White,
//                contentColor = Color.Black,
//                tonalElevation = 8.dp,
//                scrimColor = Color.Black.copy(alpha = 0.5f),
//                windowInsets = WindowInsets.ime,
//                modifier = Modifier
//                ) {
//                Column(
//                    modifier = Modifier
//                        .padding(horizontal = 20.dp)
//                        .fillMaxWidth()
//                        .imePadding()
//                ) {
//                    Text(
//                        "Voucher Code sheet",
//                        color = Color.Black,
//                        fontSize = 20.sp,
//                        fontWeight = FontWeight.Bold,
//                    )
//                    Spacer(Modifier.height(10.dp))
//                    MyTextField(
//                        hint = "Nhập mã giảm giá",
//                        value = voucherCode.value,
//                        onValueChange = { voucherCode.value = it },
//                        isPassword = false
//                    )
//                    Spacer(Modifier.height(10.dp))
//                    MyButton(
//                        text = "Áp dụng",
//                        onClick = { /*TODO()*/ },
//                        modifier = Modifier,
//                        backgroundColor = Color.Black,
//                        textColor = Color.White,
//                    )
//                    Spacer(Modifier.height(20.dp))
//                }
//            }
//        }
    }//end scaffold



}// end cart UI

@Composable
fun OrderSummary(navController:NavController, selectedItems: List<CartModel>) {
    val subtotal = selectedItems.sumOf { it.price * it.quantity }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        Text("Thông tin đặt hàng", fontWeight = FontWeight.W600, fontSize = 14.sp)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Tổng giá tiền", fontSize = 12.sp, color = Color.Gray)
            Text("VND ${"%.2f".format(subtotal)}", fontSize = 12.sp)
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Phí vận chuyển", fontSize = 12.sp, color = Color.Gray)
            Text("VND 0.00", fontSize = 12.sp)
        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 0.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Tổng thanh toán", fontSize = 16.sp, fontWeight = FontWeight.W500)
            Text("VND ${"%.2f".format(subtotal)}", fontSize = 16.sp, fontWeight = FontWeight.W500)
        }
        Spacer(Modifier.height(5.dp))
        MyButton(
            text = "Thanh Toán(${selectedItems.size})",
            onClick = {navController.navigate("payment") },
            backgroundColor = Color.Black,
            textColor = Color.White,
        )
    }
}//end order summary


@Composable
fun CartItemTile(
    item: CartModel,
    isSelected: Boolean,
    onSelectionChange: (Boolean) -> Unit,
    onQuantityChange: (Int, Int) -> Unit,
    onDelete: (Int) -> Unit,
    navController: NavController,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .background(Color.White)
            .clickable {
                navController.navigate("product_detail/${item.id}") // Chuyển đến chi tiết sản phẩm
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = rememberAsyncImagePainter(item.imageUrl),
            contentDescription = null,
            modifier = Modifier
                .size(80.dp)
                .background(Color(0xFFF4FDFA))
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop,

        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(item.name, fontSize = 12.sp, fontWeight = FontWeight.W600, maxLines = 2, overflow = TextOverflow.Ellipsis, lineHeight = 12.sp)
            Text("VND ${item.price}", fontSize = 10.sp, fontWeight = FontWeight.W500, lineHeight = 1.sp)
            Text("VND ${item.originalPrice}", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.W500, textDecoration = TextDecoration.LineThrough, lineHeight = 1.sp)
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
                    onClick = { if (item.quantity > 1) onQuantityChange(item.id, item.quantity - 1) },
                    modifier = Modifier.size(16.dp)
                ) {
                    Icon(Icons.Default.Remove, contentDescription = "Decrease")
                }
                Text("${item.quantity}", fontSize = 12.sp, modifier = Modifier.padding(horizontal = 12.dp))
                IconButton(
                    onClick = { onQuantityChange(item.id, item.quantity + 1) },
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
                .width(20.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Checkbox(
                checked = isSelected,
                onCheckedChange = { onSelectionChange(it) },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFF21D4B4),
                    uncheckedColor = Color.Gray,
                    checkmarkColor = Color.White,
                    disabledCheckedColor = Color.LightGray, // vô hiệu hóa và được chọn
                    disabledUncheckedColor = Color.LightGray // vô hiệu hóa và không được chọn
                ),
            )
//            Spacer(Modifier.height(10.dp))
            Icon(
                Icons.Default.DeleteOutline,
                contentDescription = "Delete",
                tint = Color.Red,
                modifier = Modifier
                    .size(20.dp)
                    .clickable { onDelete(item.id) },
            )
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

