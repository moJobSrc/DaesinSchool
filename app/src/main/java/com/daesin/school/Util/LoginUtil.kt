package com.daesin.school.Util

import android.content.Context
import android.util.Log
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException
import java.net.CookieManager
import java.net.CookiePolicy
import java.net.URL


object LoginUtil {

    //로그인
    fun login(id: String, pw: String): String {
        var res = ""
        // URL을 만들어 주고
        val url = URL("https://school.busanedu.net/daesin-m/lo/login/login.do")

        //데이터를 담아 보낼 바디를 만든다
        //sysId=daesin-m&loginType=2&agreCnt=1&mberId=id&mberPassword=pw
        val requestBody: RequestBody = FormBody.Builder()
                .add("sysId", "daesin")
                .add("loginType", "2")
                .add("agreCnt", "1")
                .add("mberId", id)
                .add("mberPassword", pw)
                .build()

        // 클라이언트 생성
        val client = OkHttpClient().newBuilder().cookieJar(App.cookieJar).build()

        GlobalScope.launch(Dispatchers.IO) {
            //로그인 화면 쿠키 연동
            val cookie = client.newCall(Request.Builder().url("https://school.busanedu.net/daesin-m/lo/login/loginPage.do").build()).execute()

            // OkHttp Request 를 만들어준다.
            val request = Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build()

            // 요청 전송
            client.newCall(request).enqueue(object : Callback {

                override fun onResponse(call: Call, response: Response) {
                    res = response.body!!.string()
                    Log.d("Response", res)
                }

                override fun onFailure(call: Call, e: IOException) {
                    Log.d("ERROR", e.message.toString())
                }

            })
        }
        return res
    }

    private fun logout() {
        val cookieManager = CookieManager()
        cookieManager.cookieStore.removeAll()
    }

}