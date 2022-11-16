package com.example.palexis3.newssum.composable

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    val context = LocalContext.current
    val customTabsIntentBuilder = CustomTabsIntent.Builder()
    val colorInt = Color.parseColor("#FFFDFFD9")
    val colorSchemeParams = CustomTabColorSchemeParams.Builder()
        .setToolbarColor(colorInt)
        .build()
    customTabsIntentBuilder.setDefaultColorSchemeParams(colorSchemeParams)
    customTabsIntentBuilder.build().launchUrl(context, Uri.parse(url))
}

@Composable
fun WebViewFallBack(url: String, closeScreen: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxWidth()) {
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
        if (error?.errorCode == ERROR_CONNECT || error?.errorCode == ERROR_FILE_NOT_FOUND) {
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
