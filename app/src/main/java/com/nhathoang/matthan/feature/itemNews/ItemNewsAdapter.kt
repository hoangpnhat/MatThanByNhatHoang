package com.nhathoang.matthan.feature.itemNews

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.nhathoang.matthan.R
import com.nhathoang.matthan.feature.CatergoryItem
import android.view.LayoutInflater
import android.content.Context
import android.support.constraint.ConstraintLayout

class ItemNewsAdapter(private val listNews: ArrayList<ResponseListNews.NewsItem>?, val callback: ItemNewsCallBack, val context: Context) : RecyclerView.Adapter<ItemNewsAdapter.ItemNewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemNewsViewHolder {
        return ItemNewsViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_category_layout,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return listNews?.size ?: 0
    }

    override fun onBindViewHolder(holder: ItemNewsViewHolder, position: Int) {
        listNews?.let{ list ->
            holder.txtNewsTitle?.text = list[position].title
        }
    }

    class ItemNewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var txtNewsTitle : TextView? = itemView.findViewById(R.id.txtCategoryName)
        var ctrItemNews : ConstraintLayout? = itemView.findViewById(R.id.ctrItemCategory)
    }

    interface ItemNewsCallBack {
        fun onItemClick(item: CatergoryItem)
    }
}