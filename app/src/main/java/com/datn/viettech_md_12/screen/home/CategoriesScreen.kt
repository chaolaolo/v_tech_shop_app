package com.datn.viettech_md_12.screen.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.datn.viettech_md_12.R
import com.datn.viettech_md_12.component.CustomItemCategories
import com.datn.viettech_md_12.component.CustomTopAppBar2
import com.datn.viettech_md_12.viewmodel.CategoriesViewModel

@Composable
fun CategoriesScreen(navController: NavController, viewModel: CategoriesViewModel = viewModel()) {
    val categories by viewModel.categories.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

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
        // Nếu đang tải, hiển thị CircularProgressIndicator
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
            // Khi dữ liệu đã được tải, hiển thị danh sách categories
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
                        image = item.image,
                        title = item.name
                    )
                }
            }
        }
    }
}
