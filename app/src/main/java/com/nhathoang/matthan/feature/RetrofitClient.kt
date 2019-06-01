package com.nhathoang.matthan.feature

import android.content.Context
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import com.google.gson.GsonBuilder
import com.google.gson.Gson



object RetrofitClient {
    private const val CACHE_SIZE = 10 * 1024 * 1024
    private const val CONNECT_TIMEOUT = 180 // ms
    private const val READ_TIMEOUT = 180 // ms
    private const val WRITE_TIMEOUT = 180 // ms

    private var retrofit: Retrofit? = null
    private var okHttpClient: OkHttpClient? = null

    fun getClient(context: Context): Retrofit {
        if (retrofit == null) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            okHttpClient = OkHttpClient.Builder()
                .cache(Cache(context.cacheDir, CACHE_SIZE.toLong()))
                .addInterceptor(logging)
                .addInterceptor {
                    if (!NetworkUtil.isConnected(context))
                        throw NoConnectionException("No network")

                    val request = it.request()
                    val builder = request.newBuilder()
                    it.proceed(builder.build())

                }
                .connectTimeout(CONNECT_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build()

            val gson = GsonBuilder()
                .setLenient()
                .create()
            retrofit = Retrofit.Builder()
                .client(okHttpClient!!)
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }
        return retrofit!!
    }
}