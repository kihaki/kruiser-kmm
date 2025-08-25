package net.gaw.kruiser.ui.transition

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import net.gaw.kruiser.core.BackStackItem

data class BackstackTransitionState(
    val event: BackstackTransitionEvent,
    val targetContentZIndex: Float,
)

/**
 * The latest change to the backstack which can be used to apply different animations.
 */
sealed interface BackstackTransitionEvent {
    data class Idle(val size: Int) : BackstackTransitionEvent

    sealed interface BackstackSizeChange : BackstackTransitionEvent {
        val fromSize: Int
        val toSize: Int
    }

    data class Grow(
        override val fromSize: Int,
        override val toSize: Int,
    ) : BackstackSizeChange

    data class Shrink(
        override val fromSize: Int,
        override val toSize: Int,
    ) : BackstackSizeChange
}

@Composable
internal fun produceBackstackTransitionState(
    items: List<BackStackItem>,
): BackstackTransitionState {
    var previousBackstackSize by remember { mutableStateOf(items.size) }
    val addedItemsCount = items.size - previousBackstackSize

    val event = when {
        addedItemsCount > 0 -> BackstackTransitionEvent.Grow(
            fromSize = previousBackstackSize,
            toSize = items.size,
        )

        addedItemsCount < 0 -> BackstackTransitionEvent.Shrink(
            fromSize = previousBackstackSize,
            toSize = items.size,
        )

        else -> BackstackTransitionEvent.Idle(size = items.size)
    }

    val transitionState = BackstackTransitionState(
        event = event,
        targetContentZIndex = items.size.toFloat(),
    )

    previousBackstackSize = items.size

    return transitionState
}