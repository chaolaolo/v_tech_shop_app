@file:OptIn(ExperimentalMaterial3Api::class)

package com.datn.viettech_md_12.presentations.screens.authentication

import MyButton
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.datn.viettech_md_12.R
import com.datn.viettech_md_12.presentations.components.MyTextField
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment

class EmailVertificationScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EmailVertificationUI()
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EmailVertificationUI() {

    var verificationCode by remember { mutableStateOf(List(6) { "" }) }
    val focusRequesters = remember { List(6) { FocusRequester() } }


    Scaffold(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .systemBarsPadding(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Xác thực OTP") },
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
                    text = "Xác thực Email",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
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
                            value = digit,
                            onValueChange = { newValue ->
                                if (newValue.length <= 1) {
                                    val newCode = verificationCode.toMutableList()
                                    newCode[index] = newValue
                                    verificationCode = newCode

                                    // Move focus to next field if available
                                    if (newValue.isNotEmpty() && index < 5) {
                                        focusRequesters[index + 1].requestFocus()
                                    }
                                }
                            },
                            focusRequester = focusRequesters[index]
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
                        text = "Resend Code",
                        color = Color(0xFF00BFA5),
                        fontSize = 16.sp
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerificationCodeDigit(
    value: String,
    onValueChange: (String) -> Unit,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .size(50.dp)
            .focusRequester(focusRequester)
            .border(
                width = 1.dp,
                color = if (value.isEmpty()) Color(0xFFF4F5FD) else Color(0xFF21D4B4),
//                color = Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            ),
        textStyle = TextStyle(
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
        )
    )
}

@Preview(showSystemUi = true)
@Composable
fun EmailVertificationPreview() {
    EmailVertificationUI()
}