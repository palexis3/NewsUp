package com.example.palexis3.newssum.composable

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.airbnb.mvrx.compose.collectAsState
import com.example.palexis3.newssum.R
import com.example.palexis3.newssum.models.news_api.NewsApiArticle
import com.example.palexis3.newssum.models.news_api.NewsApiNewsSource
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
    val newsSource by remember { newsSourcesViewModel.currentNewsApiNewsSource }

    newsSource?.let { source ->
        // Get the headline article for this news source
        LaunchedEffect(Unit) {
            articleViewModel.getArticles(sources = source.id)
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
    newsApiNewsSource: NewsApiNewsSource,
    articlesState: ArticlesState,
    goToWebView: (String) -> Unit,
    articleSelected: (NewsApiArticle) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        IconButton(onClick = closeScreen) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Go Back"
            )
        }

        val name = newsApiNewsSource.name ?: ""
        if (name.isNotEmpty()) {
            Text(
                text = name,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        val description = newsApiNewsSource.description ?: ""
        if (description.isNotEmpty()) {
            Text(text = description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
        }

        val newsSourceUrl = newsApiNewsSource.url ?: ""
        if (newsSourceUrl.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                ElevatedButton(
                    elevation = ButtonDefaults.buttonElevation(6.dp),
                    onClick = { goToWebView(newsSourceUrl) }
                ) {
                    Text(text = stringResource(id = R.string.open_web_version))
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        Text(
            text = stringResource(id = R.string.articles),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(Modifier.height(8.dp))

        ShowArticlesState(
            articlesState = articlesState,
            articleSelected = articleSelected
        )
    }
}
