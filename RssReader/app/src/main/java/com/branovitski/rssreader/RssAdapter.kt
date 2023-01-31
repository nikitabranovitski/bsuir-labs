package com.branovitski.rssreader

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.branovitski.rssreader.databinding.ListItemBinding

class RssAdapter : ListAdapter<NewsListItem, RssAdapter.RssViewHolder>(RssDiffCallback()) {

    lateinit var onItemClick: (link: String) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RssViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = ListItemBinding.inflate(inflater, parent, false)
        return RssViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: RssViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class RssViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(item: NewsListItem) {

            itemView.setOnClickListener {
                onItemClick(item.link)
            }

            bindName(item.title)
        }

        private fun bindName(name: String) {
            binding.nameEditText.text = name
        }


    }
}

private class RssDiffCallback : DiffUtil.ItemCallback<NewsListItem>() {
    override fun areItemsTheSame(oldItem: NewsListItem, newItem: NewsListItem): Boolean {
        return oldItem.link == newItem.link
    }

    override fun areContentsTheSame(oldItem: NewsListItem, newItem: NewsListItem): Boolean {
        return oldItem == newItem
    }
}
