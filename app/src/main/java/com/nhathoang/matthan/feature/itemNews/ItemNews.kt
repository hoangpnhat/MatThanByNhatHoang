package com.nhathoang.matthan.feature.itemNews

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.nhathoang.matthan.R
import com.nhathoang.matthan.feature.CatergoryItem
import com.yasgard.bartr.base.BaseActivity
import kotlinx.android.synthetic.main.activity_item_news.*

class ItemNews : BaseActivity<ItemNewsPresenterImp>(), ItemNewsView, ItemNewsAdapter.ItemNewsCallBack {


    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var mAdapter: ItemNewsAdapter
    private var listNews = ArrayList<ResponseListNews.NewsItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_news)
        layoutManager = LinearLayoutManager(this@ItemNews, LinearLayoutManager.VERTICAL, false)
        rcListNews?.layoutManager = layoutManager
        mAdapter = ItemNewsAdapter(listNews, this@ItemNews, this)
        rcListNews?.adapter = mAdapter
        mPresenter?.getListNews(intent.getStringExtra("id"))
    }

    override fun getPresenter(): ItemNewsPresenterImp? {
        return ItemNewsPresenterImp(this)
    }

    override fun onItemClick(item: CatergoryItem) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getContextView(): BaseActivity<*> {
        return this
    }

    override fun onGetListSuccess(list: ArrayList<ResponseListNews.NewsItem>?) {
        list?.let{
            listNews.clear()
            listNews.addAll(it)
            mAdapter.notifyDataSetChanged()
        }
    }

    override fun onGetListFailed(error: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
