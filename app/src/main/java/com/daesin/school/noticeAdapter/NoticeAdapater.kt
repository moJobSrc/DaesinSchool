package com.daesin.school.noticeAdapter

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.daesin.school.R
import kotlinx.android.synthetic.main.notice_item.view.*

class NoticeAdapater(private val noticeList: ArrayList<NoticeData>): RecyclerView.Adapter<NoticeAdapater.CustomViewHolder>() {

    inner class CustomViewHolder(itemView: ViewGroup): RecyclerView.ViewHolder(LayoutInflater.from(itemView.context).inflate(R.layout.notice_item, itemView, false))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CustomViewHolder(parent)

    override fun getItemCount(): Int = noticeList.size

    override fun onBindViewHolder(holder: NoticeAdapater.CustomViewHolder, position: Int) {
        with(noticeList[position]) {
            with(holder.itemView) {
                if (file)
                    notice_file.visibility = View.VISIBLE
                else
                    notice_file.visibility = View.GONE
                if (announce)
                    noti_important.visibility = View.VISIBLE
                else
                    noti_important.visibility = View.GONE
                notice_title.text = title
                notice_writer.text = writer
                notice_date.text = date
                //웹창이동
                notice.setOnClickListener {
                    Log.d("Notice Intent", link)
                    it.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://school.busanedu.net${link}")))
                }
            }
        }
    }

}