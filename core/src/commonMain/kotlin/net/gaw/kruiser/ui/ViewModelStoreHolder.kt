package net.gaw.kruiser.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

/**
 * A [ViewModelStoreOwner] implementation that holds ViewModels for backstack entries.
 * ViewModels are scoped to the backstack entry's key and cleared when the entry is removed.
 */
class BackstackViewModelStoreOwner(
    override val viewModelStore: ViewModelStore
) : ViewModelStoreOwner

/**
 * Holds and manages [ViewModelStore] instances for backstack entries.
 * Each backstack entry gets its own ViewModelStore identified by the entry's key.
 */
class ViewModelStoreHolder {
    private val stores = mutableMapOf<String, ViewModelStore>()
    
    /**
     * Gets or creates a ViewModelStore for the given key.
     */
    fun getOrCreate(key: String): ViewModelStore {
        return stores.getOrPut(key) { ViewModelStore() }
    }
    
    /**
     * Removes and clears the ViewModelStore for the given key.
     * This will cause all ViewModels in the store to be cleared.
     */
    fun remove(key: String) {
        stores.remove(key)?.clear()
    }
    
    /**
     * Checks if a ViewModelStore exists for the given key.
     */
    fun contains(key: String): Boolean = stores.containsKey(key)
}

/**
 * Remembers a [ViewModelStoreHolder] across recomposition.
 */
@Composable
fun rememberViewModelStoreHolder(): ViewModelStoreHolder {
    return remember { ViewModelStoreHolder() }
}