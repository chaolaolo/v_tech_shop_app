package com.datn.viettech_md_12.component.cart_component

import MyButton
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.datn.viettech_md_12.component.MyTextField
import com.datn.viettech_md_12.data.model.DiscountResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoucherBottomSheetContent(
    scaffoldState: BottomSheetScaffoldState,
    snackbarHostState: SnackbarHostState,
    listDiscount: List<DiscountResponse.DiscountModel>,
    selectedVoucherId: MutableState<String?>,
    selectedVoucher: MutableState<DiscountResponse.DiscountModel?>,
    voucherCode: MutableState<String>,
    scope: CoroutineScope,
) {
            val sortedDiscounts = remember(voucherCode.value, listDiscount) {
                if (voucherCode.value.isBlank()) {
                    listDiscount
                } else {
                    val lowerCaseQuery = voucherCode.value.lowercase()
                    listDiscount.sortedByDescending {
                        it.code?.lowercase()?.contains(lowerCaseQuery) == true
                    }
                }
            }
    val lazyListState = rememberLazyListState()
    LaunchedEffect(voucherCode.value) {
        if (voucherCode.value.isNotBlank()) {
            lazyListState.animateScrollToItem(0)
        }
    }
    Column(
        modifier = Modifier
//            .padding(start = 16.dp, end = 16.dp, top = 10.dp)
            .fillMaxSize()
//            .fillMaxWidth()
//            .heightIn(max = LocalConfiguration.current.screenHeightDp.dp * 0.7f)
            .imePadding()
    ) {
        // Sheet header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Mã giảm giá",
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            Icon(
                Icons.Filled.Close,
                contentDescription = "Thoát bottomsheet",
                modifier = Modifier
                    .size(20.dp)
                    .clickable {
                        scope.launch {
                            scaffoldState.bottomSheetState.hide()
                        }
                    }
            )
        }
        Spacer(Modifier.height(8.dp))
        HorizontalDivider()
        Spacer(Modifier.height(10.dp))

        // TextField nhập mã
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MyTextField(
                hint = "Nhập mã giảm giá",
                value = voucherCode.value,
                onValueChange = { voucherCode.value = it },
                isPassword = false,
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(4.dp))
            Card(
                onClick = {
                    val enteredCode = voucherCode.value
                    val matchingVoucher = listDiscount.firstOrNull { it.code == enteredCode }
                    if (matchingVoucher != null) {
                        selectedVoucherId.value = matchingVoucher.id
                        selectedVoucher.value = matchingVoucher
                        scope.launch {
                            snackbarHostState.showSnackbar("Áp dụng mã thành công!")
                        }
                        scope.launch {
                             scaffoldState.bottomSheetState.hide()
                        }
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar("Mã không hợp lệ.")
                        }
                    }
                },
                modifier = Modifier
                    .width(100.dp)
                    .height(50.dp),
                colors = CardDefaults.cardColors(Color(0xFF21D4B4)),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Transparent),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Áp dụng",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }

        Spacer(Modifier.height(10.dp))
        Text("Voucher dành cho bạn", color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.Bold)

        // Danh sách mã giảm giá
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xfff4f5fd))
                .heightIn(max = LocalConfiguration.current.screenHeightDp.dp * 0.7f)
        ) {
            items(sortedDiscounts, key = { it.id ?: "" }) { discount ->
                VoucherItem(
                    voucher = discount,
                    selectedVoucher = selectedVoucherId.value == discount.id,
                    onSelectedVoucher = { selectedVoucher ->
                        selectedVoucherId.value = selectedVoucher.id
                        voucherCode.value = selectedVoucher.code ?: ""
                    },
                )
            }
            item {
                Spacer(Modifier.height(10.dp))
                // Button xác nhận dùng mã
                MyButton(
                    text = "Xác nhận",
                    onClick = {
                        val enteredCode = voucherCode.value
                        val matchingVoucher = listDiscount.firstOrNull { it.code == enteredCode }
                        if (matchingVoucher != null) {
                            selectedVoucherId.value = matchingVoucher.id
                            selectedVoucher.value = matchingVoucher
                            scope.launch {
                                snackbarHostState.showSnackbar("Áp dụng mã thành công!")
                            }
                            scope.launch {
                                scaffoldState.bottomSheetState.hide()
                            }
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar("Mã không hợp lệ.")
                            }
                        }
                    },
                    modifier = Modifier,
                    backgroundColor = Color.Black,
                    textColor = Color.White,
                )
                Spacer(Modifier.height(10.dp))
            }
        }
    }
}


