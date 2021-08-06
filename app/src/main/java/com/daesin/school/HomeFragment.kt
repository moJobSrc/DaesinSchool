package com.daesin.school

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.daesin.school.homeNoti.NotiAdapater
import com.daesin.school.homeNoti.NotiData
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

class HomeFragment : Fragment() {
    //공지사항
    private var notice = ArrayList<NotiData>()
    //가정통신문
    private var letter = ArrayList<NotiData>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        parse()
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    private fun parse() {
        GlobalScope.launch(Dispatchers.IO) {
            //getNotice()
            //getLetter()

            activity?.runOnUiThread {
                //recyclerView.layoutManager = LinearLayoutManager(requireContext())
                //recyclerView.adapter = NotiAdapater(notice)
            }
        }
    }
    
    //공지사항
    private fun getNotice() {
        val jsoup = Jsoup.connect("http://school.busanedu.net/daesin-m/na/ntt/selectNttList.do?mi=618566&bbsId=1011229")
                .data("listCo","5").post()
        val doc = jsoup.select("tbody tr")
        for (tr in doc) {
            //공지가아닌것만 가져오기

            if (tr.select("td:nth-child(1)").text() != "공지")
                notice.add(NotiData(title = tr.select("td:nth-child(2)").text()))
        }
    }
    
    //가정통신문
    private fun getLetter() {
        val jsoup = Jsoup.connect("http://school.busanedu.net/daesin-m/na/ntt/selectNttList.do?mi=618567&bbsId=1011230")
                .data("listCo","5").post()
        val doc = jsoup.select("tbody tr")
        for (tr in doc) {
            //공지가아닌것만 가져오기

            if (tr.select("td:nth-child(1)").text() != "공지")
                letter.add(NotiData(title = tr.select("td:nth-child(2)").text()))
        }
    }

    //선택했을때 텍스트뷰 컬러변경
    private fun change(textView: TextView) {
        //home_alert.setTextColor(resources.getColor(R.color.grey))
        //letter_alert.setTextColor(resources.getColor(R.color.grey))
        textView.setTextColor(resources.getColor(R.color.colorPrimaryDark))
    }
}