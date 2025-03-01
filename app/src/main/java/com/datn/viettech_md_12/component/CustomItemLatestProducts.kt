package com.datn.viettech_md_12.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.datn.viettech_md_12.R

@Composable
fun ItemType1(image: Int, colorHexList: List<String>) {
    fun parseColor(hex: String): Color {
        return Color(android.graphics.Color.parseColor("#$hex"))
    }

    var selectedColor by remember { mutableStateOf<String?>(null) }
    var colors by remember { mutableStateOf(colorHexList.map { it to parseColor(it) }) }

    Card(
        shape = MaterialTheme.shapes.large,
        modifier = Modifier
            .width(183.dp)
            .height(260.dp)
            .clickable {},
        colors = CardColors(
            containerColor = Color.White,
            contentColor = Color.White,
            disabledContainerColor = Color.White,
            disabledContentColor = Color.White
        )
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(158.dp)
                    .clip(MaterialTheme.shapes.extraLarge)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(image),
                    contentDescription = "Profile image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                colors.forEachIndexed { index, (colorHex, color) ->
                    Box(
                        modifier = Modifier
                            .offset(x = (-6 * index).dp)
                            .size(27.dp)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = if (selectedColor == colorHex) 2.dp else 0.dp,
                                color = Color(0xFF1F8BDA),
                                shape = CircleShape
                            )
                            .zIndex(colors.size - index.toFloat())
                            .clickable {
                                selectedColor =
                                    if (selectedColor == colorHex) null else colorHex
                                colors = colors.sortedByDescending { it.first == selectedColor }
                            }
                    )
                }
                Text(
                    "All ${colors.size} colors",
                    color = Color(0xFF1C1B1B),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Column(
                modifier = Modifier
                    .padding(18.dp, 0.dp, 0.dp, 0.dp)
                    .fillMaxHeight()
            ) {
                Text(
                    "Nike air jordan retro fashion",
                    color = Color(0xFF1C1B1B),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    "$126.00",
                    color = Color(0xFF1C1B1B),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "$186.00",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        textDecoration = TextDecoration.LineThrough
                    )
                )
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_7)
@Composable
fun CustomTopicItemPreview() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val myColorHexList = listOf("FF1C1B1B", "FF08E488", "FF21D4B4")

        ItemType1(image = R.drawable.banner3, colorHexList = myColorHexList)
    }
}