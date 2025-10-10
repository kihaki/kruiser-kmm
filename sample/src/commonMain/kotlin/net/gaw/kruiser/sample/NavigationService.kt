package net.gaw.kruiser.sample

import kotlinx.coroutines.flow.MutableStateFlow
import net.gaw.kruiser.core.BackStackItem

class NavigationService {
    val backstack = MutableStateFlow(
        listOf(
            BackStackItem(EmojiDestination(1)),
        )
    )
}