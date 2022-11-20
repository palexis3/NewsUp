package com.example.palexis3.newssum.composable.articles

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.palexis3.newssum.R
import com.example.palexis3.newssum.helper.formatToReadableDate
import com.example.palexis3.newssum.helper.toDate
import com.example.palexis3.newssum.models.news_api.NewsApiArticle
import com.example.palexis3.newssum.viewmodels.ArticleViewModel

@Composable
fun NewsApiArticleDetailsScreen(
    articleViewModel: ArticleViewModel,
    closeScreen: () -> Unit,
    goToWebView: (String) -> Unit,
    title: (String) -> Unit
) {
    val article by remember { articleViewModel.currentNewsApiArticle }

    // Close the screen automatically if the article is null
    article?.let { item ->
        ShowNewsApiArticleState(
            item,
            closeScreen,
            goToWebView,
            title
        )
    } ?: closeScreen()
}

@Composable
fun ShowNewsApiArticleState(
    newsApiArticle: NewsApiArticle,
    closeScreen: () -> Unit,
    goToWebView: (String) -> Unit,
    screenTitle: (String) -> Unit
) {
    LazyColumn {
        item {
            Box(Modifier.fillMaxWidth()) {
                val urlImage = newsApiArticle.urlToImage
                if (urlImage != null) {
                    AsyncImage(
                        model = urlImage,
                        contentDescription = "Article Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        contentScale = ContentScale.Crop,
                        alignment = Center,
                        error = painterResource(R.drawable.not_found)
                    )
                }
            }
            Spacer(Modifier.height(4.dp))

            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                val title = newsApiArticle.title ?: ""
                screenTitle(title)

                val publishedAt = newsApiArticle.publishedAt ?: ""
                if (publishedAt.isNotEmpty()) {
                    val date = publishedAt.toDate().formatToReadableDate()
                    Text(
                        text = date,
                        style = MaterialTheme.typography.bodySmall,
                        fontStyle = FontStyle.Italic
                    )
                    Spacer(Modifier.height(2.dp))
                }

                val newsSource = newsApiArticle.source?.name ?: ""
                if (newsSource.isNotEmpty()) {
                    val newsSourceText = "News source: $newsSource"
                    Text(
                        text = newsSourceText,
                        style = MaterialTheme.typography.bodySmall,
                        fontStyle = FontStyle.Italic
                    )
                    Spacer(Modifier.height(2.dp))
                }

                val author = newsApiArticle.author ?: ""
                if (author.isNotEmpty()) {
                    val authorText = "Written by: $author"
                    Text(
                        text = authorText,
                        style = MaterialTheme.typography.bodySmall,
                        fontStyle = FontStyle.Italic
                    )
                    Spacer(Modifier.height(2.dp))
                }

                val articleUrl = newsApiArticle.url ?: ""
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

                val content = newsApiArticle.content ?: ""
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
