package net.gaw.kruiser.core

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

private typealias MutableBackstack = MutableStateFlow<List<BackStackEntry>>

/**
 * Pops the last [BackStackEntry] from the backstack.
 */
fun MutableBackstack.pop() = update { it.dropLast(1) }

/**
 * Pushes a new [BackStackEntry] to the backstack.
 */
fun MutableBackstack.push(entry: BackStackEntry) = update { it + entry }