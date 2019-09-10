package com.duyp.architecture.clean.redux.app.di.scopes

import javax.inject.Scope

/**
 * The FragmentScoped custom scoping annotation specifies that the lifespan of a dependency be
 * the same as that of a Fragment. This is used to annotate dependencies that behave like a
 * singleton within the lifespan of a Fragment
 */
@MustBeDocumented
@Scope
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FILE,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
annotation class FragmentScope
