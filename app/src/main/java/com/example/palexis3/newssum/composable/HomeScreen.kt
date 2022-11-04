package com.example.palexis3.newssum.composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
import com.example.palexis3.newssum.state.ArticlesState
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

    Column(
        Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        TitleHeader(title = R.string.header_title)
        Spacer(Modifier.height(12.dp))
        ShowHeadlineState(articlesState = articlesState)
    }
}

@Composable
fun ShowHeadlineState(articlesState: ArticlesState) {
    when (val state = articlesState.articles) {
        is Loading -> {
            LoadingIcon()
        }
        is Fail -> {
            ErrorText(title = R.string.header_error)
        }
        is Success -> {
            LazyRow(modifier = Modifier.fillMaxWidth()) {
                items(state.invoke(), itemContent = { article ->
                    HeadlineCard(article = article)
                })
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
