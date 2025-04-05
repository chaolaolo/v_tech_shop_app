package com.datn.viettech_md_12.screen.checkout

import MyButton
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.datn.viettech_md_12.R

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OrderSuccessfullyScreen(navController:NavController) {
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
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                    }
                },
            )
        }
    ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White)
                    .padding(innerPadding)
                    .systemBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.White)
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    //ảnh
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
                            painter = painterResource(R.drawable.order_successfully),
                            contentDescription = "contentDescription"
                        )
                    }
                    Spacer(Modifier.height(20.dp))
                    //Text
                    androidx.compose.material.Text(
                        "Đơn hàng của bạn đã được đặt thành công",
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(8.dp))
                    androidx.compose.material.Text(
                        "Cảm ơn bạn đã lựa chọn chúng tôi! Hãy thoải mái tiếp tục mua sắm và khám phá nhiều sản phẩm của chúng tôi. Chúc bạn mua sắm vui vẻ!",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(Modifier.height(10.dp))
                    //Button
                    MyButton(
                        text = "Tiếp tục mua sắm",
                        onClick = { },
                        modifier = Modifier,
                        backgroundColor = Color.Black,
                        textColor = Color.White,
                    )
                }
            }
    }
}


@Preview(showBackground = true)
@Composable
fun OrderSuccessfullyPreview() {
    OrderSuccessfullyScreen(rememberNavController())
}