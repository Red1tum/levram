package com.reditum.marvel.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.reditum.marvel.ui.theme.Sizes.mediumPadding
import com.reditum.marvel.ui.theme.Sizes.roundedShapeClipping
import com.reditum.marvel.viewmodels.HeroViewModel

@Composable
fun HeroScreen(
    navController: NavController,
    viewmodel: HeroViewModel = hiltViewModel()
) {
    val hero by viewmodel.hero.collectAsState()

    Box(modifier = Modifier) {
        hero?.let { hero ->
            AsyncImage(
                model = hero.thumbnailUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .safeDrawingPadding()
                    .fillMaxSize()
                    .padding(mediumPadding)
            ) {
                IconButton(onClick = navController::navigateUp) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(roundedShapeClipping))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(mediumPadding)
                ) {
                    Text(
                        hero.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (hero.description.isNotBlank()) {
                        Text(
                            hero.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}