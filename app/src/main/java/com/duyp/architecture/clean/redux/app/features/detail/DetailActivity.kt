package com.duyp.architecture.clean.redux.app.features.detail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import com.duyp.architecture.clean.redux.R
import com.duyp.architecture.clean.redux.app.common.*
import com.duyp.architecture.clean.redux.app.features.setRepoNameIsFork
import com.duyp.architecture.clean.redux.app.features.setRepoNameIsPrivate
import com.duyp.architecture.clean.redux.app.utils.ParseDateFormat
import com.duyp.architecture.clean.redux.app.utils.imageloader.ActivityGlideImageLoader
import kotlinx.android.synthetic.main.activity_repo_detail.*
import java.text.NumberFormat

class DetailActivity : BaseActivity() {

    private lateinit var viewModel: DetailViewModel

    private lateinit var imageLoader: ImageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repo_detail)
        viewModel = getViewModel()

        imageLoader = ActivityGlideImageLoader(this)

        observe(viewModel.state) {
            renderRepoData(it)
        }

        val repoId = intent.getLongExtra(AppConstants.EXTRA_REPO_ID, 0)
        val fromRecentRepo = intent.getBooleanExtra(AppConstants.EXTRA_FROM_RECENT_REPO, false)
        // only load repo if this isn't created from screen rotation
        if (savedInstanceState == null) {
            viewModel.loadRepo(repoId, fromRecentRepo)
        }
    }

    private fun renderRepoData(state: DetailState) {
        llRepoData.setVisible(state.viewData != null)
        tvError.setTextOrHideIfEmpty(state.errorMessage)

        state.viewData?.let { data ->
            when {
                data.isFork -> tvTitle.setRepoNameIsFork(data.name)
                data.isPrivate -> tvTitle.setRepoNameIsPrivate(data.name)
                else -> tvTitle.text = data.fullName
            }

            tvDes.text = data.description

            data.ownerAvatarUrl?.let {
                imageLoader.loadImage(imvAvatar, it)
            }

            tvSize.text = data.size

            val numberFormat = NumberFormat.getNumberInstance()
            stars.text = numberFormat.format(data.stargazersCount)
            forks.text = numberFormat.format(data.forks)
            tvDate.text = ParseDateFormat.getTimeAgo(data.updatedAt)

            tvLanguage.setTextOrHideIfEmpty(data.language)
            data.languageColor?.let {
                tvLanguage.setTextColor(it)
            }
        }
    }

    companion object {

        fun start(
            activity: Activity,
            repoId: Long,
            fromRecentRepo: Boolean,
            transitionViews: List<View>
        ) {
            val intent = Intent(activity, DetailActivity::class.java)
            intent.putExtra(AppConstants.EXTRA_REPO_ID, repoId)
            intent.putExtra(AppConstants.EXTRA_FROM_RECENT_REPO, fromRecentRepo)

            val pair = transitionViews.map {
                androidx.core.util.Pair(it, it.transitionName)
            }
                .toTypedArray()

            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, *pair)
            activity.startActivity(intent, options.toBundle())
        }
    }
}
