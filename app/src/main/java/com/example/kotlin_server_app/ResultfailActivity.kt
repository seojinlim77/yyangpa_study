package com.example.kotlin_server_app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.result_page.*
import kotlinx.android.synthetic.main.resultfail_page.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ResultfailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.resultfail_page)
        val TAG = "tokenlogin"
        var log_token_c : String

        /////////////////////////////////////////////// 가져온 정보 확인
        var ustoken = intent.getStringExtra("ustoken")
        val message2 = "Token "+ ustoken
        //println("resultactivity : userid : "+message2)
        ///////////////////////////////////////////////

        logout_button2.setOnClickListener {
            var retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.0.8:8000")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            //var logoutService = retrofit.create(LogoutService::class.java)
            var logouttoken = retrofit.create(Logouttoken::class.java)

            //var checksign = "givemetoken"
            //val logout_b = Intent(this, ::class.java)

            logouttoken.logout(message2).enqueue(object : Callback<Logoutt> { // 토큰 전송
                override fun onResponse(call: Call<Logoutt>, response: Response<Logoutt>) {
                    var dialog2 = AlertDialog.Builder(this@ResultfailActivity)
                    dialog2.setTitle("로그아웃")
                    dialog2.setMessage("성공")
                    dialog2.show()
                }

                override fun onFailure(call: Call<Logoutt>, t: Throwable) {
                    var dialog3 = AlertDialog.Builder(this@ResultfailActivity)
                    dialog3.setTitle("로그아웃")
                    dialog3.setMessage("실패")
                    dialog3.show()
                }
            })
        }

        result_home2.setOnClickListener {
            val home_button = Intent(this, Filepass::class.java)
            //startActivity(home_button) // home 으로 이동
            finish()
        }
    }
}