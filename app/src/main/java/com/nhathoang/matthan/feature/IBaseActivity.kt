package com.nhathoang.matthan.feature

interface IBaseActivity {

    fun showDialogSuccess(msg: String): DialogMessage

    fun forceDismissLoading()

    fun showDialogError(msg: String): DialogMessage

    fun dismissLoading()

    fun showLoading()

    fun showDialogPermission(msg: String)

    fun showToastSuccess(message: String)

    fun showErrorMessage(message: String)

    fun showError(throwable: Throwable)
}
