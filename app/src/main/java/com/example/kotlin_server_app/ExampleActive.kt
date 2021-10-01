package com.example.kotlin_server_app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.new_user_page.*

class ExampleActive : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_user_page)

        registerbutton.setOnClickListener {
            val registerstart = Intent(this,Filepass::class.java)
        }
    }

}
