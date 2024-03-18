package com.jagadeesh.passwordmanager.presentation

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jagadeesh.passwordmanager.presentation.composables.BottomNavigationBar
import com.jagadeesh.passwordmanager.presentation.navigation.Routes
import com.jagadeesh.passwordmanager.presentation.screens.add_item.AddItemScreen
import com.jagadeesh.passwordmanager.presentation.screens.home.HomeScreen
import com.jagadeesh.passwordmanager.presentation.screens.item_detail.ItemDetailScreen
import com.jagadeesh.passwordmanager.presentation.screens.password_lock.PasswordLockScreen
import com.jagadeesh.passwordmanager.presentation.screens.password_lock.PasswordLockViewModel
import com.jagadeesh.passwordmanager.presentation.screens.password_generator.PasswordGeneratorScreen
import com.jagadeesh.passwordmanager.presentation.screens.settings.SettingsScreen
import com.jagadeesh.passwordmanager.presentation.theme.PasswordManagerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    private val passwordEntryViewModel: PasswordLockViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { passwordEntryViewModel.state.hasPasswordSet == null }

        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

        setContent {
            PasswordManagerTheme {
                val navController = rememberNavController()

                Scaffold(
                    bottomBar = { BottomNavigationBar(navController = navController) }
                ) { innerPadding ->
                    NavHost(
                        navController,
                        startDestination = Routes.PasswordLock.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Routes.PasswordLock.route) {
                            PasswordLockScreen(navController, passwordEntryViewModel)
                        }

                        composable(Routes.Home.route) {
                            HomeScreen(navController)
                        }

                        composable(Routes.AddItem.route) {
                            AddItemScreen(navController)
                        }

                        composable(Routes.PasswordGenerator.route) {
                            PasswordGeneratorScreen()
                        }

                        composable(Routes.Settings.route) {
                            SettingsScreen(navController)
                        }

                        composable(Routes.ItemDetail.route) {
                            ItemDetailScreen(navController)
                        }
                    }
                }
            }
        }
    }
}
