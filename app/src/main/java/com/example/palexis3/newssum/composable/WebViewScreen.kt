package com.example.palexis3.newssum.composable

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun WebViewScreen(
    url: String,
    closeScreen: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            IconButton(onClick = closeScreen) {
                Icon(
                    modifier = Modifier.padding(12.dp),
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close web screen"
                )
            }

            ShowWebView(url = url)
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ShowWebView(url: String) {
    val context = LocalContext.current
    var isLoadingIconVisible by remember { mutableStateOf(false) }

    Box {
        AndroidView(factory = {
            WebView(context).apply {
                val loadingWebViewClient = LoadingWebViewClient(
                    loadingState = { isLoading -> isLoadingIconVisible = isLoading }
                )
                webViewClient = loadingWebViewClient
                loadUrl(url)
                settings.javaScriptEnabled = true
            }
        })

        if (isLoadingIconVisible) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
    }
}

class LoadingWebViewClient(
    private val loadingState: (Boolean) -> Unit
) : WebViewClient() {

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        loadingState(true)
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        loadingState(false)
        super.onPageFinished(view, url)
    }
}
