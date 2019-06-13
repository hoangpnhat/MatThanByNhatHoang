package com.nhathoang.matthan.feature.readNews

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.nhathoang.matthan.R
import com.nhathoang.matthan.feature.CatergoryItem
import android.view.LayoutInflater
import android.content.Context
import android.support.constraint.ConstraintLayout

class CategoryAdapter(
    private val listCategoryItem: ArrayList<CatergoryItem>?,
    val callback: CategoryCallback,
    val context: Context) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.item_category_layout,parent,false))
    }

    override fun getItemCount(): Int {
        return listCategoryItem?.size ?: 0
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
//        holder.ctrItemCategory?.setOnClickListener {
//            callback.onItemClick()
//        }
        listCategoryItem?.let{ list ->
            holder.txtCategoryName?.text =  list[position].title
            holder.ctrItemCategory?.setOnClickListener {
                callback.onItemClick(list[position])
            }
        }
    }

    class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var ctrItemCategory: ConstraintLayout? = itemView.findViewById(R.id.ctrItemCategory)
        var txtCategoryName : TextView? = itemView.findViewById(R.id.txtCategoryName)
    }

    interface CategoryCallback {
        fun onItemClick(item: CatergoryItem)
    }
}