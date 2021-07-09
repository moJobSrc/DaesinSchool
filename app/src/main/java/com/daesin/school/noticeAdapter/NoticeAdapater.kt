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

class NoticeAdapater : RecyclerView.Adapter<NoticeAdapater.NoticeViewHolder>() {
    private val list =  ArrayList<NoticeData>()

    inner class NoticeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(data: NoticeData) {
            with(itemView) {
                if (data.file)
                    notice_file.visibility = View.VISIBLE
                else
                    notice_file.visibility = View.GONE
                notice_title.text = data.title
                notice_writer.text = data.writer
                notice_date.text = data.date
                //웹창이동
                notice.setOnClickListener {
                    Log.d("Notice Intent", data.link)
                    it.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://school.busanedu.net${data.link}")))
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notice_item, parent, false)

        return NoticeViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: NoticeViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun addList(items: ArrayList<NoticeData>) {
        list.addAll(items)
        notifyDataSetChanged()
    }

    fun clear() {
        list.clear()
        notifyDataSetChanged()
    }

}