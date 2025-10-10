package net.gaw.kruiser.sample.di

import net.gaw.kruiser.sample.AppViewModel
import net.gaw.kruiser.sample.NavigationService
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { NavigationService() }
    viewModel { AppViewModel(get()) }
}
