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
import androidx.compose.runtime.collectAsState
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
import com.example.palexis3.newssum.viewmodels.PreferencesViewModel

private val SORT_BY = listOf("relevancy", "popularity", "publishedAt")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchView(
    articleViewModel: ArticleViewModel,
    preferencesViewModel: PreferencesViewModel,
    goToNewsApiArticleDetailsScreen: () -> Unit,
    closeScreen: () -> Unit
) {
    var query by rememberSaveable { mutableStateOf<String?>(null) }
    var sortBy by rememberSaveable { mutableStateOf<String?>(null) }

    val language by preferencesViewModel.language.collectAsState()

    LaunchedEffect(key1 = query, key2 = sortBy, key3 = language) {
        if (query.isNullOrEmpty().not()) {
            articleViewModel.search(
                keyword = query,
                sortBy = sortBy,
                language = preferencesViewModel.languageMap[language]
            )
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
                value = query ?: "",
                singleLine = true,
                onValueChange = { value ->
                    query = value
                },
                leadingIcon = {
                    Icon(
                        modifier = Modifier.padding(start = 4.dp),
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                trailingIcon = {
                    if (query.isNullOrEmpty().not()) {
                        IconButton(onClick = {
                            query = null
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

            var expanded by remember { mutableStateOf(false) }

            SortDropDown(
                currentSelectedItem = sortBy,
                selected = { item -> sortBy = item },
                expanded = expanded,
                onExpanded = { bool -> expanded = bool }
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
                if (query.isNullOrEmpty().not()) {
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
    selected: (String) -> Unit,
    expanded: Boolean,
    onExpanded: (Boolean) -> Unit
) {
    Box {
        IconButton(onClick = { onExpanded(true) }) {
            Icon(
                imageVector = Icons.Filled.Menu,
                contentDescription = "Sort By"
            )
        }

        if (expanded) {
            DropdownMenu(
                expanded = true,
                onDismissRequest = { onExpanded(false) }
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
                        onClick = {
                            selected(item)
                            onExpanded(false)
                        }
                    )
                }
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
                Column(modifier = Modifier.clickable { articleSelected(article) }) {
                    Text(
                        modifier = Modifier
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
