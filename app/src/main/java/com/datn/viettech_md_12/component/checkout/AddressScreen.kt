package com.datn.viettech_md_12.component.checkout

import MyButton
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.datn.viettech_md_12.component.MyTextField
import com.datn.viettech_md_12.component.address_selection.DistrictDropdown
import com.datn.viettech_md_12.component.address_selection.ProvinceDropdown

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AddressScreen(navController:NavController) {
    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var detailAddress by remember { mutableStateOf("") }
    var postalCode by remember { mutableStateOf("") }
    var selectedProvince by remember { mutableStateOf("Hà Nội") }
    var selectedDistrict by remember { mutableStateOf("") }

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
//            Row {
//                Text(
//                    text = "Mã bưu chính",
//                    fontSize = 16.sp,
//                    fontWeight = FontWeight.Medium
//                )
//                Text(
//                    text = " *",
//                    fontSize = 16.sp,
//                    fontWeight = FontWeight.Medium,
//                    color = Color.Red
//                )
//            }
//            Spacer(modifier = Modifier.height(4.dp))
//            MyTextField(
//                hint = "Nhập mã bưu chính",
//                value = postalCode,
//                onValueChange = { postalCode = it },
//                modifier = Modifier,
//                isPassword = false
//            )
            //Button save
            Spacer(modifier = Modifier.weight(1f))
            MyButton(
                text = "Save",
                onClick = { },
                modifier = Modifier,
                backgroundColor = Color.Black,
                textColor = Color.White,
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }

}

@Composable
@Preview(showSystemUi = true)
fun PreviewAddressScreen() {
    AddressScreen(rememberNavController())
}