package com.duyp.architecture.clean.redux.app.features.search.items

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.duyp.architecture.clean.redux.R
import com.duyp.architecture.clean.redux.app.common.inflate
import kotlinx.android.synthetic.main.item_error.view.*

class ErrorViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    parent.inflate(R.layout.item_error)
) {

    fun bindData(text: String) {
        itemView.tvError.text = text
    }
}
