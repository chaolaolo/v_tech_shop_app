package com.datn.viettech_md_12.presentations.screens.cart

import MyButton
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.datn.viettech_md_12.R
import com.datn.viettech_md_12.data.model.CartModel
import com.datn.viettech_md_12.presentations.components.MyTextField

class CartScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CartUI()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CartUI() {
    var cartItems = remember {
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
    val selectedItems = remember { mutableStateListOf<Int>() }
    val isShowVoucherSheet = remember { mutableStateOf(false) }
    var voucherCode = remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .systemBarsPadding(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Giỏ Hàng") },
                backgroundColor = Color.White,
                navigationIcon = {
                    IconButton(onClick = { /* nút back */ }) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(
                        onClick = { isShowVoucherSheet.value = true },
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Text(
                            text = "Mã giảm giá",
                            color = Color(0xFF00C2A8),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                        )
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
                items(cartItems) { item ->
                    CartItemTile(
                        item,
                        selectedItems.contains(item.id),
                        onSelectionChange = { selected ->
                            if (selected) {
                                if (!selectedItems.contains(item.id)) {
                                    selectedItems.add(item.id)
                                }
                            } else {
                                selectedItems.remove(item.id)
                            }
                        },
                        onQuantityChange = { id, newQuantity ->
                            val index = cartItems.indexOfFirst { it.id == id }
                            if (index != -1) {
                                cartItems[index] = cartItems[index].copy(
                                    quantity = newQuantity,
                                )
                            }
                        }, onDelete = { id ->
                            selectedItems.remove(id)
                            cartItems.removeAll() { it.id == id }
                        })
                }
            }


            //Hiện EmptyCart nếu không có item nào
            if (cartItems.size == 0) {
                EmptyCart()
            }

            //
            val selectedCartItems = cartItems.filter { selectedItems.contains(it.id) }
            OrderSummary(selectedCartItems)

        }//end column

        if (isShowVoucherSheet.value) {
            ModalBottomSheet(
                onDismissRequest = { isShowVoucherSheet.value = false }, // Đóng
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                containerColor = Color.White,
                contentColor = Color.Black,
                tonalElevation = 8.dp,
                scrimColor = Color.Black.copy(alpha = 0.5f),

                ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                ) {
                    Text(
                        "Voucher Code sheet",
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(Modifier.height(10.dp))
                    MyTextField(
                        hint = "Nhập mã giảm giá",
                        value = voucherCode.value,
                        onValueChange = { voucherCode.value = it },
//                    modifier = TODO(),
                        isPassword = false
                    )
                    Spacer(Modifier.height(10.dp))
                    MyButton(
                        text = "Áp dụng",
                        onClick = { /*TODO()*/ },
                        modifier = Modifier,
                        backgroundColor = Color.Black,
                        textColor = Color.White,
                    )
                    Spacer(Modifier.height(20.dp))
                }
            }
        }
    }//end scaffold



}// end cart UI

@Composable
fun OrderSummary(selectedItems: List<CartModel>) {
    val subtotal = selectedItems.sumOf { it.price * it.quantity }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Thông tin đặt hàng", fontWeight = FontWeight.W500, fontSize = 18.sp)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Tổng giá tiền", color = Color.Gray)
            Text("VND ${"%.2f".format(subtotal)}")
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Phí vận chuyển", color = Color.Gray)
            Text("VND 0.00")
        }
        Divider(modifier = Modifier.padding(vertical = 8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Tổng thanh toán", fontSize = 20.sp, fontWeight = FontWeight.W500)
            Text("VND ${"%.2f".format(subtotal)}", fontSize = 20.sp, fontWeight = FontWeight.W500)
        }
        Spacer(Modifier.height(5.dp))
        MyButton(
            text = "Thanh Toán(${selectedItems.size})",
            onClick = { },
            backgroundColor = Color.Black,
            textColor = Color.White,
        )
    }
}//end order summary


@Composable
fun CartItemTile(
    item: CartModel,
    isSelected: Boolean,
    onSelectionChange: (Boolean) -> Unit,
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
            Text(item.name, fontWeight = FontWeight.W500)
            Spacer(Modifier.height(4.dp))
            Text("VND ${item.price}", fontSize = 14.sp, fontWeight = FontWeight.W400)
            Text("VND ${item.originalPrice}", fontSize = 14.sp, color = Color.Gray, textDecoration = TextDecoration.LineThrough)
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
                Text("${item.quantity}", modifier = Modifier.padding(horizontal = 14.dp))
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
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Checkbox(
                checked = isSelected,
                onCheckedChange = { onSelectionChange(it) },
//                colors = TODO()
            )

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


@Composable
fun EmptyCart() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(20.dp)
            .systemBarsPadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //ảnh giỏ hàng
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .clip(shape = RoundedCornerShape(16.dp))
                .background(color = Color(0xFFF4FDFA)),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                modifier = Modifier
                    .size(200.dp),
                painter = painterResource(R.drawable.google_logo),
                contentDescription = "empty cart image"
            )
        }
        Spacer(Modifier.height(40.dp))
        //Text
        Text(
            "Giỏ hàng đang trống",
            color = Color.Black,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(20.dp))
        Text(
            "Có vẻ như bạn chưa thêm bất kỳ sản phẩm nào vào giỏ hàng. Hãy tiếp tục và khám phá các danh mục hàng đầu.",
            color = Color.Gray,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(30.dp))
        //Button
        MyButton(
            text = "Khám phá",
            onClick = { },
            modifier = Modifier,
            backgroundColor = Color.Black,
            textColor = Color.White,
        )
    }
}// end empty cart

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showSystemUi = true)
@Composable
fun CartPreview() {
    CartUI()
}