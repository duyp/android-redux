package ${escapeKotlinIdentifiers(packageName)}

import ch.immoscout24.ImmoScout24.v4.base.BaseStateViewModel
import ch.immoscout24.ImmoScout24.v4.util.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

/**
* todo move following code to [ch.immoscout24.ImmoScout24.v4.injection.modules.ViewModelFactoryModule]
*/
@Binds
@IntoMap
@ViewModelKey(${featureName}ViewModelImpl::class)
internal abstract fun bind${featureName}ViewModel(viewModel: ${featureName}ViewModelImpl): ViewModel

internal abstract class ${featureName}ViewModel : BaseStateViewModel<${featureName}State, ${featureName}Action>() {
<#if hasNavigation>

    val nav = SingleLiveEvent<${featureName}Navigation>()
</#if>

}

internal class ${featureName}ViewModelImpl @Inject constructor(
    private val stateMachine: ${featureName}StateMachine
) : ${featureName}ViewModel() {

    init {
        // init state and action
        init(stateMachine.input, stateMachine.state)
<#if hasNavigation>

        // init navigation
        addDisposable {
            stateMachine.nav
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    nav.value = it
                }
        }
</#if>
    }
}
