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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
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
import com.example.palexis3.newssum.helper.EmptyScreenWidth
import com.example.palexis3.newssum.helper.formatToReadableDate
import com.example.palexis3.newssum.helper.toDate
import com.example.palexis3.newssum.models.NEWS_API_CATEGORY_TYPES
import com.example.palexis3.newssum.models.news_api.NewsApiArticle
import com.example.palexis3.newssum.state.ArticlesState
import com.example.palexis3.newssum.viewmodels.ArticleViewModel
import com.example.palexis3.newssum.viewmodels.PreferencesViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HeadlineScreen(
    articleViewModel: ArticleViewModel,
    preferencesViewModel: PreferencesViewModel,
    goToNewsApiArticleDetailsScreen: () -> Unit,
    goToSearchView: () -> Unit
) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    val articlesState by articleViewModel.collectAsState(ArticlesState::newsApiArticles)

    val country by preferencesViewModel.country.collectAsState()
    val countryKey: String? = preferencesViewModel.countryMap[country]

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            val category = NEWS_API_CATEGORY_TYPES[page]
            articleViewModel.getNewsApiArticles(
                category = category,
                country = countryKey
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 12.dp),
            verticalAlignment = CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(Modifier.width(EmptyScreenWidth()))
            TitleHeader(
                title = R.string.headlines
            )
            IconButton(
                onClick = goToSearchView
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        HorizontalTabs(
            scope = coroutineScope,
            pagerState = pagerState
        )

        Spacer(Modifier.height(8.dp))

        HorizontalPager(
            count = NEWS_API_CATEGORY_TYPES.size,
            state = pagerState
        ) {
            ShowNewsApiArticlesState(
                articlesState = articlesState,
                articleSelected = { article ->
                    articleViewModel.setCurrentNewsApiArticle(article)
                    goToNewsApiArticleDetailsScreen()
                }
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HorizontalTabs(
    scope: CoroutineScope,
    pagerState: PagerState
) {
    ScrollableTabRow(
        selectedTabIndex = pagerState.currentPage
    ) {
        NEWS_API_CATEGORY_TYPES.forEachIndexed { index, category ->
            Tab(
                modifier = Modifier.padding(8.dp),
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(page = index)
                    }
                }
            ) {
                Text(text = category)
            }
        }
    }
}

@Composable
fun ShowNewsApiArticlesState(
    articlesState: Async<List<NewsApiArticle>>,
    articleSelected: (NewsApiArticle) -> Unit
) {
    when (articlesState) {
        is Loading -> {}
        is Fail -> {
            Box {
                ErrorText(title = R.string.articles_error)
            }
        }
        is Success -> {
            LazyColumn {
                val items = articlesState.invoke()
                if (items.isEmpty()) {
                    item {
                        ErrorText(title = R.string.articles_error)
                    }
                } else {
                    items(items, itemContent = { article ->
                        NewsApiArticleCard(
                            newsApiArticle = article,
                            articleSelected = articleSelected
                        )
                    })
                }
            }
        }
        else -> {}
    }
}

@Composable
fun NewsApiArticleCard(
    newsApiArticle: NewsApiArticle,
    articleSelected: (NewsApiArticle) -> Unit
) {
    Card(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .padding(12.dp)
            .clickable { articleSelected(newsApiArticle) },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 10.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            val urlImage = newsApiArticle.urlToImage ?: ""
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
                val title = newsApiArticle.title ?: ""
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

                val publishedAt = newsApiArticle.publishedAt ?: ""
                if (publishedAt.isNotEmpty()) {
                    val date = publishedAt.toDate().formatToReadableDate()
                    Text(text = date, style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.height(4.dp))
                }

                val description = newsApiArticle.description ?: ""
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
