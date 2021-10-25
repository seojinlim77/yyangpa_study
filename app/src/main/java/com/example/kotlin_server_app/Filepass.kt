package com.example.kotlin_server_app

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.os.SystemClock.sleep
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.ImageView
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
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.nio.channels.FileChannel
import java.util.concurrent.TimeUnit


class Filepass : AppCompatActivity() {

    val TAG:String = "Filepass"
    //var myApplication: MyApplication = MyApplication()
    //var pfd: ParcelFileDescriptor? = null
    //var fileInputStream: FileInputStream? = null
    //var mOutputDir = "/storage/emulated/0/Download/NewTextFile.txt"

    fun showPrograss(isShow:Boolean) {
        if (isShow) loading_linear_a.visibility = View.VISIBLE
        else loading_linear_a.visibility = View.GONE
    }

    //////////////////////////////////////////////////////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loginafter)

        showPrograss(false)

        val sharedPreferences = getSharedPreferences("auto_token", 0) // 자동 로그인 토큰 파일
        val editor = sharedPreferences.edit()
        val token_check = sharedPreferences.getString("token", null)
        val token_s = sharedPreferences.getString("ustoken",null)
        //val user_name = sharedPreferences.getString("username",null)
        val checktoken = "Token "+token_s
        val checktoken2 = "Token "+token_check

        println("????????????????????????????????????/"+token_s)

