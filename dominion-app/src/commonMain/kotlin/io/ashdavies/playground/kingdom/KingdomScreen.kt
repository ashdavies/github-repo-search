package io.ashdavies.playground.kingdom

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import io.ashdavies.http.onLoading
import io.ashdavies.http.produceStateInline
import io.ashdavies.playground.DominionCard
import io.ashdavies.playground.DominionExpansion
import io.ashdavies.playground.DominionRoot.Child.Kingdom
import io.ashdavies.playground.RemoteImage
import io.ashdavies.playground.windowInsetsPadding

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun KingdomScreen(child: Kingdom, viewModel: KingdomViewModel = rememberKingdomViewModel()) {
    val state by produceStateInline { viewModel.getViewState(child.expansion) }
    val scrollBehavior = remember { TopAppBarDefaults.enterAlwaysScrollBehavior() }

    Scaffold(
        topBar = { KingdomTopBar(child.expansion, scrollBehavior) { child.navigateToExpansion() } },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { contentPadding ->
        state.onSuccess {
            KingdomScreen(
                onClick = child::navigateToCard,
                contentPadding = contentPadding,
                kingdom = it,
            )
        }

        state.onLoading {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            )
        }
    }
}

@Composable
private fun KingdomTopBar(
    expansion: DominionExpansion,
    scrollBehavior: TopAppBarScrollBehavior,
    onBack: () -> Unit = { }
) {
    Surface(color = MaterialTheme.colorScheme.surface) {
        LargeTopAppBar(
            title = { Text(expansion.name) },
            navigationIcon = {
                /*Image(
                    contentDescription = expansion.name,
                    modifier = Modifier.fillMaxWidth(),
                    urlString = expansion.image,
                )*/
                BackIconButton(onBack)
            },
            scrollBehavior = scrollBehavior,
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .windowInsetsPadding()
        )
    }
}

@Composable
private fun BackIconButton(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Image(
            painter = rememberVectorPainter(Icons.Filled.ArrowBack),
            contentDescription = null,
        )
    }
}

@Composable
@ExperimentalMaterial3Api
private fun KingdomScreen(
    kingdom: List<DominionCard>,
    contentPadding: PaddingValues,
    onClick: (DominionCard) -> Unit = { },
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier.padding(4.dp),
        contentPadding = contentPadding,
    ) {
        items(kingdom) {
            KingdomCard(it) { onClick(it) }
        }
    }
}

@Composable
@ExperimentalMaterial3Api
private fun KingdomCard(
    value: DominionCard,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { },
) {
    Box(Modifier.padding(4.dp)) {
        Card(
            modifier = modifier
                .clickable(onClick = onClick)
                .aspectRatio(1.0f)
        ) {
            when (val image = value.image) {
                null -> Text(value.name, color = Color.White)
                else -> RemoteImage(
                    modifier = Modifier.aspectRatio(0.62F).height(300.dp),
                    urlString = image
                )
            }
        }
    }
}
