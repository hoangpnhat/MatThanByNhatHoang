package com.nhathoang.matthan.feature.readNews

import com.nhathoang.matthan.feature.BasePresenter
import com.nhathoang.matthan.feature.CatergoryItem

class ReadNewsPresenterImp (private val mView : ReadNewsView?) : BasePresenter(){

        fun getListCategory(): ArrayList<CatergoryItem>?{
            val listCategory = ArrayList<CatergoryItem>()
            listCategory.add(CatergoryItem("22,8,9", "Văn hoá xã hội, Sự kiện"))
            listCategory.add(CatergoryItem("13", "Sức khoẻ"))
            listCategory.add(CatergoryItem("14", " Giáo dục "))
            listCategory.add(CatergoryItem("20", "Thời tiết"))
            listCategory.add(CatergoryItem("10", "Thế giới"))
            listCategory.add(CatergoryItem("6", "Thể thao"))
            listCategory.add(CatergoryItem("5", "Pháp luật"))
            listCategory.add(CatergoryItem("15,12", "Du lịch, Giải trí"))
            listCategory.add(CatergoryItem("19,11", "Sức mạnh số, Công nghệ"))
            return listCategory
        }

    fun getListNewsById( categoryId : String){

    }
}