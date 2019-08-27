package com.duyp.architecture.clean.redux.app.features.search

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

val searchDiffCallback = object : DiffUtil.ItemCallback<SearchItem>() {

    override fun areItemsTheSame(oldItem: SearchItem, newItem: SearchItem): Boolean {
        if (oldItem::class != newItem::class)
            return false

        if (oldItem is SearchItem.RecentRepo && newItem is SearchItem.RecentRepo)
            return oldItem.data.id == newItem.data.id

        if (oldItem is SearchItem.PublicRepo && newItem is SearchItem.PublicRepo)
            return oldItem.data.id == newItem.data.id

        if (oldItem is SearchItem.Loading ||
            oldItem is SearchItem.RecentRepoHeader ||
            oldItem is SearchItem.PublicRepoHeader
        )
            return true

        return false
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: SearchItem, newItem: SearchItem): Boolean {
        // make sure to use data class or object
        return oldItem == newItem
    }
}