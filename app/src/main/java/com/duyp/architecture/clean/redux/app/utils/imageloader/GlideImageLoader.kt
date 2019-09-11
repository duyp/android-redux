package com.duyp.architecture.clean.redux.app.utils.imageloader

import android.widget.ImageView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.duyp.architecture.clean.redux.app.common.ImageLoader

abstract class GlideImageLoader(private val requestManager: RequestManager) : ImageLoader {

    override fun loadImage(imageView: ImageView, url: String) {
        requestManager
            .load(url)
            .thumbnail(0.2f)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imageView)
    }
}
