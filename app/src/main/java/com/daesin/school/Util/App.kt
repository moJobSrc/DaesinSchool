package com.daesin.school.Util

import PreferenceUtil
import android.app.Application

class App : Application() {

    companion object { lateinit var prefs: PreferenceUtil }

    override fun onCreate() {
        prefs = PreferenceUtil(applicationContext)
        super.onCreate()
    }
}