package com.daesin.school.homeNoti

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.daesin.school.R
import kotlinx.android.synthetic.main.simplenoti_item.view.*

//가정통신문 공지사항 학부모게시판에 모두 사용되는 어뎁터
class NotiAdapater(private val noticeList: ArrayList<NotiData>): RecyclerView.Adapter<NotiAdapater.CustomViewHolder>() {

    inner class CustomViewHolder(itemView: ViewGroup): RecyclerView.ViewHolder(LayoutInflater.from(itemView.context).inflate(R.layout.simplenoti_item, itemView, false))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CustomViewHolder(parent)

    override fun getItemCount(): Int = noticeList.size

    override fun onBindViewHolder(holder: NotiAdapater.CustomViewHolder, position: Int) {
        with(holder.itemView) {
            title.text = noticeList[position].title
        }
    }

}