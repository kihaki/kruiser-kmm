package net.gaw.kruiser.sample

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import net.gaw.kruiser.sample.di.initKoin

fun main() {
    initKoin()

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Kruiser",
        ) {
            App()
        }
    }
}