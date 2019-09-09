package com.duyp.architecture.clean.redux.app.features.search.items

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.duyp.architecture.clean.redux.R
import com.duyp.architecture.clean.redux.app.common.inflate

class PageLoadingViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    parent.inflate(R.layout.item_progress)
)
