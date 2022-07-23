package io.ashdavies.http

internal actual object Software {
    actual val buildVersion: String = Environment.properties["os.version"] as String
    actual val productName: String = Environment.properties["os.name"] as String
    actual val clientName: String get() {
        println(System.env)
        return "Ktor/2.0.0"
    }
}
