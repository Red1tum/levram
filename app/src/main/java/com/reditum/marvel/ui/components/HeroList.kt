package com.reditum.marvel.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import coil.compose.AsyncImage
import com.reditum.marvel.data.Character
import com.reditum.marvel.ui.theme.DefaultThemeColor
import com.reditum.marvel.ui.theme.Sizes.heroCardWidth
import com.reditum.marvel.ui.theme.Sizes.heroListContentPadding
import com.reditum.marvel.ui.theme.Sizes.listSpacing
import com.reditum.marvel.ui.theme.Sizes.mediumPadding
import com.reditum.marvel.ui.theme.Sizes.roundedShapeClipping
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.abs

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HeroList(
    heroes: List<Character>,
    onColorChange: (Color) -> Unit,
    onHeroClicked: (Int) -> Unit,
    loadMore: () -> Unit,
    loadingItem: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    val state = rememberLazyListState()
    val fling = rememberSnapFlingBehavior(state)

    var centerIdx by remember { mutableIntStateOf(0) }

    LaunchedEffect(state, heroes) {
        snapshotFlow {
            state.layoutInfo.visibleItemsInfo.any { it.key == "loading" }
        }.collectLatest { shouldLoadMore ->
            if (!shouldLoadMore) return@collectLatest
            loadMore()
            if (state.firstVisibleItemIndex >= heroes.size - 1) {
                state.animateScrollToItem(heroes.size - 1)
            }
        }
    }

    LaunchedEffect(centerIdx, heroes) {
        val hero = heroes.getOrNull(centerIdx)
        val color = if (hero != null) hero.colors!!.color else DefaultThemeColor
        onColorChange(color)
    }

    LazyRow(
        state = state,
        flingBehavior = fling,
        horizontalArrangement = Arrangement.spacedBy(listSpacing),
        contentPadding = PaddingValues(horizontal = heroListContentPadding),
        modifier = modifier.fillMaxSize(),
    ) {
        itemsIndexed(heroes) { idx, hero ->
            val scale by remember {
                derivedStateOf {
                    val currentItem =
                        state.layoutInfo.visibleItemsInfo.firstOrNull { it.index == idx }
                            ?: return@derivedStateOf 1.0f
                    val halfRowWidth = state.layoutInfo.viewportSize.width / 2
                    // Don't know how this works
                    // The problem is when we scroll to the left
                    // something happens to item size and because
                    // of this scale starts to grow for a little bit
                    // and then, as we continue to scroll, the behavior
                    // becomes normal
                    val offset = abs(halfRowWidth - currentItem.size) / 2
                    (1f - minOf(
                        1f,
                        abs(currentItem.offset + offset + (currentItem.size / 2f) - halfRowWidth) / halfRowWidth
                    ) * 0.07f)
                }
            }
            HeroCard(
                hero = hero,
                modifier = Modifier
                    .width(heroCardWidth)
                    .fillMaxHeight()
                    .graphicsLayer {
                        scale.also { scale ->
                            // This seems like the easiest way to change center index
                            if (scale in 0.99f..<0.999f && centerIdx != idx) {
                                centerIdx = idx
                            }
                            scaleX = scale
                            scaleY = scale
                        }
                    }
                    .clip(RoundedCornerShape(roundedShapeClipping))
                    .clickable {
                        if (centerIdx != idx) {
                            onColorChange(hero.colors!!.primary)
                        }
                        onHeroClicked(hero.id)
                    }
            )
        }
        item(key = "loading") {
            loadingItem()
        }
    }
}

@Composable
fun HeroCard(hero: Character, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.BottomStart,
        modifier = modifier
    ) {
        AsyncImage(
            model = hero.thumbnail.getUrl(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Text(
            hero.name,
            style = MaterialTheme.typography.titleMedium,
            color = hero.colors?.onPrimary ?: Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(mediumPadding)
                .clip(RoundedCornerShape(roundedShapeClipping))
                .background(hero.colors?.primary ?: Color.Transparent)
        )
    }
}