package com.datn.viettech_md_12.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.datn.viettech_md_12.data.model.Category

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(28.dp))
            CustomHorizontalPager(banners)
            Spacer(Modifier.height(28.dp))
            Column(
                modifier = Modifier
                    .height(109.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically // Căn giữa theo chiều dọc
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
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.End
                    )
                }
                Spacer(Modifier.height(14.dp))
                CustomLazyRow(categories)
            }
            Spacer(Modifier.height(28.dp))
            Column(
                modifier = Modifier
                    .height(109.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically // Căn giữa theo chiều dọc
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
                        modifier = Modifier
                            .clickable {},
                    )
                }
                Spacer(Modifier.height(14.dp))
                CustomLazyRow(categories)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_7)
@Composable
fun PreviewCustomBanner() {
    HomeScreen()
}