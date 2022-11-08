package com.example.palexis3.newssum.composable

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.example.palexis3.newssum.R
import com.example.palexis3.newssum.helper.formatToReadableDate
import com.example.palexis3.newssum.helper.toDate
import com.example.palexis3.newssum.models.Article
import com.example.palexis3.newssum.viewmodels.ArticleViewModel

@Composable
fun ArticleDetailsScreen(
    articleViewModel: ArticleViewModel,
    closeScreen: () -> Unit
) {
    val article by remember { articleViewModel.currentArticle }

    // Close the screen automatically if the article is null
    article?.let { it ->
        ShowArticleState(it, closeScreen = closeScreen)
    } ?: closeScreen()
}

@Composable
fun ShowArticleState(article: Article, closeScreen: () -> Unit) {
    LazyColumn {
        item {
            Box(Modifier.fillMaxWidth()) {
                AsyncImage(
                    model = article.urlToImage,
                    contentDescription = "Article Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    contentScale = ContentScale.Crop,
                    alignment = Center
                )
                IconButton(
                    onClick = closeScreen,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Go Back",
                        tint = Color.White
                    )
                }
            }
            Spacer(Modifier.height(4.dp))

            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                val title = article.title
                if (title != null) {
                    Text(text = title, style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(12.dp))
                }

                val publishedAt = article.publishedAt
                if (publishedAt != null) {
                    val date = publishedAt.toDate().formatToReadableDate()
                    Text(
                        text = date,
                        style = MaterialTheme.typography.bodySmall,
                        fontStyle = FontStyle.Italic
                    )
                    Spacer(Modifier.height(2.dp))
                }

                val newsSource = article.source?.name
                if (newsSource != null) {
                    val newsSourceText = "News source: $newsSource"
                    Text(
                        text = newsSourceText,
                        style = MaterialTheme.typography.bodySmall,
                        fontStyle = FontStyle.Italic
                    )
                    Spacer(Modifier.height(2.dp))
                }

                val author = article.author
                if (author != null) {
                    val authorText = "Written by: $author"
                    Text(
                        text = authorText,
                        style = MaterialTheme.typography.bodySmall,
                        fontStyle = FontStyle.Italic
                    )
                    Spacer(Modifier.height(2.dp))
                }

                Spacer(Modifier.height(20.dp))

                val content = article.content
                val articleUrl = article.url

                if (articleUrl != null) {
                    ShowWebView(url = articleUrl)
                } else if (content != null) {
                    Text(text = content, style = MaterialTheme.typography.bodyLarge)
                } else {
                    Text(
                        text = stringResource(id = R.string.article_content_error),
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.align(CenterHorizontally)
                    )
                }
            }
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
                val loadingWebViewClient = LoadingWebViewClient { isLoading ->
                    isLoadingIconVisible = isLoading
                }
                webViewClient = loadingWebViewClient
                loadUrl(url)
                settings.javaScriptEnabled = true
            }
        })

        if (isLoadingIconVisible) {
            CircularProgressIndicator(Modifier.align(Center))
        }
    }
}

/**
 *  TODO: Add a lambda to capture whether or not the webview was able to successfully load URL else
 *  send an exception state to turn the Webview box visibility to false
 */
class LoadingWebViewClient(private val loadingState: (Boolean) -> Unit) : WebViewClient() {

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        loadingState(true)
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        loadingState(false)
        super.onPageFinished(view, url)
    }
}
