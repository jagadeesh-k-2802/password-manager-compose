package com.jackappsdev.password_manager.presentation.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.scene.SinglePaneSceneStrategy
import androidx.navigation3.ui.NavDisplay
import com.jackappsdev.password_manager.presentation.components.BottomNavigationBar
import com.jackappsdev.password_manager.presentation.screens.add_category_item.AddCategoryItemRoot
import com.jackappsdev.password_manager.presentation.screens.add_password_item.AddPasswordItemRoot
import com.jackappsdev.password_manager.presentation.screens.android_watch.AndroidWatchRoot
import com.jackappsdev.password_manager.presentation.screens.category_item_detail.CategoryItemDetailRoot
import com.jackappsdev.password_manager.presentation.screens.change_password.ChangePasswordRoot
import com.jackappsdev.password_manager.presentation.screens.edit_password_item.EditPasswordItemRoot
import com.jackappsdev.password_manager.presentation.screens.home.HomeRoot
import com.jackappsdev.password_manager.presentation.screens.manage_categories.ManageCategoriesRoot
import com.jackappsdev.password_manager.presentation.screens.password_generator.PasswordGeneratorRoot
import com.jackappsdev.password_manager.presentation.screens.password_item_detail.PasswordItemDetailRoot
import com.jackappsdev.password_manager.presentation.screens.password_lock.PasswordLockRoot
import com.jackappsdev.password_manager.presentation.screens.password_lock.PasswordLockViewModel
import com.jackappsdev.password_manager.presentation.screens.pin.PinRoot
import com.jackappsdev.password_manager.presentation.screens.settings.SettingsRoot
import kotlinx.coroutines.flow.collectLatest

@Composable
fun Router(
    navigator: Navigator,
    navigationState: NavigationState,
    passwordLockViewModel: PasswordLockViewModel
) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navigator, navigationState) }
    ) { contentPadding ->
        LaunchedEffect(key1 = Unit) {
            passwordLockViewModel.hasBeenUnlockedFlow.collectLatest { hasBeenUnlocked ->
                if (hasBeenUnlocked) {
                    navigator.replace(Routes.Home)
                } else {
                    navigator.replace(Routes.PasswordLock)
                }
            }
        }

        val entryProvider = entryProvider {
            featureLockGraph(passwordLockViewModel = passwordLockViewModel)
            featureUnlockedGraph(navigator = navigator)
        }

        NavDisplay(
            entries = navigationState.toEntries(entryProvider),
            onBack = { navigator.navigateUp() },
            sceneStrategy = remember { SinglePaneSceneStrategy() },
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            popTransitionSpec = { fadeIn() togetherWith fadeOut() },
            predictivePopTransitionSpec = { fadeIn() togetherWith fadeOut() },
            modifier = Modifier
                .padding(contentPadding)
                .consumeWindowInsets(contentPadding)
        )
    }
}

private fun EntryProviderScope<NavKey>.featureLockGraph(
    passwordLockViewModel: PasswordLockViewModel
) {
    entry<Routes.PasswordLock> { PasswordLockRoot(passwordLockViewModel) }
}

private fun EntryProviderScope<NavKey>.featureUnlockedGraph(
    navigator: Navigator
) {
    entry<Routes.Home> { HomeRoot(navigator) }
    entry<Routes.AddPasswordItem>(metadata = verticalTransition) { AddPasswordItemRoot(navigator) }
    entry<Routes.PasswordItemDetail> { key -> PasswordItemDetailRoot(navigator, key) }
    entry<Routes.EditPasswordItem> { key -> EditPasswordItemRoot(navigator, key) }
    entry<Routes.PasswordGenerator> { PasswordGeneratorRoot() }
    entry<Routes.Settings> { SettingsRoot(navigator) }
    entry<Routes.AndroidWatch> { AndroidWatchRoot(navigator) }
    entry<Routes.Pin> { PinRoot(navigator) }
    entry<Routes.ChangePassword> { ChangePasswordRoot(navigator) }
    entry<Routes.ManageCategories> { ManageCategoriesRoot(navigator) }
    entry<Routes.AddCategoryItem> { AddCategoryItemRoot(navigator) }
    entry<Routes.CategoryItemDetail> { key -> CategoryItemDetailRoot(navigator, key) }
}
