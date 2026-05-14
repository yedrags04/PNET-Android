package com.heistcorp.heistcraft.network

import retrofit2.HttpException

fun Throwable.toNetworkMessage(defaultMessage: String): String =
    when (this) {
        is HttpException -> "Error HTTP ${code()}: ${message()}"
        else -> message ?: defaultMessage
    }
