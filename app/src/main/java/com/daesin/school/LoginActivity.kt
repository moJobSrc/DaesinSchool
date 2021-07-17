package com.daesin.school

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.daesin.school.Util.App
import com.daesin.school.Util.LoginUtil
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.actionbar.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.*
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import org.json.JSONObject
import org.jsoup.Jsoup
import java.io.IOException
import java.lang.IllegalArgumentException
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

        login.setOnClickListener {

            try {
                var res = 0
                runBlocking {
                    val job = GlobalScope.async(Dispatchers.IO) {
                        res = LoginUtil.login(id.text.toString(), pw.text.toString())
                    }
                    job.await()
                }
                Log.d("result", res.toString())
                if (res == LoginUtil.LOGIN_SUCCESS) {
                    finish()
                }
            } catch (e:IllegalArgumentException) {
                runOnUiThread { Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show() }
            }
        }

        changeSetup()

    }



    fun toast(res: Int) {
        runOnUiThread { Toast.makeText(applicationContext, res, Toast.LENGTH_SHORT).show() }
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

    private fun changeSetup() {
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
}
