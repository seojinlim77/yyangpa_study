package com.example.kotlin_server_app

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.result_page.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.result_page)
        val TAG = "tokenlogin"
        var log_token_c : String

        val sharedPreferences = getSharedPreferences("auto_token", 0)
        val editor = sharedPreferences.edit()

        val token_s = sharedPreferences.getString("token", null)
        val user_token = sharedPreferences.getString("ustoken",null)
        println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<resultactivity : "+ user_token)
        var exit_message = AlertDialog.Builder(this@ResultActivity)

        val real_token = "Token "+user_token

        logout_button.setOnClickListener {
            var retrofit = Retrofit.Builder()
                    .baseUrl("http://10.20.89.14:8000")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            var logouttoken = retrofit.create(Logouttoken::class.java)

            logouttoken.logout(real_token).enqueue(object : Callback<Logoutt> { // 토큰 전송
                override fun onResponse(call: Call<Logoutt>, response: Response<Logoutt>) {
                    var logout_message = response.body() // 토큰 받아와서 저장
                    if(logout_message?.code == "logout_success")
                    {
                        println("logok : <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"+logout_message?.code+"<<<<<<<<<<<<<<<<<<<<<<<<<")
                        editor.clear()
                        editor.apply()
                        Toast.makeText(this@ResultActivity, "로그아웃 성공", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Logoutt>, t: Throwable) {
                    var dialog3 = AlertDialog.Builder(this@ResultActivity)
                    dialog3.setTitle("로그아웃")
                    dialog3.setMessage("실패")
                    dialog3.show()
                }
            })
        }

        result_home.setOnClickListener {
            exit_message.setTitle("정말로 종료하시겠습니까?")
            exit_message.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
                finish()
            })
            exit_message.setNegativeButton("아니오", DialogInterface.OnClickListener { dialog, which->
            })
            exit_message.show()
        }
    }
}