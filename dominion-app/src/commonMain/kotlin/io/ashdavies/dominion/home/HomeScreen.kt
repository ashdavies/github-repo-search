package io.ashdavies.dominion.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.ashdavies.dominion.DominionExpansion
import io.ashdavies.graphics.rememberAsyncImagePainter
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.http.onLoading
import io.ashdavies.http.produceStateInline
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun HomeScreen(modifier: Modifier = Modifier, onClick: (DominionExpansion) -> Unit = { }) {
    val httpClient = LocalHttpClient.current
    val expansionService = remember(httpClient) {
        ExpansionService(httpClient)
    }

    val state by produceStateInline {
        expansionService.getExpansionList()
    }

    Scaffold(
        topBar = { CenterAlignedTopAppBar({ Text("Dominion") }) },
        modifier = modifier,
    ) { contentPadding ->
        state.onSuccess {
            HomeScreen(
                expansions = it.toImmutableList(),
                contentPadding = contentPadding,
                onClick = onClick,
            )
        }

        state.onLoading {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
            )
        }
    }
}

@Composable
@ExperimentalMaterial3Api
private fun HomeScreen(
    expansions: ImmutableList<DominionExpansion>,
    contentPadding: PaddingValues,
    onClick: (DominionExpansion) -> Unit = { },
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier.padding(4.dp),
        contentPadding = contentPadding,
    ) {
        items(expansions) {
            ExpansionCard(it) { onClick(it) }
        }
    }
}

@Composable
@ExperimentalMaterial3Api
private fun ExpansionCard(
    value: DominionExpansion,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { },
) {
    Box(Modifier.padding(4.dp)) {
        Card(
            modifier = modifier
                .clickable(onClick = onClick)
                .aspectRatio(1.0f),
        ) {
            Image(
                painter = rememberAsyncImagePainter(value.image),
                contentDescription = value.name,
            )
        }
    }
}
