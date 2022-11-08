package com.example.palexis3.newssum.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.compose.collectAsState
import com.example.palexis3.newssum.R
import com.example.palexis3.newssum.helper.formatToReadableDate
import com.example.palexis3.newssum.helper.toDate
import com.example.palexis3.newssum.models.Article
import com.example.palexis3.newssum.models.NEWS_CATEGORY_TYPES
import com.example.palexis3.newssum.state.ArticlesState
import com.example.palexis3.newssum.viewmodels.ArticleViewModel
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
    goToArticleDetailsScreen: () -> Unit
) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TitleHeader(
            modifier = Modifier.align(CenterHorizontally),
            title = R.string.headlines
        )
        Spacer(modifier = Modifier.height(8.dp))

        HorizontalTabs(
            scope = coroutineScope,
            pagerState = pagerState
        )

        Spacer(Modifier.height(8.dp))

        HorizontalPager(
            modifier = Modifier.padding(12.dp),
            count = NEWS_CATEGORY_TYPES.size,
            state = pagerState
        ) { currentPage ->

            ShowPagerState(
                headlineCategory = NEWS_CATEGORY_TYPES[currentPage],
                articleViewModel = articleViewModel,
                goToArticleDetailsScreen = goToArticleDetailsScreen
            )
        }
    }
}

@Composable
fun ShowPagerState(
    headlineCategory: String,
    articleViewModel: ArticleViewModel,
    goToArticleDetailsScreen: () -> Unit
) {
    LaunchedEffect(key1 = headlineCategory) {
        articleViewModel.getHeadlines(category = headlineCategory)
    }

    val articlesState by articleViewModel.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ShowHeadlinesState(
            articlesState = articlesState,
            articleSelected = { article ->
                articleViewModel.setCurrentArticle(article)
                goToArticleDetailsScreen()
            }
        )
    }
}

@Composable
fun ShowHeadlinesState(
    articlesState: ArticlesState,
    articleSelected: (Article) -> Unit
) {
    when (val state = articlesState.articles) {
        is Loading -> {
            Box {
                LoadingIcon()
            }
        }
        is Fail -> {
            Box {
                ErrorText(title = R.string.header_error)
            }
        }
        is Success -> {
            LazyColumn {
                val items = state.invoke()
                if (items.isEmpty()) {
                    item {
                        ErrorText(title = R.string.header_error)
                    }
                } else {
                    items(items, itemContent = { article ->
                        HeadlineCard(
                            article = article,
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
fun HeadlineCard(
    article: Article,
    articleSelected: (Article) -> Unit
) {
    Card(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .padding(12.dp)
            .aspectRatio(1f)
            .clickable { articleSelected(article) },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 10.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            val urlImage = article.urlToImage
            if (urlImage != null) {
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
                val title = article.title
                if (title != null) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(4.dp))
                }

                val publishedAt = article.publishedAt
                if (publishedAt != null) {
                    val date = publishedAt.toDate().formatToReadableDate()
                    Text(text = date, style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.height(4.dp))
                }

                val description = article.description
                if (description != null) {
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

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HorizontalTabs(
    scope: CoroutineScope,
    pagerState: PagerState
) {
    ScrollableTabRow(
        selectedTabIndex = pagerState.currentPage
    ) {
        NEWS_CATEGORY_TYPES.forEachIndexed { index, category ->
            Tab(
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
