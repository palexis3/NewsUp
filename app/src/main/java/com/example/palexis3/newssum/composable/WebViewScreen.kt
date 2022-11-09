package com.example.palexis3.newssum.composable

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.palexis3.newssum.R

@Composable
fun WebViewScreen(
    url: String,
    closeScreen: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        IconButton(onClick = closeScreen) {
            Icon(
                modifier = Modifier.padding(12.dp),
                imageVector = Icons.Default.Close,
                contentDescription = "Close web screen"
            )
        }

        Spacer(Modifier.height(8.dp))

        var webErrorOccurred by remember { mutableStateOf(false) }

        if (webErrorOccurred) {
            Text(
                text = stringResource(id = R.string.webview_loading_error),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(CenterHorizontally)
                    .padding(12.dp)
            )
        } else {
            ShowWebView(
                url = url,
                webErrorOccurred = { webErrorOccurred = true }
            )
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ShowWebView(url: String, webErrorOccurred: () -> Unit) {
    val context = LocalContext.current
    var isLoadingIconVisible by remember { mutableStateOf(false) }

    Box {
        AndroidView(factory = {
            WebView(context).apply {
                val loadingWebViewClient = LoadingWebViewClient(
                    loadingState = { isLoading -> isLoadingIconVisible = isLoading },
                    webErrorOccurred = webErrorOccurred
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
    private val loadingState: (Boolean) -> Unit,
    private val webErrorOccurred: () -> Unit
) : WebViewClient() {

    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        webErrorOccurred.invoke()
        super.onReceivedError(view, request, error)
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        loadingState(true)
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        loadingState(false)
        super.onPageFinished(view, url)
    }
}
