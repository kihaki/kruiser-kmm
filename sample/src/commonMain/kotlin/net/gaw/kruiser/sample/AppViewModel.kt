package net.gaw.kruiser.sample

import androidx.lifecycle.ViewModel

class AppViewModel(
    private val navigationService: NavigationService,
) : ViewModel() {
    val backstack = navigationService.backstack
}