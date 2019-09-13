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

    private var sharedElementTransitionViews: List<View>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        viewModel = getViewModel()
        imageLoader = getImageLoader()

        // list
        val adapter = SearchAdapter(
            imageLoader,
            delegate = object : SearchAdapter.Delegate {

                override fun onRecentRepoItemClick(id: Long, transitionViews: List<View>) {
                    sharedElementTransitionViews = transitionViews
                    viewModel.doAction(SearchViewAction.RecentRepoItemClick(id))
                }

                override fun onPublicRepoItemClick(id: Long, transitionViews: List<View>) {
                    sharedElementTransitionViews = transitionViews
                    viewModel.doAction(SearchViewAction.PublicRepoItemClick(id))
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
                viewModel.doAction(SearchViewAction.ScrollToEnd)
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
                is SearchNavigation.RecentRepoDetail ->
                    openDetail(repoId = it.id, fromRecentRepo = true)

                is SearchNavigation.PublicRepoDetail ->
                    openDetail(repoId = it.id, fromRecentRepo = false)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    private fun openDetail(repoId: Long, fromRecentRepo: Boolean) {
        DetailActivity.start(
            this,
            repoId,
            fromRecentRepo,
            transitionViews = sharedElementTransitionViews ?: emptyList()
        )
    }
}
