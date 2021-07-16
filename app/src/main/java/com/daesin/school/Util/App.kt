package com.daesin.school.Util

import PreferenceUtil
import android.app.Application
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import okhttp3.CookieJar

class App : Application() {

    companion object {
        lateinit var prefs: PreferenceUtil
        lateinit var cookieJar: CookieJar
    }

    override fun onCreate() {
        prefs = PreferenceUtil(applicationContext)
        cookieJar = PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(applicationContext))

        super.onCreate()
    }

}