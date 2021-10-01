package com.example.kotlin_server_app

import android.net.Uri
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

// 파일이나 데이터를  MultipartBody 로 변경
object FormData {
    fun getfileBody(key: String, file: File): MultipartBody.Part {
        return MultipartBody.Part.createFormData(
            name = key,
            filename = file.name,
            body = file.asRequestBody("*/*".toMediaType())
        )
    }
/*
    fun writeReview(id: Long, reviewImage: File, reviewType: String) {

        val formFile = FormDataUtil.getImageBody("media", reviewImage)
        val formType = FormDataUtil.getImageBody("mediaType", reviewType)

        viewModelScope.launch {
            reviewApi.writeReview(
                formFile,
                formType
            ).apply {
                if (this.resultCode == SUCCESS) {
                    // Todo 성공했을 때 처리
                }
            }
        }
    }
 */
}