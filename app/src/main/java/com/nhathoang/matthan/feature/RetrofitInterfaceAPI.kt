package com.nhathoang.matthan.feature
import com.nhathoang.matthan.feature.Constant
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RetrofitInterfaceAPI{
    @POST(Constant.BASE_URL+"tts")
    fun getUserDetail(@Query("app_id") app_id: String,
                      @Query("key") key: String,
                      @Query("voice") voice: String,
                      @Query("rate") rate: Int,
                      @Query("time") time: String,
                      @Query("user_id") userId: Int,
                      @Query("service_type") serviceType: Int,
                      @Query("input_text") inputText : String ) : Single<ResponseBody>
}