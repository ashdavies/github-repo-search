package io.ashdavies.check

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.app
import io.ashdavies.http.AppCheckToken
import io.ashdavies.http.LocalHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow

public actual typealias AppCheckToken = com.google.firebase.appcheck.AppCheckToken

@Composable
public actual fun ProvideAppCheckToken(client: HttpClient, content: @Composable () -> Unit) {
    val appCheck = remember { Firebase.app.android.appCheck }
    val token by appCheck.appCheckToken().collectAsState(null)

    LaunchedEffect(appCheck) {
        val factory = PlayIntegrityAppCheckProviderFactory.getInstance()
        appCheck.installAppCheckProviderFactory(factory)
    }

    CompositionLocalProvider(
        LocalHttpClient provides client.config {
            install(DefaultRequest) {
                header(HttpHeaders.AppCheckToken, token?.token)
            }
        },
        content = content,
    )
}

private fun FirebaseAppCheck.appCheckToken(): Flow<AppCheckToken> = channelFlow {
    val appCheckListener = FirebaseAppCheck.AppCheckListener { trySend(it) }
    addAppCheckListener(appCheckListener)

    invokeOnClose {
        removeAppCheckListener(appCheckListener)
    }
}
