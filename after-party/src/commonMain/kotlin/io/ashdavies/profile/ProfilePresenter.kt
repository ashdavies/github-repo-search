package io.ashdavies.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import com.slack.circuit.retained.collectAsRetainedState

@Composable
internal fun ProfilePresenter(
    repository: ProfileRepository = rememberProfileRepository(),
    uriHandler: UriHandler = LocalUriHandler.current,
): ProfileScreen.State {
    val profile: Profile? by repository
        .getProfile(generateRandomIfEmpty = true)
        .collectAsRetainedState(initial = null)

    return ProfileScreen.State(profile) {
        if (it is ProfileScreen.Event.Login) uriHandler.openUri("http://localhost:8080/callback")
    }
}
