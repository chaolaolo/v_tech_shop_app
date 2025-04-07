package com.datn.viettech_md_12.data.repository
import com.datn.viettech_md_12.data.interfaces.ImageService
import okhttp3.MultipartBody

class ImageRepository(private val imageService: ImageService) {

    suspend fun uploadImage(image: MultipartBody.Part) =
        imageService.uploadImage(image)
}
