package com.example.palexis3.newssum.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.example.palexis3.newssum.R
import com.example.palexis3.newssum.helper.formatToReadableDate
import com.example.palexis3.newssum.helper.toDate
import com.example.palexis3.newssum.models.Article
import com.example.palexis3.newssum.models.Source
import com.example.palexis3.newssum.state.ArticlesState
import com.example.palexis3.newssum.state.SourcesState
import com.example.palexis3.newssum.viewmodels.ArticleViewModel
import com.example.palexis3.newssum.viewmodels.SourceViewModel

@Composable
fun HomeScreen() {
    val articleViewModel: ArticleViewModel = mavericksViewModel()
    val sourceViewModel: SourceViewModel = mavericksViewModel()

    LaunchedEffect(Unit) {
        articleViewModel.getHeadlines()
        sourceViewModel.getSources()
    }

    val articlesState by articleViewModel.collectAsState()
    val sourcesState by sourceViewModel.collectAsState()

    Column(
        Modifier
            .padding(12.dp)
            .verticalScroll(rememberScrollState())
    ) {
        TitleHeader(title = R.string.header_title)
        Spacer(Modifier.height(8.dp))
        ShowHeadlinesState(articlesState = articlesState)

        Spacer(Modifier.height(56.dp))

        TitleHeader(title = R.string.sources_title)
        Spacer(Modifier.height(8.dp))
        ShowNewsSources(sourcesState = sourcesState)
    }
}

@Composable
fun ShowNewsSources(sourcesState: SourcesState) {
    when (val state = sourcesState.sources) {
        is Loading -> {
            LoadingIcon()
        }
        is Fail -> {
            ErrorText(title = R.string.sources_error)
        }
        is Success -> {
            val items = state.invoke()
            if (items.isEmpty()) {
                ErrorText(title = R.string.sources_error)
            } else {
                LazyHorizontalGrid(
                    rows = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(state.invoke()) { source ->
                        SourceCard(source)
                    }
                }
            }
        }
        else -> {}
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SourceCard(source: Source) {
    Card(
        Modifier.padding(12.dp),
        elevation = 10.dp
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(
                text = source.name,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1
            )
            Spacer(Modifier.height(4.dp))

            Text(text = source.description, style = MaterialTheme.typography.body1)

            Spacer(Modifier.height(4.dp))

            Chip(
                onClick = {},
                border = BorderStroke(
                    ChipDefaults.OutlinedBorderSize,
                    Color.Green
                ),
                colors = ChipDefaults.chipColors(
                    backgroundColor = Color.White,
                    contentColor = Color.Black
                ),
                modifier = Modifier
                    .height(24.dp)
                    .padding(2.dp)
            ) {
                Text(text = source.category)
            }
        }
    }
}

@Composable
fun ShowHeadlinesState(articlesState: ArticlesState) {
    when (val state = articlesState.articles) {
        is Loading -> {
            LoadingIcon()
        }
        is Fail -> {
            ErrorText(title = R.string.header_error)
        }
        is Success -> {
            val items = state.invoke()
            if (items.isEmpty()) {
                ErrorText(title = R.string.header_error)
            } else {
                LazyRow(modifier = Modifier.fillMaxWidth()) {
                    items(state.invoke(), itemContent = { article ->
                        HeadlineCard(article = article)
                    })
                }
            }
        }
        else -> {}
    }
}

@Composable
fun HeadlineCard(article: Article) {
    Card(
        modifier = Modifier.padding(12.dp),
        elevation = 10.dp
    ) {
        Column(Modifier.padding(12.dp)) {
            AsyncImage(
                model = article.urlToImage,
                contentDescription = "Headline Image",
                modifier = Modifier.height(56.dp)
            )
            Spacer(Modifier.height(8.dp))

            Text(
                text = article.title,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1
            )
            Spacer(Modifier.height(4.dp))

            val date = article.publishedAt.toDate().formatToReadableDate()
            Text(text = date, style = MaterialTheme.typography.body1)

            Spacer(Modifier.height(4.dp))

            Text(text = article.description, style = MaterialTheme.typography.body1)
        }
    }
}
