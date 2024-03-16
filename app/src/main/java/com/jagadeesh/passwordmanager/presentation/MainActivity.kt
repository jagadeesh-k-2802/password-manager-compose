package com.jagadeesh.passwordmanager.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jagadeesh.passwordmanager.presentation.composables.BottomNavigationBar
import com.jagadeesh.passwordmanager.presentation.navigation.Routes
import com.jagadeesh.passwordmanager.presentation.screens.home.HomeScreen
import com.jagadeesh.passwordmanager.presentation.screens.item_detail.ItemDetailScreen
import com.jagadeesh.passwordmanager.presentation.screens.password_generator.PasswordGeneratorScreen
import com.jagadeesh.passwordmanager.presentation.screens.settings.SettingsScreen
import com.jagadeesh.passwordmanager.presentation.theme.PasswordManagerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PasswordManagerTheme {
                val navController = rememberNavController()

                Scaffold(
                    bottomBar = { BottomNavigationBar(navController = navController) }
                ) { innerPadding ->
                    NavHost(
                        navController,
                        startDestination = Routes.Home.route,
                        Modifier.padding(innerPadding)
                    ) {
                        composable(Routes.Home.route) {
                            HomeScreen(navController)
                        }
                        composable(Routes.PasswordGenerator.route) {
                            PasswordGeneratorScreen(navController)
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
