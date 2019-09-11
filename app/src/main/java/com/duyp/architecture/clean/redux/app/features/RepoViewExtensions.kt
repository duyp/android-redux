package com.duyp.architecture.clean.redux.app.features

import android.graphics.Color
import android.widget.TextView
import com.duyp.architecture.clean.redux.R
import com.duyp.architecture.clean.redux.app.widgets.LabelSpan
import com.duyp.architecture.clean.redux.app.widgets.SpannableBuilder

fun TextView.setRepoNameIsPrivate(repoName: String) {
    text = SpannableBuilder.builder()
        .append(
            " ${context.getString(R.string.private_repo)} ",
            LabelSpan(context.resources.getColor(R.color.material_grey_700))
        )
        .append(" ")
        .append(repoName, LabelSpan(Color.TRANSPARENT))
}

fun TextView.setRepoNameIsFork(repoName: String) {
    text = SpannableBuilder.builder()
        .append(
            " ${context.getString(R.string.forked)} ",
            LabelSpan(context.resources.getColor(R.color.material_indigo_700))
        )
        .append(" ")
        .append(repoName, LabelSpan(Color.TRANSPARENT))
}