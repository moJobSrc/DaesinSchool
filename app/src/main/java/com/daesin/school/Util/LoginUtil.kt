package com.daesin.school.Util

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.FormBody
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject


object LoginUtil {

    val LOGIN_SUCCESS = 1
    val LOGIN_FAILED = 0

    //로그인
    suspend fun login(id: String, pw: String): Int = withContext(Dispatchers.IO) {
        val url = "https://school.busanedu.net/daesin-m/lo/login/login.do".toHttpUrl()

        // 클라이언트 생성
        val client = OkHttpClient().newBuilder().cookieJar(App.cookieJar).build()

        //데이터를 담아 보낼 바디를 만든다
        //sysId=daesin-m&loginType=2&agreCnt=1&mberId=id&mberPassword=pw
        val requestBody: RequestBody = FormBody.Builder()
                .add("sysId", "daesin")
                .add("loginType", "2")
                .add("agreCnt", "1")
                .add("mberId", id)
                .add("mberPassword", pw)
                .build()

        // OkHttp Request 를 만들어준다.
        val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

        // 요청 전송
        val response = client.newCall(request).execute()

        if (response.isSuccessful) {
            val res = response.body!!.string()
            Log.d("LoginResult", res)
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
                else -> LOGIN_FAILED
            }
        } else LOGIN_FAILED

    }

    private fun logout() {
        App.cookiePrefs.clear()
    }

}