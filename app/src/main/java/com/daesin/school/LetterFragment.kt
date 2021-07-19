package com.daesin.school

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.daesin.school.letterAdapter.LetterAdapater
import com.daesin.school.letterAdapter.LetterData
import kotlinx.android.synthetic.main.fragment_letter.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

class LetterFragment : Fragment() {
    //공지사항 객체 리스트
    private val letterList: ArrayList<LetterData> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        parse()
        return inflater.inflate(R.layout.fragment_letter, container, false)
    }

    private fun parse() {
        //코루틴은 asyncTask랑 비슷한 역활을해줌 비동기 코드 동작
        GlobalScope.launch(Dispatchers.IO) {
            //50개 가져옴
            val jsoup = Jsoup.connect("http://school.busanedu.net/daesin-m/na/ntt/selectNttList.do?clasHmpgId=&clubHmpgId=&bbsId=1011230&nttSn=&listCo=20").get()
            val document = jsoup.select("tbody tr")
            for (tr in document) {
                //차례대로 제목, 작성자, 날짜, 첨부파일, 링크 확인
                letterList.add(LetterData(title = tr.select("td:nth-child(2)").text(),
                        writer = tr.select("td:nth-child(3)").text(),
                        date = tr.select("td:nth-child(4)").text(),
                        link = tr.select("a").attr("href"),
                        file = tr.select("img").hasAttr("src")))
            }

            /**코루틴 상태에서 바로 연결시켜버리면 에러뜸 쓰레드가 필요
             * extensions사용으로 findViewId 해줄필요가 없음
             */

            activity?.runOnUiThread {
                letter_view.layoutManager = LinearLayoutManager(requireContext())
                letter_view.adapter = LetterAdapater(letterList)
                letter_loading.visibility = View.GONE
                letter_view.visibility = View.VISIBLE
            }
        }
    }
}