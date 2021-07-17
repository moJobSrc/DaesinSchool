package com.daesin.school.Util

import android.util.Log
import kotlinx.coroutines.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.net.CookieManager
import java.net.URL


object LoginUtil {

    val LOGIN_SUCCESS = 1
    val LOGIN_FAILED = 2

    //로그인
    fun login(id: String, pw: String) = runBlocking {

        val result = GlobalScope.async(Dispatchers.IO) {

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

            //로그인 화면 쿠키 연동
            client.newCall(Request.Builder().url("https://school.busanedu.net/daesin-m/lo/login/loginPage.do").build()).execute()

            // OkHttp Request 를 만들어준다.
            val request = Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build()

            // 요청 전송
            client.newCall(request).enqueue(object : Callback {

                override fun onResponse(call: Call, response: Response) {
                    val res = response.body!!.string()
                    Log.d("LOGIN", res)

                    if (JSONObject(res).has("passwordFailrCoCheck") && JSONObject(res).getString("passwordFailrCoCheck") == "N") {
                        throw IllegalArgumentException("비밀번호 5회이상 실패")
                    }
                    when (JSONObject(res).getString("result")) {
                        "N" -> {
                            throw IllegalArgumentException("아이디 또는 비밀번호를 확인해주세요")
                        }
                        "D" -> {
                            throw IllegalArgumentException("장기간 미접속및 개인정보 미동의로 삭제된회원입니다")
                        }
                        "C" -> {
                            throw IllegalArgumentException("본인인증이 필요합니다")
                        }
                        "W" -> {
                            throw IllegalArgumentException("승인 대기중입니다")
                        }
                        "NM", "Y" -> {
                            LOGIN_SUCCESS
                        }
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    LOGIN_FAILED
                    Log.d("ERROR", e.message.toString())
                }

            })
        }

        return result
    }

    private fun logout() {
        val cookieManager = CookieManager()
        cookieManager.cookieStore.removeAll()
    }

}