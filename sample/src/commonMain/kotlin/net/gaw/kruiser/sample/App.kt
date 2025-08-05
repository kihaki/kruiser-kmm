package net.gaw.kruiser.sample

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import net.gaw.kruiser.core.BackStackEntry
import net.gaw.kruiser.core.Destination
import net.gaw.kruiser.core.pop
import net.gaw.kruiser.core.push
import net.gaw.kruiser.ui.BackstackView
import net.gaw.kruiser.ui.LocalMutableBackstackState
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class CounterDestination(
    val count: Int,
) : Destination {
    @OptIn(ExperimentalUuidApi::class)
    @Composable
    override fun Content() {
        Surface {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text("The counter is at $count")
                Spacer(modifier = Modifier.height(8.dp))

                val randomSavedHash = rememberSaveable { Uuid.random().toString().takeLast(5) }
                Text("Hash: $randomSavedHash")
                Spacer(modifier = Modifier.height(8.dp))

                val localBackstack = LocalMutableBackstackState.current
                Button(
                    onClick = { localBackstack.push(BackStackEntry(CounterDestination(count + 1))) },
                ) {
                    Text("Push on the stack")
                }
            }
        }
    }
}

class AppViewModel : ViewModel() {
    val backstack = MutableStateFlow(
        listOf(
            BackStackEntry(CounterDestination(1)),
        )
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview
fun App() {
    MaterialTheme {
        val appViewModel = viewModel<AppViewModel>()
        val backstack by appViewModel.backstack.collectAsStateWithLifecycle()

        CompositionLocalProvider(
            LocalMutableBackstackState provides appViewModel.backstack,
        ) {
            Column(
                modifier = Modifier
                    .navigationBarsPadding()
                    .statusBarsPadding()
                    .fillMaxSize(),
            ) {
                BackstackView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .animateContentSize(),
                    entries = backstack,
                )
                backstack.reversed().forEach {
                    Text("${it.destination::class.simpleName} - ${it.key.takeLast(5)}")
                }
            }
        }

        BackHandler(
            enabled = backstack.size > 1,
            onBack = { appViewModel.backstack.pop() }
        )
    }
}