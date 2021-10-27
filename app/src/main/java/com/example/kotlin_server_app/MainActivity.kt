package com.example.kotlin_server_app

//import androidx.appcompat.app.AlertDialog
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Thread.sleep


class MainActivity : AppCompatActivity() {

    var auth: FirebaseAuth? = null
    val GOOGLE_REQUEST_CODE = 99
    val TAG = "<<<<<<<<<<<<<<LoginPage>>>>>>>>>>>>"

    private lateinit var sharedPreferences : SharedPreferences
    private lateinit var editor : SharedPreferences.Editor

    fun showPrograss(isShow:Boolean) {
        if (isShow) loading_linear.visibility = View.VISIBLE
        else loading_linear.visibility = View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferences = getSharedPreferences("auto_token", 0) // 자동 로그인 파일
        //val sharedPreferencess = getSharedPreferences("user_token",0) // 그냥 유저 토큰 파일
        val editor = sharedPreferences.edit()
        //val editor2 = sharedPreferences.edit()
        val token_check = sharedPreferences.getString("token", null)
        val authentication_page = Intent(this, Filepass::class.java)
        val bluetooth_active = Intent(this,BluetoothActivity::class.java) // 모듈 선택창 intent

        //var api: ApiInterface
        showPrograss(false)

        // 자동로그인
        if (token_check != null) {
            startActivity(bluetooth_active); // 토큰 존재시 파일 받는 페이지로 이동 (블루투스)
        }

        // retrofit 객체 생성
        var retrofit = Retrofit.Builder()
                .baseUrl("http://172.30.1.3:8000")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        // retrofit 객체에서 create를 통해서 서비스를 올려줌
        var loginService = retrofit.create(LoginService::class.java)


        new_user_button.setOnClickListener {
            val intent2 = Intent(this, Newperson::class.java)
            var dialog = AlertDialog.Builder(this@MainActivity)
            dialog.setTitle("회원 가입 페이지로 이동합니다.")
            dialog.show()
            startActivity(intent2) // 회원 가입 페이지로 이동
        }

        // 로그인시 아이디와 비번 화면에 이벤트처럼 출력됨
        button.setOnClickListener {

            showPrograss(true)

            val intent = Intent(this, Filepass::class.java)

            // 로그인 버튼 클릭 시 다음 화면으로 넘김
            val loginkeep = Intent(this, MainActivity::class.java)

            var textId = editTextTextPersonName.text.toString()
            var textPw = editTextTextPassword.text.toString()


            // 먼저 토큰 생성을 위해서 로그인 (서버와 통신)
            loginService.requestLogin(textId, textPw).enqueue(object : Callback<Login> {
                override fun onFailure(call: Call<Login>, t: Throwable) {
                    // 웹 통신에 실패시 실행
                    var dialog = AlertDialog.Builder(this@MainActivity)
                    dialog.setTitle("실패!")
                    dialog.setMessage("통신에 실패....")
                    dialog.show()
                }

                override fun onResponse(call: Call<Login>, response: Response<Login>) {
                    // 성공시 응답값을 받아옴
                    var login = response.body() // succensscheck
                    //var dialog = AlertDialog.Builder(this@MainActivity)
                    println("#################################################login_check : " + login?.code + "#################################################")

                    if (login?.code != "login_success") {
                        var file_messages = androidx.appcompat.app.AlertDialog.Builder(this@MainActivity)
                        file_messages.setTitle("회원가입된 사용자가 아닙니다.")
                        file_messages.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
                        })
                        file_messages.show()
                        editTextTextPersonName.setText("")
                        editTextTextPassword.setText("")
                        showPrograss(false)
                    } else {

                        //sleep(5000)
                        // 로그인 후 토큰 얻어오고 페이지 넘어가기
                        var logoutService = retrofit.create(LogoutService::class.java)
                        logoutService.requestlogout(textId, textPw).enqueue(object : Callback<Logout> {
                            override fun onResponse(call: Call<Logout>, response: Response<Logout>) {
                                var dialog = AlertDialog.Builder(this@MainActivity)
                                var log_token = response.body() // 토큰 받아와서 저장
                                var token2 = log_token?.code // 토큰 받아온 값

                                println("((((((((((((((((((((((((((((((((((((((("+token2)
                                //////////////// 토큰 저장 /////////////////////////////
                                editor.putString("ustoken",token2) // 유저의 토큰 파일로 저장
                                editor.apply()
                                Log.e(TAG, "쉐어드에 저장된 ustoken = " + sharedPreferences.getString("ustoken", ""))
                                ///////////////////////////////////////////////////////

                                if (log_token?.msg != null) {
                                    println(log_token?.code)
                                    if (checkboxbutton.isChecked) { // 자동 로그인 버튼 클릭 되어있으면
                                        editor.putString("token", token2)
                                        editor.putString("username",textId) // 아이디도 저장
                                        editor.apply()
                                        Log.e(TAG, "쉐어드에 저장된 token = " + sharedPreferences.getString("token", ""))
                                    }
                                } else {
                                    println("successok == NULL")
                                }

                                showPrograss(false)
                                startActivity(authentication_page) // 인증 이동
                                finish()
                            }

                            override fun onFailure(call: Call<Logout>, t: Throwable) {
                                var dialog1 = AlertDialog.Builder(this@MainActivity)
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
            })

        }

    }

}