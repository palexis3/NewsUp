package com.example.palexis3.newssum.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.compose.collectAsState
import com.example.palexis3.newssum.R
import com.example.palexis3.newssum.models.NEWS_API_CATEGORY_TYPES
import com.example.palexis3.newssum.models.NewsSource
import com.example.palexis3.newssum.state.NewsSourcesState
import com.example.palexis3.newssum.viewmodels.NewsSourcesViewModel
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun NewsSourcesScreen(
    newsSourcesViewModel: NewsSourcesViewModel,
    goToNewsSourcesDetailsScreen: () -> Unit
) {
    var newsSourceCategory by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(key1 = newsSourceCategory) {
        newsSourcesViewModel.getNewsSources(category = newsSourceCategory)
    }

    val newsSourcesState by newsSourcesViewModel.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        TitleHeader(
            modifier = Modifier.align(CenterHorizontally),
            title = R.string.news_sources
        )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryChipGroup(
    selectedCategory: String? = null,
    onSelectedCategory: (String) -> Unit
) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth().padding(12.dp)
    ) {
        NEWS_API_CATEGORY_TYPES.forEach { category ->
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
    newsSourcesState: NewsSourcesState,
    newsSourceSelected: (NewsSource) -> Unit
) {
    when (val state = newsSourcesState.newsSources) {
        is Loading -> {
            Box {
                LoadingIcon(modifier = Modifier.align(Center))
            }
        }
        is Fail -> {
            Box {
                ErrorText(
                    modifier = Modifier.align(Center), title = R.string.sources_error
                )
            }
        }
        is Success -> {
            LazyColumn(modifier = modifier) {
                val items = state.invoke()
                item {
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
        }
        else -> {}
    }
}

@Composable
fun NewsSourceCard(
    newsSource: NewsSource,
    newsSourceSelected: (NewsSource) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
            .clickable { newsSourceSelected(newsSource) },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 10.dp)
    ) {
        Column(
            Modifier.padding(12.dp)
        ) {
            val name = newsSource.name ?: ""
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

            val description = newsSource.description ?: ""
            if (description.isNotEmpty()) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 6,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
            }
        }
    }
}
