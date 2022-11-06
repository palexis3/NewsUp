package com.example.palexis3.newssum.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
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
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.google.accompanist.flowlayout.SizeMode

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

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        item {
            TitleHeader(title = R.string.header_title)
            Spacer(Modifier.height(8.dp))
            ShowHeadlinesState(articlesState = articlesState)

            Spacer(Modifier.height(72.dp))

            TitleHeader(title = R.string.sources_title)
            Spacer(Modifier.height(8.dp))
            ShowNewsSources(sourcesState = sourcesState)
        }
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
            val cellWidthSize: Dp = LocalConfiguration.current.screenWidthDp.dp / 3

            FlowRow(
                mainAxisSize = SizeMode.Expand,
                mainAxisAlignment = MainAxisAlignment.SpaceBetween
            ) {
                val items = state.invoke()
                if (items.isEmpty()) {
                    ErrorText(title = R.string.sources_error)
                } else {
                    items.forEach { source ->
                        SourceCard(source, Modifier.width(cellWidthSize))
                    }
                }
            }
        }
        else -> {}
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SourceCard(source: Source, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .padding(bottom = 20.dp),
        elevation = 10.dp
    ) {
        Column(
            Modifier
                .padding(12.dp)
        ) {
            if (source.name != null) {
                var name by remember { mutableStateOf(source.name) }
                val minLines = 2
                Text(
                    text = name,
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = minLines,
                    overflow = TextOverflow.Ellipsis,
                    onTextLayout = { textLayoutResult ->
                        // This will recompose to ensure that the text item will occupy two lines
                        // although some strings only require 1. This is to make sure the source cards
                        // consist with of the same paddings and height.
                        // Reference: https://stackoverflow.com/a/72639044/3681456
                        if ((textLayoutResult.lineCount) < minLines) {
                            name = source.name + "\n ".repeat(minLines - textLayoutResult.lineCount)
                        }
                    }
                )
                Spacer(Modifier.height(4.dp))
            }

            val description = source.description
            if (description != null) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.body1,
                    maxLines = 6,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
            }

            val category = source.category
            if (category != null) {
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
                        .height(32.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = category,
                        modifier = Modifier
                            .align(Alignment.CenterVertically),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
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
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
            ) {
                val items = state.invoke()
                if (items.isEmpty()) {
                    item {
                        ErrorText(title = R.string.header_error)
                    }
                } else {
                    items(items, itemContent = { article ->
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
        modifier = Modifier
            .padding(12.dp)
            .aspectRatio(1f),
        elevation = 10.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = article.urlToImage,
                contentDescription = "Headline Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            )

            Spacer(Modifier.height(8.dp))

            Column(modifier = Modifier.padding(12.dp)) {
                val title = article.title
                if (title != null) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.ExtraBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(4.dp))
                }

                val publishedAt = article.publishedAt
                if (publishedAt != null) {
                    val date = publishedAt.toDate().formatToReadableDate()
                    Text(text = date, style = MaterialTheme.typography.body1)
                    Spacer(Modifier.height(4.dp))
                }

                val description = article.description
                if (description != null) {
                    Text(
                        text = description,
                        style = MaterialTheme.typography.body1,
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
