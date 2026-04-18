package com.jackappsdev.password_manager.presentation.screens.home

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.jackappsdev.password_manager.presentation.navigation.Navigator
import com.jackappsdev.password_manager.presentation.navigation.Routes
import com.jackappsdev.password_manager.presentation.screens.home.panes.DetailPaneEditPassword
import com.jackappsdev.password_manager.presentation.screens.home.panes.DetailPanePasswordDetail
import com.jackappsdev.password_manager.presentation.screens.home.panes.HomeListPane
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun HomeRoot(navigator: Navigator) {
    val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator<Int>()
    val scope = rememberCoroutineScope()
    var isEditing by rememberSaveable { mutableStateOf(false) }
    var editVersion by rememberSaveable { mutableIntStateOf(0) }

    NavigableListDetailPaneScaffold(
        navigator = scaffoldNavigator,
        listPane = {
            AnimatedPane {
                HomeListPane(
                    onNavigateToPasswordItem = { id ->
                        isEditing = false
                        scope.launch {
                            scaffoldNavigator.navigateTo(
                                ListDetailPaneScaffoldRole.Detail,
                                id
                            )
                        }
                    },
                    onNavigateToAddPassword = {
                        navigator.navigate(Routes.AddPasswordItem)
                    }
                )
            }
        },
        detailPane = {
            AnimatedPane {
                val selectedId = scaffoldNavigator.currentDestination?.contentKey
                val isListVisible = scaffoldNavigator.scaffoldValue[ListDetailPaneScaffoldRole.List] == PaneAdaptedValue.Expanded
                if (selectedId != null) {
                    if (isEditing) {
                        DetailPaneEditPassword(
                            id = selectedId,
                            editVersion = editVersion,
                            navigator = navigator,
                            onEditComplete = {
                                isEditing = false
                                editVersion++
                            },
                            onCancel = {
                                isEditing = false
                                editVersion++
                            }
                        )
                    } else {
                        DetailPanePasswordDetail(
                            id = selectedId,
                            showBackButton = !isListVisible,
                            onEditRequested = { isEditing = true },
                            onNavigateUp = {
                                scope.launch { scaffoldNavigator.navigateBack() }
                            }
                        )
                    }
                }
            }
        }
    )
}
