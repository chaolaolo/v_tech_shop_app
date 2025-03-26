package com.datn.viettech_md_12.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.datn.viettech_md_12.R
import com.datn.viettech_md_12.data.model.Category
import com.datn.viettech_md_12.data.model.CategoryModel

@Composable
fun CustomLazyRow(
    categories: List<CategoryModel>
) {

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        items(categories.take(4)) { category ->
            CustomCategoryItem(
                name = category.name,
                painter = painterResource(R.drawable.ic_category1)
            )
        }
    }
}

@Composable
fun CustomCategoryItem(
    name: String,
    painter: Painter,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .size(87.dp, 69.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(Color(0xFFFFFFFF)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painter,
                contentDescription = "Category image",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(8.dp, 8.dp, 8.dp, 0.dp)
                    .height(33.dp)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = name,
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF1C1B1B),
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(8.dp, 0.dp, 8.dp, 8.dp)

            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_7)
@Composable
fun PreviewCustomLazyRow() {
    val categories: List<Category> = listOf(
        Category("Electronics", R.drawable.ic_category1),
        Category("Fashion", R.drawable.ic_category2),
        Category("Furniture", R.drawable.ic_category3),
        Category("Industrial", R.drawable.ic_category4),
    )

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
//        CustomLazyRow(categories)
    }
}