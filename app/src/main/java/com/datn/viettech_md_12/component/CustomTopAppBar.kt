package com.datn.viettech_md_12.component

//class CustomTopAppBar {
//}

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.datn.viettech_md_12.R
import com.datn.viettech_md_12.navigation.NavigationGraph

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    title: String,
    iconLogo: Int,
    iconSearch: Int,
    iconProfile: Int

) {
    SmallTopAppBar(
        title = {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .clickable { /* Xử lý khi click vào cả tiêu đề và icon */ },
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(painter = painterResource(iconLogo), contentDescription = "menu")
                    Text(text = title, fontWeight = FontWeight.Bold)
                }
            }
        },
        actions = {
            IconButton(onClick = { /* Xử lý khi click vào search */ }) {
                Icon(painter = painterResource(iconSearch), contentDescription = "search")
            }
            IconButton(onClick = { /* Xử lý khi click vào more */ }) {
                Icon(painter = painterResource(iconProfile), contentDescription = "more")
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = Color(0xFF309A5F),
            navigationIconContentColor = Color(0xFF309A5F),
            actionIconContentColor = Color(0xFF1C1B1B),
        )
    )
}


@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_7)
@Composable
fun PreviewTopAppBar() {
    CustomTopAppBar(
        "Hehe",
        R.drawable.ic_home_page,
        R.drawable.ic_home_page,
        R.drawable.ic_home_page
    )
}