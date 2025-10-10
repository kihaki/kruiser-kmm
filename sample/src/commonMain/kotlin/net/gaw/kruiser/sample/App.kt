package net.gaw.kruiser.sample

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.update
import org.koin.compose.viewmodel.koinViewModel
import net.gaw.kruiser.core.BackStackItem
import net.gaw.kruiser.core.Destination
import net.gaw.kruiser.core.pop
import net.gaw.kruiser.core.push
import net.gaw.kruiser.sample.di.appModule
import net.gaw.kruiser.ui.BackstackView
import net.gaw.kruiser.ui.LocalMutableBackstackState
import net.gaw.kruiser.ui.transition.DisableTransitions
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class EmojiDestination(
    val count: Int,
) : Destination, DisableTransitions {
    @OptIn(ExperimentalUuidApi::class, ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val localBackstack = LocalMutableBackstackState.current

        Scaffold(
            topBar = { TopAppBar(title = { Text("Destination $count") }) }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround,
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text("Hash from SavedState:")
                        Spacer(modifier = Modifier.weight(1f))
                        val savedHashToTestStateSaving =
                            rememberSaveable { Uuid.random().toString().takeLast(5) }
                        Text(savedHashToTestStateSaving)
                    }
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text("Items on Backstack:")
                        Spacer(modifier = Modifier.weight(1f))
                        val backstackItems by localBackstack.collectAsStateWithLifecycle()
                        Text("${backstackItems.size}")
                    }
                }

                Button(
//                    modifier = with(LocalAnimatedVisibilityScope.current) {
//                        Modifier.animateEnterExit(
//                            enter = fadeIn(),
//                            exit = fadeOut(),
//                        )
//                    },
                    onClick = { localBackstack.push(BackStackItem(EmojiDestination(count + 1))) },
                ) {
                    Text("Push on the stack")
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview
fun App() {
    KoinApplication(
        application = {
            modules(appModule)
        }
    ) {
        MaterialTheme {
            val appViewModel = koinViewModel<AppViewModel>()
            val backstack by appViewModel.backstack.collectAsStateWithLifecycle()

            CompositionLocalProvider(
                LocalMutableBackstackState provides appViewModel.backstack,
            ) {
                Box(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .statusBarsPadding()
                        .fillMaxSize(),
                ) {
                    BackstackView(
                        modifier = Modifier
                            .fillMaxSize()
                            .animateContentSize(),
                        items = backstack,
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomStart)
                            .padding(16.dp),
                    ) {
                        backstack.forEach { entry ->
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .clickable {
                                        appViewModel
                                            .backstack
                                            .update { stack ->
                                                stack.dropLastWhile { it.key != entry.key }
                                            }
                                    }
                                    .background(color = Color.Blue),
                            )
                        }
                    }
                }
            }

            BackHandler(
                enabled = backstack.size > 1,
                onBack = { appViewModel.backstack.pop() }
            )
        }
    }
}