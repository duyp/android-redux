package com.duyp.architecture.clean.redux.app.widgets

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.duyp.architecture.clean.redux.app.R
import com.duyp.architecture.clean.redux.app.inflate

class PageLoadingViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    parent.inflate(R.layout.item_progress)
)