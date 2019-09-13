package ${escapeKotlinIdentifiers(packageName)}

import android.os.Bundle
import androidx.fragment.app.Fragment
<#if hasImageLoader>
import ch.immoscout24.ImmoScout24.v4.base.ImageLoader
import ch.immoscout24.ImmoScout24.v4.injection.qualifier.FragmentImageLoader
</#if>
import ch.immoscout24.ImmoScout24.v4.injection.Injectable
import ch.immoscout24.ImmoScout24.v4.util.observe
import javax.inject.Inject

internal class ${featureName}Fragment: Fragment(), Injectable {

    @Inject
    lateinit var viewModel: ${featureName}ViewModel
<#if hasImageLoader>

    @Inject
    @field:FragmentImageLoader
    internal lateinit var imageLoader: ImageLoader
</#if>

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

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
