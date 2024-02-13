package io.ashdavies.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import io.ashdavies.routing.ComputeRoutesResponse

@Stable
internal data class RouteMapState(
    val startPosition: LatLng = KnownLocations.Berlin,
    val endPosition: LatLng? = null,
    val routes: List<ComputeRoutesResponse.Route> = emptyList(),
    val zoomLevel: Float = 12f,
)

@Composable
internal expect fun RouteMap(
    state: RouteMapState,
    modifier: Modifier = Modifier,
    onEndPosition: (LatLng) -> Unit,
)

internal data class LatLng(
    val latitude: Double,
    val longitude: Double,
)
