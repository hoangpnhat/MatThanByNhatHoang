package com.nhathoang.matthan.feature

import android.content.Context

object ApiUtils{
    fun getAPIService(context: Context): RetrofitInterfaceAPI {
        return RetrofitClient.getClient(context).create(RetrofitInterfaceAPI::class.java)
    }
}