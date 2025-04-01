package com.datn.viettech_md_12.component

import android.util.Log
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.datn.viettech_md_12.R
import com.datn.viettech_md_12.data.model.CategoryModel
import androidx.navigation.NavController

@Composable
fun CustomLazyRow(
    categories: List<CategoryModel>,
    navController: NavController
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        items(categories.take(4), key = { it.id }) { category ->
            CustomCategoryItem(
                name = category.name,
                imageUrl = category.thumbnail,
                navController = navController,
                id = category.id,
            )
        }
    }
}

@Composable
fun CustomCategoryItem(
    id: String,
    name:String,
    imageUrl: String,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val BASE_URL = "http://103.166.184.249:3056"
    Log.d("23222", "CustomCategoryItem: $BASE_URL$imageUrl")
    Card(
        modifier = modifier
            .size(87.dp, 69.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = LocalIndication.current,
                onClick = { navController.navigate("category/$id") }
            ),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    )
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = "$BASE_URL$imageUrl",
                contentDescription = name,
                modifier = Modifier.size(50.dp),
                contentScale = ContentScale.Crop,
                error = painterResource(R.drawable.ic_launcher_foreground)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = name,
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF1C1B1B),
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}
