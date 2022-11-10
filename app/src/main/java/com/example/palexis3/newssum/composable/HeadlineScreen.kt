package com.example.palexis3.newssum.composable

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.mvrx.compose.collectAsState
import com.example.palexis3.newssum.R
import com.example.palexis3.newssum.models.NEWS_CATEGORY_TYPES
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

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            val category = NEWS_CATEGORY_TYPES[page]
            articleViewModel.getHeadlines(category = category)
        }
    }

    val articlesState by articleViewModel.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TitleHeader(
            modifier = Modifier.align(CenterHorizontally),
            title = R.string.headlines
        )
        Spacer(modifier = Modifier.height(16.dp))

        HorizontalTabs(
            scope = coroutineScope,
            pagerState = pagerState
        )

        Spacer(Modifier.height(8.dp))

        HorizontalPager(
            modifier = Modifier.padding(12.dp),
            count = NEWS_CATEGORY_TYPES.size,
            state = pagerState
        ) {
            ShowArticlesState(
                articlesState = articlesState,
                articleSelected = { article ->
                    articleViewModel.setCurrentArticle(article)
                    goToArticleDetailsScreen()
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
