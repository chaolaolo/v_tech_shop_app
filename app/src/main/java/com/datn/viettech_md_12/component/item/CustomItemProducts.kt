package com.datn.viettech_md_12.component.item

import android.content.Context
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.datn.viettech_md_12.data.model.ProductByCateModel
import com.datn.viettech_md_12.data.model.ProductModel
import com.datn.viettech_md_12.viewmodel.ProductViewModel

@Composable
fun CustomItemProducts(product: ProductModel, onClick: () -> Unit) {
    CustomItemProductsBase(product = product, onClick = onClick)
}

@Composable
fun CustomItemProductsByCate(productByCateModel: ProductByCateModel, onClick: () -> Unit) {
    CustomItemProductsBase(
        productByCateModel = productByCateModel,
        onClick = onClick
    )
}

@Composable
fun CustomItemProductsBase(
    product: ProductModel? = null,
    productByCateModel: ProductByCateModel? = null,
    onClick: () -> Unit,
) {
    val imageUrl = product?.productThumbnail ?: productByCateModel?.productThumbnail
    val name = product?.productName ?: productByCateModel?.productName
    val price = product?.productPrice ?: productByCateModel?.productPrice
    val BASE_URL = "http://103.166.184.249:3056/"

    Card(
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .width(160.dp)
            .height(200.dp)
            .clickable {},
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF8F8F8)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable { onClick() }) {
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
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, shape = RoundedCornerShape(5.dp))
                        .padding(8.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                name ?: "",
                                color = Color.Black,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Text(
                                "${price ?: 0.0}VND",
                                color = Color(0xFFF44336),
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                    }
                }
            }
        }
    }
}