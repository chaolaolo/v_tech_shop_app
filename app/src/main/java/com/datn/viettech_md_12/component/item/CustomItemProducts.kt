package com.datn.viettech_md_12.component.item

import android.content.Context
import android.util.Log
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
fun CustomItemProducts(product: ProductModel, viewModel: ProductViewModel, context: Context,  onClick: () -> Unit) {
    CustomItemProductsBase(product = product, viewModel = viewModel, context = context, onClick = onClick)
}

@Composable
fun CustomItemProductsByCate(productByCateModel: ProductByCateModel,  onClick: () -> Unit) {
    CustomItemProductsBase(
        productByCateModel = productByCateModel,
        onClick = onClick
    )
}

@Composable
fun CustomItemProductsBase(
    product: ProductModel? = null,
    productByCateModel: ProductByCateModel? = null,
    viewModel: ProductViewModel? = null,
    context: Context? = null,
    onClick: () -> Unit,
) {
    val imageUrl = product?.productThumbnail ?: productByCateModel?.productThumbnail
    val name = product?.productName ?: productByCateModel?.productName
    val price = product?.productPrice ?: productByCateModel?.productPrice

    var isFavorite by remember { mutableStateOf(false) }
    val BASE_URL = "http://103.166.184.249:3056/"
    val sharedPreferences =
        context?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE) //lay trang thai da luu tru
    if (product != null) {
        if (sharedPreferences != null) {
            isFavorite = sharedPreferences.getBoolean(product.id, false)
        }
    }

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
        Box(modifier = Modifier
            .fillMaxSize()
            .clickable {onClick()}) {
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
//                            if (product != null) {
//                                Text(
//                                    name ?: "",
//                                    color = Color.Black,
//                                    style = MaterialTheme.typography.bodyMedium,
//                                    fontWeight = FontWeight.Bold,
//                                    maxLines = 1,
//                                    overflow = TextOverflow.Ellipsis
//                                )
//                            }
//                            if (product != null) {
//                                Text(
//                                    "${price ?: 0.0}VND",
//                                    color = Color(0xFF4CAF50),
//                                    fontWeight = FontWeight.Bold,
//                                    style = MaterialTheme.typography.bodySmall
//                                )
//                            }
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

            IconButton(
                onClick = {
                    isFavorite = !isFavorite
                    if (isFavorite) {
//                        viewModel.addToFavorites(product.id, context)
                        val productId = product?.id
                        if (productId != null) {
                            if (context != null) {
                                viewModel?.addToFavorites(productId, context)
                            }
                        }
                    } else {
                        val favoriteId = product?.id
                        if (context != null) {
                            if (favoriteId != null) {
                                viewModel?.removeFromFavorites(favoriteId, context)
                            }
                        }
                    }
                },
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

//@Composable
//fun CustomItemProductsBase(
//    product: ProductModel? = null,
//    productByCateModel: ProductByCateModel? = null,
//    viewModel: ProductViewModel? = null,
//    context: Context? = null
//) {
//    val imageUrl = product?.productThumbnail ?: productByCateModel?.productThumbnail
//    val name = product?.productName ?: productByCateModel?.productName
//    val price = product?.productPrice ?: productByCateModel?.productPrice
//
//    var isFavorite by remember { mutableStateOf(false) }
//    val BASE_URL = "http://103.166.184.249:3056/"
//    val sharedPreferences =
//        context?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE) //lay trang thai da luu tru
//    if (product != null) {
//        if (sharedPreferences != null) {
//            isFavorite = sharedPreferences.getBoolean(product.id, false)
//        }
//    }
//    Card(
//                shape = RoundedCornerShape(16.dp),
//        modifier = Modifier
//            .width(160.dp)
//            .height(200.dp)
//            .clickable {},
//        colors = CardDefaults.cardColors(
//            containerColor = Color(0xFFF8F8F8)
//        ),
//        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
//    ) {
//        Box(modifier = Modifier.fillMaxSize()) {
//            Column(
//                modifier = Modifier.padding(8.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(120.dp)
//                        .clip(RoundedCornerShape(12.dp)),
//                    contentAlignment = Alignment.Center
//                ) {
//                    AsyncImage(
//                        model = "$BASE_URL$imageUrl",
//                        contentDescription = "Product Image",
//                        modifier = Modifier.fillMaxSize(),
//                        contentScale = ContentScale.Fit,
//                        placeholder = painterResource(R.drawable.img_test_order)
//                    )
//                }
//                Spacer(modifier = Modifier.height(8.dp))
//
//                Text(
//                    name ?: "",
//                    color = Color(0xFF1C1B1B),
//                    style = MaterialTheme.typography.bodyLarge,
//                    fontWeight = FontWeight.Bold,
//                    maxLines = 1,
//                    overflow = TextOverflow.Ellipsis
//                )
//
//                Text(
//                    "${price ?: 0.0}$",
//                    color = Color(0xFF1C1B1B),
//                    fontWeight = FontWeight.Bold,
//                    style = MaterialTheme.typography.bodyMedium
//                )
//            }
//
//            IconButton(
//                onClick = {
//                    isFavorite = !isFavorite
//                    if (isFavorite) {
////                        viewModel.addToFavorites(product.id, context)
//                        val productId = product?.id
//                        if (productId != null) {
//                            if (context != null) {
//                                viewModel?.addToFavorites(productId, context)
//                            }
//                        }
//                    } else {
//                        val favoriteId = product?.id
//                        if (context != null) {
//                            if (favoriteId != null) {
//                                viewModel?.removeFromFavorites(favoriteId, context)
//                            }
//                        }
//                    }
//                },
//                modifier = Modifier
//                    .align(Alignment.TopEnd)
//                    .padding(8.dp)
//                    .size(24.dp)
//            ) {
//                Icon(
//                    painter = painterResource(if (!isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_selected),
//                    contentDescription = "Favorite",
//                    tint = Color.Unspecified
//                )
//            }
//        }
//    }
//}

//@Composable
//fun CustomItemProducts(product: ProductModel) {
////    fun parseColor(hex: String): Color {
////        return Color(android.graphics.Color.parseColor("#$hex"))
////    }
////
////    var selectedColor by remember { mutableStateOf<String?>(null) }
////    var colors by remember { mutableStateOf(colorHexList.map { it to parseColor(it) }) }
//    var isFavorite by remember { mutableStateOf(false) }
//    val BASE_URL = "http://103.166.184.249:3056/"
//
//    Card(
//        shape = MaterialTheme.shapes.large,
//        modifier = Modifier
//            .width(183.dp)
//            .height(260.dp)
//            .clickable {},
//        colors = CardColors(
//            containerColor = Color.White,
//            contentColor = Color.White,
//            disabledContainerColor = Color.White,
//            disabledContentColor = Color.White
//        )
//    ) {
//        Box {
//            Column {
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(158.dp)
//                        .clip(MaterialTheme.shapes.extraLarge)
//                        .background(MaterialTheme.colorScheme.surfaceVariant),
//                    contentAlignment = Alignment.Center
//                ) {
//                    AsyncImage(
//                        model = "$BASE_URL${product.productThumbnail}",
//                        contentDescription = "ta_dcm",
//                        modifier = Modifier.fillMaxSize(),
//                        contentScale = ContentScale.Crop,
//                        placeholder = painterResource(R.drawable.img_test_order), // hien thi trong truong hop load
////                        error = painterResource(R.drawable.ic_launcher_foreground) // hien thi neu nhu anh loi
//                    )
//                }
//                Spacer(modifier = Modifier.height(8.dp))
////                Row(
////                    modifier = Modifier.fillMaxWidth(),
////                    verticalAlignment = Alignment.CenterVertically
////                ) {
////                    colors.forEachIndexed { index, (colorHex, color) ->
////                        Box(
////                            modifier = Modifier
////                                .offset(x = (-6 * index).dp)
////                                .size(27.dp)
////                                .clip(CircleShape)
////                                .background(color)
////                                .border(
////                                    width = if (selectedColor == colorHex) 2.dp else 0.dp,
////                                    color = Color(0xFF1F8BDA),
////                                    shape = CircleShape
////                                )
////                                .zIndex(colors.size - index.toFloat())
////                                .clickable {
////                                    selectedColor =
////                                        if (selectedColor == colorHex) null else colorHex
////                                    colors = colors.sortedByDescending { it.first == selectedColor }
////                                }
////                        )
////                    }
//////                    Text(
//////                        "All ${colors.size} colors",
//////                        color = Color(0xFF1C1B1B),
//////                        style = MaterialTheme.typography.bodyMedium
//////                    )
////                }
//                Text(
//                    product.productName,
//                    color = Color(0xFF1C1B1B),
//                    style = MaterialTheme.typography.bodyLarge,
//                    fontWeight = FontWeight.Bold,
//                    maxLines = 1,
//                    overflow = TextOverflow.Ellipsis
//                )
//
//                Text(
//                    "${product.productPrice}$",
//                    color = Color(0xFF1C1B1B),
//                    fontWeight = FontWeight.Bold,
//                    style = MaterialTheme.typography.bodyMedium
//                )
////                    Text(
////                        text = "$186.00",
////                        color = Color.Gray,
////                        style = MaterialTheme.typography.bodyMedium.copy(
////                            textDecoration = TextDecoration.LineThrough
////                        )
////                    )
//
//            }
//            IconButton(
//                onClick = { isFavorite = !isFavorite },
//                modifier = Modifier
//                    .align(Alignment.TopEnd)
//                    .padding(8.dp)
//                    .size(24.dp)
//            ) {
//                Icon(
//                    painter = painterResource(if (!isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_selected),
//                    contentDescription = "Favorite",
//                    tint = Color.Unspecified
//                )
//            }
//        }
//    }
//}

//@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_7)
//@Composable
//fun CustomTopicItemPreview() {
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        val myColorHexList = listOf("FF1C1B1B", "FF08E488", "FF21D4B4")
//
//        ItemType1(image = R.drawable.banner3, colorHexList = myColorHexList)
//    }
//}
