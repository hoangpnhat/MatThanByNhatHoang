package com.nhathoang.matthan.feature.itemNews
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class ResponseListNews {
    @SerializedName("status")
    @Expose
    var status: Int? = null
    @SerializedName("results")
    @Expose
    var results: ArrayList<NewsItem>? = null

    inner class Audios {
        @SerializedName("hn_male_xuantin_vdts_48k-hsmm")
        var voice: Voice? = null

    }

    inner class Voice {

        @SerializedName("summary")
        @Expose
        var summary: String? = null
        @SerializedName("content")
        @Expose
        var content: String? = null

    }

    inner class NewsItem {

        @SerializedName("id")
        @Expose
        var id: Int? = null
        @SerializedName("title")
        @Expose
        var title: String? = null
        @SerializedName("audios")
        @Expose
        var audios: Audios? = null

    }

}