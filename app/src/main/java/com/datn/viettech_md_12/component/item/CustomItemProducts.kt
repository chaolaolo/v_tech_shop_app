package com.datn.viettech_md_12.component.item

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.datn.viettech_md_12.viewmodel.ProductViewModel

@Composable
fun CustomItemProducts(product: ProductModel, viewModel: ProductViewModel, context: Context) {
    CustomItemProductsBase(product = product, viewModel = viewModel, context = context)
}

@Composable
fun CustomItemProductsByCate(productByCateModel: ProductByCateModel) {
    CustomItemProductsBase(productByCateModel = productByCateModel)
}

@Composable
fun CustomItemProductsBase(
    product: ProductModel? = null,
    productByCateModel: ProductByCateModel? = null,
    viewModel: ProductViewModel? = null,
    context: Context? = null
) {
    val imageUrl = product?.productThumbnail ?: productByCateModel?.productThumbnail
    val name = product?.productName ?: productByCateModel?.productName
    val price = product?.productPrice ?: productByCateModel?.productPrice

    var isFavorite by remember { mutableStateOf(false) }
    val BASE_URL = "http://103.166.184.249:3056/"

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .width(160.dp)
            .height(200.dp)
            .clickable {},
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF8F8F8)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = "$BASE_URL$imageUrl",
                        contentDescription = "Product Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit,
                        placeholder = painterResource(R.drawable.img_test_order)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    name ?: "",
                    color = Color(0xFF1C1B1B),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    "${price ?: 0.0}$",
                    color = Color(0xFF1C1B1B),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            IconButton(
                onClick = {
                    isFavorite = !isFavorite
                    if (isFavorite && product != null && viewModel != null && context != null) {
                        viewModel.addToFavorites(product.id, context)
                    }
                },
                modifier = Modifier
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