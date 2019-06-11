package com.yasgard.bartr.base

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.load.HttpException
import com.nhathoang.matthan.R
import com.nhathoang.matthan.feature.*
import com.nhathoang.matthan.feature.main.MainActivity
import java.io.File


/**
 * Created by thaivuvo on 2018/04/24
 */

abstract class BaseActivity<BP : BasePresenter> : AppCompatActivity(), IBaseActivity {

    protected var mPresenter: BP? = null


    override fun showErrorMessage(message: String) {
        val dialogError = ErrorDialog()
        dialogError.apply {
            setTitle("ERROR")
            setMessage(message)
        }.show(supportFragmentManager, "")
    }

    var loadingProgress: LoadingProgress? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter = getPresenter()
        loadingProgress = LoadingProgress(this)
    }

    override fun showLoading() {
        if (!loadingProgress?.isShowing!!)
            loadingProgress?.show()
    }

    override fun onDestroy() {
        try {
            if (loadingProgress?.isShowing!!) {
                loadingProgress!!.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        super.onDestroy()
        mPresenter?.onCleared()
    }

    protected abstract fun getPresenter(): BP?

    override fun dismissLoading() {
        if (!isFinishing) {
            if (loadingProgress?.isShowing!!)
                loadingProgress!!.dismiss()
        }
    }

    override fun showDialogError(msg: String): DialogMessage {
        val dialogMessage = DialogMessage(this, msg)
        dialogMessage.show()
        dialogMessage.setOnDismissListener {
            //            if (msg == getString(R.string.unauthorized)) {
//                authenticationFailed()
//            }
        }
        return dialogMessage
    }

    override fun forceDismissLoading() {
        loadingProgress?.forceDismiss()
    }

    override fun showDialogSuccess(msg: String): DialogMessage {
        val dialogMessage = DialogMessage(this, msg)
        dialogMessage.show()
        return dialogMessage
    }

    override fun onResume() {
        super.onResume()
    }

    override fun startActivity(intent: Intent) {
        super.startActivity(intent)
    }

    override fun showDialogPermission(msg: String) {
        showDialogSuccess(msg)
    }

    @SuppressLint("InflateParams")
    override fun showToastSuccess(message: String) {
        val inflater = layoutInflater
        val layout = inflater.inflate(R.layout.view_dialog_add_favorite, null)
        layout.findViewById<TextView>(R.id.tvMessage).text = message
        Toast(applicationContext).apply {
            setGravity(Gravity.CENTER_VERTICAL, 0, 0)
            duration = Toast.LENGTH_SHORT
            view = layout
            show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    }

    override fun showError(throwable: Throwable) {
        val msg = throwable.message
        if (msg != null) {
            showDialogError(msg)
        }
    }

}
