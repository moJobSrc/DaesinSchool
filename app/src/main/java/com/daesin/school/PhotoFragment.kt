package com.daesin.school

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.daesin.school.photoFrag.photoAdapter
import com.daesin.school.photoFrag.photoData
import kotlinx.android.synthetic.main.fragment_photo.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup

class PhotoFragment : Fragment() {

    private lateinit var adapter: photoAdapter
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private var page = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_photo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getImage(page)

        adapter = photoAdapter()
        gridView.adapter = adapter
        gridView.layoutManager = GridLayoutManager(requireContext(), 2)
        layoutManager = gridView.layoutManager!!

        gridView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition = (recyclerView.layoutManager as GridLayoutManager?)!!.findLastCompletelyVisibleItemPosition() // 화면에 보이는 마지막 아이템의 position
                val itemTotalCount = recyclerView.adapter!!.itemCount - 1
                
                //리사이클러뷰가 끝인지 확인
                if (!recyclerView.canScrollVertically(1) && lastVisibleItemPosition == itemTotalCount) {
                    getImage(++page)
                }
            }
        })
    }

    private fun getImage(num: Int) {
        val photoList: ArrayList<photoData> = ArrayList()

        GlobalScope.launch(Dispatchers.IO) {
            val client = OkHttpClient()
            val request = Request.Builder().url("http://school.busanedu.net/daesin-m/na/ntt/selectNttList.do?mi=618572&bbsId=1011232")
                    .post(FormBody.Builder().add("currPage", num.toString()).build()).build()
            val doc = Jsoup.parse(client.newCall(request).execute().body?.string()).select(".photo_list ul li")

            for (tag in doc) {
                val imageUrl = "http://school.busanedu.net" + tag.select("img").attr("src")
                val title = tag.select("p").text()
                val date = tag.select("span").text()

                photoList.add(photoData(
                        title, date, imageUrl
                ))
                //Log.d("Title", title)
            }

            activity?.runOnUiThread {
                adapter.addList(photoList)
                Log.d("Page", num.toString())
            }
        }
    }

}