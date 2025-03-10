package com.datn.viettech_md_12.screen.authentication

import MyButton
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TextButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.datn.viettech_md_12.R
import com.datn.viettech_md_12.component.MyTextField

class RegisterScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RegisterUI()
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RegisterUI() {
    val context = LocalContext.current
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = Color.White)
            .padding(horizontal = 20.dp)
            .systemBarsPadding()
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "logo",
            modifier = Modifier
                .width(150.dp)
//                    .background(color = Color.Red)
            ,
            contentScale = ContentScale.FillWidth
        )
        Spacer(Modifier.height(20.dp))
        Text(
            "Đăng Ký",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Row (
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                "Bạn đã có tài khoản? ",
                fontSize = 16.sp,
                color = Color.Gray
            )
            TextButton(
                modifier = Modifier,
                onClick = {},
                contentPadding = PaddingValues(0.dp)
            ) {
            Text(
                "Đăng Nhập",
                fontSize = 16.sp,
                color = Color(0xFF21D4B4),
            )
        }
        }
        Spacer(Modifier.height(20.dp))
        //Full Name TextField
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
            modifier = Modifier
        )
        //Email TextField
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
            hint = "Email",
            value = email,
            onValueChange = { email = it },
            modifier = Modifier
        )
        //password TextField
        Spacer(modifier = Modifier.height(10.dp))
        Row {
            Text(
                text = "Mật khẩu",
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
            hint = "Mật khẩu",
            value = password,
            onValueChange = { password = it },
            modifier = Modifier,
            isPassword = true
        )
        //confirm password TextField
        Spacer(modifier = Modifier.height(10.dp))
        Row {
            Text(
                text = "Xác nhận mật khẩu",
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
            hint = "Xác nhận mật khẩu",
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            modifier = Modifier,
            isPassword = true
        )
        //Login button
        Spacer(modifier = Modifier.height(20.dp))
        MyButton(
            text = "Đăng Ký",
            onClick = {},
            modifier = Modifier,
            backgroundColor = Color.Black,
            textColor = Color.White
        )
        //login with google button
        Spacer(modifier = Modifier.height(10.dp))
        MyButton(
            text = "Đăng ký với Google",
            onClick = {},
            modifier = Modifier.border(
                width = 1.dp,
                brush = SolidColor(Color(0xFFF4F5FD)),
                shape = RoundedCornerShape(8.dp)
            ),
            backgroundColor = Color.White,
            textColor = Color.Black,
            painterIconResId = R.drawable.google_logo
        )
        //accept privacy policy notice text
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Preview(showSystemUi = true)
@Composable
fun RegisterPreview() {
    RegisterUI()
}