package com.duyp.architecture.clean.redux.app.features.search.items.repoitem

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.duyp.architecture.clean.redux.R
import com.duyp.architecture.clean.redux.app.inflate

class RepoViewHolder(isShowImage: Boolean, parent: ViewGroup, onItemClick: (Long) -> Unit) :
    RecyclerView.ViewHolder(
        parent.inflate(
            if (isShowImage) R.layout.item_repo else R.layout.item_repo_no_image
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