package net.gaw.kruiser.ui.transition

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import net.gaw.kruiser.core.Destination
import net.gaw.kruiser.ui.LocalAnimatedVisibilityScope

/**
 * Adds a custom transition override for a [Destination]
 */
interface CustomTransitionProvider {
    @Composable
    fun provideCustomTransition(transitionState: BackstackTransitionState): ContentTransform
}

/**
 * Disables Transitions for this [Destination].
 * This can be used together with [LocalAnimatedVisibilityScope] for providing fancy custom transitions.
 */
interface DisableTransitions : CustomTransitionProvider {
    @Composable
    override fun provideCustomTransition(transitionState: BackstackTransitionState): ContentTransform =
        EnterTransition.None togetherWith ExitTransition.None
}