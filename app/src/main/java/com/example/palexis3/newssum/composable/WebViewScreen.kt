package com.example.palexis3.newssum.composable

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Build
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
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
    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            IconButton(onClick = closeScreen) {
                Icon(
                    modifier = Modifier.padding(12.dp),
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close web screen"
                )
            }

            var errorMessageVisible by remember { mutableStateOf(false) }

            if (errorMessageVisible) {
                Spacer(Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.webview_loading_error),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(CenterHorizontally)
                )
            } else {
                ShowWebView(url = url, errorOccurred = { errorMessageVisible = true })
            }
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ShowWebView(url: String, errorOccurred: () -> Unit) {
    val context = LocalContext.current
    var isLoadingIconVisible by remember { mutableStateOf(false) }

    Box {
        AndroidView(factory = {
            WebView(context).apply {
                val loadingWebViewClient = LoadingWebViewClient(
                    loadingState = { isLoading -> isLoadingIconVisible = isLoading },
                    errorOccurred = errorOccurred
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
    private val errorOccurred: () -> Unit
) : WebViewClient() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        if (error?.errorCode == ERROR_CONNECT || error?.errorCode == ERROR_FILE_NOT_FOUND || error?.errorCode == ERROR_IO) {
            errorOccurred()
        }
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
