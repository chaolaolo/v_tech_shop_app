package com.datn.viettech_md_12.screen.checkout

import CheckoutPaymentUI
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.datn.viettech_md_12.component.checkout.CheckoutReviewUI
import com.datn.viettech_md_12.component.checkout.CheckoutShippingUI
import com.datn.viettech_md_12.component.checkout.CheckoutTopBar

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CheckoutScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf("Shipping") }

    Scaffold(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .systemBarsPadding(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Thanh Toán") },
                colors = TopAppBarColors(
                    containerColor = Color.White,
                    scrolledContainerColor = Color.Transparent,
                    navigationIconContentColor = Color.Black,
                    titleContentColor = Color.Black,
                    actionIconContentColor = Color.Transparent
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                    }
                },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(Color.White)
                .fillMaxHeight()
        ) {
            //Checkout TopBar
//            Spacer(Modifier.height(10.dp))
            CheckoutTopBar(selectedTab) { newTab -> selectedTab = newTab }

            when (selectedTab) {
                "Shipping" -> CheckoutShippingUI()
                "Payment" -> CheckoutPaymentUI()
                "Review" -> CheckoutReviewUI(
                    navController = navController
                )
            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun CheckoutPreview() {
    CheckoutScreen(rememberNavController())
}