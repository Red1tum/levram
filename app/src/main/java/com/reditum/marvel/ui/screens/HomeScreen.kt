package com.reditum.marvel.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.reditum.marvel.R
import com.reditum.marvel.data.HeroProvider
import com.reditum.marvel.ui.components.HeroList
import com.reditum.marvel.ui.theme.Sizes.listSpacing
import com.reditum.marvel.ui.theme.Sizes.logoWidth
import com.reditum.marvel.ui.theme.getNetworkImageColor
import com.reditum.marvel.ui.theme.getPrimaryColors
import kotlinx.coroutines.flow.update

@Composable
fun HomeScreen(navController: NavController, setColor: (Color) -> Unit) {
    val context = LocalContext.current
    val heroes by HeroProvider.getHeroes().collectAsState()

    val isDark = isSystemInDarkTheme()

    // basically checking for null to avoid running
    // this launched effect on every screen recomposition
    if (heroes.first().colors == null) {
        LaunchedEffect(Unit) {
            HeroProvider.getHeroes().update {
                it.map { char ->
                    val color = getNetworkImageColor(context, char.url)
                    char.copy(colors = getPrimaryColors(color, isDark))
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .paint(
                painterResource(id = R.drawable.background),
                contentScale = ContentScale.FillBounds,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )
            .statusBarsPadding()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize(),
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
                style = MaterialTheme.typography.titleLarge
            )
            HeroList(
                heroes,
                onColorChange = setColor,
                onHeroClicked = { id: Int -> navController.navigate("hero/${id}") }
            )
        }
    }
}