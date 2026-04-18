package com.jackappsdev.password_manager.presentation.screens.settings

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.jackappsdev.password_manager.presentation.navigation.Navigator
import com.jackappsdev.password_manager.presentation.screens.settings.panes.DetailPaneAndroidWatch
import com.jackappsdev.password_manager.presentation.screens.settings.panes.DetailPaneChangePassword
import com.jackappsdev.password_manager.presentation.screens.settings.panes.DetailPanePin
import com.jackappsdev.password_manager.presentation.screens.settings.panes.SettingsListPane
import kotlinx.coroutines.launch

private const val DETAIL_CHANGE_PASSWORD = "change_password"
private const val DETAIL_PIN = "pin"
private const val DETAIL_ANDROID_WATCH = "android_watch"

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun SettingsRoot(navigator: Navigator) {
    val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator<String>()
    val scope = rememberCoroutineScope()

    NavigableListDetailPaneScaffold(
        navigator = scaffoldNavigator,
        listPane = {
            AnimatedPane {
                SettingsListPane(
                    navigator = navigator,
                    onNavigateToChangePassword = {
                        scope.launch {
                            scaffoldNavigator.navigateTo(
                                ListDetailPaneScaffoldRole.Detail,
                                DETAIL_CHANGE_PASSWORD
                            )
                        }
                    },
                    onNavigateToPin = {
                        scope.launch {
                            scaffoldNavigator.navigateTo(
                                ListDetailPaneScaffoldRole.Detail,
                                DETAIL_PIN
                            )
                        }
                    },
                    onNavigateToAndroidWatch = {
                        scope.launch {
                            scaffoldNavigator.navigateTo(
                                ListDetailPaneScaffoldRole.Detail,
                                DETAIL_ANDROID_WATCH
                            )
                        }
                    }
                )
            }
        },
        detailPane = {
            AnimatedPane {
                val isListVisible = scaffoldNavigator.scaffoldValue[ListDetailPaneScaffoldRole.List] == PaneAdaptedValue.Expanded
                val onNavigateUp: () -> Unit = {
                    scope.launch { scaffoldNavigator.navigateBack() }
                }

                when (scaffoldNavigator.currentDestination?.contentKey) {
                    DETAIL_CHANGE_PASSWORD -> DetailPaneChangePassword(
                        showBackButton = !isListVisible,
                        onNavigateUp = onNavigateUp
                    )

                    DETAIL_PIN -> DetailPanePin(
                        showBackButton = !isListVisible,
                        onNavigateUp = onNavigateUp
                    )

                    DETAIL_ANDROID_WATCH -> DetailPaneAndroidWatch(
                        showBackButton = !isListVisible,
                        onNavigateUp = onNavigateUp
                    )
                }
            }
        }
    )
}
