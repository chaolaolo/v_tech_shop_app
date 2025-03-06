package com.datn.viettech_md_12.screen.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.datn.viettech_md_12.R
import com.datn.viettech_md_12.component.*
import com.datn.viettech_md_12.viewmodel.HomeViewModel

@Composable
fun HomeScreen(navController: NavController, homeViewModel: HomeViewModel = viewModel()) {

    // Lắng nghe StateFlow từ ViewModel
    val banners by homeViewModel.banners.collectAsState()
    val categories by homeViewModel.categories.collectAsState()
    val latestProducts by homeViewModel.latestProducts.collectAsState()

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "VietTech",
                iconLogo = R.drawable.ic_logo,
                iconSearch = R.drawable.ic_search,
                iconProfile = R.drawable.ic_category2,
                navController = navController
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Banners
            item {
                Spacer(Modifier.height(24.dp))
                if (banners.isEmpty()) {
                    CircularProgressIndicator()
                } else {
                    CustomHorizontalPager(banners.map { painterResource(it) })
                }
            }
            // Categories
            item {
                Spacer(Modifier.height(24.dp))
                Column(
                    modifier = Modifier
                        .height(112.dp)
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Categories",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1C1B1B),
                            modifier = Modifier.weight(1f)
                        )

                        Text(
                            text = "SEE ALL",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF21D4B4),
                            textAlign = TextAlign.End,
                            modifier = Modifier.clickable {
                                navController.navigate("categories")
                            },
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    if (categories.isEmpty()) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    } else {
                        CustomLazyRow(categories)
                    }
                }
            }

            // Latest Products
            item {
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Latest Products",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1C1B1B),
                        maxLines = 1,
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = "SEE ALL",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF21D4B4),
                        textAlign = TextAlign.End,
                        modifier = Modifier.clickable {},
                    )
                }
                Spacer(Modifier.height(8.dp))
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(520.dp)
                ) {
                    if (latestProducts.isEmpty()) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(latestProducts.take(4)) { item ->
                                CustomItemLatestProducts(
                                    image = item.image,
                                    colorHexList = item.color,
                                    title = item.name
                                )
                            }
                        }
                    }
                }
            }

            item {
                Spacer(Modifier.height(80.dp))
            }
        }
    }
}
