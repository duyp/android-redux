package ${escapeKotlinIdentifiers(packageName)}

import com.freeletics.rxredux.SideEffect
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

internal class ${featureName}Tracking @Inject constructor() {

    val trackingSideEffect: SideEffect<${featureName}State, ${featureName}Action> = { actions, state ->
        actions.flatMapCompletable {
            // todo call tracking use case, example:
            when (it) {
                is InternalAction1 -> Completable.complete() // replace tracking use case here
                else -> Completable.complete()
            }
                .subscribeOn(Schedulers.io())
                .onErrorComplete()
        }
        .toObservable()
    }
}
