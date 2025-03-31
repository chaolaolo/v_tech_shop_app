package com.datn.viettech_md_12.screen

import FavoriteItem
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.datn.viettech_md_12.R
import com.datn.viettech_md_12.data.model.ProductModel
import com.datn.viettech_md_12.viewmodel.ProductViewModel

@Composable
fun WishlistScreen(viewModel: ProductViewModel) {
    val favoriteProducts by viewModel.favoriteProducts.collectAsState(initial = emptyList())
    Log.d("dcm_list_fav", "WishlistScreen - Số lượng sản phẩm: ${favoriteProducts.size}")
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.getFavoriteProducts(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xfff4f5fd))
    ) {
        LazyColumn {
            if (favoriteProducts.isNotEmpty()) {
                items(favoriteProducts) { product ->
                    ItemFavorite(product)
                    Log.d("dcm_list_fav", "WishlistScreen - Product: $product")
                }
            } else {
                item {
                  EmptyWishList()
                }
            }
        }
    }

}
@Composable
fun ItemFavorite(favoriteItem: FavoriteItem) {
    val product = favoriteItem.product
    val BASE_URL = "http://103.166.184.249:3056/"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clip(RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = "$BASE_URL${product.product_thumbnail}",
                contentDescription = null,
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.product_name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${product.product_price}đ",
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
//            Button(
//                onClick = { /* Xóa khỏi danh sách yêu thích */ },
//                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
//                modifier = Modifier.clip(RoundedCornerShape(8.dp))
//            ) {
//                Text("Xóa", color = Color.White)
//            }
//
        }
    }
}

@Composable
fun EmptyWishList() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.img_empty_wishlist),
            contentDescription = null,
            modifier = Modifier.size(200.dp)
        )
        Text(
            text = "Danh sách yêu thích của bạn đang trống",
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = Color.Black,
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Nhấn vào nút trái tim để bắt đầu lưu các mục yêu thích của bạn.",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {

                },
                modifier = Modifier
                    .width(380.dp)
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Khám phá danh mục", color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_7)
@Composable
fun PreviewWishList() {
//    EmptyWishList()
}