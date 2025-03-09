package com.datn.viettech_md_12.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    title: String,
    iconLogo: Int,
    icon1: Int?,
    icon2: Int,
    navController: NavController,
    actionTitle1: String,
    actionTitle2: String
) {
    SmallTopAppBar(
        title = {
            Box(
                modifier = Modifier
                    .fillMaxHeight(),
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
            IconButton(onClick = {
                navController.navigate(actionTitle1)
            }) {
                icon1?.let { painterResource(it) }
                    ?.let { Icon(painter = it, contentDescription = "search") }
            }
            IconButton(onClick = {
                if (actionTitle2 == "back") {
                    navController.popBackStack()
                } else {
                    navController.navigate(actionTitle2)
                }
            }) {
                Icon(painter = painterResource(icon2), contentDescription = "more")
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


//@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_7)
//@Composable
//fun PreviewTopAppBar() {
//    CustomTopAppBar(
//        "Hehe",
//        R.drawable.ic_home_page,
//        R.drawable.ic_home_page,
//        R.drawable.ic_home_page
//    )
//}