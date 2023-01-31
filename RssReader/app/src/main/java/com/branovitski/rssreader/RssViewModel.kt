package com.branovitski.rssreader

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.branovitski.rssreader.model.Item
import com.branovitski.rssreader.repository.RssRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RssViewModel @Inject constructor(private val repository: RssRepository) : ViewModel() {

    val data = MutableLiveData<List<NewsListItem>>()
    val isRefreshingData = MutableLiveData<Boolean>()

    init {
        getNews()
    }

    private fun getNews() {
        isRefreshingData.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getGifs()
                if (response.isSuccessful) {
                    val newListItems = response.body()?.items?.map { it.toNewsListItem() }
                    data.postValue(newListItems)
                }
            } catch (e: Exception) {
                //handle exception
            } finally {
                isRefreshingData.postValue(false)
            }
        }
    }

    fun onSwipedToRefresh() {
        getNews()
    }

    private fun Item.toNewsListItem() = NewsListItem(
        title,
        content,
        link
    )
}

data class NewsListItem(
    val title: String,
    val text: String,
    val link: String
)
