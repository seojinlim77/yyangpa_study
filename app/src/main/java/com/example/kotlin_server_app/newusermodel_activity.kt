package com.example.kotlin_server_app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.newusermodel_ok.*

class newusermodel_activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.newusermodel_ok)

        val ustoken = intent.getStringExtra("token") // 토큰 유지
        println("newusermodel_activity??????????????????????? "+ustoken)

        filepage.setOnClickListener {
            val choosep = Intent(this, Choosepage::class.java)
            choosep.putExtra("token", ustoken)
            startActivity(choosep) // 선택 페이지로 이동
        }
    }
}