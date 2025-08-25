package net.gaw.kruiser.core

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

private typealias MutableBackstack = MutableStateFlow<List<BackStackItem>>

/**
 * Pops the last [BackStackItem] from the backstack.
 */
fun MutableBackstack.pop() = update { it.dropLast(1) }

/**
 * Pushes a new [BackStackItem] to the backstack.
 */
fun MutableBackstack.push(entry: BackStackItem) = update { it + entry }