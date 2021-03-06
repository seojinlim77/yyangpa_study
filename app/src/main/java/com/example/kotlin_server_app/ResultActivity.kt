package com.example.kotlin_server_app

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.result_page.*
import kotlinx.android.synthetic.main.resultfail_page.*
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

        val ecg = findViewById<View>(R.id.success_gif) as ImageView
        Glide.with(this).asGif().load(R.raw.heart_rate).into(ecg)
        val animation = AnimationUtils.loadAnimation(this,R.anim.top)
        success_msg.startAnimation(animation)

        val sharedPreferences = getSharedPreferences("auto_token", 0)
        val editor = sharedPreferences.edit()

        val token_s = sharedPreferences.getString("token", null)
        val user_token = sharedPreferences.getString("ustoken",null)
        println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<resultactivity : "+ user_token)
        var exit_message = AlertDialog.Builder(this@ResultActivity)

        val real_token = "Token "+user_token
        val loginpage = Intent(this, MainActivity::class.java)

        logout_button.setOnClickListener {
            var retrofit = Retrofit.Builder()
                    .baseUrl("http://223.194.46.83:25900")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            var logouttoken = retrofit.create(Logouttoken::class.java)

            logouttoken.logout(real_token).enqueue(object : Callback<Logoutt> { // ?????? ??????
                override fun onResponse(call: Call<Logoutt>, response: Response<Logoutt>) {
                    var logout_message = response.body() // ?????? ???????????? ??????
                    if(logout_message?.code == "logout_success")
                    {
                        println("logok : <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"+logout_message?.code+"<<<<<<<<<<<<<<<<<<<<<<<<<")
                        editor.clear()
                        editor.apply()
                        Toast.makeText(this@ResultActivity, "???????????? ??????", Toast.LENGTH_SHORT).show()
                        startActivity(loginpage) // ????????? ???????????? ?????????
                    }
                }
                override fun onFailure(call: Call<Logoutt>, t: Throwable) {
                    var dialog3 = AlertDialog.Builder(this@ResultActivity)
                    dialog3.setTitle("????????????")
                    dialog3.setMessage("??????")
                    dialog3.show()
                }
            })
        }

        result_home.setOnClickListener {
            val measure_ten = Intent(this,Filepass::class.java)
            exit_message.setTitle("?????? ???????????? ?????????????????????????")
            exit_message.setPositiveButton("??????", DialogInterface.OnClickListener { dialog, which ->
                startActivity(measure_ten)
                finish()
            })
            exit_message.setNegativeButton("?????????", DialogInterface.OnClickListener { dialog, which->
            })
            exit_message.show()
        }
    }
}