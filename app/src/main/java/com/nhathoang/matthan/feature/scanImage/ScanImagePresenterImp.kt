package com.nhathoang.matthan.feature.scanImage

import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.document.FirebaseVisionCloudDocumentRecognizerOptions
import com.nhathoang.matthan.feature.BasePresenter
import java.io.File
import java.io.IOException
import java.util.*

class ScanImagePresenterImp(private val mView: ScanImageView?) : BasePresenter() {
    fun postImageToOCRService(imageUri: Uri?) {
//        fun getObservable(data: String?): Observable<String>{
//            var image: FirebaseVisionImage? = null
//            var result = ""
//            try {
//                val imgFile = File(data)
//                if (imgFile.exists()) {
//                    val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
//                    image = FirebaseVisionImage.fromBitmap(myBitmap)
//                }
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//            val options = FirebaseVisionCloudDocumentRecognizerOptions.Builder()
//                .setLanguageHints(Arrays.asList("vi", "vi"))
//                .build()
//            val detector = FirebaseVision.getInstance().getCloudDocumentTextRecognizer(options)
//            image?.let {
//                detector.processImage(it)
//                    .addOnSuccessListener { firebaseVisionText ->
//                        //                    mView?.onRecognizerSuccess(firebaseVisionText.toString())
//                        Log.i("result", firebaseVisionText.text)
//                        result = firebaseVisionText.text
//                    }
//                    .addOnFailureListener { exception ->
//                        //                    mView?.onRecognizerError(exception.message)
//                        Log.i("result", exception.message)
//                        result = exception.message.toString()
//                    }
//            }
//            return Observable.just(result)
//        }
//
//        fun getObserver(): Observer<String>{
//            return object : Observer<String>{
//                override fun onComplete() {
//                }
//
//                override fun onSubscribe(d: Disposable) {
//                }
//
//                override fun onNext(t: String) {
//                    Log.i("data",t)
//                }
//
//                override fun onError(e: Throwable) {
//                }
//            }
//        }
//        mView?.getContextView().apply {
//            val myObserver = getObserver()
//            val myObservable = getObservable(imageUri?.path)
//            myObservable.subscribe(myObserver)
//        }


//        val ocrService = OcrService()
//        ocrService.execute(imageUri)


        var image: FirebaseVisionImage? = null
        var result = ""
        try {
            val imgFile = File(imageUri?.path)
            if (imgFile.exists()) {
                val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                image = FirebaseVisionImage.fromBitmap(myBitmap)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val options = FirebaseVisionCloudDocumentRecognizerOptions.Builder()
            .setLanguageHints(Arrays.asList("vi", "vi"))
            .build()
        val detector = FirebaseVision.getInstance().getCloudDocumentTextRecognizer(options)
        image?.let {
            detector.processImage(it)
                .addOnSuccessListener { firebaseVisionText ->
                    mView?.onRecognizerSuccess(firebaseVisionText.text)
                    Log.i("result", firebaseVisionText.text)

//                    result = firebaseVisionText.text
                }
                .addOnFailureListener { exception ->
                    mView?.onRecognizerError(exception.message)
                    Log.i("result", exception.message)
                    result = exception.message.toString()
                }
        }
    }

//    class OcrService : AsyncTask<Uri, String, String>() {
//        override fun doInBackground(vararg params: Uri?): String {
//
//            return result
//        }
//    }
}