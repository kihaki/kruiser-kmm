package net.gaw.kruiser.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.runtime.compositionLocalOf
import kotlinx.coroutines.flow.MutableStateFlow
import net.gaw.kruiser.core.BackStackItem

val LocalBackstackItem =
    compositionLocalOf<BackStackItem> { error("No LocalBackstackEntry provided.") }

val LocalMutableBackstackState =
    compositionLocalOf<MutableStateFlow<List<BackStackItem>>> { error("No LocalMutableBackstackState provided.") }

val LocalAnimatedVisibilityScope =
    compositionLocalOf<AnimatedVisibilityScope> { error("No LocalAnimatedVisibilityScope provided.") }