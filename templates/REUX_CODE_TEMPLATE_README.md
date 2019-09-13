# Using code template in Android Studio for IS24 Android project

Live templates are “frequently-used or custom code constructs that you can insert into your source code file quickly, efficiently and accurately.”

Reference articles: 
- [Make your own file template in Android Studio][1]
- [Create a group of file templates in Android Studio][2]

## Redux feature
As we are starting implement new features / rebuild old features by using [Redux][3] pattern, which includes some tricky rules and boilerplate code that might take time to create necessary classes even for experienced developers. Therefore using code template is a good way to:
- Reduce time to implement a feature
- Enforce developers to follow our rules to structure a feature codes, making code review easier and more efficient

Purpose: 1 click to generate all necessary structured classes: state machine, state, action, navigation, view model, fragment / activity, tracking, dagger module...

### Install Redux code template

Run this in command line (MacOS). Please make sure you `cd` to your root directory of the project:

`cp -a templates/Redux/. /Applications/Android\ Studio.app/Contents/plugins/android/lib/templates/other/Redux`

Using System Terminal app or Terminal tab in Android Studio which already pointed to project location. It will copy all Redux template code into Android Studio template directory.

Please note that whenever you update Android studio, Redux template folder will be deleted and you have to copy it again.

### Using Redux code template
After installation, you might have to restart Android Studio prior to use it.

To create a new Redux feature, first <b>create a feature package</b> then follow steps in these screenshots:

![First Step](/templates/redux_template_instruction_1.png?raw=true)
![Second Step](/templates/redux_template_instruction_2.png?raw=true)

Then the Android Studio will generate for you all necessary files in selected package.

See example classes generated for Detail feature:

![Example 1](/templates/redux_template_instruction_3.png?raw=true)

Example State Machine:
``` kotlin
package ch.immoscout24.ImmoScout24.v4.feature.detail

import androidx.annotation.VisibleForTesting
import com.freeletics.rxredux.SideEffect
import com.freeletics.rxredux.reduxStore
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable
import ch.immoscout24.ImmoScout24.domain.utils.log.Timber
import javax.inject.Inject

internal interface DetailState {

    // TODO add necessary state fields
    // example: val title: String

    class Init : DetailState {

        override fun toString(): String = "Initial state"
    }

    class SubState1(currentState: DetailState) : DetailState by currentState {

        // TODO override necessary properties for this sub state, the rest will be delegated to given currentState

        override fun toString(): String = this::class.java.simpleName
    }
}

internal sealed class DetailNavigation {

    // TODO define navigation screens

    // examples:

    // navigate to screen A without parameters
    object ScreenA : DetailNavigation()

    // navigate to screenB with parameters
    // data class ScreenB(val param1: String, val param2: Int) : DetailNavigation()

    override fun toString(): String = this::class.java.simpleName
}

internal sealed class DetailAction {

    // TODO define necessary actions

    // action without parameter
    object Example1 : DetailAction()

    // action with parameters
    // data class Example2(val param1: String, val param2: Int) : DetailAction()

    override fun toString(): String = this::class.java.simpleName
}

// INTERNAL ACTIONS

internal object InternalAction1 : DetailAction()

internal class DetailStateMachine @Inject constructor(
    tracking: DetailTracking
) {

    @VisibleForTesting
    val exampleSideEffect: SideEffect<DetailState, DetailAction> = { actions, state ->
        actions.ofType(DetailAction.Example1::class.java)
            .map {
                InternalAction1
            }
            .doOnNext {
                // example navigation call
                nav.accept(DetailNavigation.ScreenA)
            }
    }

    val input: Relay<DetailAction> = PublishRelay.create()

    val nav: Relay<DetailNavigation> = PublishRelay.create()

    val state: Observable<DetailState> = input
        .doOnNext { Timber.v("Action fired: $it") }
        .reduxStore(
            initialState = DetailState.Init(),
            sideEffects = listOf(
                exampleSideEffect,
                tracking.trackingSideEffect
            ),
            reducer = this::reducer
        )
        .distinctUntilChanged()
        .doOnNext { Timber.v("State updated: $it") }

    @VisibleForTesting
    fun reducer(state: DetailState, action: DetailAction): DetailState {
        return when (action) {
            is InternalAction1 -> DetailState.SubState1(state)
            else -> state
        }
    }
}
```
### Modify code template
Feel free to improve the template as long as you can make it better.

Some classes extending base classes (such as ViewModel extends BaseViewModel) need to be adjusted whenever anyone change the base classes (class name or methods).

[1]: https://riggaroo.co.za/custom-file-templates-android-studio/
[2]: https://riggaroo.co.za/custom-file-template-group-android-studiointellij/
[3]: https://github.com/freeletics/RxRedux
