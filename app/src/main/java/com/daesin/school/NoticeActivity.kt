package com.daesin.school

import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.daesin.school.noticeAdapter.NoticeAdapater
import com.daesin.school.noticeAdapter.NoticeData
import kotlinx.android.synthetic.main.activity_notice.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import java.net.UnknownHostException
import kotlin.concurrent.timer

class NoticeActivity : AppCompatActivity() {

    private val noticeList: ArrayList<NoticeData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }
        parse()
    }

    private fun parse() {
        //코루틴은 asyncTask랑 비슷한 역활을해줌 비동기 코드 동작
        GlobalScope.launch(Dispatchers.IO) {
            //20개 가져옴
            try {
                val jsoup = Jsoup.connect("http://school.busanedu.net/daesin-m/na/ntt/selectNttList.do?mi=618566&bbsId=1011229")
                        .data("listCo", "100").post()
                val document = jsoup.select("tbody tr")

                for (tr in document) {
                    //차례대로 제목, 작성자, 날짜, 첨부파일, 링크 확인
                    noticeList.add(NoticeData(title = tr.select("td:nth-child(2)").text(),
                            writer = tr.select("td:nth-child(3)").text(),
                            date = tr.select("td:nth-child(4)").text(),
                            link = tr.select("a").attr("href"),
                            file = tr.select("img").hasAttr("src"),
                            announce = tr.select("b").text() == "공지"))
                }
            } catch (e: UnknownHostException) {
                runOnUiThread { Handler().postDelayed({parse()}, 1000) }
            } catch (e: Exception) {
                runOnUiThread { Toast.makeText(this@NoticeActivity, R.string.error_message, Toast.LENGTH_SHORT).show() }
            }

            runOnUiThread {
                notice_view.layoutManager = LinearLayoutManager(this@NoticeActivity)
                notice_view.adapter = NoticeAdapater(noticeList)
            }
        }
    }
}