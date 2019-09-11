package com.duyp.architecture.clean.redux.app.features.search.items.repoitem

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.duyp.architecture.clean.redux.R
import com.duyp.architecture.clean.redux.app.common.ImageLoader
import com.duyp.architecture.clean.redux.app.common.inflate
import com.duyp.architecture.clean.redux.app.common.setTextOrHideIfEmpty
import com.duyp.architecture.clean.redux.app.features.setRepoNameIsFork
import com.duyp.architecture.clean.redux.app.features.setRepoNameIsPrivate
import com.duyp.architecture.clean.redux.app.utils.ParseDateFormat
import kotlinx.android.synthetic.main.item_repo.view.*
import java.text.NumberFormat

class RepoViewHolder(
    private val imageLoader: ImageLoader,
    parent: ViewGroup,
    onItemClick: (Long) -> Unit
) :
    RecyclerView.ViewHolder(parent.inflate(R.layout.item_repo)) {

    private var repoId: Long? = null

    init {
        itemView.setOnClickListener {
            repoId?.let { onItemClick(it) }
        }
    }

    fun bindData(data: RepoViewData) {
        repoId = data.id

        when {
            data.isFork -> itemView.title.setRepoNameIsFork(data.name)
            data.isPrivate -> itemView.title.setRepoNameIsPrivate(data.name)
            else -> itemView.title.text = data.fullName
        }

        itemView.tvDes.text = data.description

        data.ownerAvatarUrl?.let {
            imageLoader.loadImage(itemView.imvAvatar, it)
        }

        itemView.tvSize.text = data.size

        val numberFormat = NumberFormat.getNumberInstance()
        itemView.stars.text = numberFormat.format(data.stargazersCount)
        itemView.forks.text = numberFormat.format(data.forks)
        itemView.date.text = ParseDateFormat.getTimeAgo(data.updatedAt)

        itemView.language.setTextOrHideIfEmpty(data.language)
        data.languageColor?.let {
            itemView.language.setTextColor(it)
        }
    }
}
