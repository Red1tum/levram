package com.reditum.marvel.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.reditum.marvel.R
import com.reditum.marvel.ui.components.ErrorBox
import com.reditum.marvel.ui.components.HeroList
import com.reditum.marvel.ui.components.shimmer.HeroCardPlaceHolder
import com.reditum.marvel.ui.components.shimmer.HeroListPlaceholder
import com.reditum.marvel.ui.components.shimmer.ShimmerHost
import com.reditum.marvel.ui.theme.Sizes.listSpacing
import com.reditum.marvel.ui.theme.Sizes.logoWidth
import com.reditum.marvel.ui.theme.Sizes.mediumPadding
import com.reditum.marvel.viewmodels.HomeViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    setColor: (Color) -> Unit,
    viewmodel: HomeViewModel = hiltViewModel()
) {
    val heroes by viewmodel.characters.collectAsState()
    val hasErrored by viewmodel.errored.collectAsState()
    val isDark = isSystemInDarkTheme()

    LaunchedEffect(isDark) {
        if (heroes.isNotEmpty()) {
            viewmodel.updateColors(isDark)
        }
    }

    Box(
        modifier = Modifier
            .paint(
                painterResource(id = R.drawable.background),
                contentScale = ContentScale.FillBounds,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )
            .safeDrawingPadding()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(listSpacing)
        ) {
            Image(
                painter = painterResource(id = R.drawable.marvel_logo),
                contentDescription = null,
                modifier = Modifier.width(logoWidth)
            )
            Text(
                color = MaterialTheme.colorScheme.onBackground,
                text = stringResource(R.string.choose_hero),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = mediumPadding)
            )
            if (heroes.isNotEmpty()) {
                HeroList(
                    heroes,
                    onColorChange = setColor,
                    onHeroClicked = { id: Int -> navController.navigate("hero/${id}") },
                    loadMore = { viewmodel.load() },
                    loadingItem = {
                        if (hasErrored) {
                            HeroCardPlaceHolder(Modifier.scale(0.9f)) {
                                ErrorBox(
                                    tryAgain = { viewmodel.load() },
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        } else {
                            ShimmerHost {
                                HeroCardPlaceHolder(Modifier.scale(0.9f))
                            }
                        }
                    },
                    modifier = Modifier
                )
            } else if (!hasErrored) {
                // The first load of app is slow (very optimized api, marvel)
                // so shimmer was added for user to understand that app is doing some work
                ShimmerHost {
                    HeroListPlaceholder()
                }
            } else {
                ErrorBox(
                    tryAgain = { viewmodel.load() },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}