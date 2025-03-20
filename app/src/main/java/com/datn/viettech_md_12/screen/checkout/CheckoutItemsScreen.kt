package com.datn.viettech_md_12.screen.checkout

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.datn.viettech_md_12.component.checkout.CheckoutItemTile
import com.datn.viettech_md_12.data.model.CartModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CheckoutReviewItemsScreen(navController: NavController) {
    val checkoutItems = remember {
        mutableStateListOf(
            CartModel(
                1,
                "Loop Silicone Strong Magnetic Watch",
                "https://i5.walmartimages.com/seo/YuiYuKa-Magnetic-Loop-Strap-Silicone-Band-Compatible-Apple-watch-band-45mm-44mm-Ultra-49mm-40mm-41mm-38mm-42mm-Women-Men-Strong-Magnet-Closure-Bracel_c0c1391f-6af4-4b66-8cca-a77c27428b5a.db0a2fbc84d23aebf027a6dd56bab110.jpeg?odnHeight=612&odnWidth=612&odnBg=FFFFFF",
                15.25,
                20.00,
                1
            ),
            CartModel(
                2,
                "M6 Smart watch IP67 Waterproof",
                "https://gomhang.vn/wp-content/uploads/2022/01/m6-138.webp",
                12.00,
                18.00,
                1
            ),
            CartModel(
                3,
                "Loop Silicone Strong Magnetic Watch",
                "https://i5.walmartimages.com/seo/YuiYuKa-Magnetic-Loop-Strap-Silicone-Band-Compatible-Apple-watch-band-45mm-44mm-Ultra-49mm-40mm-41mm-38mm-42mm-Women-Men-Strong-Magnet-Closure-Bracel_c0c1391f-6af4-4b66-8cca-a77c27428b5a.db0a2fbc84d23aebf027a6dd56bab110.jpeg?odnHeight=612&odnWidth=612&odnBg=FFFFFF",
                15.25,
                20.00,
                1
            ),
            CartModel(
                4,
                "M6 Smart watch IP67 Waterproof",
                "https://gomhang.vn/wp-content/uploads/2022/01/m6-138.webp",
                12.00,
                18.00,
                1
            ),
        )
    }


    Scaffold(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .systemBarsPadding(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Sản phẩm") },
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
                .fillMaxSize()
                .background(color = Color.White)
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .background(Color.White)
                    .padding(20.dp)
            ) {
                items(checkoutItems) { item ->
                    CheckoutItemTile(
                        item = item,
                        onQuantityChange = { id, newQuantity ->
                            val index = checkoutItems.indexOfFirst { it.id == id }
                            if (index != -1) {
                                checkoutItems[index] = checkoutItems[index].copy(
                                    quantity = newQuantity,
                                )
                            }

                        },
                        onDelete = { id ->
                            checkoutItems.removeAll() { it.id == id }
                        }
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    CheckoutReviewItemsScreen(rememberNavController())
}