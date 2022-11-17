package com.example.palexis3.newssum.composable

import android.annotation.SuppressLint
import android.content.ComponentName
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsCallback
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.browser.customtabs.CustomTabsSession
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

private const val CHROME_CHANNEL_NAME = "com.android.chrome"

@Composable
fun WebViewScreen(
    url: String,
    closeScreen: () -> Unit
) {
    var chromeTabsError by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        ShowChromeTabs(
            url = url,
            closeScreen = closeScreen,
            chromeLoadError = { chromeTabsError = true }
        )
        if (chromeTabsError) {
            ChromeTabFallBack(url = url, closeScreen = closeScreen)
        }
    }
}

// TODO: Should be wrapped in SideEffect since it might be called every recomposition
@Composable
fun ShowChromeTabs(
    url: String,
    closeScreen: () -> Unit,
    chromeLoadError: () -> Unit
) {
    val context = LocalContext.current
    var customTabsClient: CustomTabsClient?
    var customTabsSession: CustomTabsSession? = null

    val customTabsCallback = object : CustomTabsCallback() {
        override fun onNavigationEvent(navigationEvent: Int, extras: Bundle?) {
            super.onNavigationEvent(navigationEvent, extras)
            if (navigationEvent == NAVIGATION_FAILED) {
                chromeLoadError.invoke()
            }
        }
    }

    // connecting the client to custom tabs service to make web page load faster
    val customTabsServiceConnection = object : CustomTabsServiceConnection() {
        override fun onCustomTabsServiceConnected(name: ComponentName, client: CustomTabsClient) {
            customTabsClient = client
            customTabsClient?.warmup(0L)
            customTabsSession = customTabsClient?.newSession(customTabsCallback)
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            customTabsClient = null
            customTabsSession = null
        }
    }

    CustomTabsClient.bindCustomTabsService(context, CHROME_CHANNEL_NAME, customTabsServiceConnection)

    // set the toolbar color to match theme of app
    val colorInt = Color.parseColor("#FFFDFFD9")
    val colorSchemeParams = CustomTabColorSchemeParams.Builder()
        .setToolbarColor(colorInt)
        .build()

    val customTabsIntent = CustomTabsIntent.Builder(customTabsSession)
        .setDefaultColorSchemeParams(colorSchemeParams)
        .build()

    customTabsIntent.launchUrl(context, Uri.parse(url))
}

@Composable
fun ChromeTabFallBack(url: String, closeScreen: () -> Unit) {
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
                settings.javaScriptEnabled = true
                loadUrl(url)
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
            errorOccurred.invoke()
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
