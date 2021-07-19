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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import org.jsoup.Jsoup
import java.io.IOException


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
        GlobalScope.launch(Dispatchers.IO){
            val client = OkHttpClient.Builder().cookieJar(App.cookieJar).build()
            client.newCall(Request.Builder().url("http://school.busanedu.net/daesin-m/main.do#menuOpen").build()).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    TODO("Not yet implemented")
                }

                override fun onResponse(call: Call, response: Response) {
                    val res = response.body!!.string()
                    var html = Jsoup.parse(res).head().html()
                    html += Jsoup.parse(res).select("#mNavi")
                    Log.d("HTML", Jsoup.parse(res).select("#mNavi").html())
                    runOnUiThread {
                        nav_view.settings.javaScriptEnabled = true
                        nav_view.settings.domStorageEnabled = true
                        nav_view.setLayerType(View.LAYER_TYPE_HARDWARE, null)
                        nav_view.loadDataWithBaseURL("http://school.busanedu.net",html,"text/html", "UTF-8", null)
                    }
                }

            })
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