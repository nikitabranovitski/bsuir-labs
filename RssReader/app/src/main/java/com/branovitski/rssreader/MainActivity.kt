package com.branovitski.rssreader

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.branovitski.rssreader.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val binding by viewBinding(ActivityMainBinding::bind)

    private val viewModel by viewModels<RssViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupList()
        setupSwipeToRefresh()

    }

    override fun onBackPressed() {
        if (binding.webView.isVisible) {
            binding.webView.isVisible = false
        }else{
            super.onBackPressed()
        }
    }

    private fun setupSwipeToRefresh() {
        viewModel.isRefreshingData.observe(this) {
            binding.refreshLayout.isRefreshing = it
        }

        binding.refreshLayout.setOnRefreshListener { viewModel.onSwipedToRefresh() }
    }


    private fun setupList() {
        val adapter = RssAdapter().apply {
            onItemClick = { link ->
                binding.webView.loadUrl(link)
                binding.webView.isVisible = true
            }
        }
        binding.list.layoutManager =
            LinearLayoutManager(this)
        binding.list.adapter = adapter

        viewModel.data.observe(this) {
            adapter.submitList(it)
        }
    }
}