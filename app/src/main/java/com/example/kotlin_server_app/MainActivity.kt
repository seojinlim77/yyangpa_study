package com.example.kotlin_server_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider


class MainActivity : AppCompatActivity() {

    var auth: FirebaseAuth? = null
    val GOOGLE_REQUEST_CODE = 99
    val TAG = "googleLogin"
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // retrofit 객체 생성
        var retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.8:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // retrofit 객체에서 create를 통해서 서비스를 올려줌
        var loginService = retrofit.create(LoginService::class.java)

        new_user_button.setOnClickListener {
            val intent2 = Intent(this,Newperson::class.java)
            var dialog = AlertDialog.Builder(this@MainActivity)
            dialog.setTitle("회원 가입 페이지로 이동합니다.")
            dialog.show()
            startActivity(intent2) // 회원 가입 페이지로 이동
        }

        // 로그인시 아이디와 비번 화면에 이벤트처럼 출력됨
        button.setOnClickListener {
            val intent = Intent(this,Choosepage::class.java)
            // 로그인 버튼 클릭 시 다음 화면으로 넘김

            var textId = editTextTextPersonName.text.toString()
            var textPw = editTextTextPassword.text.toString()


            loginService.requestLogin(textId,textPw).enqueue(object: Callback<Login> {
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
                    var dialog = AlertDialog.Builder(this@MainActivity)
                    if(login?.successcheck != null)
                    {
                        dialog.setTitle("경고!")
                        dialog.setMessage("가입자가 아닙니다.")
                        dialog.show()
                    }
                    else
                    {
                        dialog.setTitle("확인!")
                        // dialog.setMessage("username = " + login?.username + "   pw = " + login?.password)
                        dialog.setMessage("가입자가 맞습니다.")
                        dialog.show()
                        intent.putExtra("userid",textId) // 유저 데이터 가져가기
                        intent.putExtra("userpw",textPw)
                        startActivity(intent) // 확인시 페이지 넘어가기
                    }

                }
            })


        }

        auth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this,gso)
        Google_button.setOnClickListener {
            signIn()
        }
    }

    // 구글 로그인 관련 코드
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, GOOGLE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == GOOGLE_REQUEST_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "로그인 성공")
                    val user = auth!!.currentUser
                    loginSuccess()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }
    private fun loginSuccess(){
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}