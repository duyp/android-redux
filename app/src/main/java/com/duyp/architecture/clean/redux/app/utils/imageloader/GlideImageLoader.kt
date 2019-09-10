package com.duyp.architecture.clean.redux.app.utils.imageloader

import android.widget.ImageView
import com.bumptech.glide.RequestManager
import com.duyp.architecture.clean.redux.app.common.ImageLoader

abstract class GlideImageLoader(private val requestManager: RequestManager) : ImageLoader {

    override fun loadImage(imageView: ImageView, url: String) {
        requestManager
            .load(url)
            .into(imageView)
    }
}
