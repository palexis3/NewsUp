package com.example.palexis3.newssum.composable.news_sources

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedSuggestionChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.compose.collectAsState
import com.example.palexis3.newssum.R
import com.example.palexis3.newssum.composable.ErrorText
import com.example.palexis3.newssum.composable.LoadingIcon
import com.example.palexis3.newssum.composable.TitleHeader
import com.example.palexis3.newssum.helper.EmptyScreenWidth
import com.example.palexis3.newssum.models.NEWS_DATA_CATEGORY_TYPES
import com.example.palexis3.newssum.models.news_data.NewsDataNewsSource
import com.example.palexis3.newssum.state.NewsSourcesState
import com.example.palexis3.newssum.viewmodels.NewsSourcesViewModel
import com.example.palexis3.newssum.viewmodels.PreferencesViewModel
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun NewsSourcesScreen(
    newsSourcesViewModel: NewsSourcesViewModel,
    preferencesViewModel: PreferencesViewModel,
    goToNewsSourcesDetailsScreen: () -> Unit,
    goToSearchView: () -> Unit
) {
    var newsSourceCategory by rememberSaveable { mutableStateOf<String?>(null) }

    val country by preferencesViewModel.country.collectAsState()
    val language by preferencesViewModel.language.collectAsState()

    LaunchedEffect(key1 = newsSourceCategory, key2 = language, key3 = country) {
        newsSourcesViewModel.getNewsSources(
            category = newsSourceCategory,
            language = preferencesViewModel.languageMap[language],
            country = preferencesViewModel.countryMap[country]
        )
    }

    val newsSourcesState by newsSourcesViewModel.collectAsState(NewsSourcesState::newsDataNewsSources)

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(Modifier.width(EmptyScreenWidth()))
                TitleHeader(title = R.string.news_sources)
                IconButton(onClick = goToSearchView) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            CategoryChipGroup(
                selectedCategory = newsSourceCategory,
                onSelectedCategory = { selectedCategory ->
                    newsSourceCategory = selectedCategory
                }
            )

            ShowNewsSources(
                modifier = Modifier.padding(12.dp),
                newsSourcesState = newsSourcesState,
                newsSourceSelected = { newsSource ->
                    newsSourcesViewModel.setCurrentNewsSource(newsSource)
                    goToNewsSourcesDetailsScreen()
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryChipGroup(
    selectedCategory: String? = null,
    onSelectedCategory: (String) -> Unit
) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        NEWS_DATA_CATEGORY_TYPES.forEach { category ->
            ElevatedSuggestionChip(
                modifier = Modifier.padding(2.dp),
                onClick = {
                    onSelectedCategory(category)
                },
                icon = {
                    if (selectedCategory == category) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Currently checked item"
                        )
                    }
                },
                label = { Text(category) },
            )
        }
    }
}

@Composable
fun ShowNewsSources(
    modifier: Modifier = Modifier,
    newsSourcesState: Async<List<NewsDataNewsSource>>,
    newsSourceSelected: (NewsDataNewsSource) -> Unit
) {
    when (newsSourcesState) {
        is Loading -> {
            Box {
                LoadingIcon(modifier = Modifier.align(Center))
            }
        }
        is Fail -> {
            Box(modifier = Modifier.fillMaxWidth()) {
                ErrorText(
                    modifier = Modifier.align(Center), title = R.string.sources_error
                )
            }
        }
        is Success -> {
            FlowRow(modifier = modifier) {
                val items = newsSourcesState.invoke()
                if (items.isEmpty()) {
                    ErrorText(title = R.string.sources_error)
                } else {
                    items.forEach { newsSource ->
                        NewsSourceCard(
                            newsSource,
                            newsSourceSelected = newsSourceSelected
                        )
                    }
                }
            }
        }
        else -> {}
    }
}

@Composable
fun NewsSourceCard(
    newsDataNewsSource: NewsDataNewsSource,
    newsSourceSelected: (NewsDataNewsSource) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
            .clickable { newsSourceSelected(newsDataNewsSource) },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 10.dp)
    ) {
        Column(
            Modifier.padding(12.dp)
        ) {
            val name = newsDataNewsSource.name ?: ""
            if (name.isNotEmpty()) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
            }
        }
    }
}
