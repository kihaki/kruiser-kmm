package net.gaw.kruiser.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable

/**
 * Represents a [Destination] in the application, e.g. a Screen or Dialog or anything the user can
 * navigate to.
 */
@Immutable
interface Destination {
    @Composable
    fun Content()
}

