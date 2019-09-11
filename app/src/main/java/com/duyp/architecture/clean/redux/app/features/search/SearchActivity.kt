package com.duyp.architecture.clean.redux.app.features.search

import android.os.Bundle
import android.view.View
import com.duyp.architecture.clean.redux.R
import com.duyp.architecture.clean.redux.app.common.*
import com.duyp.architecture.clean.redux.app.features.detail.DetailActivity
import com.duyp.architecture.clean.redux.app.features.search.redux.SearchNavigation
import com.duyp.architecture.clean.redux.app.features.search.redux.SearchViewAction
import com.duyp.architecture.clean.redux.app.utils.infiniteScroller
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : BaseActivity() {

    private val disposables = CompositeDisposable()

    private lateinit var viewModel: SearchViewModel

    lateinit var imageLoader: ImageLoader

    private var textChangeCount = 0

    private var sharedElementTransitionView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        viewModel = getViewModel()
        imageLoader = getImageLoader()

        // list
        val adapter = SearchAdapter(
            imageLoader,
            delegate = object : SearchAdapter.Delegate {

                override fun onItemClick(id: Long, transitionView: View) {
                    sharedElementTransitionView = transitionView
                    viewModel.doAction(SearchViewAction.RepoItemClick(id))
                }

                override fun onReloadClick() {
                    viewModel.doAction(SearchViewAction.ReloadClick)

                }
            }
        )
        searchRecyclerView.adapter = adapter
        searchRecyclerView
            .infiniteScroller {
                viewModel.state.value?.canLoadMore() ?: false
            }
            .subscribe {
                viewModel.doAction(SearchViewAction.LoadNextPage)
            }
            .addTo(disposables)

        val isCreatedFromScreenRotated = savedInstanceState != null
        // search input
        edtSearch.onTextChanged()
            .doOnNext { textChangeCount++ }
            .filter {
                if (isCreatedFromScreenRotated)
                // ignore first time text change after screen rotated
                    textChangeCount > 1
                else
                    true
            }
            .subscribe {
                viewModel.doAction(SearchViewAction.SearchTyping(searchQuery = it))
            }
            .addTo(disposables)

        // don't show soft keyboard again if user rotate screen
        if (!isCreatedFromScreenRotated)
            edtSearch.requestFocus()

        observe(viewModel.state) {
            adapter.submitList(it.items)
        }

        observe(viewModel.navigation) {
            when (it) {
                is SearchNavigation.RepoDetail ->
                    DetailActivity.start(this, sharedElementTransitionView, it.id)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }
}
