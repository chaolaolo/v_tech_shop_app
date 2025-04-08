package com.datn.viettech_md_12.component.item

import android.content.Context
import android.content.Intent
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.datn.viettech_md_12.R
import com.datn.viettech_md_12.data.model.ProductByCateModel
import com.datn.viettech_md_12.data.model.ProductModel
import com.datn.viettech_md_12.screen.authentication.LoginScreen
import com.datn.viettech_md_12.screen.authentication.RegisterScreen
import com.datn.viettech_md_12.viewmodel.ProductViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CustomItemProducts(
    product: ProductModel,
    viewModel: ProductViewModel,
    context: Context,
    onClick: () -> Unit
) {
    CustomItemProductsBase(
        product = product,
        viewModel = viewModel,
        context = context,
        onClick = onClick
    )
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
    viewModel: ProductViewModel? = null,
    context: Context? = null,
    onClick: () -> Unit,
) {
    val imageUrl = product?.productThumbnail ?: productByCateModel?.productThumbnail
    val name = product?.productName ?: productByCateModel?.productName
    val price = product?.productPrice ?: productByCateModel?.productPrice
    val itemPriceFormatted = NumberFormat.getNumberInstance(Locale("vi", "VN")).format(price)

    var isFavorite by remember { mutableStateOf(false) }
    var showLoginDialog by remember { mutableStateOf(false) }

    val BASE_URL = "http://103.166.184.249:3056/"
    val sharedPreferences =
        context?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    if (product != null) {
        if (sharedPreferences != null) {
            isFavorite = sharedPreferences.getBoolean(product.id, false)
        }
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .width(160.dp)
            .height(200.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF8F8F8)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable { onClick() }
        ) {
            Column(
                modifier = Modifier.padding(top = 8.dp),
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
                        .padding(vertical = 8.dp, horizontal = 10.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.SpaceBetween,
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            name ?: "",
                            color = Color.Black,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            "$itemPriceFormatted₫",
                            color = Color(0xFFF44336),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

//                    }
                }
            }

            // Favorite button
            IconButton(
                onClick = {
                    val token = context?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                        ?.getString("accessToken", "")
                    val isLoggedIn = !token.isNullOrEmpty()

                    if (!isLoggedIn) {
                        showLoginDialog = true
                        return@IconButton
                    }

                    isFavorite = !isFavorite
                    val productId = product?.id

                    if (isFavorite) {
                        if (productId != null && context != null) {
                            viewModel?.addToFavorites(productId, context)
                        }
                    } else {
                        if (productId != null && context != null) {
                            viewModel?.removeFromFavorites(productId, context)
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

    // Custom Dialog yêu cầu đăng nhập
    if (showLoginDialog) {
        Dialog(onDismissRequest = { showLoginDialog = false }) {
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.width(300.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Bạn cần đăng nhập",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Vui lòng đăng nhập hoặc tạo tài khoản để thực hiện hành động này.",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = {
                            showLoginDialog = false
                            val intent = Intent(context, RegisterScreen::class.java)
                            context?.startActivity(intent)
                        }) {
                            Text("Tạo tài khoản mới")
                        }
                        TextButton(onClick = {
                            showLoginDialog = false
                            val intent = Intent(context, LoginScreen::class.java)
                            context?.startActivity(intent)
                        }) {
                            Text("Đăng nhập")
                        }
                    }
                }
            }
        }
    }
}