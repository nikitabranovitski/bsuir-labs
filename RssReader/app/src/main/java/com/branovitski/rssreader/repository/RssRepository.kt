package com.branovitski.rssreader.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.branovitski.rssreader.network.RssApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RssRepository @Inject constructor(
    private val service: RssApiService
) {

    suspend fun getGifs() = service.getNews()

}