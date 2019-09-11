package com.duyp.architecture.clean.redux.app.common

import android.content.Context
import android.text.format.Formatter
import javax.inject.Inject

class AppDataFormatter @Inject constructor(private val context: Context) : DataFormatter {

    override fun getFormattedFileSize(sizeInByte: Long): String {
        return Formatter.formatFileSize(context, sizeInByte)
    }
}