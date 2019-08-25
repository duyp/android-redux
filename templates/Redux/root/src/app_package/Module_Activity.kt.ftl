package ${escapeKotlinIdentifiers(packageName)}

import ch.immoscout24.ImmoScout24.v4.base.module.ActivityWithViewModelModule
import dagger.Module

@Module
internal class ${featureName}ActivityModule : ActivityWithViewModelModule<${featureName}Activity, ${featureName}ViewModel>()
