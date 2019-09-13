package com.duyp.architecture.clean.redux.app.di.modules

import com.duyp.architecture.clean.redux.app.common.AppDataFormatter
import com.duyp.architecture.clean.redux.app.common.DataFormatter
import dagger.Binds
import dagger.Module

@Module
interface AppBindingModule {

    @Binds
    fun bindDataFormatter(formatter: AppDataFormatter): DataFormatter
}