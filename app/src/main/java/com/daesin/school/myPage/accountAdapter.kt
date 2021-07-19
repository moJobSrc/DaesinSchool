package com.daesin.school.myPage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.daesin.school.R
import kotlinx.android.synthetic.main.table_layout.view.*

class accountAdapter(private val infoList: ArrayList<myPageData>): RecyclerView.Adapter<accountAdapter.CustomViewHolder>() {

    inner class CustomViewHolder(itemView: ViewGroup): RecyclerView.ViewHolder(LayoutInflater.from(itemView.context).inflate(R.layout.table_layout, itemView, false))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CustomViewHolder(parent)

    override fun getItemCount(): Int = infoList.size

    override fun onBindViewHolder(holder: accountAdapter.CustomViewHolder, position: Int) {
        with(infoList[position]) {
            with(holder.itemView) {
                columnName.text = headerName
                columnContent.text = content
            }
        }
    }

}