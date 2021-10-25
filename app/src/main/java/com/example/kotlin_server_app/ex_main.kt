package com.example.kotlin_server_app

import android.os.AsyncTask
import android.os.Bundle
import android.provider.CallLog
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlin_server_app.MeasureActivity.retrofit
import com.google.android.gms.auth.account.WorkAccount.API
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//var api = retrofit.create(LoginService::class.java)

class ex_main : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*
        var retrofit = Retrofit.Builder()
                .baseUrl("http://172.30.1.59:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        var api = MeasureActivity.retrofit.create(LoginService::class.java)

        var textId = editTextTextPersonName.text.toString()
        var textPw = editTextTextPassword.text.toString()

        retrofit.create(file_check::class.java)
        val modelCall: Call<Login> = api.requestLogin(textId, textPw) // 필요한 모델
        val isModelTask = isModelAPI() // 만든 AsyncTask
        isModelTask.execute(modelCall)
    }


    class isModelAPI : AsyncTask<Call<Login>, Void, Response<Login>>() {

//        override fun void onPreExecute() {
//            super.onPreExecute();
//            // 로딩창 추가해도 될 듯.
//        }
//
//        override fun Response<ResIsModel> doInBackground(Call... calls) {
//            try {
//                Call<ResIsModel> call = calls[0];
//                return call.execute();
//            } catch (IOException e) {
//                Log.e().printStackTrace();
//            }
//            return null;
//        }
//
//        override fun void onPostExecute(Response<ResIsModel> response){
//            TextView t = (TextView) findViewById(R.id.time);
//            Boolean isModel = response.body().isModel();
//            if (isModel){
//                t.setText("모델 있음");
//            }else {
//                t.setText("모델 없음");
//            }
//        }

        override fun doInBackground(vararg calls : Call<Login>): Response<Login> {
            var call = calls[0]
            return call.execute()
        }

        override fun onPostExecute(result: Response<Login>?) {
            super.onPostExecute(result)
            println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<okkkkkkkkkkkkkkkkkkkkkk")
        }
    }

    class MyAsyncTask(var tvText: TextView) : AsyncTask<Void, Int, Boolean>() {
        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: Void?): Boolean {
            for (i in 1..50) {
                try {
                    Thread.sleep(5)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                publishProgress(i)
            } return true
        }

        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)
            tvText.text = values[0].toString() + " 초 경과"
        }

        override fun onPostExecute(result: Boolean?) {
            super.onPostExecute(result)
        }

        fun onCancelled(result: Boolean?) {
            super.onCancelled(result)
        }
    }
    */
    }

}



