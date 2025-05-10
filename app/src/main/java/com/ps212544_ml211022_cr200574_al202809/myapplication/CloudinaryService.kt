package com.ps212544_ml211022_cr200574_al202809.myapplication

import com.ps212544_ml211022_cr200574_al202809.myapplication.registros.CloudinaryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface CloudinaryService {
    @Multipart
    @POST("image/upload")
    fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("upload_preset") uploadPreset: RequestBody
    ): Call<CloudinaryResponse>
}