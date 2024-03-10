package com.reditum.marvel.ui.components.shimmer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.reditum.marvel.ui.theme.Sizes
import com.reditum.marvel.ui.theme.Sizes.heroListContentPadding

@Composable
fun HeroListPlaceholder(modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Sizes.listSpacing),
        modifier = modifier
            .fillMaxSize()
            .padding(start = heroListContentPadding)
    ) {
        // 3 items for landscape mode
        HeroCardPlaceHolder()
        HeroCardPlaceHolder()
        HeroCardPlaceHolder()
    }
}

@Composable
fun HeroCardPlaceHolder(modifier: Modifier = Modifier) {
    Spacer(
        modifier
            .width(Sizes.heroCardWidth)
            .fillMaxHeight()
            .clip(RoundedCornerShape(Sizes.roundedShapeClipping))
            .background(MaterialTheme.colorScheme.onSurface)
    )
}