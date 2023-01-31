package com.branovitski.rssreader.network

import com.branovitski.rssreader.model.OnlinerResponse
import retrofit2.Response
import retrofit2.http.GET


interface RssApiService {

    @GET("api.json?rss_url=https://www.onliner.by/feed")
    suspend fun getNews(): Response<OnlinerResponse>

}