package com.datn.viettech_md_12.component.checkout

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.datn.viettech_md_12.data.model.CartMode
import com.datn.viettech_md_12.data.model.CartModel

@Composable
fun CheckoutItemTile(
    item: CartMode,
    onQuantityChange: (Int, Int) -> Unit,
    onDelete: (Int) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(item.imageUrl),
            contentDescription = null,
            modifier = Modifier
                .size(80.dp)
                .background(Color(0xFFF4FDFA))
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(item.name, fontSize = 12.sp, fontWeight = FontWeight.W600, maxLines = 2, overflow = TextOverflow.Ellipsis, lineHeight = 12.sp)
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column() {
                    Text("VND ${item.price}", fontSize = 10.sp, fontWeight = FontWeight.W500, lineHeight = 1.sp)
                    Text("VND ${item.originalPrice}", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.W500, textDecoration = TextDecoration.LineThrough, lineHeight = 1.sp)
                }
                Row(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            brush = SolidColor(Color(0xFFF4F5FD)),
                            shape = RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 4.dp, vertical = 2.dp),
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
        }
//        Column(
//            modifier = Modifier
//                .fillMaxHeight(),
//            verticalArrangement = Arrangement.Top,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            IconButton(
//                onClick = { onDelete(item.id) }) {
//                Icon(
//                    Icons.Default.DeleteOutline,
//                    contentDescription = "Delete",
//                    tint = Color.Red,
//                    modifier = Modifier.size(30.dp),
//                )
//            }
//        }
    }
}