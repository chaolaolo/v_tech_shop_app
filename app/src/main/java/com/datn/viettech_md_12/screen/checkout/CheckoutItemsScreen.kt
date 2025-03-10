package com.datn.viettech_md_12.screen.checkout

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.datn.viettech_md_12.data.model.CartModel

class CheckoutItemsScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CheckoutReviewItemsUI()
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CheckoutReviewItemsUI() {
    var checkoutItems = remember {
        mutableStateListOf(
            CartModel(
                1,
                "Loop Silicone Strong Magnetic Watch",
                "https://i5.walmartimages.com/seo/YuiYuKa-Magnetic-Loop-Strap-Silicone-Band-Compatible-Apple-watch-band-45mm-44mm-Ultra-49mm-40mm-41mm-38mm-42mm-Women-Men-Strong-Magnet-Closure-Bracel_c0c1391f-6af4-4b66-8cca-a77c27428b5a.db0a2fbc84d23aebf027a6dd56bab110.jpeg?odnHeight=612&odnWidth=612&odnBg=FFFFFF",
                15.25,
                20.00,
                1
            ),
            CartModel(
                2,
                "M6 Smart watch IP67 Waterproof",
                "https://gomhang.vn/wp-content/uploads/2022/01/m6-138.webp",
                12.00,
                18.00,
                1
            ),
            CartModel(
                3,
                "Loop Silicone Strong Magnetic Watch",
                "https://i5.walmartimages.com/seo/YuiYuKa-Magnetic-Loop-Strap-Silicone-Band-Compatible-Apple-watch-band-45mm-44mm-Ultra-49mm-40mm-41mm-38mm-42mm-Women-Men-Strong-Magnet-Closure-Bracel_c0c1391f-6af4-4b66-8cca-a77c27428b5a.db0a2fbc84d23aebf027a6dd56bab110.jpeg?odnHeight=612&odnWidth=612&odnBg=FFFFFF",
                15.25,
                20.00,
                1
            ),
            CartModel(
                4,
                "M6 Smart watch IP67 Waterproof",
                "https://gomhang.vn/wp-content/uploads/2022/01/m6-138.webp",
                12.00,
                18.00,
                1
            ),
        )
    }


    Scaffold(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .systemBarsPadding(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Sản phẩm") },
                backgroundColor = Color.White,
                navigationIcon = {
                    IconButton(onClick = { /* nút back */ }) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                    }
                },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .background(Color.White)
                    .padding(20.dp)
            ) {
                items(checkoutItems) { item ->
                    CheckoutItemTile(
                        item = item,
                        onQuantityChange = { id, newQuantity ->
                            val index = checkoutItems.indexOfFirst { it.id == id }
                            if (index != -1) {
                                checkoutItems[index] = checkoutItems[index].copy(
                                    quantity = newQuantity,
                                )
                            }

                        },
                        onDelete = { id ->
                            checkoutItems.removeAll() { it.id == id }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CheckoutItemTile(
    item: CartModel,
    onQuantityChange: (Int, Int) -> Unit,
    onDelete: (Int) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(item.imageUrl),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .background(Color.Transparent)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            androidx.compose.material.Text(item.name, fontWeight = FontWeight.W500)
            Spacer(Modifier.height(4.dp))
            androidx.compose.material.Text("VND ${item.price}", fontSize = 14.sp, fontWeight = FontWeight.W400)
            androidx.compose.material.Text("VND ${item.originalPrice}", fontSize = 14.sp, color = Color.Gray, textDecoration = TextDecoration.LineThrough)
            Spacer(Modifier.height(4.dp))
            Row(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        brush = SolidColor(Color(0xFFF4F5FD)),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 6.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { if (item.quantity > 1) onQuantityChange(item.id, item.quantity - 1) },
                    modifier = Modifier.size(20.dp)
                ) {
                    Icon(Icons.Default.Remove, contentDescription = "Decrease")
                }
                androidx.compose.material.Text("${item.quantity}", modifier = Modifier.padding(horizontal = 14.dp))
                IconButton(
                    onClick = { onQuantityChange(item.id, item.quantity + 1) },
                    modifier = Modifier.size(20.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Increase")
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(
                onClick = { onDelete(item.id) }) {
                Icon(
                    Icons.Default.DeleteOutline,
                    contentDescription = "Delete",
                    tint = Color.Red,
                    modifier = Modifier.size(30.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    CheckoutReviewItemsUI()
}