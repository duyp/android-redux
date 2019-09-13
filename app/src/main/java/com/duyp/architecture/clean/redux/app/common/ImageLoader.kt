package com.duyp.architecture.clean.redux.app.common

import android.widget.ImageView

interface ImageLoader {

    fun loadImage(imageView: ImageView, url: String)
}