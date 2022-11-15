package com.example.palexis3.newssum.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.compose.collectAsState
import com.example.palexis3.newssum.R
import com.example.palexis3.newssum.models.news_api.NewsApiArticle
import com.example.palexis3.newssum.state.ArticlesState
import com.example.palexis3.newssum.viewmodels.ArticleViewModel

private val SORT_BY = listOf("relevancy", "popularity", "publishedAt")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchView(
    articleViewModel: ArticleViewModel,
    goToNewsApiArticleDetailsScreen: () -> Unit,
    closeScreen: () -> Unit
) {
    var searchText by rememberSaveable { mutableStateOf<String?>(null) }
    var sortBy by rememberSaveable { mutableStateOf<String?>(null) }

    LaunchedEffect(key1 = searchText, key2 = sortBy) {
        if (searchText.isNullOrEmpty().not()) {
            articleViewModel.search(keyword = searchText, sortBy = sortBy)
        } else {
            articleViewModel.resetSearch()
        }
    }

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
                value = searchText ?: "",
                singleLine = true,
                onValueChange = { value ->
                    searchText = value
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
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            SortDropDown(
                currentSelectedItem = sortBy,
                selected = { item -> sortBy = item }
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
                    SearchTextError(modifier = Modifier.align(CenterHorizontally))
                }
            }
            is Uninitialized -> {
                // if the article search state has not been initialized and there's text being searched for
                // show an error message
                if (searchText.isNullOrEmpty().not()) {
                    SearchTextError(modifier = Modifier.align(CenterHorizontally))
                }
            }
            else -> {}
        }
    }
}

@Composable
fun SortDropDown(
    currentSelectedItem: String?,
    selected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Filled.Menu,
                contentDescription = "Sort By"
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            SORT_BY.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = CenterVertically) {
                            Text(item)
                            if (currentSelectedItem == item) {
                                Spacer(Modifier.width(2.dp))
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Checked item"
                                )
                            }
                        }
                    },
                    onClick = { selected(item) }
                )
            }
        }
    }
}

@Composable
fun SearchTextError(modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = stringResource(id = R.string.search_results_not_found)
    )
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
                            .clickable { articleSelected(article) }
                            .padding(8.dp),
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
