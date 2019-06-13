package com.nhathoang.matthan.feature.itemNews

import com.yasgard.bartr.base.BaseActivity

interface ItemNewsView {
    fun getContextView() : BaseActivity<*>
    fun onGetListSuccess(list: ArrayList<ResponseListNews.NewsItem>?)
    fun onGetListFailed(error : String?)
}