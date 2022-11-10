package com.example.palexis3.newssum.composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.airbnb.mvrx.compose.collectAsState
import com.example.palexis3.newssum.R
import com.example.palexis3.newssum.models.Article
import com.example.palexis3.newssum.models.NewsSource
import com.example.palexis3.newssum.state.ArticlesState
import com.example.palexis3.newssum.viewmodels.ArticleViewModel
import com.example.palexis3.newssum.viewmodels.NewsSourcesViewModel

@Composable
fun NewsSourceDetailsScreen(
    closeScreen: () -> Unit,
    newsSourcesViewModel: NewsSourcesViewModel,
    articleViewModel: ArticleViewModel,
    goToArticleDetailsScreen: () -> Unit,
    goToWebView: (String) -> Unit
) {
    val newsSource by remember { newsSourcesViewModel.currentNewsSource }

    newsSource?.let { source ->
        // Get the headline article for this news source
        LaunchedEffect(key1 = source.id) {
            articleViewModel.getHeadlines(sources = source.id)
        }
        val articlesState by articleViewModel.collectAsState()

        ShowNewsSourceDetails(
            closeScreen,
            source,
            articlesState,
            goToWebView,
            articleSelected = { article ->
                articleViewModel.setCurrentArticle(article)
                goToArticleDetailsScreen()
            }
        )
    } ?: closeScreen()
}

@Composable
fun ShowNewsSourceDetails(
    closeScreen: () -> Unit,
    newsSource: NewsSource,
    articlesState: ArticlesState,
    goToWebView: (String) -> Unit,
    articleSelected: (Article) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        item {
            IconButton(
                onClick = closeScreen,
                modifier = Modifier
                    .padding(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Go Back"
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            val name = newsSource.name ?: ""
            if (name.isNotEmpty()) {
                Text(text = name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
                Spacer(Modifier.height(12.dp))
            }

            val category = newsSource.category ?: ""
            if (category.isNotEmpty()) {
                IconButton(onClick = {}) {
                    Text(text = category, style = MaterialTheme.typography.labelMedium)
                }
                Spacer(Modifier.height(4.dp))
            }

            val newsSourceUrl = newsSource.url ?: ""
            if (newsSourceUrl.isNotEmpty()) {
                Column {
                    ElevatedButton(
                        modifier = Modifier.align(Alignment.End),
                        elevation = ButtonDefaults.buttonElevation(6.dp),
                        onClick = { goToWebView(newsSourceUrl) }
                    ) {
                        Text(text = stringResource(id = R.string.open_web_version))
                    }
                    Spacer(Modifier.height(4.dp))
                }
            }

            val description = newsSource.description ?: ""
            if (description.isNotEmpty()) {
                Text(text = description, style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(Modifier.height(20.dp))

            Text(
                text = stringResource(id = R.string.articles),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(12.dp))

            ShowArticlesState(
                articlesState = articlesState,
                articleSelected = articleSelected
            )
        }
    }
}
