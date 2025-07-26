package net.gaw.kruiser.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.SaveableStateHolder
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import net.gaw.kruiser.core.BackStackEntry
import net.gaw.kruiser.ui.BackstackEvent.GROW
import net.gaw.kruiser.ui.BackstackEvent.IDLE
import net.gaw.kruiser.ui.BackstackEvent.SHRINK

/**
 * The latest change to the backstack which can be used to apply different animations.
 */
enum class BackstackEvent {
    GROW, SHRINK, IDLE;
}

data class BackstackTransitionState(
    val event: BackstackEvent,
    val targetContentZIndex: Float,
)

/**
 * A composable that shows the topmost backstack item animated.
 * The destination content is wrapped in a [SaveableStateHolder] keyed by the backstack item’s key.
 */
@Composable
fun BackstackView(
    entries: List<BackStackEntry>,
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
    val currentBackstackEntry = entries.lastOrNull()

    var previousBackstackSize by remember { mutableStateOf(entries.size) }
    val sizeDiff = entries.size - previousBackstackSize

    val event = when {
        sizeDiff > 0 -> GROW
        sizeDiff < 0 -> SHRINK
        else -> IDLE
    }

    val transitionState = BackstackTransitionState(
        event = event,
        targetContentZIndex = entries.size.toFloat(),
    )

    previousBackstackSize = entries.size

    AnimatedContent(
        targetState = currentBackstackEntry,
        transitionSpec = {
            when (transitionState.event) {
                IDLE,
                GROW -> onEnter(transitionState) togetherWith onExit(transitionState)
                SHRINK -> onPopEnter(transitionState) togetherWith onPopExit(transitionState)
            }.apply {
                targetContentZIndex = transitionState.targetContentZIndex
            }
        },
        label = label,
        contentAlignment = contentAlignment,
        contentKey = { it?.key },
        modifier = modifier,
    ) { entry ->
        if (entry != null) {
            val key = remember(entry) { entry.key }
            saveableStateHolder.SaveableStateProvider(key) {
                DisposableEffect(key) {
                    onDispose {
                        // Remove saved state for this backstack entry if it is no longer
                        // on the backstack, using onDispose will trigger this event when the
                        // entry leaves the composition (e.g. tied to UI transitions).
                        if (!entries.contains(entry)) {
                            saveableStateHolder.removeState(key)
                        }
                    }
                }
                CompositionLocalProvider(LocalBackstackEntry provides entry) {
                    entry.destination.Content()
                }
            }
        } else {
            Spacer(modifier = modifier)
        }
    }
}
