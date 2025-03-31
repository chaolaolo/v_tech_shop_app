package com.datn.viettech_md_12.screen.checkout

import MyButton
import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.BottomAppBar
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.datn.viettech_md_12.R
import com.datn.viettech_md_12.component.checkout.CheckoutItemTile
import com.datn.viettech_md_12.data.model.CartMode

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PaymentUI(navController: NavController) {
    val payOptions =
        listOf("Thanh toán khi nhận hàng" to R.drawable.banner2, "VnPay" to R.drawable.banner3)
    var selectedPayOption by remember { mutableStateOf(payOptions[0].first) }

    val checkoutItems = remember {
        mutableStateListOf(
            CartMode(
                1,
                "Loop Silicone Strong Magnetic Watch",
                "https://i5.walmartimages.com/seo/YuiYuKa-Magnetic-Loop-Strap-Silicone-Band-Compatible-Apple-watch-band-45mm-44mm-Ultra-49mm-40mm-41mm-38mm-42mm-Women-Men-Strong-Magnet-Closure-Bracel_c0c1391f-6af4-4b66-8cca-a77c27428b5a.db0a2fbc84d23aebf027a6dd56bab110.jpeg?odnHeight=612&odnWidth=612&odnBg=FFFFFF",
                15.25,
                20.00,
                1
            ),
            CartMode(
                2,
                "M6 Smart watch IP67 Waterproof",
                "https://gomhang.vn/wp-content/uploads/2022/01/m6-138.webp",
                12.00,
                18.00,
                1
            ),
            CartMode(
                3,
                "Loop Silicone Strong Magnetic Watch",
                "https://i5.walmartimages.com/seo/YuiYuKa-Magnetic-Loop-Strap-Silicone-Band-Compatible-Apple-watch-band-45mm-44mm-Ultra-49mm-40mm-41mm-38mm-42mm-Women-Men-Strong-Magnet-Closure-Bracel_c0c1391f-6af4-4b66-8cca-a77c27428b5a.db0a2fbc84d23aebf027a6dd56bab110.jpeg?odnHeight=612&odnWidth=612&odnBg=FFFFFF",
                15.25,
                20.00,
                1
            ),
            CartMode(
                4,
                "M6 Smart watch IP67 Waterproof",
                "https://gomhang.vn/wp-content/uploads/2022/01/m6-138.webp",
                12.00,
                18.00,
                1
            ),
            CartMode(
                5,
                "Loop Silicone Strong Magnetic Watch",
                "https://i5.walmartimages.com/seo/YuiYuKa-Magnetic-Loop-Strap-Silicone-Band-Compatible-Apple-watch-band-45mm-44mm-Ultra-49mm-40mm-41mm-38mm-42mm-Women-Men-Strong-Magnet-Closure-Bracel_c0c1391f-6af4-4b66-8cca-a77c27428b5a.db0a2fbc84d23aebf027a6dd56bab110.jpeg?odnHeight=612&odnWidth=612&odnBg=FFFFFF",
                15.25,
                20.00,
                1
            ),
            CartMode(
                6,
                "M6 Smart watch IP67 Waterproof",
                "https://gomhang.vn/wp-content/uploads/2022/01/m6-138.webp",
                12.00,
                18.00,
                1
            ),
            CartMode(
                7,
                "Loop Silicone Strong Magnetic Watch",
                "https://i5.walmartimages.com/seo/YuiYuKa-Magnetic-Loop-Strap-Silicone-Band-Compatible-Apple-watch-band-45mm-44mm-Ultra-49mm-40mm-41mm-38mm-42mm-Women-Men-Strong-Magnet-Closure-Bracel_c0c1391f-6af4-4b66-8cca-a77c27428b5a.db0a2fbc84d23aebf027a6dd56bab110.jpeg?odnHeight=612&odnWidth=612&odnBg=FFFFFF",
                15.25,
                20.00,
                1
            ),
            CartMode(
                8,
                "M6 Smart watch IP67 Waterproof",
                "https://gomhang.vn/wp-content/uploads/2022/01/m6-138.webp",
                12.00,
                18.00,
                1
            ),
        )
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
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    )
    { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .clickable {
                    navController.navigate("address")
                },
            verticalArrangement = Arrangement.Top,
        ) {
            //Địa chỉ
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp)
            ) {
                Icon(
                    Icons.Outlined.LocationOn,
                    contentDescription = "icon địa chỉ",
                    tint = Color.Black
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 2.dp)
                ) {
                    Row {
                        Text(
                            "Chao Lao Lo",
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500
                        )
                        Spacer(Modifier.width(2.dp))
                        Text(
                            "(0123****89)",
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500
                        )
                    }
                    Text(
                        "Số 1, ngách 2, ngõ 3, đường Trần Duy Hưng, Trung Hoà, Cầu Giấy, Hà Nội",
                        color = Color(0xFF1A1A1A),
                        fontSize = 13.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Icon(
                    Icons.Default.ArrowForwardIos,
                    contentDescription = "icon địa chỉ",
                    tint = Color.Black,
                    modifier = Modifier

                )
            }
            HorizontalDivider(
                Modifier
                    .height(1.dp)
                    .padding(top = 2.dp)
            )
            //Chọn phương thức thanh toán
            Spacer(Modifier.height(8.dp))
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
                    fontWeight = FontWeight.W500
                )
                payOptions.forEach { (method, imageRes) ->
                    PayMethodItem(
                        text = method,
                        imageRes = imageRes,
                        selected = (method == selectedPayOption),
                        onSelected = { selectedPayOption = method },
                    )
                }
            }
            //Danh sách sản phẩm sẽ thanh toán
            LazyColumn(
                modifier = Modifier
//                        .height(400.dp)
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp)
                    .nestedScroll(rememberNestedScrollInteropConnection())
            ) {
                items(checkoutItems) { item ->
                    CheckoutItemTile(
                        item = item,
                        onQuantityChange = { id, newQuantity ->
                            val index = checkoutItems.indexOfFirst { it.id == id }
                            if (index != -1) {
                                checkoutItems[index] = checkoutItems[index].copy(
                                    quantity = newQuantity,
                                )
                            }

                        },
                        onDelete = { id ->
                            checkoutItems.removeAll() { it.id == id }
                        }
                    )
                }
            }
            //Tóm tắt đơn hàng
            Spacer(
                Modifier
                    .fillMaxHeight(1f)
                    .background(Color.Red)
            )
            Box(
                Modifier
                    .height(4.dp)
                    .fillMaxWidth()
                    .background(Color.LightGray)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .background(Color.White)
            ) {
                Spacer(Modifier.height(4.dp))
                Text(
                    "Tóm tắt đơn hàng",
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Tổng phụ",
                        color = Color.Black,
                        fontSize = 14.sp,
                    )
                    Text(
                        "1.400.500 VND",
                        color = Color.Black,
                        fontSize = 14.sp,
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Vận chuyển",
                        color = Color.Black,
                        fontSize = 14.sp,
                    )
                    Text(
                        "10.500 VND",
                        color = Color(0xFFFF3333),
                        fontSize = 14.sp,
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Vận chuyển",
                        color = Color.Black,
                        fontSize = 14.sp,
                    )
                    Text(
                        "- 89.000 VND",
                        color = Color(0xFFFF3333),
                        fontSize = 14.sp,
                    )
                }
                HorizontalDivider(Modifier.height(1.dp), color = Color.Gray)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Tổng (${checkoutItems.size} mặt hàng)",
                        color = Color.Black,
                        fontSize = 14.sp,
                    )
                    Text(
                        "10 230 000 VND",
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W500
                    )
                }
                MyButton(
                    text = "Đặt hàng",
                    onClick = { },
                    modifier = Modifier.padding(top = 8.dp),
                    backgroundColor = Color.Black,
                    textColor = Color.White,
                )
                Spacer(Modifier.height(4.dp))
            }
        }
    }
//    }
}

@Composable
fun PayMethodItem(
    text: String,
    imageRes: Int,
    selected: Boolean,
    onSelected: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
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