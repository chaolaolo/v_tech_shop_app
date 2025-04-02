@file:OptIn(ExperimentalMaterial3Api::class)

package com.datn.viettech_md_12.screen.authentication

import MyButton
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.datn.viettech_md_12.component.MyTextField
import com.datn.viettech_md_12.component.authentication.VerificationCodeDigit


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ConfirmEmail(navController: NavController) {


    Scaffold(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .systemBarsPadding(), topBar = {
            TopAppBar(
                title = { Text(text = "Quên mật khẩu") },
                colors = TopAppBarColors(
                    containerColor = Color.White,
                    scrolledContainerColor = Color.Transparent,
                    navigationIconContentColor = Color.Black,
                    titleContentColor = Color.Black,
                    actionIconContentColor = Color.Black
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                    }
                },
            )

        }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White)
                    .padding(20.dp)
                    .systemBarsPadding(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Xác thực qua email", fontSize = 28.sp, fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Nhập địa chỉ email đăng ký để xác thực.",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(15.dp))
                // Resend Code Button
                Row {
                    Text(
                        text = "Email",
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
                    hint = "Nhập email để xác thực",
                    value = "",
                    onValueChange = { },
                    modifier = Modifier
                )
                Spacer(modifier = Modifier.height(20.dp))
                // Proceed Button
                MyButton(
                    text = "Gửi",
                    onClick = {

                    },
                    backgroundColor = Color.Black,
                    textColor = Color.White,
                )
            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun PreviewDefault() {
    ConfirmEmail(rememberNavController())
}