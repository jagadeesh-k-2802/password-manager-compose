package com.jackappsdev.password_manager.presentation.screens.manage_categories

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.jackappsdev.password_manager.presentation.navigation.Navigator
import com.jackappsdev.password_manager.presentation.navigation.Routes
import com.jackappsdev.password_manager.presentation.screens.manage_categories.panes.CategoriesListPane
import com.jackappsdev.password_manager.presentation.screens.manage_categories.panes.DetailPaneCategoryDetail
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ManageCategoriesRoot(navigator: Navigator) {
    val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator<Int>()
    val scope = rememberCoroutineScope()

    NavigableListDetailPaneScaffold(
        navigator = scaffoldNavigator,
        listPane = {
            AnimatedPane {
                CategoriesListPane(
                    onNavigateToCategoryItem = { id ->
                        scope.launch {
                            scaffoldNavigator.navigateTo(
                                ListDetailPaneScaffoldRole.Detail,
                                id
                            )
                        }
                    },
                    onNavigateToAddCategory = { navigator.navigate(Routes.AddCategoryItem) },
                    onNavigateUp = { navigator.navigateUp() }
                )
            }
        },
        detailPane = {
            AnimatedPane {
                val selectedId = scaffoldNavigator.currentDestination?.contentKey
                val isListVisible = scaffoldNavigator.scaffoldValue[ListDetailPaneScaffoldRole.List] == PaneAdaptedValue.Expanded
                if (selectedId != null) {
                    DetailPaneCategoryDetail(
                        id = selectedId,
                        showBackButton = !isListVisible,
                        onNavigateUp = { scope.launch { scaffoldNavigator.navigateBack() } }
                    )
                }
            }
        }
    )
}
