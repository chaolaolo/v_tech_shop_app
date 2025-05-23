package com.datn.viettech_md_12.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.datn.viettech_md_12.R
import com.datn.viettech_md_12.component.CustomTopAppBar
import com.datn.viettech_md_12.component.item.CustomItemProducts
import com.datn.viettech_md_12.viewmodel.ProductViewModel
import com.datn.viettech_md_12.viewmodel.SearchViewModel

@Composable
fun SearchScreen(
    navController: NavController,
    searchViewModel: SearchViewModel = viewModel(),
    productViewModel: ProductViewModel = viewModel()

) {
    val text = remember { mutableStateOf("") }
    val searchResults by searchViewModel.searchResults.collectAsState()
    val isLoading by searchViewModel.isLoading.collectAsState()
    val errorMessage by searchViewModel.errorMessage.collectAsState()

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "VietTech",
                iconLogo = R.drawable.ic_logo,
                icon1 = null,
                icon2 = R.drawable.ic_cancel,
                navController = navController,
                actionTitle1 = "",
                actionTitle2 = "back",
            )
        },
        containerColor = Color.White,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Box(
                contentAlignment = Alignment.TopStart,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 16.dp),
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .border(1.dp, Color(0xFFF4F5FD), MaterialTheme.shapes.medium)
                        .padding(horizontal = 12.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .weight(1f)
                            .align(Alignment.CenterVertically)
                    ) {
                        BasicTextField(
                            value = text.value,
                            onValueChange = {
                                text.value = it
                                if (it.isNotEmpty()) {
                                    searchViewModel.searchProducts(it)
                                } else {
                                    searchViewModel.clearSearchResults()
                                }
                            },
                            textStyle = LocalTextStyle.current.copy(
                                fontSize = 14.sp,
                                color = Color.Black
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterStart) // Căn thẳng hàng với placeholder
                                .background(Color.Transparent),
                            singleLine = true // giúp input không bị nhảy dòng
                        )

                        if (text.value.isEmpty()) {
                            Text(
                                text = "Search",
                                color = Color(0xFF6F7384),
                                fontSize = 14.sp,
                                textAlign = TextAlign.Left,
                                modifier = Modifier.align(Alignment.CenterStart)
                            )
                        }
                    }
                }
            }

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (errorMessage != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = errorMessage!!, color = Color.Red)
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(16.dp)
                ) {
                    items(searchResults) { product ->
                        val context = LocalContext.current

                        CustomItemProducts(
                            product = product,
                            context = context,
                            viewModel = productViewModel,
                            onClick = {
                                navController.navigate("product_detail/${product.id}") // Chuyển đến chi tiết sản phẩm
                            }
                        )
                    }
                }
            }
        }
    }
}