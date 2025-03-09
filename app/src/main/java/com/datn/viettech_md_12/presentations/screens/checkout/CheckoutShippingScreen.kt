package com.datn.viettech_md_12.presentations.screens.checkout

import MyButton
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Outbox
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.PlaylistAddCheckCircle
import androidx.compose.material.icons.filled.PriceCheck
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.datn.viettech_md_12.R
import com.datn.viettech_md_12.presentations.components.DistrictDropdown
import com.datn.viettech_md_12.presentations.components.MyTextField
import com.datn.viettech_md_12.presentations.components.ProvinceDropdown

class CheckoutShippingScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CheckoutUI()
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CheckoutUI() {
    var selectedTab by remember { mutableStateOf("Shipping") }

    Scaffold(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .systemBarsPadding(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Thanh Toán") },
                backgroundColor = Color.White,
                navigationIcon = {
                    IconButton(onClick = { /* nút back */ }) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                    }
                },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(Color.White)
                .fillMaxHeight()
        ) {
            //Checkout TopBar
            Spacer(Modifier.height(10.dp))
            CheckoutTopBar(selectedTab) {newTab -> selectedTab = newTab}

            when (selectedTab) {
                "Shipping" -> CheckoutShippingUI()
                "Payment" -> CheckoutPaymentUI()
                "Review" -> CheckoutReviewUI()
            }
        }
    }
}

//CheckoutTopBar
@Composable
fun CheckoutTopBar(selectedTab: String, onTabSelected: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(
                onClick = {onTabSelected("Shipping")},
                Modifier
                    .padding(0.dp)
                    .size(30.dp),
            ) {
            Icon(
                Icons.Default.LocalShipping,
                modifier = Modifier
                    .size(30.dp),
                contentDescription = "Outbox Icon",
                tint = if (selectedTab == "Shipping") Color(0xFF21D4B4) else Color.Gray
            )
            }
            Text("Shipping", color = if (selectedTab == "Shipping") Color(0xFF21D4B4) else Color.Gray)
        }
        Divider(
            Modifier
                .weight(1f)
                .height(1.dp)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(
                onClick = {onTabSelected("Payment")},
                Modifier
                    .padding(0.dp)
                    .size(30.dp),

            ) {
                Icon(
                    Icons.Default.Payments,
                    modifier = Modifier
                        .size(30.dp),
                    contentDescription = "Outbox Icon",
                    tint = if (selectedTab == "Payment") Color(0xFF21D4B4) else Color.Gray
                )
            }
            Text("Payment", color = if (selectedTab == "Payment") Color(0xFF21D4B4) else Color.Gray)
        }
        Divider(
            Modifier
                .weight(1f)
                .height(1.dp)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(
                onClick = {onTabSelected("Review")},
                Modifier
                    .padding(0.dp)
                    .size(30.dp),
            ) {
                Icon(
                    Icons.Default.PlaylistAddCheckCircle,
                    modifier = Modifier
                        .size(30.dp),
                    contentDescription = "Outbox Icon",
                    tint = if (selectedTab == "Review") Color(0xFF21D4B4) else Color.Gray
                )
            }
            Text("Review", color = if (selectedTab == "Review") Color(0xFF21D4B4) else Color.Gray)
        }
    }
}

