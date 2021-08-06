package com.daesin.school

import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.daesin.school.Util.App
import com.daesin.school.Util.LoginUtil
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl
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
            //loadMyPage()
        }
        Log.d("Cookies", App.cookieJar.loadForRequest(App.MAIN_PAGE.toHttpUrl()).toString())
        login.setOnClickListener {
            GlobalScope.launch {
                try {
                    val res = LoginUtil.login(id.text.toString(), pw.text.toString())
                    if (res == LoginUtil.LOGIN_SUCCESS) {
                        App.prefs.apply {
                            setBoolean("login", true)
                            setString("id", id.text.toString())
                            setString("pw", pw.text.toString())
                        }
                        runOnUiThread {
                            //loadMyPage()
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

    fun buttonDisable() {
        if (!enable){
            login.background = transitionDrawable
            transitionDrawable.reverseTransition(0)
            login.isEnabled =  false
            login.setTextColor(resources.getColor(R.color.black))
            enable = true
        }
    }

    fun buttonEnable() {
        if (enable) {
            login.background = transitionDrawable
            transitionDrawable.startTransition(0)
            login.isEnabled =  true
            login.setTextColor(resources.getColor(R.color.white))
            enable = false
        }
    }
}
