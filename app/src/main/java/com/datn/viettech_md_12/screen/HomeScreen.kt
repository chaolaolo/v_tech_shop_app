package com.datn.viettech_md_12.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.datn.viettech_md_12.R
import com.datn.viettech_md_12.component.CustomHorizontalPager
import com.datn.viettech_md_12.component.CustomLazyRow
import com.datn.viettech_md_12.component.CustomTopAppBar
import com.datn.viettech_md_12.component.ItemType1
import com.datn.viettech_md_12.data.model.Category
import com.datn.viettech_md_12.data.model.Product

@Composable
fun HomeScreen() {
    val banners = listOf(
        painterResource(id = R.drawable.banner1),
        painterResource(id = R.drawable.banner2),
        painterResource(id = R.drawable.banner3),
        painterResource(id = R.drawable.banner4)
    )
    val categories: List<Category> = listOf(
        Category("Electronics", R.drawable.ic_category1),
        Category("Fashion", R.drawable.ic_category2),
        Category("Furniture", R.drawable.ic_category3),
        Category("Industrial", R.drawable.ic_category4),
    )
    val myColorHexList: List<String> = listOf("FF1C1B1B", "FF08E488", "FF21D4B4")
    val latestProducts: List<Product> = listOf(
        Product(
            image = R.drawable.banner3,
            isFavorite = false,
            color = myColorHexList,
            name = "TODO()0",
            originalPrice = 186.00,
            salePrice = 126.00
        ),
        Product(
            image = R.drawable.banner3,
            isFavorite = false,
            color = myColorHexList,
            name = "TODO()1",
            originalPrice = 186.00,
            salePrice = 126.00
        ),
        Product(
            image = R.drawable.banner3,
            isFavorite = false,
            color = myColorHexList,
            name = "TODO()2",
            originalPrice = 186.00,
            salePrice = 126.00
        ),
        Product(
            image = R.drawable.banner3,
            isFavorite = false,
            color = myColorHexList,
            name = "TODO()3",
            originalPrice = 186.00,
            salePrice = 126.00
        ),
        Product(
            image = R.drawable.banner3,
            isFavorite = false,
            color = myColorHexList,
            name = "TODO()4",
            originalPrice = 186.00,
            salePrice = 126.00
        ),
    )

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "ietTech",
                iconLogo = R.drawable.ic_logo,
                iconSearch = R.drawable.ic_search,
                iconProfile = R.drawable.ic_category2
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
            item {
                Spacer(Modifier.height(24.dp))
                CustomHorizontalPager(banners)
            }

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
                            modifier = Modifier.clickable {},
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    CustomLazyRow(categories)
                }
            }

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
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(latestProducts.take(4)) { item ->
                            ItemType1(
                                image = item.image,
                                colorHexList = item.color
                            )
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


@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_7)
@Composable
fun PreviewCustomBanner() {
    HomeScreen()
}