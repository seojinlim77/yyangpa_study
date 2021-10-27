package com.example.kotlin_server_app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_loginafter.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

class auth_fin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading_page)

        val sharedPreferences = getSharedPreferences("auto_token", 0) // 자동 로그인 토큰 파일
        val editor = sharedPreferences.edit()
        val token_s = sharedPreferences.getString("ustoken",null)
        val checktoken = "Token "+token_s

        fun showPrograss(isShow:Boolean) {
            if (isShow) loading_linear_a.visibility = View.VISIBLE
            else loading_linear_a.visibility = View.GONE
        }

        showPrograss(true)

        val clientBuilder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        clientBuilder.addInterceptor(loggingInterceptor)

        val okHttpClient = OkHttpClient.Builder() // 모델 확인 하는데 걸리는 시간에 timeout 설정 해두기 (오래 걸리기 때문)
                .connectTimeout(10, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()

        var retrofit = Retrofit.Builder()
                .baseUrl("http://172.30.1.3:8000")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        var fileuploadservice = retrofit.create(Uploadfile::class.java)

        val file2 = File("/data/user/0/com.example.kotlin_server_app/files/ECG.cvs")
        val requestFile = RequestBody.create("*/*".toMediaTypeOrNull(), file2)
        val requestFile1 = RequestBody.create("text/plain".toMediaTypeOrNull(), token_s.toString())
        val intent2 = Intent(this,ResultfailActivity::class.java)
        val intent = Intent(this,ResultActivity::class.java)
        val body1 = MultipartBody.Part.createFormData("EEG", file2.name, requestFile)

        fileuploadservice.request(requestFile1,body1).enqueue(object : Callback<Upfile> {
            override fun onResponse(call: Call<Upfile>, response: Response<Upfile>) {
                var dialog = AlertDialog.Builder(this@auth_fin)
                println("result <<<<<<<<<<<<<<<<<<<<<<<<"+response.body()?.code)
                if (response.body()?.code == "authok") { // 인증 성공
                    showPrograss(false)
                    startActivity(intent) // 인증 성공시 성공 페이지로 이동
                    finish()
                } else {
                    println("인증 실패")
                    startActivity(intent2) // 인증 실패시 실패 페이지로 이동
                    finish()
                }
            }

            override fun onFailure(call: Call<Upfile>, t: Throwable) {
                var dialog = AlertDialog.Builder(this@auth_fin)
                dialog.setTitle("전송실패!")
                dialog.setMessage("다시 시도 하시오")
                dialog.show()

            }
        })

    }
}