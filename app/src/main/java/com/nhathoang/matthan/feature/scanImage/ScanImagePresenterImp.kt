package com.nhathoang.matthan.feature.scanImage

import android.graphics.BitmapFactory
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.AsyncTask
import android.util.Log
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.document.FirebaseVisionCloudDocumentRecognizerOptions
import com.nhathoang.matthan.feature.ApiUtils
import com.nhathoang.matthan.feature.BasePresenter
import com.nhathoang.matthan.feature.Constant
import com.nhathoang.matthan.feature.main.MainActivity
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.*
import java.io.File
import java.io.IOException
import java.util.*

class ScanImagePresenterImp(private val mView: ScanImageView?) : BasePresenter() {

    fun postImageToOCRService(imageUri: Uri?) {
        // chay ngam
        val ocrService = OcrService(mView)
        ocrService.execute(imageUri)
    }

    class OcrService(private val mView: ScanImageView?) : AsyncTask<Uri, String, String>() {
        override fun doInBackground(vararg params: Uri?): String {
            var image: FirebaseVisionImage? = null
            try {
                val imgFile = File(params[0]?.path)
                if (imgFile.exists()) {
                    /// Decode anh thanh dang bitmap
                    val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                    image = FirebaseVisionImage.fromBitmap(myBitmap)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

            // Set ngon ngu de nhan dien
            val options = FirebaseVisionCloudDocumentRecognizerOptions.Builder()
                .setLanguageHints(Arrays.asList("vi", "vi"))
                .build()


            /// Detector de xu li anh.
            val detector = FirebaseVision.getInstance().getCloudDocumentTextRecognizer(options)
            image?.let {
                detector.processImage(it)
                    .addOnSuccessListener { firebaseVisionText ->
                        if(firebaseVisionText == null){
                            mView?.onRecognizerError("null text") // tra ve rong
                        } else{
                            mView?.onRecognizerSuccess(firebaseVisionText.text) // tra ve success
                            Log.i("result", firebaseVisionText.text)
                        }
                    }
                    .addOnFailureListener { exception ->
                        mView?.onRecognizerError(exception.message)  // tra ve loi
                        Log.i("result", exception.message)
                    }
            }
            return ""
        }
    }

    fun getSpeech(inputText: String?) {
        mView?.apply {
            inputText?.let {
                getContextView().apply {
                    addDisposable(
                        ApiUtils.getAPIService(this).getUserDetail(
                            Constant.APP_ID,
                            Constant.KEY,
                            Constant.VOICE,
                            Constant.RATE,
                            Constant.TIME,
                            Constant.USER_ID,
                            Constant.SERVICE_TYPE,
                            inputText
                        ).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnSubscribe { }
                            .doFinally { }
                            .subscribe({
                                Log.i("data", it.string())


                            }, {
                                Log.i("error", it.message)
                            })
                    )
                }
            }
        }
    }


    fun getSpeechOK(text: String) {
        val client = OkHttpClient()
        val urlBuidler = HttpUrl.parse(Constant.BASE_URL+"tts")?.newBuilder()
        urlBuidler?.addQueryParameter("app_id", Constant.APP_ID)
        urlBuidler?.addQueryParameter("key", Constant.KEY)
        urlBuidler?.addQueryParameter("voice", Constant.VOICE)
        urlBuidler?.addQueryParameter("rate", Constant.VOICE)
        urlBuidler?.addQueryParameter("time", Constant.TIME)
        urlBuidler?.addQueryParameter("user_id",Constant.USER_ID.toString())
        urlBuidler?.addQueryParameter("service_type",Constant.SERVICE_TYPE.toString())
        urlBuidler?.addQueryParameter("input_text",text)

        val urlRequest = urlBuidler?.build().toString()
        val request = Request.Builder().url(urlRequest).build()


        client.run {
            newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {
                    Log.i("data",response.toString())
                    mView?.onGetURLSuccess(response.toString())
                }

            })
        }
    }
}