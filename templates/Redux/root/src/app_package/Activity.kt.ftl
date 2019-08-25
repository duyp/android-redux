package ${escapeKotlinIdentifiers(packageName)}

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import ch.immoscout24.ImmoScout24.v4.base.BaseActivity
import ch.immoscout24.ImmoScout24.v4.injection.Injectable
<#if hasImageLoader>
import ch.immoscout24.ImmoScout24.v4.base.ImageLoader
import ch.immoscout24.ImmoScout24.v4.injection.modules.ActivityImageLoaderModule
import ch.immoscout24.ImmoScout24.v4.injection.qualifier.ActivityImageLoader
</#if>
import ch.immoscout24.ImmoScout24.v4.util.observe
import javax.inject.Inject

/**
* todo move following code to [ch.immoscout24.ImmoScout24.v4.injection.modules.UiModules]
*/
@ActivityScope
@ContributesAndroidInjector(
<#if hasImageLoader>
    modules = [${featureName}ActivityModule::class]
<#else>
    modules = [
        ${featureName}ActivityModule::class,
        ActivityImageLoaderModule::class
    ]
</#if>
)
internal abstract fun ${featureName}Activity(): ${featureName}Activity

internal class ${featureName}Activity: BaseActivity(), Injectable {

    @Inject
    lateinit var viewModel: ${featureName}ViewModel
<#if hasImageLoader>

        @Inject
        @field:ActivityImageLoader
        internal lateinit var imageLoader: ImageLoader
</#if>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // todo initialize stuffs

        // state
        observe(viewModel.state) {
            // todo render
        }
<#if hasNavigation>

        // navigation
        observe(viewModel.nav) {
            // todo navigation
            when (it) {
                is ${featureName}Navigation.ScreenA -> {

                }
            }
        }
</#if>

        // example action call
        viewModel.doAction(${featureName}Action.Example1)
    }
}
