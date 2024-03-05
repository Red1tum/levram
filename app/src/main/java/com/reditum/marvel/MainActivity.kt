package com.reditum.marvel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.reditum.marvel.ui.screens.HeroScreen
import com.reditum.marvel.ui.screens.HomeScreen
import com.reditum.marvel.ui.theme.ColorSaver
import com.reditum.marvel.ui.theme.DefaultThemeColor
import com.reditum.marvel.ui.theme.MarvelTheme

class MainActivity : ComponentActivity() {
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
                            arguments = listOf(
                                navArgument("id") {
                                    type = NavType.StringType
                                }
                            )
                        ) { backStackEntry ->
                            val heroId = backStackEntry.arguments?.getString("id")!!.toInt()
                            HeroScreen(navController, heroId)
                        }
                    }
                }
            }
        }
    }
}

const val HOME = "home"