package com.datn.viettech_md_12.screen.checkout

import MyButton
import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.datn.viettech_md_12.R
import com.datn.viettech_md_12.component.checkout.CheckoutItemTile
import com.datn.viettech_md_12.data.model.CartMode
import com.datn.viettech_md_12.viewmodel.CartViewModel
import com.datn.viettech_md_12.viewmodel.CartViewModelFactory
import com.datn.viettech_md_12.viewmodel.CheckoutViewModel
import com.datn.viettech_md_12.viewmodel.CheckoutViewModelFactory

data class PaymentMethod(
    val displayName: String,
    val imageRes: Int,
    val apiValue: String,
)

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PaymentUI(
    navController: NavController,
    checkoutViewModel: CheckoutViewModel = viewModel(factory = CheckoutViewModelFactory(LocalContext.current.applicationContext as Application)),
    cartViewModel: CartViewModel = viewModel(factory = CartViewModelFactory(LocalContext.current.applicationContext as Application)),
) {
    val context = LocalContext.current
    val checkoutState by checkoutViewModel.addressState.collectAsState()
    val isLoading by checkoutViewModel.isLoading.collectAsState()
    val selectedCartItems by checkoutViewModel.selectedCartItems.collectAsState()

    LaunchedEffect(Unit) {
        checkoutViewModel.getAddress()
        checkoutViewModel.getIsSelectedItemInCart()
    }

    val payOptions =
//        listOf("Thanh toán khi nhận hàng" to R.drawable.codpay_img, "VnPay" to R.drawable.vnpay_img)
        listOf(
            PaymentMethod("Thanh toán khi nhận hàng", R.drawable.codpay_img, "tm"),
            PaymentMethod("Thanh toán VNPay", R.drawable.vnpay_img, "ck")
        )
    var selectedPayOption by remember { mutableStateOf(payOptions[0]) }

    // Tính tổng giá trị đơn hàng
    val subtotal = remember(selectedCartItems) {
        selectedCartItems?.sumOf { item ->
            item.price * item.quantity
        } ?: 0.0
    }

// Phí vận chuyển cố định hoặc tính toán
    val shippingFee = remember(selectedCartItems) {
        if (selectedCartItems.isNullOrEmpty()) 0.0 else 35000.0
    }

// Giảm giá (nếu có)
    val discount = remember { 0.0 }

// Tổng thanh toán
    val total = remember(subtotal, shippingFee, discount) {
        subtotal + shippingFee - discount
    }

    Scaffold(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .systemBarsPadding(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Thanh Toán") },
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
    )
    { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(Color.White),
            verticalArrangement = Arrangement.Top,
        ) {
            //Địa chỉ
            Spacer(Modifier.height(4.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp)
                    .clickable {
                        navController.navigate("address_screen")
                    },
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
                    if (isLoading) {
                        // Hiển thị placeholder khi đang tải
                        Text(
                            "Đang tải thông tin...",
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W600,
                            lineHeight = 15.sp
                        )
                    } else {
                        val addressData = checkoutState?.body()?.data
                        Row {
                            Text(
                                if (addressData?.full_name.isNullOrEmpty()) "Chưa có tên" else addressData?.full_name ?: "Chưa có tên",
                                color = Color.Black,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W600,
                                lineHeight = 15.sp
                        )
                        Spacer(Modifier.width(2.dp))
                        Text(
                            "(${if (addressData?.phone.isNullOrEmpty()) "Chưa có số điện thoại" else addressData?.phone ?: "Chưa có số điện thoại"})",
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W600,
                            lineHeight = 15.sp
                        )
                    }
                    Text(
                        if (addressData?.address.isNullOrEmpty()) "Chưa có địa chỉ" else addressData?.address ?: "Chưa có địa chỉ",
                        color = Color(0xFF1A1A1A),
                        fontSize = 13.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 14.sp
                    )
                    }
                }
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = "icon xem địa chỉ",
                    tint = Color.Black,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(Modifier.height(1.dp))
            HorizontalDivider(
                Modifier
                    .height(2.dp)
                    .background(Color(0xfff4f5fd)),
                color = Color(0xfff4f5fd)
            )
            //Chọn phương thức thanh toán
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    "Phương thức thanh toán",
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    lineHeight = 15.sp
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
            LazyColumn(
                modifier = Modifier
//                        .height(400.dp)
                    .weight(1f)
                    .fillMaxSize()
                    .background(Color(0xfff4f5fd))
//                    .padding(horizontal = 16.dp)
                    .nestedScroll(rememberNestedScrollInteropConnection())
            ) {
                if (selectedCartItems.isNullOrEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Không có sản phẩm nào được chọn")
                        }
                    }
                } else {
                    items(selectedCartItems!!) { item ->
                    CheckoutItemTile(
                        product = item,
                        cartViewModel = cartViewModel,
                        checkoutViewModel = checkoutViewModel,
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
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W600,
                    lineHeight = 15.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Tổng phụ",
                        color = Color.Black,
                        fontSize = 14.sp,
                        lineHeight = 15.sp,
                    )
                    Text(
                        "${formatCurrency(subtotal)} VND",
                        color = Color.Black,
                        fontSize = 14.sp,
                        lineHeight = 15.sp,
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Vận chuyển",
                        color = Color.Black,
                        fontSize = 14.sp,
                        lineHeight = 15.sp,
                    )
                    Text(
                        "${formatCurrency(shippingFee)} VND",
                        color = Color(0xFFFF3333),
                        fontSize = 14.sp,
                        lineHeight = 15.sp,
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 2.dp, start = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Giảm giá",
                        color = Color.Black,
                        fontSize = 14.sp,
                        lineHeight = 15.sp,
                    )
                    Text(
                        "- ${formatCurrency(discount)} VND",
                        color = Color(0xFFFF3333),
                        fontSize = 14.sp,
                        lineHeight = 15.sp,
                    )
                }
                HorizontalDivider(Modifier.height(0.5.dp), color = Color.LightGray)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Tổng (${selectedCartItems?.size ?: 0} mặt hàng)",
                        color = Color.Black,
                        fontSize = 14.sp,
                        lineHeight = 15.sp,
                    )
                    Text(
                        "${formatCurrency(total)} VND",
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W600,
                        lineHeight = 15.sp,
                    )
                }
                MyButton(
                    text = "Đặt hàng",
                    onClick = {
                        val addressData = checkoutState?.body()?.data
                        val address = addressData?.address ?: ""
                        val phone = addressData?.phone ?: ""
                        val name = addressData?.full_name ?: ""
                        Log.d("PaymentUI", "address: $address, phone: $phone, name:$name ")
                        if (address.isNotEmpty() && phone.isNotEmpty() && name.isNotEmpty()) {
                            checkoutViewModel.checkout(
                                address = address,
                                phone_number = phone,
                                receiver_name = name,
                                payment_method = selectedPayOption.apiValue
                            )
                            Log.d("PaymentUI", "address: $address, phone: $phone, name:$name, payment_method ${selectedPayOption.apiValue}")
                            navController.navigate("order_successfully")
                        } else {
                            Toast.makeText(
                                context, "Vui lòng cung cấp đầy đủ thông tin!", Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    modifier = Modifier.padding(top = 6.dp),
                    backgroundColor = Color.Black,
                    textColor = Color.White,
                    enabled = selectedCartItems?.size != 0
                )
                Spacer(Modifier.height(4.dp))
            }
        }
    }
//    }
}

fun formatCurrency(amount: Double): String {
    return "%,.0f".format(amount).replace(",", ".")
}

@Composable
fun PayMethodItem(
    text: String,
    imageRes: Int,
    selected: Boolean,
    onSelected: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "Thanh toán tiền mặt",
            modifier = Modifier
                .width(46.dp)
                .height(25.dp)
                .background(Color.Transparent)
                .clip(RoundedCornerShape(4.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = text,
            color = Color.Black,
            fontSize = 14.sp,
            modifier = Modifier.weight(1f)
        )
        Spacer(Modifier.width(4.dp))
        RadioButton(
            selected = selected,
            onClick = { onSelected() },
            modifier = Modifier,
            colors = RadioButtonColors(
                selectedColor = Color(0xFF21D4B4),
                unselectedColor = Color.Black,
                disabledSelectedColor = Color.Transparent,
                disabledUnselectedColor = Color.Transparent
            ),
        )
    }
}

@Composable
@Preview(showSystemUi = true)
fun PreviewPayScreen() {
    PaymentUI(rememberNavController())
}