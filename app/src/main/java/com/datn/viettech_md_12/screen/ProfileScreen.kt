package com.datn.viettech_md_12.screen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.datn.viettech_md_12.R
import com.datn.viettech_md_12.screen.authentication.OnbroadingActivity

@Composable
fun ProfileScreen(navController: NavController) {
    //Link Policy cua app
    val context = LocalContext.current
    //link policy
    val url = "https://sites.google.com/view/viet-tech-md-12/trang-ch%E1%BB%A7"
    //link Terms & Conditions
    val url2 = "https://sites.google.com/view/term-conditions-md-12/trang-ch%E1%BB%A7"
    //link faqs
    val url3 = "https://sites.google.com/view/faqs-md-12/trang-ch%E1%BB%A7"
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xff21D4B4))
    )
    {
        Spacer(modifier = Modifier.height(20.dp))
        ProfileHeader()
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(Color.White)
        ) {
            item { Spacer(modifier = Modifier.height(26.dp)) }
            item { ProfileTitle(stringResource(R.string.personal_information)) }
            item {
                ProfileItem(
                    R.drawable.ic_shipping_profile,
                    stringResource(R.string.shipping_address),
                    onClick = { navController.navigate("address_screen") }
                )
            }
//            item { DividerItem() }
//            item {
//                ProfileItem(
//                    R.drawable.ic_payment_profile,
//                    stringResource(R.string.payment_method),
//                    onClick = { navController.navigate("payment_screen") }
//                )
//            }
            item { DividerItem() }
            item {
                ProfileItem(
                    R.drawable.ic_order_profile,
                    stringResource(R.string.order_history),
                    onClick = { navController.navigate("order_history_screen") }
                )
            }
            item { DividerItem() }
            item { Spacer(modifier = Modifier.height(26.dp)) }
            item { ProfileTitle(stringResource(R.string.support_information)) }
            item {
                ProfileItem(
                    R.drawable.ic_policy_profile,
                    stringResource(R.string.privacy_policy),
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                    }
                )
            }
            item { DividerItem() }
            item {
                ProfileItem(
                    R.drawable.ic_terms_profile,
                    stringResource(R.string.terms_conditions),
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url2))
                        context.startActivity(intent)
                    }
                )
            }
            item { DividerItem() }
            item {
                ProfileItem(
                    R.drawable.ic_faqs_profile,
                    stringResource(R.string.faqs),
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url3))
                        context.startActivity(intent)
                    }
                )
            }
            item { DividerItem() }
            item { Spacer(modifier = Modifier.height(26.dp)) }
            item { ProfileTitle(stringResource(R.string.account_mangager)) }
            item {
                ProfileItem(
                    R.drawable.ic_change_password,
                    stringResource(R.string.change_password),
                    onClick = { navController.navigate("change_password_screen") }
                )
            }
            item { DividerItem() }
//            item {
//                ProfileItem(
//                    R.drawable.ic_dark_theme_profile,
//                    stringResource(R.string.dark_theme),
//                    onClick = {}
//                )
//            }
//            item { DividerItem() }
        }
    }
}

@Composable
fun ProfileHeader() {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    val token = sharedPreferences.getString("accessToken", null)
    val fullName = sharedPreferences.getString("fullname", "")
    val email = sharedPreferences.getString("email", "")

    val isLoggedIn = !token.isNullOrEmpty()&&!fullName.isNullOrEmpty()&&!email.isNullOrEmpty()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xff21D4B4))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.user_home),
            contentDescription = null,
            modifier = Modifier.size(60.dp)
        )
        Spacer(modifier = Modifier.width(15.dp))
        Column {
            Text(
                text = if (isLoggedIn) fullName!! else "Họ và tên",
                color = Color.White,
                fontSize = 18.sp
            )
            Text(
                text = if (isLoggedIn) email!! else "Địa chỉ email",
                color = Color.White,
                fontSize = 18.sp
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = {
            logout(context)
        }) {
            Icon(
                painter = painterResource(R.drawable.ic_logout_profile),
                contentDescription = null, tint = Color.White
            )
        }
    }
}

fun logout(context: Context) {
    val sharedPrefs = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)

    // Đặt lại trạng thái đăng nhập = false
    sharedPrefs.edit().putBoolean("IS_LOGGED_IN", false).apply()

    // Tạo Intent để chuyển đến màn hình Onboarding
    val intent = Intent(context, OnbroadingActivity::class.java)

    // Xóa hết backstack
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    context.startActivity(intent)

    // Kết thúc Activity hiện tại
    if (context is Activity) {
        context.finish()
    }
}

@Composable
fun ProfileTitle(title: String) {
    Text(
        text = title,
        fontSize = 14.sp,
        color = Color.Black,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 8.dp)
    )
}

@Composable
fun ProfileItem(icon: Int, title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            Modifier.size(24.dp)
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = title,
            color = Color.Gray,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(R.drawable.ic_next_right),
            contentDescription = null,
            Modifier.size(12.dp)
        )
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
fun ProfileScreenPreview() {

}