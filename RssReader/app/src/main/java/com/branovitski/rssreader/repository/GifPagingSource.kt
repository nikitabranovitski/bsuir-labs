/*
package com.branovitski.chililab.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.branovitski.chililab.ui.gif_list.GifListItem
import com.branovitski.rssreader.network.GIFApiService
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

private const val TMDB_STARTING_PAGE_INDEX = 1
const val NETWORK_PAGE_SIZE = 20

class GifPagingSource @Inject constructor(
    private val service: GIFApiService,
    private val query: String? = null
) : PagingSource<Int, GifListItem>() {

    override fun getRefreshKey(state: PagingState<Int, GifListItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GifListItem> {
        val page = params.key ?: TMDB_STARTING_PAGE_INDEX
        val pageSize = params.loadSize
        return try {
            val response =
                if (query != null) service.getSearchedGifs(
                    query,
                    page,
                    pageSize
                ) else service.getGifs(
                    page,
                    pageSize
                )
            val gifs =
                response.body()?.data?.map {
                    GifListItem(
                        it.id,
                        it.title,
                        it.url
                    )
                }
            val nextKey =
                if (gifs!!.isEmpty()) {
                    null
                } else {
                    page + (params.loadSize / NETWORK_PAGE_SIZE)
                }
            LoadResult.Page(
                data = gifs,
                prevKey = if (page == TMDB_STARTING_PAGE_INDEX) null else page,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

}*/
