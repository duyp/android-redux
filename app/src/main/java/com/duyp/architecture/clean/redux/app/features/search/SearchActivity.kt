package com.duyp.architecture.clean.redux.app.features.search

import android.os.Bundle
import com.duyp.architecture.clean.redux.R
import com.duyp.architecture.clean.redux.app.common.BaseActivity

class SearchActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
    }
}
