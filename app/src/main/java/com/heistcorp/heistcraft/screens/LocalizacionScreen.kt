package com.heistcorp.heistcraft.screens

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocalizacionScreen(onBack: () -> Unit) {
    val mapEmbed =
        "https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d2930.2060631333566!2d-6.2016160000000005!3d36.5373394!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x152f79854b8f0de1%3A0x8d075bd9e5895558!2sEscuela%20Superior%20de%20Ingenier%C3%ADa!5e1!3m2!1ses!2ses!4v1774105141460!5m2!1ses!2ses"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Localización") },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF1E2333),
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White,
                    ),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
            )
        },
    ) { padding ->
        AndroidView(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding),
            factory = { context ->
                WebView(context).apply {
                    webViewClient = WebViewClient()
                    settings.javaScriptEnabled = true
                    settings.loadWithOverviewMode = true
                    settings.useWideViewPort = true
                    val html =
                        "<html><head><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"/></head>" +
                            "<body style=\"margin:0;padding:0;height:100%\">" +
                            "<iframe style=\"border:0;width:100%;height:100vh\" src=\"$mapEmbed\" " +
                            "allowfullscreen loading=\"lazy\"></iframe></body></html>"
                    loadDataWithBaseURL("https://www.google.com", html, "text/html", "UTF-8", null)
                }
            },
        )
    }
}
