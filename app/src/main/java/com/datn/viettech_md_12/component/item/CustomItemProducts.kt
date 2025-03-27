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
import com.datn.viettech_md_12.data.model.ProductByCateModel
import com.datn.viettech_md_12.data.model.ProductModel

@Composable
fun CustomItemProducts(product: ProductModel) {
    CustomItemProductsBase(
        imageUrl = product.productThumbnail,
        name = product.productName,
        price = product.productPrice
    )
}

@Composable
fun CustomItemProductsByCate(productByCateModel: ProductByCateModel) {
    CustomItemProductsBase(
        imageUrl = productByCateModel.productThumbnail,
        name = productByCateModel.productName,
        price = productByCateModel.productPrice
    )
}

@Composable
private fun CustomItemProductsBase(imageUrl: String, name: String, price: Double) {
    var isFavorite by remember { mutableStateOf(false) }
    val BASE_URL = "http://103.166.184.249:3056/"

    Card(
        shape = MaterialTheme.shapes.large,
        modifier = Modifier
            .width(183.dp)
            .height(260.dp)
            .clickable {
                /*
                Product detail screen
                 */
            },
        colors = CardDefaults.cardColors(containerColor = Color.White)
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
                        model = "$BASE_URL$imageUrl",
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    name,
                    color = Color(0xFF1C1B1B),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    "$price$",
                    color = Color(0xFF1C1B1B),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            IconButton(
                onClick = { isFavorite = !isFavorite },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(24.dp)
            ) {
                Icon(
                    painter = painterResource(if (!isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_selected),
                    contentDescription = "Favorite",
                    tint = Color.Unspecified
                )
            }
        }
    }
}

