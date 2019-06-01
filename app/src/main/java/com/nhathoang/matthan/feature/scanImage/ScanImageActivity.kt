package com.nhathoang.matthan.feature.scanImage

import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.Rotate
import com.bumptech.glide.request.RequestOptions
import com.nhathoang.matthan.feature.Constant
import com.yasgard.bartr.base.BaseActivity
import kotlinx.android.synthetic.main.activity_scan_image.*
import java.io.File
import java.io.IOException


class ScanImageActivity : BaseActivity<ScanImagePresenterImp>(), ScanImageView {


    override fun onRecognizerSuccess(data: String?) {
        Log.i("data", data)
    }

    override fun onRecognizerError(error: String?) {
        Log.i("error", error)
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
        setContentView(com.nhathoang.matthan.R.layout.activity_scan_image)
        pathImage = intent.getStringExtra(IMAGE_PATH)
        Log.i("DATA RECEIVE", pathImage)
        Glide.with(imgScan)
            .load(File(Uri.parse(pathImage).path)) // Uri of the picture
            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
            .into(imgScan)
//        mPresenter?.postImageToOCRService(Uri.parse(pathImage))
//        mPresenter?.getSpeech("Tôi tên là Phạm Khánh Huy")
        mediaPlayer = MediaPlayer()
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mPresenter?.getSpeechOK("Tôi tên là Phạm Khánh Huy")


    }

    private fun streamAudio(url: String) {
        val position = url.indexOf("https://")
        val link = url.substring(position)
        mediaPlayer.apply {
            try {
                this.setDataSource(link)
                this.prepare()
            } catch (e: IllegalArgumentException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            } catch (e: SecurityException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            } catch (e: IllegalStateException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            } catch (e: IOException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }
            this.start()
        }
    }
    override fun onGetURLSuccess(url: String?) {
        url?.let{
            streamAudio(it)
        }
    }
}

