package io.ashdavies.playground

import com.arkivanov.decompose.ComponentContext
import com.slack.circuit.CircuitConfig

public fun CircuitConfig(
    componentContext: ComponentContext,
    initialRoute: LauncherRoute,
): CircuitConfig = CircuitConfig.Builder()
    .addPresenterFactory(LauncherPresenterFactory(initialRoute))
    .addUiFactory(LauncherUiFactory(componentContext))
    .build()
