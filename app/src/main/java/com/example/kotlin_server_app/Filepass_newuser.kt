package com.example.kotlin_server_app

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.os.SystemClock.sleep
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_loginafter.*
import kotlinx.android.synthetic.main.activity_main.*
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
import java.io.FileInputStream
import okhttp3.*
import java.util.concurrent.TimeUnit


class Filepass_newuser : AppCompatActivity() {
    private lateinit var sharedPreferencess : SharedPreferences
    private lateinit var editors : SharedPreferences.Editor

    fun showPrograss(isShow:Boolean) {
        if (isShow) loading_linear.visibility = View.VISIBLE
        else loading_linear.visibility = View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loginnew_after)

        val sharedPreferences = getSharedPreferences("auto_token", 0) // 저장된 토큰 파일
        val editor2 = sharedPreferences.edit()
        val token_s = sharedPreferences.getString("ustoken", null)
        println("모델 생성 페이지 토큰 : <<<<<<<<<<<<<<<<<<<<<<<<<<<<"+ token_s)

        val clientBuilder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        clientBuilder.addInterceptor(loggingInterceptor)

        val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()

        showPrograss(false)
        var retrofit = Retrofit.Builder()
                .baseUrl("http://223.194.46.83:25900")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            filepassbutton1.setOnClickListener { // 파일 전송 버튼
                // intent 종류
                val intent2 = Intent(this,ResultfailActivity::class.java)
                val auth_page = Intent(this,Filepass::class.java) // 인증 페이지로 이동

                showPrograss(true)

                //var fileuploadservice = retrofit.create(Uploadfile_new::class.java)
                val file2 = java.io.File("/data/user/0/com.example.kotlin_server_app/files/BSW_200629_new.mat")
                val requestFile = RequestBody.create("*/*".toMediaTypeOrNull(), file2)
                //val requestFile1 = RequestBody.create("text/plain".toMediaTypeOrNull(), token_s.toString()) // 형변환
                // 일단 multipart 사용하기 위해서 requestbody로 변환
                //val body1 = MultipartBody.Part.createFormData("EEG", file2.name, requestFile)

                sleep(10)
                Toast.makeText(this@Filepass_newuser, "모델 생성 성공", Toast.LENGTH_SHORT).show()

                showPrograss(false)
                startActivity(auth_page)

                /*
                fileuploadservice.request(requestFile1, body1).enqueue(object : Callback<Upfile> {
                    override fun onResponse(call: Call<Upfile>, response: Response<Upfile>) {
                        var makemodel_ok = AlertDialog.Builder(this@Filepass_newuser) //

                        // 저장되었는지 확인
                        showPrograss(false)
                        if (response.body()?.code == "saveok") {
                            makemodel_ok.setTitle("파일이 생성되었습니다. 인증 페이지로 이동합니다.")
                            makemodel_ok.setPositiveButton("네", DialogInterface.OnClickListener { dialog, which ->
                                //auth_page.putExtra("ustoken", ustoken) // 유저 데이터 계속 전달
                                //auth_page.putExtra("username",username)
                                //startActivityForResult(auth_page, 1) // 모델 생성 성공시 확인 페이지로 이동
                                startActivity(auth_page)
                            })
                            makemodel_ok.setNegativeButton("아니오", DialogInterface.OnClickListener { dialog, which->
                            })
                            makemodel_ok.show()
                            //dialog.setTitle("모델 생성 성공!")
                            //dialog.setMessage("생성 성공")
                            //dialog.show()
                            //auth_page.putExtra("ustoken", ustoken) // 유저 데이터 계속 전달
                            //auth_page.putExtra("username",username)
                            //startActivityForResult(auth_page, 1) // 모델 생성 성공시 확인 페이지로 이동
                            finish()
                        } else {
                            println("모델 생성 실패")
                        }
                    }

                    override fun onFailure(call: Call<Upfile>, t: Throwable) {
                        Log.e(TAG, "error : $t.message")
                        var dialog = AlertDialog.Builder(this@Filepass_newuser)
                        dialog.setTitle("모델 생성 실패!")
                        dialog.setMessage("다시 시도 하시오")
                        dialog.show()
                    }
                })
                 */
            }
        }

    }
