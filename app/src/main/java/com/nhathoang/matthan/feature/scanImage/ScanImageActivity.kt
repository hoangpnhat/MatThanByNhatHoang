package com.nhathoang.matthan.feature.scanImage

import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.Rotate
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.nhathoang.matthan.R
import com.nhathoang.matthan.feature.Constant
import com.yasgard.bartr.base.BaseActivity
import kotlinx.android.synthetic.main.activity_scan_image.*
import java.io.File
import java.io.IOException


class ScanImageActivity : BaseActivity<ScanImagePresenterImp>(), ScanImageView {


    override fun onRecognizerSuccess(data: String?) {
        data?.let{
            mPresenter?.getSpeechOK(data)
        }
    }

    override fun onRecognizerError(error: String?) {
        Log.i("error", error)
        if(error == "null text"){
            // voice: khong nhan duoc ki tu, xin vui long thu lai
        }
    }

    override fun getContextView(): BaseActivity<*> {
        return this
    }

    override fun getPresenter(): ScanImagePresenterImp? {
        return ScanImagePresenterImp(this)
    }

    var pathImage: String? = null
    lateinit var mediaPlayer: MediaPlayer

    companion object {
        const val IMAGE_PATH = "path"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_image)
        pathImage = intent.getStringExtra(IMAGE_PATH)
        Log.i("DATA RECEIVE", pathImage)
        // Sau khi lay dc Path se hien thi bang Glide
        Glide.with(imgScan)
            .load(File(Uri.parse(pathImage).path)) // Uri of the picture
            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
            .fitCenter()
            .into(imgScan)
//        mPresenter?.postImageToOCRService(Uri.parse(pathImage))
//        mPresenter?.getSpeech("Tôi tên là Phạm Khánh Huy")
        // Chuan bi media
        mediaPlayer = MediaPlayer()
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)

        // Gui anh len GOOGLE ML VISION URL
//        mPresenter?.postImageToOCRService(Uri.parse(pathImage))
        mPresenter?.getSpeechOK("pham khanh huy")


    }

    private fun streamAudio(url: String) {
        val position = url.indexOf("https://")
        val link = url.substring(position)
        mediaPlayer.apply {
            try {
                this.setDataSource(link)
                this.prepare()
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            } catch (e: SecurityException) {
                e.printStackTrace()
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            this.start()
        }
    }

    override fun onGetURLSuccess(url: String?) {
        url?.let {
            streamAudio(it)
        }
    }
}

