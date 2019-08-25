package ${escapeKotlinIdentifiers(packageName)}

import androidx.annotation.VisibleForTesting
import com.freeletics.rxredux.SideEffect
import com.freeletics.rxredux.reduxStore
import io.reactivex.Observable
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import ch.immoscout24.ImmoScout24.domain.utils.log.Timber
import javax.inject.Inject

internal interface ${featureName}State {

    // TODO add necessary state fields
    // example: val title: String

    class Init: ${featureName}State {

        override fun toString(): String = "Initial state"
    }

    class SubState1(currentState: ${featureName}State): ${featureName}State by currentState {

        // TODO override necessary properties for this sub state, the rest will be delegated to given currentState

        override fun toString(): String = this::class.java.simpleName
    }
}
<#if hasNavigation>

internal sealed class ${featureName}Navigation {

    // TODO define navigation screens

    // examples:

    // navigate to screen A without parameters
    object ScreenA : ${featureName}Navigation()

    // navigate to screenB with parameters
    // data class ScreenB(val param1: String, val param2: Int) : ${featureName}Navigation()

    override fun toString(): String = this::class.java.simpleName
}
</#if>

internal sealed class ${featureName}Action {

    // TODO define necessary actions

    // action without parameter
    object Example1 : ${featureName}Action()

    // action with parameters
    // data class Example2(val param1: String, val param2: Int) : ${featureName}Action()

    override fun toString(): String = this::class.java.simpleName
}

// INTERNAL ACTIONS

internal object InternalAction1: ${featureName}Action()

<#if hasTracking>
internal class ${featureName}StateMachine @Inject constructor(
    tracking: ${featureName}Tracking
) {
<#else>
internal class ${featureName}StateMachine @Inject constructor() {
</#if>

    @VisibleForTesting
    val exampleSideEffect: SideEffect<${featureName}State, ${featureName}Action> = { actions, state ->
        actions.ofType(${featureName}Action.Example1::class.java)
            .map {
                InternalAction1
            }
<#if hasNavigation>
            .doOnNext {
                // example navigation call
                nav.accept(${featureName}Navigation.ScreenA)
            }
</#if>
    }

    val input: Relay<${featureName}Action> = PublishRelay.create()
<#if hasNavigation>

    val nav: Relay<${featureName}Navigation> = PublishRelay.create()
</#if>

    val state: Observable<${featureName}State> = input
        .doOnNext { Timber.v("Action fired: $it") }
        .reduxStore(
            initialState = ${featureName}State.Init(),
            sideEffects = listOf(
                exampleSideEffect<#if hasTracking>,
                tracking.trackingSideEffect
                </#if>
            ),
            reducer = this::reducer
        )
        .distinctUntilChanged()
        .doOnNext { Timber.v("State updated: $it") }

    @VisibleForTesting
    fun reducer(state: ${featureName}State, action: ${featureName}Action): ${featureName}State {
        return when (action) {
            is InternalAction1 -> ${featureName}State.SubState1(state)
            else -> state
        }
    }
}
