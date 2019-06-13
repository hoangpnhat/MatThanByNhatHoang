package com.nhathoang.matthan.feature.readNews

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.nhathoang.matthan.R
import com.nhathoang.matthan.feature.CatergoryItem
import com.nhathoang.matthan.feature.itemNews.ItemNews
import com.yasgard.bartr.base.BaseActivity
import kotlinx.android.synthetic.main.activity_read_news.*

class ReadNews : BaseActivity<ReadNewsPresenterImp>(), ReadNewsView, CategoryAdapter.CategoryCallback {
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var mAdapter: CategoryAdapter
    private var listCatergoryItem = ArrayList<CatergoryItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_news)
        mPresenter?.getListCategory()?.let {
            layoutManager = LinearLayoutManager(this@ReadNews, LinearLayoutManager.VERTICAL, false)
            rcListCategory?.layoutManager = layoutManager
            mAdapter = CategoryAdapter(it, this, this)
            rcListCategory?.adapter = mAdapter
        }
    }

    override fun getListNewsSuccess(listNews: ArrayList<String>?) {

    }

    override fun getPresenter(): ReadNewsPresenterImp? {
        return ReadNewsPresenterImp(this)
    }

    override fun getContextView(): BaseActivity<*> {
        return this
    }

    override fun onItemClick(item: CatergoryItem) {
        startActivity(Intent(this@ReadNews, ItemNews::class.java).apply {
            putExtra("id", item.id)
            putExtra("title", item.title)
        })
    }
}
