package com.daesin.school

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.daesin.school.noticeAdapter.NoticeAdapater
import com.daesin.school.noticeAdapter.NoticeData
import kotlinx.android.synthetic.main.activity_notice.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Connection
import org.jsoup.Jsoup
import java.net.UnknownHostException
import kotlin.concurrent.timer

class NoticeActivity : AppCompatActivity() {

    private lateinit var noticeAdapater: NoticeAdapater
    private val noticeList: ArrayList<NoticeData> = ArrayList()
    private var isLoading = false
    private var page = 1
    private var limit = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        setUpRecyclerView()
        getLimit()
        getNoti()
        notice_view.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!isLoading && !notice_view.canScrollVertically(1)) {
                    page++
                    getNoti()
                    Log.d("page", page.toString())
                }

                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

    private fun getLimit() {
        //게시판 최대 페이지 get
        GlobalScope.launch(Dispatchers.IO) {
            val jsoup = Jsoup.connect("http://school.busanedu.net/daesin-m/na/ntt/selectNttList.do?mi=618566&bbsId=1011229")
                    .data("listCo", "10","currPage",page.toString()).post()
            limit = jsoup.select("#subContent > div > div:nth-child(7) > form > div > ul > li:last-child > a")
                    .attr("onclick").replace("goPaging","")
                    .replace("(","").replace(")","").toInt()
        }
    }

    private fun getNoti() {
        //공지사항 게시물 가져오기
        isLoading = true
        scroll_progressbar.visibility = View.VISIBLE
        GlobalScope.launch(Dispatchers.IO) {
            //공지사항 10개 가져옴
            try {
                val jsoup = Jsoup.connect("http://school.busanedu.net/daesin-m/na/ntt/selectNttList.do?mi=618566&bbsId=1011229")
                        .data("listCo", "10","currPage",page.toString()).post()

                if (page > limit) {
                    runOnUiThread { scroll_progressbar.visibility = View.GONE }
                    return@launch
                }

                val document = jsoup.select("tbody tr")

                for (tr in document) {
                    //차례대로 제목, 작성자, 날짜, 첨부파일, 링크 확인 \
                    if (tr.select("b").text() != "공지") {
                        noticeList.add(NoticeData(title = tr.select("td:nth-child(2)").text(),
                                writer = tr.select("td:nth-child(3)").text(),
                                date = tr.select("td:nth-child(4)").text(),
                                link = tr.select("a").attr("href"),
                                file = tr.select("img").hasAttr("src")))
                    }
                }
                runOnUiThread {
                    scroll_progressbar.visibility = View.GONE
                    isLoading = false
                    noticeAdapater.addList(noticeList)
                }
            } catch (e: Exception) {
                runOnUiThread { Toast.makeText(this@NoticeActivity, R.string.error_message, Toast.LENGTH_SHORT).show() }
                Log.d("error", e.message.toString())
            }

        }
    }

    private fun setUpRecyclerView() {
        notice_view.apply {
            notice_view.layoutManager = LinearLayoutManager(this@NoticeActivity)
            noticeAdapater = NoticeAdapater()
            adapter = noticeAdapater
        }
    }

}