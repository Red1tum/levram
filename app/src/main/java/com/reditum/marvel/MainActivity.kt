package com.reditum.marvel

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.reditum.marvel.ui.components.shimmer.ShimmerTheme
import com.reditum.marvel.ui.screens.HeroScreen
import com.reditum.marvel.ui.screens.HomeScreen
import com.reditum.marvel.ui.theme.ColorSaver
import com.reditum.marvel.ui.theme.DefaultThemeColor
import com.reditum.marvel.ui.theme.MarvelTheme
import com.valentinilk.shimmer.LocalShimmerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val (themeColor, setThemeColor) = rememberSaveable(stateSaver = ColorSaver) {
                mutableStateOf(
                    DefaultThemeColor
                )
            }
            MarvelTheme(
                themeColor = themeColor
            ) {
                Surface(
                    contentColor = MaterialTheme.colorScheme.onBackground,
                    color = MaterialTheme.colorScheme.inversePrimary,
                    modifier = Modifier.fillMaxSize()
                ) {
                    val navController = rememberNavController()

                    CompositionLocalProvider(
                        LocalShimmerTheme provides ShimmerTheme
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = HOME
                        ) {
                            composable(
                                HOME
                            ) {
                                HomeScreen(navController, setThemeColor)
                            }
                            composable(
                                "hero/{id}",
                                deepLinks = listOf(navDeepLink {
                                    uriPattern = "marvel://hero/{id}"
                                }),
                                arguments = listOf(
                                    navArgument("id") {
                                        type = NavType.StringType
                                    }
                                )
                            ) {
                                HeroScreen(navController, setThemeColor)
                            }
                        }
                    }
                }
            }
        }

        askNotificationPermission()
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}

const val HOME = "home"