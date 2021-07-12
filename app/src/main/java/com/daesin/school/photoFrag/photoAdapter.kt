package com.daesin.school.photoFrag

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.daesin.school.R
import com.daesin.school.noticeAdapter.NoticeData
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import kotlinx.android.synthetic.main.notice_item.view.*
import kotlinx.android.synthetic.main.photo_item.view.*
import kotlinx.android.synthetic.main.simplenoti_item.view.*

class photoAdapter : RecyclerView.Adapter<photoAdapter.PhotoViewHolder>() {

    private val photoList = ArrayList<photoData>()

    inner class PhotoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(data: photoData) {
            with(itemView) {
                val shimmer = Shimmer.ColorHighlightBuilder()
                        .setBaseColor(Color.parseColor("#F3F3F3"))
                        .setBaseAlpha(1.0f)
                        .setHighlightColor(Color.parseColor("#E7E7E7"))
                        .setHighlightAlpha(1.0f)
                        .setDropoff(50f)
                        .setDuration(1500)
                        .build()

                val shimmerDrawable = ShimmerDrawable()
                shimmerDrawable.setShimmer(shimmer)

                Glide.with(context).load(data.imageUrl)
                        .placeholder(shimmerDrawable)
                        .into(itemView.photoView)

                photoTitle.text = data.title
                photoDate.text = data.date
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.photo_item, parent, false)
        return PhotoViewHolder(view)
    }

    override fun getItemCount(): Int = photoList.size

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(photoList[position])
    }

    fun addList(items: ArrayList<photoData>) {
        photoList.addAll(items)
        notifyDataSetChanged()
    }

}