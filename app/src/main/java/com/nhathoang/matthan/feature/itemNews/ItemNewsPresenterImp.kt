package com.nhathoang.matthan.feature.itemNews

import android.util.Log
import com.nhathoang.matthan.feature.ApiUtils
import com.nhathoang.matthan.feature.BasePresenter
import com.nhathoang.matthan.feature.Constant
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ItemNewsPresenterImp(private val mView: ItemNewsView?) : BasePresenter() {
    fun getListNews(categoryId: String?) {
        mView?.apply {
            categoryId?.let {id ->
                getContextView().apply {
                    addDisposable(
                        ApiUtils.getAPIService(this).getListNews(Constant.PAGE, Constant.SIZE, id, Constant.WEB_ID, Constant.VOICE)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnSubscribe { }
                            .doFinally { }
                            .subscribe({
                                Log.i("data", it.toString())
                                it.results?.let{ list ->
                                    mView.onGetListSuccess(list)
                                }

                            }, {
                                Log.i("error", it.message)
                            })
                    )
                }
            }
        }
    }
}