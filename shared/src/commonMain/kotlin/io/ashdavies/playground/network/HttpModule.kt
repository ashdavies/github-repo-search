package io.ashdavies.playground.network

import io.ashdavies.playground.Graph
import io.ashdavies.playground.database.EventsSerializer
import io.ashdavies.playground.profile.RandomUser
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.DEFAULT
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNull.serializer
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

val Graph<*>.json: Json
    get() = Json {
        serializersModule = SerializersModule {
            contextual(Envelope.serializer(RandomUser.serializer()))
            contextual(ListSerializer(EventsSerializer))
        }
        ignoreUnknownKeys = true
    }

val Graph<*>.httpClient: HttpClient
    get() = HttpClient {
        val json = Json {
            ignoreUnknownKeys = true
            serializersModule = SerializersModule {
                contextual(Envelope.serializer(RandomUser.serializer()))
                contextual(ListSerializer(EventsSerializer))
            }
        }

        install(JsonFeature) {
            serializer = KotlinxSerializer(json)
        }

        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
    }
