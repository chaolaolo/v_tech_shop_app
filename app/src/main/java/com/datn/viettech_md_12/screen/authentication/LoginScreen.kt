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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
class LoginScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LoginUser()
        }
    }
}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginUser() {
    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
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
            "Đăng Nhập",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Row (
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                "Bạn chưa có tài khoản? ",
                fontSize = 16.sp,
                color = Color.Gray
            )
            TextButton(
                modifier = Modifier,
                onClick = {},
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    "Đăng Ký",
                    fontSize = 16.sp,
                    color = Color(0xFF21D4B4),
                )
            }
        }
        Spacer(Modifier.height(20.dp))
        //Email TextField
        Row {
            Text(
                text = "Tên đăng nhập",
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
            hint = "Tên đăng nhập",
            value = username,
            onValueChange = { username = it },
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
        //forgot password text
        Spacer(modifier = Modifier.height(10.dp))
        TextButton(
            modifier = Modifier.align(Alignment.End),
            onClick = {},
            contentPadding = PaddingValues(0.dp),
        ) {
            Text(
                text = "Quên mật khẩu?",
                fontSize = 16.sp,
                color = Color(0xFF21D4B4),
                modifier = Modifier
            )
        }
        //Login button
        Spacer(modifier = Modifier.height(20.dp))
        MyButton(
            text = "Đăng Nhập",
            onClick = {},
            modifier = Modifier,
            backgroundColor = Color.Black,
            textColor = Color.White
        )
        //login with google button
        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp)
        ) {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                mainAxisAlignment = MainAxisAlignment.Center,
            ) {
                Text(
                    text = "Khi nhấn đăng nhập đồng nghĩa với việc bạn đồng ý với ",
                    fontSize = 14.sp,
                    color = Color.Black
                )
                Text(
                    text = "Chính sách bảo mật ",
                    fontSize = 14.sp,
                    color = Color(0xFF1F8BDA)
                )
                Text(
                    text = "và ",
                    fontSize = 14.sp,
                    color = Color.Black
                )
                Text(
                    text = "Điều khoản & Điều kiện ",
                    fontSize = 14.sp,
                    color = Color(0xFF1F8BDA)
                )
                Text(
                    text = "của chúng tôi.",
                    fontSize = 14.sp,
                    color = Color.Black
                )

            }

        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun GreetingPreview4() {
    LoginScreen()
}