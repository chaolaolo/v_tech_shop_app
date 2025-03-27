package com.datn.viettech_md_12.component.item

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.datn.viettech_md_12.R

@Composable
fun CustomItemCategories(image: String?, title: String, onClick: () -> Unit) {
    Card(
        shape = MaterialTheme.shapes.large,
        modifier = Modifier
            .width(183.dp)
            .wrapContentHeight()
            .clickable { onClick() },
        colors = CardColors(
            containerColor = Color.White,
            contentColor = Color.White,
            disabledContainerColor = Color.White,
            disabledContentColor = Color.White
        )
    ) {
        Box {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(158.dp)
                        .clip(MaterialTheme.shapes.extraLarge)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = image,
                        contentDescription = image,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        error = painterResource(R.drawable.ic_launcher_foreground)
                    )
                }
                Text(
                    title,
                    color = Color(0xFF1C1B1B),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}