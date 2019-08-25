package com.duyp.architecture.clean.redux.app.features.repo.list.item

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.duyp.architecture.clean.redux.R
import com.duyp.architecture.clean.redux.app.inflate

class RepoViewHolder(
    private val showImage: Boolean,
    parent: ViewGroup,
    onItemClick: (Long) -> Unit
) : RecyclerView.ViewHolder(
    parent.inflate(
        if (showImage) R.layout.repo_item else R.layout.repo_item_no_image
    )
) {

    private var repoId: Long? = null

    init {
        itemView.setOnClickListener {
            repoId?.let { onItemClick(it) }
        }
    }

    fun bindData(data: RepoViewData) {
        repoId = data.id
    }

}