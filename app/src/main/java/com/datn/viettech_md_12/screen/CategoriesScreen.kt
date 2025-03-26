package com.datn.viettech_md_12.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.datn.viettech_md_12.R
import com.datn.viettech_md_12.component.item.CustomItemCategories
import com.datn.viettech_md_12.component.CustomTopAppBar2
import com.datn.viettech_md_12.viewmodel.CategoryViewModel

@Composable
fun CategoriesScreen(navController: NavController, viewModel: CategoryViewModel = viewModel()) {
    val categories by viewModel.categories.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Gọi API khi màn hình xuất hiện
    LaunchedEffect(Unit) {
        viewModel.fetchCategories()
    }

    Scaffold(
        topBar = {
            CustomTopAppBar2(
                title = "Categories",
                icon1 = R.drawable.ic_arrow_left,
                icon2 = null,
                icon3 = null,
                navController = navController,
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                items(categories) { item ->
                    CustomItemCategories(
                        image = null,  // Đảm bảo `image` có dữ liệu hợp lệ
                        title = item.name,
                        onClick = { navController.navigate("category/${item.name}") }
                    )
                }
                item {
                    Spacer(Modifier.height(80.dp))
                }
            }
        }
    }
}

