package com.example.palexis3.newssum.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.compose.collectAsState
import com.example.palexis3.newssum.R
import com.example.palexis3.newssum.helper.formatToReadableDate
import com.example.palexis3.newssum.helper.toDateEmptySpace
import com.example.palexis3.newssum.models.news_data.NewsDataArticle
import com.example.palexis3.newssum.models.news_data.NewsDataNewsSource
import com.example.palexis3.newssum.state.ArticlesState
import com.example.palexis3.newssum.viewmodels.ArticleViewModel
import com.example.palexis3.newssum.viewmodels.NewsSourcesViewModel
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun NewsSourceDetailsScreen(
    closeScreen: () -> Unit,
    newsSourcesViewModel: NewsSourcesViewModel,
    articleViewModel: ArticleViewModel,
    goToNewsDataArticleDetailsScreen: () -> Unit,
    goToWebView: (String) -> Unit
) {
    val newsSource by remember { newsSourcesViewModel.currentNewsDataNewsSource }

    newsSource?.let { source ->
        // Get the headline article for this news source
        LaunchedEffect(source.id) {
            articleViewModel.getNewsDataArticles(domain = source.id)
        }

        val articlesState by articleViewModel.collectAsState(ArticlesState::newsDataArticles)

        ShowNewsSourceDetails(
            closeScreen,
            source,
            articlesState,
            goToWebView,
            articleSelected = { article ->
                articleViewModel.setCurrentNewsDataArticle(article)
                goToNewsDataArticleDetailsScreen()
            }
        )
    } ?: closeScreen()
}

@Composable
fun ShowNewsSourceDetails(
    closeScreen: () -> Unit,
    newsDataNewsSource: NewsDataNewsSource,
    articlesState: Async<List<NewsDataArticle>>,
    goToWebView: (String) -> Unit,
    articleSelected: (NewsDataArticle) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        item {
            IconButton(
                onClick = closeScreen
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Go Back"
                )
            }

            val name = newsDataNewsSource.name ?: ""
            if (name.isNotEmpty()) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            val category = newsDataNewsSource.category ?: listOf()
            if (category.isNotEmpty()) {
                FlowRow {
                    category.forEach { item ->
                        CategoryOutlinedText(
                            category = item,
                            modifier = Modifier.padding(end = 4.dp, bottom = 4.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
            }

            val newsSourceUrl = newsDataNewsSource.url ?: ""
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

            ShowNewsDataArticlesState(
                articlesState = articlesState,
                articleSelected = articleSelected
            )
        }
    }
}

@Composable
fun ShowNewsDataArticlesState(
    articlesState: Async<List<NewsDataArticle>>,
    articleSelected: (NewsDataArticle) -> Unit
) {
    when (articlesState) {
        is Loading -> {
            Box {
                LoadingIcon()
            }
        }
        is Fail -> {
            Box {
                ErrorText(title = R.string.articles_error)
            }
        }
        is Success -> {
            FlowRow {
                val items = articlesState.invoke()
                if (items.isEmpty()) {
                    ErrorText(title = R.string.articles_error)
                } else {
                    items.forEach { article ->
                        NewsDataArticleCard(
                            newsDataArticle = article,
                            articleSelected = articleSelected
                        )
                    }
                }
            }
        }
        else -> {}
    }
}

@Composable
fun NewsDataArticleCard(
    newsDataArticle: NewsDataArticle,
    articleSelected: (NewsDataArticle) -> Unit
) {
    Card(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .padding(bottom = 20.dp)
            .clickable { articleSelected(newsDataArticle) },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 10.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            val urlImage = newsDataArticle.image_url ?: ""
            if (urlImage.isNotEmpty()) {
                AsyncImage(
                    model = urlImage,
                    contentDescription = "Headline Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                )
                Spacer(Modifier.height(8.dp))
            }

            Column(modifier = Modifier.padding(12.dp)) {
                val title = newsDataArticle.title ?: ""
                if (title.isNotEmpty()) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(4.dp))
                }

                val publishedAt = newsDataArticle.pubDate ?: ""
                if (publishedAt.isNotEmpty()) {
                    val date = publishedAt.toDateEmptySpace().formatToReadableDate()
                    Text(text = date, style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.height(4.dp))
                }

                val description = newsDataArticle.description ?: ""
                if (description.isNotEmpty()) {
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
