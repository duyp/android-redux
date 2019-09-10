package com.duyp.architecture.clean.redux.app.features.search

import android.os.Bundle
import com.duyp.architecture.clean.redux.R
import com.duyp.architecture.clean.redux.app.common.*
import com.duyp.architecture.clean.redux.app.features.search.redux.SearchViewAction
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : BaseActivity() {

    private val disposables = CompositeDisposable()

    private lateinit var viewModel: SearchViewModel

    lateinit var imageLoader: ImageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        viewModel = getViewModel()
        imageLoader = getImageLoader()

        // list
        val adapter = SearchAdapter(imageLoader, onItemClick = {
            viewModel.doAction(SearchViewAction.RepoItemClick(it))
        })
        searchRecyclerView.adapter = adapter

        // search input
        edtSearch.onTextChanged()
            .subscribe {
                viewModel.doAction(SearchViewAction.SearchTyping(searchQuery = it))
            }
            .addTo(disposables)

        observe(viewModel.state) {
            adapter.submitList(it.items)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }
}
