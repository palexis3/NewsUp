package com.example.palexis3.newssum.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.compose.collectAsState
import com.example.palexis3.newssum.R
import com.example.palexis3.newssum.models.news_api.NewsApiArticle
import com.example.palexis3.newssum.state.ArticlesState
import com.example.palexis3.newssum.viewmodels.ArticleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchView(
    articleViewModel: ArticleViewModel,
    goToNewsApiArticleDetailsScreen: () -> Unit,
    closeScreen: () -> Unit
) {
    var searchText by rememberSaveable { mutableStateOf<String?>(null) }

    val articleState by articleViewModel.collectAsState(ArticlesState::searchedArticles)

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            IconButton(onClick = closeScreen) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Go Back"
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = searchText ?: "",
                singleLine = true,
                onValueChange = { value ->
                    searchText = value
                    if (searchText.isNullOrEmpty().not()) {
                        articleViewModel.getEverything(keyword = value)
                    }
                },
                leadingIcon = {
                    Icon(
                        modifier = Modifier.padding(start = 4.dp),
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                trailingIcon = {
                    if (searchText.isNullOrEmpty().not()) {
                        IconButton(onClick = {
                            searchText = null
                        }) {
                            Icon(
                                modifier = Modifier.padding(end = 4.dp),
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close"
                            )
                        }
                    }
                }
            )
        }

        when (articleState) {
            is Success -> {
                val articles = articleState.invoke() ?: listOf()
                if (articles.isNotEmpty()) {
                    SearchList(
                        articles = articles,
                        articleSelected = { newsApiArticle ->
                            articleViewModel.setCurrentNewsApiArticle(newsApiArticle)
                            goToNewsApiArticleDetailsScreen()
                        }
                    )
                } else {
                    // if the article search found no articles and there's text being searched for
                    // show an error message
                    if (searchText.isNullOrEmpty().not()) {
                        Text(
                            modifier = Modifier.align(CenterHorizontally),
                            text = stringResource(id = R.string.search_results_not_found)
                        )
                    }
                }
            }
            else -> {}
        }
    }
}

@Composable
fun SearchList(
    articles: List<NewsApiArticle>,
    articleSelected: (NewsApiArticle) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        itemsIndexed(articles) { index, article ->
            val title = article.title ?: ""
            if (title.isNotEmpty()) {
                Column {
                    Text(
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable { articleSelected(article) },
                        text = title,
                        style = MaterialTheme.typography.titleMedium
                    )
                    if (index < articles.size - 1) {
                        Divider(Modifier.fillMaxWidth())
                    }
                }
            }
        }
    }
}
