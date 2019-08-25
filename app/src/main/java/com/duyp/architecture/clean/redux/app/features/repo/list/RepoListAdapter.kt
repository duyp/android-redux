package com.duyp.architecture.clean.redux.app.features.repo.list

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.duyp.architecture.clean.redux.app.features.repo.list.item.RepoViewHolder
import com.duyp.architecture.clean.redux.app.features.shared.PageLoadingViewHolder

private val diffCallback = object : DiffUtil.ItemCallback<RepoListItem>() {

    override fun areItemsTheSame(oldItem: RepoListItem, newItem: RepoListItem): Boolean {
        if (oldItem is RepoListItem.Repo && newItem is RepoListItem.Repo) {
            return oldItem.data.id == newItem.data.id
        }

        if (oldItem is RepoListItem.Loading && newItem is RepoListItem.Loading) {
            return true
        }

        return false
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: RepoListItem, newItem: RepoListItem): Boolean {
        // make sure to use data class or object
        return oldItem == newItem
    }
}

class RepoListAdapter(private val onItemClick: (Long) -> Unit) :
    ListAdapter<RepoListItem, RecyclerView.ViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            RepoListItem.VIEW_TYPE_REPO -> RepoViewHolder(false, parent) {
                onItemClick(it)
            }
            else -> PageLoadingViewHolder(
                parent
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position).let {
            when (holder) {
                is RepoViewHolder -> holder.bindData((it as RepoListItem.Repo).data)
            }
        }
    }

    override fun getItemViewType(position: Int): Int = getItem(position).getViewType()

}