        if (token_check != null) { //자동로그인으로 들어온경우
            val makemodel = Intent(this,Filepass_newuser::class.java)

            homebutton.setOnClickListener { // 이거 없앨 예정
                var file_messages = AlertDialog.Builder(this@Filepass)
                val homeintent = Intent(this, Filepass_newuser::class.java)
                //homeintent.putExtra("username",user_name) // 유저 아이디 전송
                homeintent.putExtra("ustoken",token_check) // 토큰 전송

                file_messages.setTitle("파일이 존재하지 않음 - 모델 생성 진행 하시겠습니까?")
                file_messages.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
                    startActivity(homeintent)
                })
                file_messages.setNegativeButton("아니오",DialogInterface.OnClickListener { dialog, which->
                })
                file_messages.show()
            }


            filepassbutton1.setOnClickListener { // 파일 전송 버튼 // 인증 버튼
                // intent 종류
                val intent = Intent(this,ResultActivity::class.java)
                val intent2 = Intent(this,ResultfailActivity::class.java)
                var model_exist = AlertDialog.Builder(this@Filepass) // 다이얼로그 창

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
                        .baseUrl("http://192.168.0.8:8000")
                        .client(okHttpClient)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()

                var file_message = AlertDialog.Builder(this@Filepass)
                var filecheckservice = retrofit.create(file_check::class.java)

                // 파일 존재 확인 // 토큰으로 가능
                filecheckservice.check(checktoken2).enqueue(object : Callback<file_check_request> {
                    override fun onResponse(call: Call<file_check_request>, response: Response<file_check_request>) {
                        if(response.body()?.code == 200) // 파일이 존재한다면
                        {

                            var fileuploadservice = retrofit.create(Uploadfile::class.java)

                            val file2 = File("/data/user/0/com.example.kotlin_server_app/files/BSW_200629_new.mat")
                            val requestFile = RequestBody.create("*/*".toMediaTypeOrNull(), file2)
                            val requestFile1 = RequestBody.create("text/plain".toMediaTypeOrNull(), token_check.toString())

                            val body1 = MultipartBody.Part.createFormData("EEG", file2.name, requestFile)
                            val expath = Environment.getExternalStorageDirectory().getAbsolutePath();

                            File(Environment.getExternalStorageDirectory().absolutePath+"/Download/NewTextFile.txt")

                            fileuploadservice.request(requestFile1,body1).enqueue(object : Callback<Upfile> {
                                override fun onResponse(call: Call<Upfile>, response: Response<Upfile>) {
                                    var dialog = AlertDialog.Builder(this@Filepass)
                                    println("result <<<<<<<<<<<<<<<<<<<<<<<<"+response.body()?.code)
                                    if (response.body()?.code == "authok") { // 인증 성공

                                        showPrograss(false)
                                        //dialog.setTitle("==인증중==")
                                        //dialog.setMessage("인증 성공!!")
                                        //dialog.show()
                                        //intent.putExtra("username",user_name)
                                        //intent.putExtra("ustoken", token_check) // 유저 데이터 계속 전달
                                        startActivity(intent) // 인증 성공시 성공 페이지로 이동
                                        finish()
                                    } else {
                                        println("인증 실패")
                                        //intent2.putExtra("ustoken", token_check) // 유저 데이터 계속 전달
                                        startActivity(intent2) // 인증 실패시 실패 페이지로 이동
                                        finish()
                                    }
                                }

                                override fun onFailure(call: Call<Upfile>, t: Throwable) {
                                    Log.e(TAG,"error : $t.message")
                                    var dialog = AlertDialog.Builder(this@Filepass)
                                    dialog.setTitle("전송실패!")
                                    dialog.setMessage("다시 시도 하시오")
                                    dialog.show()

                                }
                            })

                        }
                        else if(response.body()?.code == 400)
                        {
                            showPrograss(false)
                            file_message.setTitle("파일이 존재하지 않음 - 모델 생성 진행 하시겠습니까?")
                            file_message.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
                                //makemodel.putExtra("ustoken",token_check) // 토큰 전송
                                startActivity(makemodel)
                            })
                            file_message.setNegativeButton("아니오",DialogInterface.OnClickListener { dialog, which->
                            })
                            file_message.show()
                        }
                        else
                        {
                            file_message.setTitle("걍 오류")
                            file_message.show()
                            makemodel.putExtra("ustoken",token_check) // 토큰 전송
                            startActivity(makemodel) // 모델 생성 페이지 이동
                        }
                    }
                    override fun onFailure(call: Call<file_check_request>, t: Throwable) {
                        file_message.setTitle("파일 찾기 실패!")
                        file_message.show()
                    }
                })

            }
        }
        else { // 로그인 후 들어온 경우 ( 자동로그인 x )

            val makemodels = Intent(this, Filepass_newuser::class.java)

            /*
            homebutton.setOnClickListener { // 이거 없앨 예정
                var file_messages = AlertDialog.Builder(this@Filepass)
                val homeintent = Intent(this, Filepass_newuser::class.java)
                //homeintent.putExtra("username", username)
                homeintent.putExtra("ustoken", token_s) // 토큰 전송

                file_messages.setTitle("파일이 존재하지 않음 - 모델 생성 진행 하시겠습니까?")
                file_messages.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
                    startActivity(homeintent)
                })
                file_messages.setNegativeButton("아니오", DialogInterface.OnClickListener { dialog, which ->
                })
                file_messages.show()

                //startActivity(homeintent) // home으로 이동
            }
             */


            filepassbutton1.setOnClickListener { // 파일 전송 버튼 // 인증 버튼
                // intent 종류
                val intent = Intent(this, ResultActivity::class.java)
                val intent2 = Intent(this, ResultfailActivity::class.java)

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
                        .baseUrl("http://192.168.0.8:8000")
                        .client(okHttpClient)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()

                var file_message = AlertDialog.Builder(this@Filepass)
                var filecheckservice = retrofit.create(file_check::class.java)

                // 파일 존재 확인
                filecheckservice.check(checktoken).enqueue(object : Callback<file_check_request> {
                    override fun onResponse(call: Call<file_check_request>, response: Response<file_check_request>) {
                        println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"+response.body()?.code)
                        if (response.body()?.code == 200) { // 파일 존재
                            //file_message.setTitle(" == 파일이 존재합니다. 인증을 진행합니다. == ")
                            //file_message.show()

                            val File = getFilesDir();
                            val getFile = File.getPath();
                            println("internalfilepath : " + File)
                            println("internalfilepath : " + getFile)

                            var fileuploadservice = retrofit.create(Uploadfile::class.java)

                            // val files = Environment.DIRECTORY_DOWNLOADS
                            val file2 = File("/data/user/0/com.example.kotlin_server_app/files/BSW_200629_new.mat")
                            val filepath = "/storage/emulated/0/Download/NewTextFile.txt"
                            val file = File("/storage/emulated/0/Download/NewTextFile.txt") // 파일의 경로 설정


                            val requestFile = RequestBody.create("*/*".toMediaTypeOrNull(), file2)
                            val requestFile1 = RequestBody.create("text/plain".toMediaTypeOrNull(), token_s.toString())
                            val body1 = MultipartBody.Part.createFormData("EEG", file2.name, requestFile)
                            val expath = Environment.getExternalStorageDirectory().getAbsolutePath();
                            File(Environment.getExternalStorageDirectory().absolutePath + "/Download/NewTextFile.txt")

                            println("경로 : " + expath)

                            fileuploadservice.request(requestFile1, body1).enqueue(object : Callback<Upfile> {
                                override fun onResponse(call: Call<Upfile>, response: Response<Upfile>) {
                                    var dialog = AlertDialog.Builder(this@Filepass)

                                    println("result <<<<<<<<<<<<<<<<<<<<<<<<" + response.body()?.code)
                                    if (response.body()?.code == "authok") {
                                        showPrograss(false)
                                        //intent.putExtra("ustoken", token_s) // 유저 데이터 계속 전달
                                        startActivity(intent) // 인증 성공시 성공 페이지로 이동
                                        finish()
                                    } else {
                                        println("인증 실패")
                                        //intent2.putExtra("ustoken", token_s) // 유저 데이터 계속 전달
                                        startActivity(intent2) // 인증 실패시 실패 페이지로 이동
                                        finish()
                                    }
                                }
                                override fun onFailure(call: Call<Upfile>, t: Throwable) {
                                    Log.e(TAG, "error : $t.message")
                                    var dialog = AlertDialog.Builder(this@Filepass)
                                    dialog.setTitle("전송실패!")
                                    dialog.setMessage("다시 시도 하시오")
                                    dialog.show()

                                }
                            })
                        } else if (response.body()?.code == 400) {
                            showPrograss(false)
                            file_message.setTitle("파일이 존재하지 않음 - 모델 생성 진행 하시겠습니까?")
                            file_message.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
                                //makemodels.putExtra("username", username)
                                //makemodels.putExtra("ustoken", token_s) // 토큰 전송
                                startActivity(makemodels)
                            })
                            file_message.setNegativeButton("아니오", DialogInterface.OnClickListener { dialog, which ->
                            })
                            file_message.show()

                        } else {
                            file_message.setTitle("걍 오류")
                            file_message.show()
                            makemodels.putExtra("ustoken", token_s) // 토큰 전송
                            startActivity(makemodels) // 모델 생성 페이지 이동
                            finish()
                        }
                    }
                    override fun onFailure(call: Call<file_check_request>, t: Throwable) {
                        file_message.setTitle("파일 찾기 실패!")
                        file_message.show()
                        finish()
                    }
                })

            }
        }
    }
}