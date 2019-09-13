package com.duyp.architecture.clean.redux.app.utils.imageloader

import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide

class FragmentGlideImageLoader(fragment: Fragment) : GlideImageLoader(Glide.with(fragment))
