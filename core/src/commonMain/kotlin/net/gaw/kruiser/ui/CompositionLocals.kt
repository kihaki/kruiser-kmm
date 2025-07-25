package net.gaw.kruiser.ui

import androidx.compose.runtime.compositionLocalOf
import kotlinx.coroutines.flow.MutableStateFlow
import net.gaw.kruiser.core.BackStackEntry

val LocalBackstackEntry =
    compositionLocalOf<BackStackEntry> { error("No LocalBackstackEntry provided.") }

val LocalMutableBackstackState =
    compositionLocalOf<MutableStateFlow<List<BackStackEntry>>> { error("No LocalMutableBackstackState provided.") }