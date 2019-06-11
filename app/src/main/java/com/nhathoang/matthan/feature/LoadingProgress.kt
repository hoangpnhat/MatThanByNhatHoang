package com.nhathoang.matthan.feature

import android.app.Dialog
import android.content.Context
import android.view.Window
import com.nhathoang.matthan.R

class LoadingProgress(context: Context) : Dialog(context) {

    private var countLoading = 0

    init {

        initLoadingProgress(context)
    }

    private fun initLoadingProgress(context: Context) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.setContentView(R.layout.loading_progress)
        this.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        this.setCancelable(false)
        this.setCanceledOnTouchOutside(false)
    }

    override fun show() {
        if (countLoading == 0) {
            super.show()
        }
        countLoading++
    }

    override fun dismiss() {
        countLoading--
        if (countLoading > 0 || !super.isShowing()) return
        super.dismiss()
    }

    fun forceDismiss() {
        countLoading = 0
        super.dismiss()
    }
}