//            Column(
//                modifier = Modifier
//                    .padding(start = 16.dp, end = 16.dp, top = 10.dp)
//                    .fillMaxWidth()
//                    .heightIn(max = LocalConfiguration.current.screenHeightDp.dp * 0.7f)
//                    .imePadding()
//            ) {
//                //Sheet header
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(end = 10.dp),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                Text(
//                    "Mã giảm giá",
//                    color = Color.Black,
//                    fontSize = 16.sp,
//                    fontWeight = FontWeight.Bold,
//                    textAlign = TextAlign.Center,
//                    modifier = Modifier.weight(1f)
//                )
//                    Icon(
//                        Icons.Filled.Close,
//                        contentDescription = "Thoát bottomsheet",
//                        modifier = Modifier
//                            .size(20.dp)
//                            .clickable {
//                                scope.launch {
//                                    scaffoldState.bottomSheetState.hide()
//                                }
//                            }
//                    )
//                }
//                Spacer(Modifier.height(8.dp))
//                HorizontalDivider()
//                Spacer(Modifier.height(10.dp))
//                //TextField nhập mã
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                MyTextField(
//                    hint = "Nhập mã giảm giá",
//                    value = voucherCode.value,
//                    onValueChange = { voucherCode.value = it },
//                    isPassword = false,
//                    modifier = Modifier.weight(1f)
//                )
//                    Spacer(Modifier.width(4.dp))
//                    Card(
//                        onClick = {
//                            val enteredCode = voucherCode.value
//                            val matchingVoucher = listDiscount.firstOrNull { it.code == enteredCode }
//                            if (matchingVoucher != null) {
//                                selectedVoucherId.value = matchingVoucher.id
//                                selectedVoucher.value = matchingVoucher
//                                scope.launch {
//                                    snackbarHostState.showSnackbar("Áp dụng mã thành công!")
//                                }
//                            } else {
//                                scope.launch {
//                                    snackbarHostState.showSnackbar("Mã không hợp lệ.")
//                                }
//                            }
//                        },
//                        modifier = Modifier
//                            .width(100.dp)
//                            .height(50.dp),
//                        colors = CardDefaults.cardColors(Color(0xFF21D4B4)),
//                    ) {
//                        Column(
//                            modifier = Modifier
//                                .fillMaxSize()
//                                .background(Color.Transparent),
//                            horizontalAlignment = Alignment.CenterHorizontally,
//                            verticalArrangement = Arrangement.Center
//                        ) {
//                            Text(
//                                "Áp dụng",
//                                color = Color.White,
//                                fontSize = 14.sp,
//                                fontWeight = FontWeight.Bold,
//                                modifier = Modifier.fillMaxWidth(),
//                                textAlign = TextAlign.Center,
//                            )
//                        }
//                    } //Card
//                }
//                Spacer(Modifier.height(10.dp))
//                Text("Voucher dành cho bạn", color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.Bold)
//                //danh sách mã giảm giá
//                LazyColumn(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .background(Color(0xfff4f5fd))
//                        .heightIn(max = LocalConfiguration.current.screenHeightDp.dp * 0.7f)
//                ) {
//                    items(listDiscount, key = { it.id ?: "" }) { discount ->
//                        VoucherItem(
//                            voucher = discount,
//                            selectedVoucher = selectedVoucherId.value == discount.id,
//                            onSelectedVoucher = { selectedVoucher ->
//                                // Xử lý khi chọn voucher
//                                selectedVoucherId.value = selectedVoucher.id
//                                voucherCode.value = selectedVoucher.code ?: ""
//                            },
//                        )
//                    }
//                    item {
//                        Spacer(Modifier.height(10.dp))
//                        //Button xác nhận dùng mã
//                        MyButton(
//                            text = "Xác nhận",
//                            onClick = {
//                                val enteredCode = voucherCode.value
//                                val matchingVoucher = listDiscount.firstOrNull { it.code == enteredCode }
//                                if (matchingVoucher != null) {
//                                    selectedVoucherId.value = matchingVoucher.id
//                                    selectedVoucher.value = matchingVoucher
//                                    scope.launch {
//                                        snackbarHostState.showSnackbar("Áp dụng mã thành công!")
//                                    }
//                                    scope.launch { scaffoldState.bottomSheetState.hide() }
//                                } else {
//                                    scope.launch {
//                                        snackbarHostState.showSnackbar("Mã không hợp lệ.")
//                                    }
//                                }
////                                selectedVoucherId.value?.let { voucherId ->
////                                    val selectedVoucherId = listDiscount.firstOrNull { it.id == voucherId }
////                                    selectedVoucherId?.let {
//////                                        cartViewModel.applyDiscount(it.code ?: "")
////                                        selectedVoucher.value = it
////                                    }
////                                }
//                            },
//                            modifier = Modifier,
//                            backgroundColor = Color.Black,
//                            textColor = Color.White,
//                        )
//                        Spacer(Modifier.height(10.dp))
//                    }
//                }
//
//            }