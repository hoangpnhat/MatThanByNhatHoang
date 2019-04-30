package com.nhathoang.matthan

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.Rotate
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_scan_image.*
import java.io.File

class ScanImageActivity : AppCompatActivity() {
    var pathImage: String? = null

    companion object {
        const val IMAGE_PATH = "path"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_image)
        pathImage = intent.getStringExtra(IMAGE_PATH)
        val imageUri = Uri.parse(pathImage)
        Log.i("DATA RECEIVE", pathImage)
        Glide.with(imgScan)
            .load(File(imageUri.path)) // Uri of the picture
            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
            .transform(Rotate(90))
            .into(imgScan)
    }
}
