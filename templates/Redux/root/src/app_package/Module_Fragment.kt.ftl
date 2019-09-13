package ${escapeKotlinIdentifiers(packageName)}

import ch.immoscout24.ImmoScout24.v4.base.module.FragmentWithViewModelModule
<#if hasImageLoader>
import ch.immoscout24.ImmoScout24.v4.injection.modules.FragmentImageLoaderModule
</#if>
import ch.immoscout24.ImmoScout24.v4.injection.scopes.FragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
* todo add this module to the activity module at [ch.immoscout24.ImmoScout24.v4.injection.modules.UiModules]
*/
@Module
internal abstract class ${featureName}Module {

    @FragmentScope
<#if hasImageLoader>
    @ContributesAndroidInjector(
        modules = [
            ${featureName}FragmentModule::class,
            FragmentImageLoaderModule::class
        ]
    )
<#else>
    @ContributesAndroidInjector(modules = [${featureName}FragmentModule::class])
</#if>
    abstract fun contribute${featureName}Fragment(): ${featureName}Fragment
}

@Module
internal class ${featureName}FragmentModule : FragmentWithViewModelModule<${featureName}Fragment, ${featureName}ViewModel>()
