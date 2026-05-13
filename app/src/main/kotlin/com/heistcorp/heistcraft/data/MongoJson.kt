package com.heistcorp.heistcraft.data

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive

/** El driver de Mongo puede serializar `_id` como string o como `{"$oid":"..."}`. */
fun JsonElement.asMongoIdString(): String =
    when {
        isJsonPrimitive && asJsonPrimitive.isString -> asString
        isJsonObject -> {
            val o = asJsonObject
            when {
                o.has("\$oid") -> o.get("\$oid")?.takeIf { it is JsonPrimitive }?.asString ?: ""
                o.has("oid") -> o.get("oid")?.takeIf { it is JsonPrimitive }?.asString ?: ""
                else -> ""
            }
        }
        else -> ""
    }
