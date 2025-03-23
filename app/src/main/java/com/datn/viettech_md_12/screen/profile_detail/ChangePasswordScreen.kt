package com.datn.viettech_md_12.screen.profile_detail

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.datn.viettech_md_12.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(navController: NavController) {
    var step by remember { mutableStateOf(1) }
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
        // Click ra ngoài ẩn bàn phím
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = Color.Black
                )
            }
            Text(
                text = stringResource(R.string.change_password),
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = if (step == 1) "01/02" else "02/01",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.padding(end = 16.dp)
            )
        }
        DividerItem()
        Spacer(modifier = Modifier.height(20.dp))

        if (step == 1) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Mật khẩu cũ",
                    color = Color.Black,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 16.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Nhập mật khẩu cũ để thay đổi mật khẩu.",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Mật khẩu",
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 16.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    OutlinedTextField(
                        value = oldPassword,
                        onValueChange = { oldPassword = it },
                        modifier = Modifier.width(380.dp),
                        shape = RoundedCornerShape(12.dp),
                        visualTransformation = PasswordVisualTransformation(),
                        placeholder = { Text(text = "Nhập mật khẩu của bạn") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = Color.Transparent, // Nền trong suốt
                            cursorColor = Color.Black, // Màu con trỏ nhập liệu
                            focusedBorderColor = Color.Black, // Viền màu đen khi focus
                            unfocusedBorderColor = Color.Gray, // Viền màu xám khi không focus
                            focusedTextColor = Color.Black,// color Noi dung
                        )
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Mật khẩu mới",
                    color = Color.Black,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 16.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Nhập mật khẩu mới để thay đổi mật khẩu.",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Mật khẩu",
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 16.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        modifier = Modifier.width(380.dp),
                        shape = RoundedCornerShape(12.dp),
                        visualTransformation = PasswordVisualTransformation(),
                        placeholder = { Text(text = "Nhập mật khẩu của bạn") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = Color.Transparent, // Nền trong suốt
                            cursorColor = Color.Black, // Màu con trỏ nhập liệu
                            focusedBorderColor = Color.Black, // Viền màu đen khi focus
                            unfocusedBorderColor = Color.Gray, // Viền màu xám khi không focus
                            focusedTextColor = Color.Black,// color Noi dung
                        )
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Xác nhận mật khẩu",
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 16.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        modifier = Modifier.width(380.dp),
                        shape = RoundedCornerShape(12.dp),
                        visualTransformation = PasswordVisualTransformation(),
                        placeholder = { Text(text = "Nhập mật khẩu của bạn") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = Color.Transparent, // Nền trong suốt
                            cursorColor = Color.Black, // Màu con trỏ nhập liệu
                            focusedBorderColor = Color.Black, // Viền màu đen khi focus
                            unfocusedBorderColor = Color.Gray, // Viền màu xám khi không focus
                            focusedTextColor = Color.Black,// color Noi dung
                        )
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {
                    if (step == 1) {
                        step = 2 // Chuyển sang bước nhập mật khẩu mới
                    } else {
                        // Xử lý đổi mật khẩu (nếu cần)
                        if (newPassword == confirmPassword) {
                            navController.popBackStack() // Quay về sau khi đổi mật khẩu
                        }
                    }
                },
                modifier = Modifier
                    .width(380.dp)
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black
                ),
                enabled = if (step == 1) oldPassword.isNotEmpty() else newPassword.isNotEmpty() && confirmPassword == newPassword
            ) {
                Text("Tiếp tục", color = Color.White)
            }
        }
    }
}


@Composable
fun DividerItem() {
    Divider(
        color = Color(0xffF4F5FD),
        thickness = 1.dp,
    )
}

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_7)
@Composable
fun ChangePasswordScreenPreview() {
    ChangePasswordScreen(navController = NavController(context = LocalContext.current))
}