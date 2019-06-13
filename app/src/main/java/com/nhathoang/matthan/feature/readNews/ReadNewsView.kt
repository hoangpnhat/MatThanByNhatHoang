package com.nhathoang.matthan.feature.readNews

import com.yasgard.bartr.base.BaseActivity

interface ReadNewsView{
    fun getContextView() : BaseActivity<*>
    fun getListNewsSuccess(listNews : ArrayList<String>?)
}