package com.example.kotlin_server_app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.editTextTextPassword
import kotlinx.android.synthetic.main.activity_main.editTextTextPersonName
import kotlinx.android.synthetic.main.new_user_page.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class Newperson : AppCompatActivity(){ // 회원가입 페이지

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_user_page)

        val clientBuilder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        clientBuilder.addInterceptor(loggingInterceptor)

        var retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.0.8:8000")
                .addConverterFactory(GsonConverterFactory.create())
                .client(clientBuilder.build())
                .build()


        var newuserservice = retrofit.create(Newuser::class.java)

       registerbutton.setOnClickListener {
           val intent = Intent(this,MainActivity::class.java) // 회원가입 후 로그인 페이지로 이동

           var newname = editTextTextname.text.toString()
           var newId = editTextTextPersonName.text.toString()
           var newPw = editTextTextPassword.text.toString()
           var newgender = editTextTextgender.text.toString()
           var buff = editTextTextage.text.toString()
           var newage = Integer.parseInt(buff)

           newuserservice.requestLogin(newId,newPw,newname,newage,newgender).enqueue(object: Callback<New>{
               override fun onFailure(call: Call<New>, t: Throwable) {
                   // 웹 통신에 실패시 실행
                   var dialog = AlertDialog.Builder(this@Newperson)
                   dialog.setTitle("실패!")
                   dialog.setMessage("통신에 실패....")
                   dialog.show()
               }
               override fun onResponse(call: Call<New>, response: Response<New>) {
                   var NewU = response.body() // msuccess
                   var dialog = AlertDialog.Builder(this@Newperson)
                   if(NewU?.msuccess != null) // 넘어오는 값이 아무것도 없을 경우 // 애매하긴 함...
                   {
                       dialog.setTitle("경고!")
                       dialog.setMessage("response : " + NewU + " 정보를 정확시 다 작성하세요.")
                       //dialog.setMessage("정보를 정확시 다 작성하세요.")
                       dialog.show()
                   }
                   else
                   {
                       dialog.setTitle("성공!")
                       dialog.setMessage("회원가입을 축하합니다.")
                       dialog.show()
                       startActivity(intent) // 파일 페이지로 이동
                   }
               }
           })


       }

    }
}