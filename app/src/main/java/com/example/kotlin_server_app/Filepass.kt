package com.example.kotlin_server_app

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.provider.OpenableColumns
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
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.nio.channels.FileChannel
import java.util.concurrent.TimeUnit


class Filepass : AppCompatActivity() {

    val TAG:String = "Filepass"
    //var myApplication: MyApplication = MyApplication()
    var pfd: ParcelFileDescriptor? = null
    var fileInputStream: FileInputStream? = null
    var mOutputDir = "/storage/emulated/0/Download/NewTextFile.txt"
/*
    fun saveFile(uri: Uri){
        val fileName = getFileName(uri)
        try {
            pfd = uri.let { contentResolver.openFileDescriptor(it, "r") }
            fileInputStream = FileInputStream(pfd?.fileDescriptor)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        var newFile: File? = null
        if(fileName!=null) {
            newFile = File(mOutputDir, fileName)
        }

        var inChannel: FileChannel? = null
        var outChannel: FileChannel? = null

        try {
            inChannel = fileInputStream?.channel
            outChannel = FileOutputStream(newFile).channel
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        try {
            inChannel?.transferTo(0, inChannel.size(), outChannel)
        } finally {
            inChannel?.close()
            outChannel?.close()
            //Util.showNotification("성공적으로 불러왔습니다.")
            fileInputStream?.close()
            pfd?.close()
        }
    }

    fun getFileName(uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor: Cursor? = applicationContext?.contentResolver?.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } finally {
                cursor?.close()
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result!!.lastIndexOf('/')
            if (cut != -1) {
                result = result.substring(cut + 1)
            }
        }
        return result
    }

    fun dumpImageMetaData(uri: Uri) { // 파일 선택 후 파일 정보 띄우기
        val cursor: Cursor? = contentResolver.query( uri, null, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val displayName: String =
                        it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))  // 1
                Log.i(TAG, "Display Name: $displayName")

                val sizeIndex: Int = it.getColumnIndex(OpenableColumns.SIZE)  // 2
                val size: String = if (!it.isNull(sizeIndex)) {
                    it.getString(sizeIndex)
                } else {
                    "Unknown"
                }
                Log.i(TAG, "Size: $size")
            }
        }
    }

    fun filethrow(uri: Uri) // 파일 전송 함수
    {
        val intent = Intent(this,ResultActivity::class.java) // 회원가입 후 로그인 페이지로 이동
        val clientBuilder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        clientBuilder.addInterceptor(loggingInterceptor)

        val expath = Environment.getExternalStorageDirectory().getAbsolutePath();
        // 외부저장소 위치 확인
        println("경로 : "+expath)

        var retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.141.1:8000")
                .addConverterFactory(GsonConverterFactory.create())
                .client(clientBuilder.build())
                .build()

        var fileuploadservice = retrofit.create(Uploadfile::class.java)

    }
*/
    //////////////////////////////////////////////////////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loginafter)

        val ustoken = intent.getStringExtra("token") // 가져온 토큰
        var imgVwSelected: ImageView


        homebutton.setOnClickListener {
            val homeintent = Intent(this, MainActivity::class.java)
            startActivity(homeintent) // home으로 이동
        }

        uploadButton.setOnClickListener { // 안드 SAF 이용하는 부분 // 파일 확인
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply{
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "*/*"
            }

            startActivityForResult(intent, 11)
        }



        filepassbutton1.setOnClickListener { // 파일 전송 버튼
            val intent = Intent(this,ResultActivity::class.java)
            val intent2 = Intent(this,ResultfailActivity::class.java)
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

            val File = getFilesDir();
            val getFile = File.getPath();
            println("internalfilepath : "+ File)
            println("internalfilepath : "+ getFile)


            var dialog1 = AlertDialog.Builder(this@Filepass)
            dialog1.setTitle("여기까지 오케이1")
            dialog1.setMessage("데이터 전송중1")
            dialog1.show()

            var fileuploadservice = retrofit.create(Uploadfile::class.java)


            // val files = Environment.DIRECTORY_DOWNLOADS
            val file1 = File("/data/user/0/com.example.kotlin_server_app/files/NewTextFile.txt")
            val file2 = File("/data/user/0/com.example.kotlin_server_app/files/BSW_200629_new.mat")
            val file3 = File("/data/user/0/com.example.kotlin_server_app/files/BSW_200701.mat")
            val filepath = "/storage/emulated/0/Download/NewTextFile.txt"
            val file = File("/storage/emulated/0/Download/NewTextFile.txt") // 파일의 경로 설정


            var dialog2 = AlertDialog.Builder(this@Filepass)
            dialog2.setTitle("여기까지 오케이2")
            dialog2.setMessage("데이터 전송중2")
            dialog2.show()



            val requestFile = RequestBody.create("*/*".toMediaTypeOrNull(), file2)
            val requestFile1 = RequestBody.create("text/plain".toMediaTypeOrNull(), ustoken.toString())

            val body1 = MultipartBody.Part.createFormData("EEG", file2.name, requestFile)
            val expath = Environment.getExternalStorageDirectory().getAbsolutePath();

            File(Environment.getExternalStorageDirectory().absolutePath+"/Download/NewTextFile.txt")



            println("경로 : "+expath)

            fileuploadservice.request(requestFile1,body1).enqueue(object : Callback<Upfile> {
                override fun onResponse(call: Call<Upfile>, response: Response<Upfile>) {
                    var dialog = AlertDialog.Builder(this@Filepass)

                    if (response.body()?.code == "saveok") {
                        dialog.setTitle("==인증중==")
                        dialog.setMessage("인증 성공!!")
                        dialog.show()
                        intent.putExtra("token", ustoken) // 유저 데이터 계속 전달
                        startActivityForResult(intent, 1) // 인증 성공시 성공 페이지로 이동
                    } else {
                        println("인증 실패")
                        intent2.putExtra("token", ustoken) // 유저 데이터 계속 전달
                        startActivityForResult(intent2, 1) // 인증 실패시 실패 페이지로 이동
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
    }

    /*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            11 -> if (resultCode == AppCompatActivity.RESULT_OK && data != null) {
                data.data?.also { uri ->
                    saveFile(uri)
                    Log.i(TAG,"okkk")
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }
    */
}