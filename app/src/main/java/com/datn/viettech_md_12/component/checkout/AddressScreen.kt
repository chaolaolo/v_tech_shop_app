package com.datn.viettech_md_12.component.checkout

import MyButton
import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.datn.viettech_md_12.component.MyTextField
import com.datn.viettech_md_12.component.address_selection.DistrictDropdown
import com.datn.viettech_md_12.component.address_selection.ProvinceDropdown
import com.datn.viettech_md_12.viewmodel.CheckoutViewModel
import com.datn.viettech_md_12.viewmodel.CheckoutViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AddressScreen(
    navController: NavController,
    checkoutViewModel: CheckoutViewModel = viewModel(factory = CheckoutViewModelFactory(LocalContext.current.applicationContext as Application)),
) {
    val checkoutState by checkoutViewModel.addressState.collectAsState()
    val isLoading by checkoutViewModel.isLoading.collectAsState()
    LaunchedEffect(Unit) {
        checkoutViewModel.getAddress()
    }
    val addressData = checkoutState?.body()?.data
    val address = addressData?.address // address sẽ có giá trị theo cấu trúc: Tỉnh, Huyện, Địa chỉ chi tiết
    val addressParts = address?.split(",") ?: emptyList()
    val province = if (addressParts.size > 0) addressParts[0].trim() else ""
    val district = if (addressParts.size > 1) addressParts[1].trim() else ""
    val detail = if (addressParts.size > 2) addressParts[2].trim() else ""

    var fullName by remember { mutableStateOf(addressData?.full_name ?: "") }
    var phoneNumber by remember { mutableStateOf(addressData?.phone ?: "") }
    var detailAddress by remember { mutableStateOf(detail) }





    Scaffold(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .fillMaxHeight()
            .systemBarsPadding(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Địa chỉ") },
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
    ) {innerPadding->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Log.d("AddressScreen", "Tỉnh: $province ")
            Log.d("AddressScreen", "Huyện: $district ")
            Log.d("AddressScreen", "Địa chỉ chi tiết: $detail ")
            var selectedProvince by remember { mutableStateOf(province) }
            var selectedDistrict by remember { mutableStateOf(district) }
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .background(Color.White)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(10.dp))
            //Email Text Field
            Row {
                Text(
                    text = "Họ tên",
                    fontSize = 16.sp,
                    color = Color.Black,
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
                hint = addressData?.full_name?.ifEmpty { "Nhập họ tên" } ?: "Nhập họ tên",
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
                    color = Color.Black,
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
                hint = addressData?.phone?.ifEmpty { "Nhập số điện thoại" } ?: "Nhập số điện thoại",
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                modifier = Modifier,
                isPassword = false
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row {
                Text(
                    text = "Địa chỉ",
                    fontSize = 16.sp,
                    color = Color.Black,
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
                    color = Color.Black,
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
                hint = detail.ifEmpty { "Nhập địa chỉ chi tiết" } ?: "Nhập địa chỉ chi tiết",
                value = detailAddress,
                onValueChange = { detailAddress = it },
                modifier = Modifier,
                isPassword = false
            )
            //Button save
            Spacer(modifier = Modifier.weight(1f))
            MyButton(
                text = "Lưu địa chỉ",
                onClick = {
                    // Sử dụng thông tin hiện tại nếu người dùng không nhập gì mới
                    val finalFullName = if (fullName.isEmpty()) addressData?.full_name ?: "" else fullName
                    val finalPhone = if (phoneNumber.isEmpty()) addressData?.phone ?: "" else phoneNumber
                    val finalProvince = if (selectedProvince.isEmpty()) province else selectedProvince
                    val finalDistrict = if (selectedDistrict.isEmpty()) district else selectedDistrict
                    val finalDetail = if (detailAddress.isEmpty()) detail else detailAddress
                    if (finalFullName.isNotEmpty() && finalPhone.isNotEmpty() && finalProvince.isNotEmpty()) {
                        checkoutViewModel.updateAddress(
                            fullName = finalFullName,
                            phone = finalPhone,
                            address = "$finalProvince, $finalDistrict, $finalDetail"
                        )
                        navController.popBackStack()
                    } else {
                        Log.e("AddressScreen", "lỗi: vui lòng nhập đủ thông tin")
                    }
                },
                modifier = Modifier,
                backgroundColor = Color.Black,
                textColor = Color.White,
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
    }

}

@Composable
@Preview(showSystemUi = true)
fun PreviewAddressScreen() {
    AddressScreen(rememberNavController())
}