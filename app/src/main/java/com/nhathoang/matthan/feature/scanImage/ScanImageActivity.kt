package com.nhathoang.matthan.feature.scanImage

import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.Rotate
import com.bumptech.glide.request.RequestOptions
import com.yasgard.bartr.base.BaseActivity
import kotlinx.android.synthetic.main.activity_scan_image.*
import java.io.File





class ScanImageActivity : BaseActivity<ScanImagePresenterImp>(), ScanImageView {
    override fun onRecognizerSuccess(data: String?) {

    }

    override fun onRecognizerError(error: String?) {

    }

    override fun getContextView(): BaseActivity<*> {
        return this
    }

    override fun getPresenter(): ScanImagePresenterImp? {
        return ScanImagePresenterImp(this)
    }

    var pathImage: String? = null

    companion object {
        const val IMAGE_PATH = "path"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.nhathoang.matthan.R.layout.activity_scan_image)
        pathImage = intent.getStringExtra(IMAGE_PATH)
        Log.i("DATA RECEIVE", pathImage)
        Glide.with(imgScan)
            .load(File(Uri.parse(pathImage).path)) // Uri of the picture
            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
            .into(imgScan)
//        mPresenter?.postImageToOCRService(Uri.parse(pathImage))
    }
}
