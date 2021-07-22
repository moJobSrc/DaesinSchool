package com.daesin.school

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.daesin.school.Util.App
import kotlinx.android.synthetic.main.actionbar.*
import kotlinx.android.synthetic.main.activity_drawer.*
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.HttpUrl.Companion.toHttpUrl


class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var drawerView: View
    private var main = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawerLayout = drawer_layout
        drawerView = drawer

        setUpNavigationDrawer()
    }

    fun openUrl(url: String) {
        if (url == "no") {
            return
        }
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        println("MainActivity : openUrl(), url = $url")
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.account) {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    private var time:Long = 0
    override fun onBackPressed() {
        // 뒤로가기 버튼 클릭
        if(System.currentTimeMillis() - time >=2000) {
            time = System.currentTimeMillis()
            if (!main) {
                time = 2000
                main = true
                supportFragmentManager.beginTransaction().replace(R.id.fragment, HomeFragment()).commit()
            } else {
                Toast.makeText(applicationContext, "뒤로가기 버튼을 한번 더 누르면 종료됩니다", Toast.LENGTH_SHORT).show()
            }
        } else {
            finish() //액티비티 종료
        }
    }

    private fun setUpNavigationDrawer() {
        setSupportActionBar(toolbar)

        supportActionBar!!.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayShowHomeEnabled(false)
            setDisplayShowCustomEnabled(true)
        }

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


        logout.setOnClickListener {
            App.prefs.removeKey("login")
            App.cookieJar.clear()
            App.cookiePrefs.clear()
            App().makeCookie()
            Log.d("Cookies", App.cookieJar.loadForRequest(App.MAIN_PAGE.toHttpUrl()).toString())
        }

        val click = View.OnClickListener { v ->
            when (v.id) {
                R.id.navi_home -> {
                    main = true
                    supportFragmentManager.beginTransaction().replace(R.id.fragment, HomeFragment()).commit()
                    drawerLayout.closeDrawers()
                }
                R.id.navi_notice -> {
                    main = false
                    supportFragmentManager.beginTransaction().replace(R.id.fragment, NoticeFragment()).commit()
                    drawerLayout.closeDrawers()
                }
                R.id.navi_letter -> {
                    main = false
                    supportFragmentManager.beginTransaction().replace(R.id.fragment, LetterFragment()).commit()
                    drawerLayout.closeDrawers()
                }
                R.id.navi_schedule -> {
                    main = false
                    supportFragmentManager.beginTransaction().replace(R.id.fragment, ScheduleFragment()).commit()
                    drawerLayout.closeDrawers()
                }
                R.id.navi_photo -> {
                    main = false
                    supportFragmentManager.beginTransaction().replace(R.id.fragment, PhotoFragment()).commit()
                    drawerLayout.closeDrawers()
                }
            }
        }

        navi_home.setOnClickListener(click)
        navi_notice.setOnClickListener(click)
        navi_letter.setOnClickListener(click)
        navi_schedule.setOnClickListener(click)
        navi_photo.setOnClickListener(click)

        supportFragmentManager.beginTransaction().replace(R.id.fragment, HomeFragment()).commit()
    }
}