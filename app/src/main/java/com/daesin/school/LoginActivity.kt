package com.daesin.school

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.daesin.school.Util.App
import kotlinx.android.synthetic.main.actionbar.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import org.json.JSONObject
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
        id.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0!!.isEmpty() || pw.text.toString().isEmpty()) {
                    buttonDisable()
                } else if (p0.isNotEmpty() && pw.text.toString().isNotEmpty()) {
                    buttonEnable()
                }
            }
        })
        pw.addTextChangedListener(object  : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0!!.isEmpty() || id.text.toString().isEmpty()) {
                    buttonDisable()
                } else if (p0.isNotEmpty() && id.text.toString().isNotEmpty()) {
                    buttonEnable()
                }
            }

        })

    }

    fun toast(message: String) {
        runOnUiThread { Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show() }
    }

    fun toast(res: Int) {
        runOnUiThread { Toast.makeText(applicationContext, res, Toast.LENGTH_SHORT).show() }
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
                    val res = response.body!!.string()
                    Log.d("Response", res)
                    try {
                        if (JSONObject(res).has("passwordFailrCoCheck") && JSONObject(res).getString("passwordFailrCoCheck") == "N") {
                            toast("비밀번호 5회이상 실패")
                        }
                        when (JSONObject(res).getString("result")) {
                            "N" -> {
                                toast("로그인 실패")
                            }
                            "D" -> {
                                toast("장기간 미접속및 개인정보 미동의로 삭제된회원입니다")
                            }
                            "C" -> {
                                toast("본인인증이 필요합니다")
                            }
                            "W" -> {
                                toast("승인 대기중입니다")
                            }
                            "NM","Y" -> {
                                toast(R.string.loginSuccess)
                                App.prefs.setString("cookie", cookieManager.cookieStore.cookies.toString())
                                finish()
                            }
                        }
                    } catch (e: Exception) {
                        Log.d("Exception", e.message.toString())
                    }

                }

                override fun onFailure(call: Call, e: IOException) {
                    Toast.makeText(applicationContext, R.string.error_request, Toast.LENGTH_SHORT).show()
                }

            })

        }
    }

    fun buttonDisable() {
        login.background = resources.getDrawable(R.drawable.bt_outline)
        login.isEnabled =  false
        login.setTextColor(resources.getColor(R.color.black))
    }

    fun buttonEnable() {
        login.background = resources.getDrawable(R.color.colorPrimary)
        login.isEnabled =  true
        login.setTextColor(resources.getColor(R.color.white))
    }
}