package com.datn.viettech_md_12.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.datn.viettech_md_12.R
import com.datn.viettech_md_12.component.CustomTopAppBar

@Composable
fun SearchScreen(navController: NavController) {
    val text = remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "VietTech",
                iconLogo = R.drawable.ic_logo,
                icon1 = null,
                icon2 = R.drawable.ic_cancel,
                navController = navController,
                actionTitle1 = "",
                actionTitle2 = "back",
            )
        },
        containerColor = Color.White,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Box(
                contentAlignment = Alignment.TopStart,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 16.dp),
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .border(1.dp, Color(0xFFF4F5FD), MaterialTheme.shapes.medium)
                        .padding(horizontal = 12.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .weight(1f)
                            .align(Alignment.CenterVertically)
                    ) {
                        BasicTextField(
                            value = text.value,
                            onValueChange = { text.value = it },
                            textStyle = LocalTextStyle.current.copy(
                                fontSize = 14.sp,
                                color = Color.Black
                            ),
                            modifier = Modifier
                                .height(56.dp)
                                .fillMaxWidth().padding(start = 0.dp, top = 12.dp)
                                .background(Color.Transparent),
                        )
                        if (text.value.isEmpty()) {
                            Text(
                                text = "Search",
                                color = Color(0xFF6F7384),
                                fontSize = 14.sp,
                                textAlign = TextAlign.Left,
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                                    .padding(start = 0.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Image(
                        painter = painterResource(id = R.drawable.ic_setting),
                        contentDescription = null,
                    )
                }
            }
        }
    }
}

