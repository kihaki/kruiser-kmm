package net.gaw.kruiser.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.SaveableStateHolder
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import net.gaw.kruiser.core.BackStackItem
import net.gaw.kruiser.ui.transition.CustomTransitionProvider
import net.gaw.kruiser.ui.transition.BackstackTransitionEvent.Grow
import net.gaw.kruiser.ui.transition.BackstackTransitionEvent.Idle
import net.gaw.kruiser.ui.transition.BackstackTransitionEvent.Shrink
import net.gaw.kruiser.ui.transition.BackstackTransitionState
import net.gaw.kruiser.ui.transition.produceBackstackTransitionState

/**
 * A composable that shows the topmost backstack item animated.
 * The destination content is wrapped in a [SaveableStateHolder] keyed by the backstack item’s key.
 */
@Composable
fun BackstackView(
    items: List<BackStackItem>,
    onEnter: (transitionState: BackstackTransitionState) -> EnterTransition =
        { slideInHorizontally { it } },
    onExit: (transitionState: BackstackTransitionState) -> ExitTransition =
        { slideOutHorizontally { -it } },
    onPopEnter: (transitionState: BackstackTransitionState) -> EnterTransition =
        { slideInHorizontally { -it } },
    onPopExit: (transitionState: BackstackTransitionState) -> ExitTransition =
        { slideOutHorizontally { it } },
    contentAlignment: Alignment = Alignment.Center,
    label: String = "BackstackView",
    modifier: Modifier = Modifier,
) {
    val saveableStateHolder: SaveableStateHolder = rememberSaveableStateHolder()
    val currentBackstackItem = items.lastOrNull()

    val transitionState = produceBackstackTransitionState(items)

    val customTransition = (currentBackstackItem?.destination as? CustomTransitionProvider)
        ?.provideCustomTransition(transitionState)

    AnimatedContent(
        targetState = currentBackstackItem,
        transitionSpec = {
            val transition: ContentTransform = customTransition ?: when (transitionState.event) {
                is Grow,
                    // Idle is treated as grow for transition purposes so that swapping the top
                    // destination for example will show a push transition to the user
                is Idle ->
                    onEnter(transitionState) togetherWith onExit(transitionState)

                is Shrink ->
                    onPopEnter(transitionState) togetherWith onPopExit(transitionState)
            }

            transition.apply {
                targetContentZIndex = transitionState.targetContentZIndex
            }
        },
        label = label,
        contentAlignment = contentAlignment,
        contentKey = { it?.key },
        modifier = modifier,
    ) { item ->
        if (item != null) {
            val key = remember(item) { item.key }
            saveableStateHolder.SaveableStateProvider(key) {
                DisposableEffect(key) {
                    onDispose {
                        // Remove saved state for this backstack item if it is no longer
                        // on the backstack, using onDispose will trigger this event when the
                        // item leaves the composition (e.g. tied to UI transitions).
                        if (!items.contains(item)) {
                            saveableStateHolder.removeState(key)
                        }
                    }
                }
                CompositionLocalProvider(
                    LocalBackstackItem provides item,
                    LocalAnimatedVisibilityScope provides this,
                ) {
                    item.destination.Content()
                }
            }
        } else {
            Spacer(modifier = modifier)
        }
    }
}

