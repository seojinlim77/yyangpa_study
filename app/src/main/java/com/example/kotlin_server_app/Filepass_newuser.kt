package com.example.kotlin_server_app

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.util.Log
import android.widget.ImageView
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
import java.io.FileInputStream
import okhttp3.*
import java.util.concurrent.TimeUnit


class Filepass_newuser : AppCompatActivity() {
    val TAG: String = "Filepass"


    //var myApplication: MyApplication = MyApplication()
    var pfd: ParcelFileDescriptor? = null
    var fileInputStream: FileInputStream? = null
    var mOutputDir = "/storage/emulated/0/Download/NewTextFile.txt"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loginnew_after)

        val ustoken = intent.getStringExtra("token")
        var imgVwSelected: ImageView
        println("token!!!!!!!!!!!!!!!!! " + ustoken)
        println("str1: $ustoken lenghth: ${ustoken?.length}")

        val extoken = "dcb457caacdcfe85db16143b2187dad56a4e89f6"
        /*
        tokencheck.setOnClickListener {
            var retrofit = Retrofit.Builder()
                    .baseUrl("http://192.168.0.8:8000")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            var tokencheck = retrofit.create(exxxx::class.java)

            tokencheck.ex(ustoken).enqueue(object : Callback<ex_data> {
                override fun onResponse(call: Call<ex_data>, response: Response<ex_data>) {
                    var dialog = AlertDialog.Builder(this@Filepass_newuser)
                }

                override fun onFailure(call: Call<ex_data>, t: Throwable) {
                    Log.e(TAG, "error : $t.message")
                    var dialog = AlertDialog.Builder(this@Filepass_newuser)
                    dialog.setTitle("전송실패!")
                    dialog.setMessage("다시 시도 하시오")
                    dialog.show()

                }
            })
*/
            homebutton.setOnClickListener { // home 버튼
                val homeintent = Intent(this, MainActivity::class.java)
                startActivity(homeintent) // home으로 이동
            }

            filepassbutton1.setOnClickListener { // 파일 전송 버튼
                val intent = Intent(this, newusermodel_activity::class.java)
                val intent2 = Intent(this,ResultfailActivity::class.java)
                val clientBuilder = OkHttpClient.Builder()
                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                clientBuilder.addInterceptor(loggingInterceptor)

                val okHttpClient = OkHttpClient.Builder()
                        .connectTimeout(10, TimeUnit.MINUTES)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .writeTimeout(30, TimeUnit.SECONDS)
                        .build()

                var retrofit = Retrofit.Builder()
                        .baseUrl("http://192.168.0.8:8000")
                        .client(okHttpClient)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()

                val File = getFilesDir();
                val getFile = File.getPath();
                println("internalfilepath : " + File)
                println("internalfilepath : " + getFile)


                var dialog1 = AlertDialog.Builder(this@Filepass_newuser)
                dialog1.setTitle("여기까지 오케이1")
                dialog1.setMessage("데이터 전송중1")
                dialog1.show()

                var fileuploadservice = retrofit.create(Uploadfile_new::class.java)


                // val files = Environment.DIRECTORY_DOWNLOADS
                val file1 =
                        java.io.File("/data/user/0/com.example.kotlin_server_app/files/NewTextFile.txt")
                val file2 =
                        java.io.File("/data/user/0/com.example.kotlin_server_app/files/BSW_200629_new.mat")
                val file3 =
                        java.io.File("/data/user/0/com.example.kotlin_server_app/files/BSW_200701.mat")
                val filepath = "/storage/emulated/0/Download/NewTextFile.txt"
                val file = java.io.File("/storage/emulated/0/Download/NewTextFile.txt") // 파일의 경로 설정


                var dialog2 = AlertDialog.Builder(this@Filepass_newuser)
                dialog2.setTitle("데이터-전송")
                dialog2.setMessage("데이터 전송중2")
                dialog2.show()


                val requestFile = RequestBody.create("*/*".toMediaTypeOrNull(), file2)
                val requestFile1 = RequestBody.create("text/plain".toMediaTypeOrNull(), ustoken.toString())
                // 일단 multipart 사용하기 위해서 requestbody로 변환

                val body1 = MultipartBody.Part.createFormData("EEG", file2.name, requestFile)
                val expath = Environment.getExternalStorageDirectory().getAbsolutePath();

                //val usstoken = MultipartBody.Part.createFormData("token",extoken,requestFile1)
                java.io.File(Environment.getExternalStorageDirectory().absolutePath + "/Download/NewTextFile.txt")

                //val titlePart: MultipartBody.Part = createFormData.createFormData("title", title)



                println("경로 : " + expath)

                fileuploadservice.request(requestFile1, body1).enqueue(object : Callback<Upfile> {
                    override fun onResponse(call: Call<Upfile>, response: Response<Upfile>) {
                        var dialog = AlertDialog.Builder(this@Filepass_newuser)
                        //var log_token = response.body() // 토큰 받아와서 저장
                        // 저장되었는지 확인
                        if (response.body()?.code == "saveok") {
                            dialog.setTitle("전송성공!")
                            dialog.setMessage("데이터 전송")
                            dialog.show()
                            intent.putExtra("token", ustoken) // 유저 데이터 계속 전달
                            startActivityForResult(intent, 1) // 모델 생성 성공시 확인 페이지로 이동
                        } else {
                            println("인증 실패")
                            intent2.putExtra("token", ustoken) // 유저 데이터 계속 전달
                            startActivityForResult(intent2, 1) // 모델 생성 성공시 확인 페이지로 이동
                        }
                    }

                    override fun onFailure(call: Call<Upfile>, t: Throwable) {
                        Log.e(TAG, "error : $t.message")
                        var dialog = AlertDialog.Builder(this@Filepass_newuser)
                        dialog.setTitle("전송실패!")
                        dialog.setMessage("다시 시도 하시오")
                        dialog.show()

                    }
                })


            }
        }

    }
