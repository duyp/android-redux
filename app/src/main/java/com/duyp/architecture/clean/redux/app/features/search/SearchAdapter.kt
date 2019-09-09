package com.duyp.architecture.clean.redux.app.features.search

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.duyp.architecture.clean.redux.app.features.search.items.HeaderViewHolder
import com.duyp.architecture.clean.redux.app.features.search.items.PageLoadingViewHolder
import com.duyp.architecture.clean.redux.app.features.search.items.repoitem.RepoViewHolder

class SearchAdapter(private val onItemClick: (Long) -> Unit) :
    ListAdapter<SearchItem, RecyclerView.ViewHolder>(searchDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            SearchItem.VIEW_TYPE_REPO -> RepoViewHolder(false, parent) {
                onItemClick(it)
            }
            SearchItem.VIEW_TYPE_HEADER -> HeaderViewHolder(parent)
            else -> PageLoadingViewHolder(
                parent
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position).let {
            when (it) {
                is SearchItem.PublicRepo -> (holder as RepoViewHolder).bindData(
                    it.data
                )
                is SearchItem.RecentRepoHeader -> (holder as HeaderViewHolder).bindData(
                    it.text
                )
                is SearchItem.PublicRepoHeader -> (holder as HeaderViewHolder).bindData(
                    it.text
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int = getItem(position).getViewType()
}
