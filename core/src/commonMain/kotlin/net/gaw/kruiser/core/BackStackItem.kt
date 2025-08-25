package net.gaw.kruiser.core

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Represents an instance of a [Destination] on the navigation backstack.
 *
 * Since the same [Destination] can have multiple incarnations, the [key] is used to distinguish
 * them on the backstack.
 * The key also serves as unique identifier for saved states and [ViewModel]s.
 *
 * @param destination The screen destination.
 * @param key A unique key used for state saving and ViewModel scoping.
 */
@Immutable
@Serializable
data class BackStackItem(
    val destination: Destination,
    val key: String = generateBackstackItemKey(),
)

@OptIn(ExperimentalUuidApi::class)
fun generateBackstackItemKey() = Uuid.random().toString()