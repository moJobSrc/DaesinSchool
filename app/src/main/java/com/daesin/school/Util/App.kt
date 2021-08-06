package com.daesin.school.Util

import PreferenceUtil
import android.app.Application
import android.util.Log
import com.franmontiel.persistentcookiejar.ClearableCookieJar
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request

class App : Application() {

    companion object {
        lateinit var prefs: PreferenceUtil
        lateinit var cookieJar: ClearableCookieJar
        lateinit var cookiePrefs: SharedPrefsCookiePersistor
        const val MAIN_PAGE = "https://school.busanedu.net/daesin-m/lo/login/loginPage.do"
    }

    override fun onCreate() {
        prefs = PreferenceUtil(applicationContext)
        cookiePrefs = SharedPrefsCookiePersistor(applicationContext)
        cookieJar = PersistentCookieJar(SetCookieCache(), cookiePrefs)

        //makeCookie()

        super.onCreate()
    }

    fun makeCookie() {
        //로그인 화면 쿠키 연동
        GlobalScope.launch(Dispatchers.IO) {
            OkHttpClient().newBuilder().cookieJar(App.cookieJar).build().newCall(Request.Builder().url(MAIN_PAGE).build()).execute()
            if (prefs.getBoolean("login")) {
                GlobalScope.launch {
                    try {
                        val res = LoginUtil.login(prefs.getString("id"), prefs.getString("pw"))
                        Log.d("LoginResult", res.toString())
                    } catch (e: IllegalArgumentException) {
                        Log.d("E", "ERROR")
                    }
                }
            }
        }
    }

}