package com.duyp.architecture.clean.redux.app.utils.imageloader

import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class ActivityGlideImageLoader(activity: AppCompatActivity) : GlideImageLoader(Glide.with(activity))
