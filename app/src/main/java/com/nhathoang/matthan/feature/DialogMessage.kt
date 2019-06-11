package com.nhathoang.matthan.feature

import android.app.Dialog
import android.content.Context
import android.text.Spanned
import android.view.Window
import android.view.WindowManager
import com.nhathoang.matthan.R
import kotlinx.android.synthetic.main.dialog_message.*


class DialogMessage : Dialog {

    //    internal var onButtonClick: OnButtonClick = null
    private var countLoading = 0

    constructor(context: Context, msg: String?) : super(context) {

        initLoadingProgress(context)
        txtMsg.text = msg ?: ""
    }

    constructor(context: Context, msg: Spanned) : super(context) {

        initLoadingProgress(context)
        txtMsg.text = msg
    }

    /*public void setTextButtonOK(String message) {
        ((Button) findViewById(R.id.btnOk)).setText(message);
    }*/
    private fun initLoadingProgress(context: Context) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.setContentView(R.layout.dialog_message)
        this.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(this.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        this.window!!.attributes = lp
        //this.setCancelable(false)
        //this.setCanceledOnTouchOutside(false)
        /*this.findViewById(R.id.btnOk).setOnClickListener(view -> {
            dismiss();
            if(onButtonClick != null){
                onButtonClick.onClick();
            }
        });*/
    }

    fun showMsg(): DialogMessage {
        if (countLoading == 0) {
            super.show()
        }
        countLoading++
        return this
    }

    fun dismissMsg() {
        countLoading--
        if (countLoading > 0) return
        super.dismiss()
    }

    fun forceDismissMsg() {
        countLoading = 0
        super.dismiss()
    }

    fun setOnButtonClick(onButtonClick: OnButtonClick) {
//        this.onButtonClick = onButtonClick
    }

    interface OnButtonClick {
        fun onClick()
    }
}
