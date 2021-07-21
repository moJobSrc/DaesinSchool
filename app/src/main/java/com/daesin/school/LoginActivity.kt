package com.daesin.school

import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.webkit.WebSettings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.htmlEncode
import androidx.recyclerview.widget.LinearLayoutManager
import com.daesin.school.Util.App
import com.daesin.school.Util.LoginUtil
import com.daesin.school.myPage.accountAdapter
import com.daesin.school.myPage.myPageData
import kotlinx.android.synthetic.main.actionbar.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import org.jsoup.Jsoup
import java.io.IOException


class LoginActivity : AppCompatActivity() {
    private lateinit var transitionDrawable :TransitionDrawable
    private var enable = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
            setDisplayShowHomeEnabled(false)
        }

        transitionDrawable = resources.getDrawable(R.drawable.button_transition) as TransitionDrawable
        textListener()
        if (App.prefs.getBoolean("login")) {
            loadMyPage()
        }

        login.setOnClickListener {
            GlobalScope.launch {
                try {
                    val res = LoginUtil.login(id.text.toString(), pw.text.toString())
                    Log.d("LoginResult", res.toString())
                    if (res == LoginUtil.LOGIN_SUCCESS) {
                        App.prefs.apply {
                            setBoolean("login", true)
                            setString("id", id.text.toString())
                            setString("pw", pw.text.toString())
                        }
                        runOnUiThread {
                            loadMyPage()
                        }
                    }
                } catch (e:IllegalArgumentException) {
                    runOnUiThread { Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show() }
                }
            }
        }
    }

    private fun textListener() {

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

    private fun loadMyPage() {
        myPageView.visibility = View.VISIBLE
        loginForm.visibility = View.GONE
        loading.visibility = View.VISIBLE
        val client = OkHttpClient().newBuilder().cookieJar(App.cookieJar).build()

        client.newCall(Request.Builder().url("https://school.busanedu.net/daesin-m/sb/sbscrb/selectSbscrbInfo.do").build()).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                TODO("Not yet implemented")
            }
            override fun onResponse(call: Call, response: Response) {
                val res = response.body!!.string()
                val infoList:ArrayList<myPageData> = arrayListOf()
                val doc = Jsoup.parse(res).select("#sbscrbInfoForm > fieldset")

                for (tr in doc.select("tr")) {
                    if (tr.className() != "common_display_none" && !tr.text().contains("비밀번호")) {

                        if (tr.select("td").text().isEmpty() || tr.select("td").select("p").hasClass("mgt10")) {
                            if (tr.select("input").`val`().isNotEmpty()) {
                                infoList.add(myPageData(tr.select("th").text(),tr.select("input").`val`()))
                            }
                        } else if (tr.select("select").isNotEmpty()) {
                            for (option in tr.select("select option")) {
                                if (option.hasAttr("selected")) {
                                    infoList.add(myPageData(tr.select("th").text(), option.text()))
                                }
                            }
                        } else {
                            infoList.add(myPageData(tr.select("th").text(), tr.select("td").text()))
                        }

                    }
                }

                runOnUiThread {
                    loading.visibility = View.GONE
                    myPageView.layoutManager = LinearLayoutManager(applicationContext)
                    myPageView.adapter = accountAdapter(infoList)
                }
            }

        })
    }

    fun buttonDisable() {
        if (!enable){
            login.background = transitionDrawable
            transitionDrawable.reverseTransition(180)
            login.isEnabled =  false
            login.setTextColor(resources.getColor(R.color.black))
            enable = true
        }
    }

    fun buttonEnable() {
        if (enable) {
            login.background = transitionDrawable
            transitionDrawable.startTransition(180)
            login.isEnabled =  true
            login.setTextColor(resources.getColor(R.color.white))
            enable = false
        }
    }
}
