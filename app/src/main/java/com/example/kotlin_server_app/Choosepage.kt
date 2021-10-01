package com.example.kotlin_server_app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.choosepage.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Choosepage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.choosepage)
        val TAG: String = "Filepass"

        var retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.0.8:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

///////////////////////////////////// new user /////////////////////////////////////////////////////
        newuser.setOnClickListener {
            var logoutService = retrofit.create(LogoutService::class.java)
            val tokenthrow = Intent(this, Filepass_newuser::class.java)

            ////////////////////////////////////////////////// 유저 데이터 계속 유지 확인
            val uid = intent.getStringExtra("userid")
            val upw = intent.getStringExtra("userpw")
            /////////////////////////////////////////////////

            logoutService.requestlogout(uid, upw).enqueue(object : Callback<Logout> {
                override fun onResponse(call: Call<Logout>, response: Response<Logout>) {
                    var dialog = AlertDialog.Builder(this@Choosepage)
                    var log_token = response.body() // 토큰 받아와서 저장
                    if (log_token?.msg != null) {
                        println(log_token?.code)
                    } else {
                        println("successok == NULL")
                    }
                    var token2 = log_token?.code
                    var token = response?.body()?.toString()
                    Log.d(TAG, "response : " + token2)
                    println("log_token : " + token2)
                    dialog.setTitle(token2)
                    dialog.setMessage(token2)
                    dialog.show()
                    tokenthrow.putExtra("token", token2)
                    startActivity(tokenthrow) // 토큰을 가지고 파일 전송 페이지 이동
                }

                override fun onFailure(call: Call<Logout>, t: Throwable) {
                    var dialog1 = AlertDialog.Builder(this@Choosepage)
                    // callback.onFailure(t)
                    Log.e("on Failure :", "retrofit error" + t)
                    Log.e("on Failure :", "retrofit error" + call)
                    dialog1.setTitle("사용자 토큰")
                    dialog1.setMessage("에러")
                    dialog1.show()
                }

            })
        }


///////////////////////////// olduser /////////////////////////////////////////////////////////////
        olduser.setOnClickListener {
            var logoutService = retrofit.create(LogoutService::class.java)
            val tokenthrow2 = Intent(this, Filepass::class.java)

            //// 조건 ////
            if (intent.getStringExtra("token") != null) {
                // 새로 사용자가 모델 등록 후 다시 파일 보낼경우
                val oldtoken = intent.getStringExtra("token")
                var dialog = AlertDialog.Builder(this@Choosepage)
                Log.d(TAG, "response : " + oldtoken)
                println("log_token : " + oldtoken)
                dialog.setTitle(oldtoken)
                dialog.setMessage(oldtoken)
                dialog.show()
                tokenthrow2.putExtra("token", oldtoken)
                startActivity(tokenthrow2) // 토큰을 가지고 파일 전송 페이지 이동
            } else { // 로그인 후 바로 들어온 경우
                ////////////////////////////////////////////////// 유저 데이터 계속 유지 확인
                val uid = intent.getStringExtra("userid")
                val upw = intent.getStringExtra("userpw")
                /////////////////////////////////////////////////

                logoutService.requestlogout(uid, upw).enqueue(object : Callback<Logout> {
                    override fun onResponse(call: Call<Logout>, response: Response<Logout>) {
                        var dialog = AlertDialog.Builder(this@Choosepage)
                        var log_token = response.body() // 토큰 받아와서 저장
                        if (log_token?.msg != null) {
                            println(log_token?.code)
                        } else {
                            println("successok == NULL")
                        }
                        var token2 = log_token?.code
                        var token = response?.body()?.toString()
                        Log.d(TAG, "response : " + token2)
                        println("log_token : " + token2)
                        dialog.setTitle(token2)
                        dialog.setMessage(token2)
                        dialog.show()
                        tokenthrow2.putExtra("token", token2)
                        startActivity(tokenthrow2) // 토큰을 가지고 파일 전송 페이지 이동
                    }

                    override fun onFailure(call: Call<Logout>, t: Throwable) {
                        var dialog1 = AlertDialog.Builder(this@Choosepage)
                        // callback.onFailure(t)
                        Log.e("on Failure :", "retrofit error" + t)
                        Log.e("on Failure :", "retrofit error" + call)
                        dialog1.setTitle("사용자 토큰")
                        dialog1.setMessage("에러")
                        dialog1.show()
                    }

                })
            }
        }
    }
}