//CheckoutTopBar
@Composable
fun CheckoutShippingUI() {
    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var detailAddress by remember { mutableStateOf("") }
    var postalCode by remember { mutableStateOf("") }
    var selectedProvince by remember { mutableStateOf("Hà Nội") }
    var selectedDistrict by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .background(Color.White)
            .fillMaxHeight()
    ) {
        Spacer(Modifier.height(10.dp))
        //Email Text Field
        Row {
            Text(
                text = "Họ tên",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = " *",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Red
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        MyTextField(
            hint = "Nhập họ tên",
            value = fullName,
            onValueChange = { fullName = it },
            modifier = Modifier,
            isPassword = false
        )
        //phone number textfield
        Spacer(modifier = Modifier.height(10.dp))
        Row {
            Text(
                text = "Số điện thoại",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = " *",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Red
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        MyTextField(
            hint = "Nhập số điện thoại",
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            modifier = Modifier,
            isPassword = false
        )
        //Dropdown Province/City selection
        ProvinceDropdown(
            selectedProvince = selectedProvince,
            onProvinceSelected = {
                selectedProvince = it
                selectedDistrict = ""
            }
        )
        //Dropdown District selection
        DistrictDropdown(
            selectedDistrict = selectedDistrict,
            selectedProvince = selectedProvince,
            onDistrictSelected = { selectedDistrict = it },
        )
        //Detail Address Text Field
        Spacer(modifier = Modifier.height(10.dp))
        Row {
            Text(
                text = "Địa chỉ chi tiết",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = " *",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Red
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        MyTextField(
            hint = "Nhập địa chỉ chi tiết",
            value = detailAddress,
            onValueChange = { detailAddress = it },
            modifier = Modifier,
            isPassword = false
        )
        //Postal code Text Field
        Spacer(modifier = Modifier.height(10.dp))
        Row {
            Text(
                text = "Mã bưu chính",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = " *",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Red
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        MyTextField(
            hint = "Nhập mã bưu chính",
            value = postalCode,
            onValueChange = { postalCode = it },
            modifier = Modifier,
            isPassword = false
        )
        //Button save
        Spacer(modifier = Modifier.height(20.dp))
        MyButton(
            text = "Save",
            onClick = { },
            modifier = Modifier,
            backgroundColor = Color.Black,
            textColor = Color.White,
        )
    }
}

//CheckoutPaymentUI
@Composable
fun CheckoutPaymentUI() {

    var cardHolderName by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var expiration by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .background(Color.White)
            .fillMaxHeight()
    ) {
        //chọn hình thức thanh toán
        Spacer(Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF4F9F7)),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.google_logo),
                        contentDescription = "contentDescription",
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
            Spacer(Modifier.width(10.dp))
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF4F9F7)),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.google_logo),
                        contentDescription = "contentDescription",
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        }
        //Card Holder TextField
        Spacer(Modifier.height(20.dp))
        Row {
            Text(
                text = "Tên chủ thẻ",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = " *",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Red
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        MyTextField(
            hint = "Nhập tên chủ thẻ",
            value = cardHolderName,
            onValueChange = { cardHolderName = it },
            isPassword = false
        )
        //Card Number TextField
        Spacer(Modifier.height(10.dp))
        Row {
            Text(
                text = "Số thẻ",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = " *",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Red
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        MyTextField(
            hint = "1111 2222 2222 2222",
            value = cardNumber,
            onValueChange = { cardNumber = it },
            isPassword = false
        )
        //Expiration, CVV TextField
        Spacer(Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row {
                    Text(
                        text = "Hạn thẻ",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = " *",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Red
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                MyTextField(
                    hint = "MM/YY",
                    value = expiration,
                    onValueChange = { expiration = it },
                    isPassword = false
                )
            }

            Spacer(Modifier.width(10.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row {
                    Text(
                        text = "CVV",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = " *",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Red
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                MyTextField(
                    hint = "123",
                    value = cvv,
                    onValueChange = { cvv = it },
                    isPassword = false
                )
            }
        }

        // Continue button
        Spacer(Modifier.weight(1f))
        MyButton(
            text = "Tiếp tục",
            onClick = { },
            modifier = Modifier,
            backgroundColor = Color.Black,
            textColor = Color.White,
        )
    }
}

//CheckoutReviewUI
@Composable
fun CheckoutReviewUI() {
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .background(Color.White)
            .fillMaxHeight()
    ) {
        Spacer(Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Items(2)",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            IconButton(
                onClick = { },
                modifier = Modifier.size(20.dp),
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = "go to view all checkout items"
                )
            }
        }
        Divider(Modifier.height(1.dp), color = Color(0xFFF4F5FD))
        Spacer(Modifier.height(16.dp))
        Text(
            "Địa chỉ giao hàng",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(Modifier.height(16.dp))
//        Address Field
        AddressField(label = "Họ tên", value = "Chao Lao Lo")
        AddressField(label = "Số điện thoại", value = "0111 222 333")
        AddressField(label = "Tỉnh/Thành", value = "Ha Noi")
        AddressField(label = "Quận/Huyện", value = "Cau Giay")
        AddressField(label = "Địa chỉ chi tiết", value = "Địa chỉ XYZ")
        AddressField(label = "Mã bưu chính", value = "000000")
        Spacer(Modifier.height(16.dp))
        Divider(Modifier.height(1.dp), color = Color(0xFFF4F5FD))
        Spacer(Modifier.height(20.dp))
        Text(
            "Thông tin thanh toán",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(16.dp))
        AddressField(label = "Tổng giá tiền", value = "VND 900")
        AddressField(label = "Phí vận chuyển", value = "VND 50")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Tổng thanh toán",
                fontSize = 17.sp,
                color = Color.Black
            )
            Text(
                "VND 950",
                fontSize = 17.sp,
                color = Color.Black
            )
        }

        // Place order button
        Spacer(Modifier.weight(1f))
        MyButton(
            text = "Đặt hàng",
            onClick = { },
            modifier = Modifier,
            backgroundColor = Color.Black,
            textColor = Color.White,
        )
    }
}

@Composable
fun AddressField(label: String, value: String){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            label,
            fontSize = 16.sp,
            color = Color.Gray
        )
        Text(
            value,
            fontSize = 16.sp,
            color = Color.Gray
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun CheckoutPreview() {
    CheckoutUI()
}