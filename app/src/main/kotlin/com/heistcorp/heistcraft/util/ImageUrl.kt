package com.heistcorp.heistcraft.util

import com.heistcorp.heistcraft.BuildConfig

fun resolveAssetUrl(path: String?): String {
    if (path.isNullOrBlank()) return ""
    if (path.startsWith("http://", ignoreCase = true) || path.startsWith(
            "https://",
            ignoreCase = true
        )
    ) {
        return path
    }
    val base = BuildConfig.API_BASE_URL.trimEnd('/')
    val rel = path.trimStart('/')
    return "$base/$rel"
}
