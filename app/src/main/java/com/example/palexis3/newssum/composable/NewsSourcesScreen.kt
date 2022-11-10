package com.example.palexis3.newssum.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.compose.collectAsState
import com.example.palexis3.newssum.R
import com.example.palexis3.newssum.models.NEWS_CATEGORY_TYPES
import com.example.palexis3.newssum.models.NewsSource
import com.example.palexis3.newssum.state.NewsSourcesState
import com.example.palexis3.newssum.viewmodels.NewsSourcesViewModel
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.google.accompanist.flowlayout.SizeMode

@Composable
fun NewsSourcesScreen(
    newsSourcesViewModel: NewsSourcesViewModel,
) {
    var newsSourceCategory by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(key1 = newsSourceCategory) {
        newsSourcesViewModel.getNewsSources(category = newsSourceCategory)
    }

    val sourcesState by newsSourcesViewModel.collectAsState()

    LazyColumn {
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                TitleHeader(
                    modifier = Modifier.align(CenterHorizontally), title = R.string.news_sources
                )

                Spacer(modifier = Modifier.height(12.dp))

                CategoryMenuBox(
                    modifier = Modifier.padding(12.dp),
                    selectedCategory = { selectedCategory ->
                        newsSourceCategory = selectedCategory
                    }
                )

                Spacer(Modifier.height(8.dp))

                ShowNewsSources(
                    modifier = Modifier.padding(12.dp), sourcesState = sourcesState
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryMenuBox(
    modifier: Modifier = Modifier,
    initialValue: String = "",
    selectedCategory: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(initialValue) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.clip(RoundedCornerShape(8.dp))
    ) {
        TextField(
            modifier = Modifier.menuAnchor(),
            value = selectedOptionText,
            onValueChange = {},
            readOnly = true,
            label = { Text("Category") },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            }
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            NEWS_CATEGORY_TYPES.forEach { selectionOption ->
                DropdownMenuItem(text = { Text(text = selectionOption) }, onClick = {
                    selectedOptionText = selectionOption
                    selectedCategory(selectionOption)
                    expanded = false
                })
            }
        }
    }
}

@Composable
fun ShowNewsSources(
    modifier: Modifier = Modifier,
    sourcesState: NewsSourcesState
) {
    when (val state = sourcesState.newsSources) {
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
            FlowRow(
                modifier = modifier,
                mainAxisSize = SizeMode.Expand,
                mainAxisAlignment = MainAxisAlignment.Start
            ) {
                val items = state.invoke()
                if (items.isEmpty()) {
                    ErrorText(title = R.string.sources_error)
                } else {
                    items.forEach { source ->
                        NewsSourceCard(source)
                    }
                }
            }
        }
        else -> {}
    }
}

@Composable
fun NewsSourceCard(source: NewsSource, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 10.dp)
    ) {
        Column(
            Modifier.padding(12.dp)
        ) {
            val name = source.name
            if (name != null) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
            }

            val description = source.description
            if (description != null) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 6,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
            }

            val category = source.category
            if (category != null) {
                OutlinedCard(
                    border = BorderStroke(
                        width = 1.dp, color = Color.Black
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.align(CenterHorizontally)
                ) {
                    Text(
                        text = category,
                        modifier = Modifier.padding(8.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
