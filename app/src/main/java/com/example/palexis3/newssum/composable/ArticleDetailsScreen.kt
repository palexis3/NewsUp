package com.example.palexis3.newssum.composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.palexis3.newssum.R
import com.example.palexis3.newssum.helper.formatToReadableDate
import com.example.palexis3.newssum.helper.toDate
import com.example.palexis3.newssum.models.Article
import com.example.palexis3.newssum.viewmodels.ArticleViewModel

@Composable
fun ArticleDetailsScreen(
    articleViewModel: ArticleViewModel,
    closeScreen: () -> Unit,
    goToWebView: (String) -> Unit
) {
    val article by remember { articleViewModel.currentArticle }

    // Close the screen automatically if the article is null
    article?.let { item ->
        ShowArticleState(item, closeScreen, goToWebView)
    } ?: closeScreen()
}

@Composable
fun ShowArticleState(
    article: Article,
    closeScreen: () -> Unit,
    goToWebView: (String) -> Unit
) {
    LazyColumn {
        item {
            Box(Modifier.fillMaxWidth()) {
                val urlImage = article.urlToImage
                if (urlImage != null) {
                    AsyncImage(
                        model = urlImage,
                        contentDescription = "Article Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        contentScale = ContentScale.Crop,
                        alignment = Center
                    )
                }
                IconButton(
                    onClick = closeScreen,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Go Back",
                        tint = if (urlImage != null) Color.White else Color.Unspecified
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

                val articleUrl = article.url ?: ""
                if (articleUrl.isNotEmpty()) {
                    Spacer(Modifier.height(4.dp))
                    ElevatedButton(
                        modifier = Modifier.align(End),
                        elevation = ButtonDefaults.buttonElevation(6.dp),
                        onClick = { goToWebView(articleUrl) }
                    ) {
                        Text(text = stringResource(id = R.string.open_web_version))
                    }
                }

                Spacer(Modifier.height(20.dp))

                val content = article.content ?: ""
                Box(modifier = Modifier.fillMaxWidth()) {
                    if (content.isNotEmpty()) {
                        Text(text = content, style = MaterialTheme.typography.bodyLarge)
                    } else {
                        Text(
                            text = stringResource(id = R.string.article_content_error),
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.align(Center)
                        )
                    }
                }
            }
        }
    }
}
