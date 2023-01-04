package io.ashdavies.playground

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import com.slack.circuit.CircuitCompositionLocals
import com.slack.circuit.CircuitConfig
import com.slack.circuit.CircuitContent
import com.slack.circuit.Screen

@Composable
internal fun LauncherApp(circuitConfig: CircuitConfig, initialBackStack: List<Screen>) {
    MaterialTheme {
        CircuitCompositionLocals(circuitConfig) {
            CircuitContent(DesktopLauncherScreen)
        }
    }
}
