package com.nhathoang.matthan.feature.scanImage

import com.yasgard.bartr.base.BaseActivity

interface ScanImageView {
    fun getContextView() : BaseActivity<*>
    fun onRecognizerSuccess(data :String?)
    fun onRecognizerError(error: String?)
    fun onGetURLSuccess(url : String?)
}