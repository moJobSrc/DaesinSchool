package com.daesin.school

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.actionbar.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException
import java.net.CookieManager
import java.net.CookiePolicy
import java.net.URL

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
            setDisplayShowHomeEnabled(false)
        }
        login.setOnClickListener { login() }
    }

    private fun login() {

        // URL을 만들어 주고
        val url = URL("https://school.busanedu.net/daesin-m/lo/login/login.do")

        //데이터를 담아 보낼 바디를 만든다
        //sysId=daesin-m&loginType=2&agreCnt=1&mberId=id&mberPassword=pw
        val requestBody: RequestBody = FormBody.Builder()
                .add("sysId", "daesin")
                .add("loginType", "2")
                .add("agreCnt", "1")
                .add("mberId", id.text.toString())
                .add("mberPassword", pw.text.toString())
                .build()

        val cookieManager = CookieManager()
        // 클라이언트 생성
        val client = OkHttpClient().newBuilder().cookieJar(JavaNetCookieJar(cookieManager)).build()

        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)
        GlobalScope.launch(Dispatchers.IO) {
            //로그인 화면 쿠키 연동
            client.newCall(Request.Builder().url("https://school.busanedu.net/daesin-m/lo/login/loginPage.do").build()).execute()

            // OkHttp Request 를 만들어준다.
            val request = Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build()

            Log.d("Cookies", cookieManager.cookieStore.cookies.toString())
            // 요청 전송
            client.newCall(request).enqueue(object : Callback {

                override fun onResponse(call: Call, response: Response) {
                    Log.d("Response", "Successful")
                    Log.d("result", response.body!!.string())
                }

                override fun onFailure(call: Call, e: IOException) {
                    Toast.makeText(applicationContext, R.string.error_message, Toast.LENGTH_SHORT).show()
                }

            })
        }
    }
}