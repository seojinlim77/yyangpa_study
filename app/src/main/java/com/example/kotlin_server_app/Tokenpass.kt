package com.example.kotlin_server_app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.logout_token_page.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Tokenpass : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.logout_token_page)

        if(intent.hasExtra("token"))
        {
            println("token is okkkk")
        }
        else{
            println("token is null....")
        }

        val message = intent.getStringExtra("token")

        println("token!!!!!!!!!!!!!!!!! " + message)

        val message2 = "Token "+message

        println("token?????????????? " + message2)

        token_pass.setOnClickListener {
            var retrofit = Retrofit.Builder()
                    .baseUrl("http://192.168.0.8:8000")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            var logouttoken = retrofit.create(Logouttoken::class.java)

            //var message = "token"
            logouttoken.logout(message2).enqueue(object : Callback<Logoutt> { // 토큰 전송
                override fun onResponse(call: Call<Logoutt>, response: Response<Logoutt>) {
                    var dialog2 = AlertDialog.Builder(this@Tokenpass)
                    dialog2.setTitle("로그아웃")
                    dialog2.setMessage("성공")
                    dialog2.show()
                }

                override fun onFailure(call: Call<Logoutt>, t: Throwable) {
                    var dialog3 = AlertDialog.Builder(this@Tokenpass)
                    dialog3.setTitle("로그아웃")
                    dialog3.setMessage("실패")
                    dialog3.show()
                }
            })
        }

        token_home.setOnClickListener {
            val home_button = Intent(this, MainActivity::class.java)
            startActivity(home_button) // home 으로 이동
        }
    }
}