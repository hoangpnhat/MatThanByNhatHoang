package com.nhathoang.matthan.feature
import com.nhathoang.matthan.feature.itemNews.ResponseListNews
import io.reactivex.Single
import okhttp3.ResponseBody
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

    @GET(Constant.BASE_URL_V2)
    fun getListNews(@Query("page") page: String,
                    @Query("size") size: String,
                    @Query("categoryIds") categoryIds: String,
                    @Query("websiteIds") websiteIds: String,
                    @Query("voiceIds") voiceIds: String) : Single<ResponseListNews>

}