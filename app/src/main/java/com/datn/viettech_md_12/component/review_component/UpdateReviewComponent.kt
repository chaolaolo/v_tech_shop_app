package com.datn.viettech_md_12.component.review_component

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import com.datn.viettech_md_12.data.remote.ApiClient
import com.datn.viettech_md_12.viewmodel.ReviewViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
@Composable
fun UpdateReviewDialog(
    reviewId: String,
    productId: String,
    initialRating: Int,
    initialContent: String,
    initialImageUrls: List<String>,
    initialImageIds: List<String>,
    reviewViewModel: ReviewViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var rating by remember { mutableStateOf(initialRating) }
    var content by remember { mutableStateOf(initialContent) }

    var selectedUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var uploadedImageUrls by remember { mutableStateOf(initialImageUrls.toMutableList()) }
    var uploadedImageIds by remember { mutableStateOf(initialImageIds.toMutableList()) }

    var isUploading by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }

    val imageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
        selectedUris = if (uris.size > 3) uris.take(3) else uris
        uploadedImageUrls = mutableListOf() // Ẩn ảnh cũ nếu đã chọn mới
        if (uris.size > 3) {
            Toast.makeText(context, "Tối đa 3 ảnh!", Toast.LENGTH_SHORT).show()
        }
    }

    val updateReviewResult by reviewViewModel.updateReviewResult.collectAsState()

    LaunchedEffect(updateReviewResult) {
        updateReviewResult?.onSuccess {
            Toast.makeText(context, "Cập nhật đánh giá thành công!", Toast.LENGTH_SHORT).show()
            Log.d("UPDATE_REVIEW", "Success = ${it.success}, Data = ${it.data}")
            onDismiss()
            reviewViewModel.getReviewsByProduct(productId)
        }?.onFailure {
            Log.d("UPDATE_REVIEW", "Review failed: $it")
            Toast.makeText(context, "Cập nhật đánh giá thất bại!", Toast.LENGTH_SHORT).show()
            onDismiss()
        }
    }

    if (showConfirmDialog) {
        ConfirmDialog(
            onConfirm = {
                showConfirmDialog = false
                coroutineScope.launch {
                    if (!validateInput(context, rating, content, selectedUris, uploadedImageUrls)) return@launch
                    isUploading = true

                    val imageIds = mutableListOf<String>().apply {
                        if (selectedUris.isEmpty()) {
                            addAll(uploadedImageIds)
                        } else {
                            for (uri in selectedUris) {
                                val file = uriToFile(context, uri)
                                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                                val body = MultipartBody.Part.createFormData("image", file.name, requestFile)
                                val response = ApiClient.imageRepository.uploadImage(body)
                                if (response.isSuccessful) {
                                    response.body()?.let { add(it.image._id) }
                                        ?: run {
                                            toast(context, "Upload ảnh thất bại (null body)")
                                            isUploading = false
                                            return@launch
                                        }
                                } else {
                                    toast(context, "Upload ảnh thất bại!")
                                    isUploading = false
                                    return@launch
                                }
                            }
                        }
                    }

                    isUploading = false
                    reviewViewModel.updateReview(reviewId, content, rating, imageIds)
                }
            },
            onDismiss = { showConfirmDialog = false }
        )
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Cập nhật đánh giá", fontWeight = FontWeight.Bold, fontSize = 18.sp)

                Spacer(modifier = Modifier.height(12.dp))
                RatingStars(rating) { rating = it }

                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Nội dung đánh giá") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 4
                )

                Spacer(modifier = Modifier.height(12.dp))
                if (selectedUris.isEmpty() && uploadedImageUrls.isNotEmpty()) {
                    Text("Ảnh trước đó:", fontWeight = FontWeight.SemiBold)
                    ImagePreviewRow(uploadedImageUrls.map {
                        it.replace("http://localhost:", "http://103.166.184.249:")
                    })
                }

                Button(
                    onClick = { imageLauncher.launch("image/*") },
                    modifier = Modifier.align(Alignment.End),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
                ) {
                    Icon(Icons.Default.Image, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Chọn ảnh (tối đa 3)")
                }

                if (selectedUris.isNotEmpty()) {
                    ImagePreviewRow(selectedUris.map { it.toString() })
                }

                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Hủy")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { showConfirmDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF43A047))
                    ) {
                        if (isUploading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(Icons.Default.Send, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Cập nhật")
                        }
                    }
                }
            }
        }
    }
}

// --- Các hàm phụ trợ ---

@Composable
fun RatingStars(currentRating: Int, onRatingChanged: (Int) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        repeat(5) { index ->
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = if (index < currentRating) Color(0xFFFFD700) else Color.Gray,
                modifier = Modifier
                    .size(32.dp)
                    .clickable { onRatingChanged(index + 1) }
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text("$currentRating sao")
    }
}

@Composable
fun ImagePreviewRow(imageUris: List<String>) {
    LazyRow(modifier = Modifier.padding(top = 8.dp)) {
        items(imageUris.size) { index ->
            Image(
                painter = rememberAsyncImagePainter(imageUris[index]),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .padding(end = 8.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}

fun validateInput(
    context: Context,
    rating: Int,
    content: String,
    selectedUris: List<Uri>,
    uploadedUrls: List<String>
): Boolean {
    val validContentRegex = Regex("^[\\p{L}\\p{N}\\s.,!?\\-()\"']+")
    return when {
        rating == 0 -> {
            toast(context, "Vui lòng chọn số sao"); false
        }

        content.isBlank() -> {
            toast(context, "Vui lòng nhập nội dung đánh giá"); false
        }

        content.length > 500 -> {
            toast(context, "Nội dung vượt quá 500 ký tự"); false
        }

        !validContentRegex.matches(content.trim()) -> {
            toast(context, "Không dùng ký tự đặc biệt"); false
        }

        selectedUris.isEmpty() && uploadedUrls.isEmpty() -> {
            toast(context, "Chọn ít nhất một ảnh"); false
        }

        else -> true
    }
}
