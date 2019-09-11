package com.duyp.architecture.clean.redux.app.common

interface DataFormatter {

    fun getFormattedFileSize(sizeInByte: Long): String
}