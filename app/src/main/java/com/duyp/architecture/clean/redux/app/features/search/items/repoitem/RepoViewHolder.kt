package com.duyp.architecture.clean.redux.app.features.search.items.repoitem

import android.graphics.Color
import android.text.format.Formatter
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.duyp.architecture.clean.redux.R
import com.duyp.architecture.clean.redux.app.common.ImageLoader
import com.duyp.architecture.clean.redux.app.common.inflate
import com.duyp.architecture.clean.redux.app.utils.ColorGenerator
import com.duyp.architecture.clean.redux.app.utils.ParseDateFormat
import com.duyp.architecture.clean.redux.app.widgets.LabelSpan
import com.duyp.architecture.clean.redux.app.widgets.SpannableBuilder
import kotlinx.android.synthetic.main.item_repo.view.*
import java.text.NumberFormat

class RepoViewHolder(
    private val imageLoader: ImageLoader,
    parent: ViewGroup,
    onItemClick: (Long) -> Unit
) :
    RecyclerView.ViewHolder(parent.inflate(R.layout.item_repo)) {

    private var repoId: Long? = null

    private var forked: String? = itemView.context.getString(R.string.forked)
    private var privateRepo: String? = itemView.context.getString(R.string.private_repo)
    private var forkColor: Int = itemView.context.resources.getColor(R.color.material_indigo_700)
    private var privateColor: Int = itemView.context.resources.getColor(R.color.material_grey_700)

    init {
        itemView.setOnClickListener {
            repoId?.let { onItemClick(it) }
        }
    }

    fun bindData(data: RepoViewData) {
        repoId = data.id
        when {
            data.fork -> itemView.title.text = SpannableBuilder.builder()
                .append(" $forked ", LabelSpan(forkColor))
                .append(" ")
                .append(data.name, LabelSpan(Color.TRANSPARENT))
            data.private -> itemView.title.text = SpannableBuilder.builder()
                .append(" $privateRepo ", LabelSpan(privateColor))
                .append(" ")
                .append(data.name, LabelSpan(Color.TRANSPARENT))
            else -> itemView.title.text = data.fullName
        }

        itemView.tvDes.text = data.description ?: "No description"
        
        data.ownerAvatarUrl?.let {
            imageLoader.loadImage(itemView.imvAvatar, it)
        }

        // size
        val repoSize = if (data.size > 0) data.size * 1000 else data.size
        itemView.tvSize.text = Formatter.formatFileSize(itemView.context, repoSize)
        val numberFormat = NumberFormat.getNumberInstance()
        itemView.stars.text = numberFormat.format(data.stargazersCount)
        itemView.forks.text = numberFormat.format(data.forks)
        itemView.date.text = ParseDateFormat.getTimeAgo(data.updatedAt)

        // language
        if (data.language.isNullOrEmpty()) {
            itemView.language.text = ""
            itemView.language.visibility = View.GONE
        } else {
            itemView.language.text = data.language
            itemView.language.setTextColor(
                ColorGenerator.getColor(itemView.context, data.language)
            )
            itemView.language.visibility = View.VISIBLE
        }
    }
}
