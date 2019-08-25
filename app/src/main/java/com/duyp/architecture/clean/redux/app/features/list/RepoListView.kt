package com.duyp.architecture.clean.redux.app.features.list

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RepoListView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private val adapter: RepoListAdapter = RepoListAdapter {
        delegate?.onRepoClick(it)
    }

    var delegate: Delegate? = null

    init {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        setAdapter(adapter)
    }

    fun bindData(state: RepoListState) {
        adapter.submitList(state.items)
    }

    interface Delegate {

        fun onRepoClick(id: Long)
    }
}