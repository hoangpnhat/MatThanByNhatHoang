package com.nhathoang.matthan.feature

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import com.nhathoang.matthan.R
import com.yasgard.bartr.base.BaseActivity
import kotlinx.android.synthetic.main.dialog_error.*

class ErrorDialog : DialogFragment() {

    private var mIOnConfirmListener: ErrorDialog.IOnErrorListener? = null
    private var mTitle: String = ""
    private var mMessage: String = ""

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_error)
        (activity as? BaseActivity<*>)?.run {
            if (dialog.window != null) {
                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
            }
        }
        dialog.setCanceledOnTouchOutside(true)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.dialog_error, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvTitle.text = mTitle
        tvMessage.text = mMessage

        tvOKDialog.setOnClickListener {
            mIOnConfirmListener?.confirmed()
            dismiss()
        }
    }

    fun setTitle(title: String) {
        mTitle = title
    }

    fun setMessage(message: String) {
        mMessage = message
    }

    fun setOnConfirmListener(onConfirmListener: ErrorDialog.IOnErrorListener) {
        mIOnConfirmListener = onConfirmListener
    }

    interface IOnErrorListener {
        fun confirmed()
    }
}
