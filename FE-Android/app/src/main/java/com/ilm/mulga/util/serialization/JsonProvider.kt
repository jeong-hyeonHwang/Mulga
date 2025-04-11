package com.ilm.mulga.util.serialization

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import java.time.LocalDateTime

object JsonProvider {
    private val module = SerializersModule {
        contextual(LocalDateTime::class, LocalDateTimeSerializer)
    }
    val json: Json = Json {
        serializersModule = module
    }
}
