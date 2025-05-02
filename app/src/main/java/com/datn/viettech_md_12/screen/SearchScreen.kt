package com.datn.viettech_md_12.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.datn.viettech_md_12.FilterBottomSheet
import com.datn.viettech_md_12.R
import com.datn.viettech_md_12.common.SortOption
import com.datn.viettech_md_12.component.item.CustomItemProducts
import com.datn.viettech_md_12.viewmodel.SearchViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    searchViewModel: SearchViewModel = viewModel(),
) {
    val text = remember { mutableStateOf("") }
    val searchResults by searchViewModel.searchResults.collectAsState()
    val isLoading by searchViewModel.isLoading.collectAsState()
    val errorMessage by searchViewModel.errorMessage.collectAsState()

    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    var selectedSortOption by remember { mutableStateOf(SortOption.PRICE_DESC) }
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                modifier = Modifier.padding(start = 16.dp),
                navigationIcon = {
                    Icon(
                        painter = painterResource(R.drawable.logo),
                        contentDescription = "logo",
                        tint = Color(0xFF309A5F),
                        modifier = Modifier.clickable { navController.popBackStack() }
                    )
                },
                actions = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "close",
                            modifier = Modifier.size(25.dp)
                        )
                    }
                },
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
                        contentDescription = "search",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(24.dp)
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        contentAlignment = Alignment.CenterStart
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
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
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

                    IconButton(
                        onClick = {
                            showBottomSheet = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = "Filter",
                            tint = Color(0xFF6F7384),
                            modifier = Modifier.size(24.dp)
                        )
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
                        CustomItemProducts(
                            product = product,
                            onClick = {
                                navController.navigate("product_detail/${product.id}")
                            }
                        )
                    }
                }
            }
        }

        // Modal Bottom Sheet đúng chuẩn Material3
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = bottomSheetState
            ) {
                FilterBottomSheet(
                    selectedOption = selectedSortOption,
                    onOptionSelected = { selectedSortOption = it },
                    onApplyClick = {
                        searchViewModel.sortSearchResults(selectedSortOption)
                        coroutineScope.launch { bottomSheetState.hide() }
                        showBottomSheet = false
                    }
                )
            }
        }
    }
}
