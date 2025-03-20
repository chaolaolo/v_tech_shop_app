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
import com.datn.viettech_md_12.component.authentication.VerificationCodeDigit


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EmailVerificationScreen(navController: NavController) {

    var verificationCode by remember { mutableStateOf(List(6) { "" }) }
    val focusRequesters = remember { List(6) { FocusRequester() } }


    Scaffold(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .systemBarsPadding(), topBar = {
            TopAppBar(
                title = { Text(text = "Xác thực OTP") },
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
                    text = "Xác thực Email", fontSize = 28.sp, fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Nhập mã xác minh gồm 6 chữ số được gửi đến địa chỉ email của bạn.",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(32.dp))
                // Verification Code Input Fields
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    verificationCode.forEachIndexed { index, digit ->
                        VerificationCodeDigit(
                            value = digit, onValueChange = { newValue ->
                                if (newValue.length <= 1) {
                                    val newCode = verificationCode.toMutableList()
                                    newCode[index] = newValue
                                    verificationCode = newCode

                                    // Move focus to next field if available
                                    if (newValue.isNotEmpty() && index < 5) {
                                        focusRequesters[index + 1].requestFocus()
                                    }
                                }
                            }, focusRequester = focusRequesters[index]
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                // Resend Code Button
                TextButton(
                    onClick = { /* khi bấm nút Resend Code */ },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = "Resend Code", color = Color(0xFF00BFA5), fontSize = 16.sp
                    )
                }
                Spacer(modifier = Modifier.height(30.dp))
                // Proceed Button
                MyButton(
                    text = "Xác thực",
                    onClick = {
                        verificationCode.joinToString("")
                        Log.d("zzzz", "EmailVertificationUI: " + verificationCode.joinToString(""))
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
fun EmailVerificationPreview() {
    EmailVerificationScreen(rememberNavController())
}