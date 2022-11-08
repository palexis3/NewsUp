package com.example.palexis3.newssum.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.compose.collectAsState
import com.example.palexis3.newssum.R
import com.example.palexis3.newssum.models.NEWS_CATEGORY_TYPES
import com.example.palexis3.newssum.models.Source
import com.example.palexis3.newssum.state.SourcesState
import com.example.palexis3.newssum.viewmodels.SourceViewModel
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.SizeMode

@Composable
fun NewsSourcesScreen(
    sourceViewModel: SourceViewModel,
) {
    var sourceCategory by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(key1 = sourceCategory) {
        sourceViewModel.getSources(category = sourceCategory)
    }

    val sourcesState by sourceViewModel.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        item {
            SourcesTitleRow(selectedCategory = { category ->
                sourceCategory = category
            })
            Spacer(Modifier.height(16.dp))
            ShowNewsSources(sourcesState = sourcesState)
        }
    }
}

@Composable
fun SourcesTitleRow(selectedCategory: (String) -> Unit) {
    Column {
        TitleHeader(title = R.string.news_sources)
        Spacer(modifier = Modifier.height(4.dp))
        CategoryMenuBox(selectedCategory = selectedCategory)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryMenuBox(
    initialValue: String = "",
    selectedCategory: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(initialValue) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.clip(RoundedCornerShape(8.dp))
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
                DropdownMenuItem(
                    text = { Text(text = selectionOption) },
                    onClick = {
                        selectedOptionText = selectionOption
                        selectedCategory(selectionOption)
                        expanded = false
                    }
                )
            }
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

            // TODO: Fix FlowRow card alignment
            FlowRow(
                mainAxisSize = SizeMode.Expand
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

@Composable
fun SourceCard(source: Source, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.padding(bottom = 20.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 10.dp)
    ) {
        Column(
            Modifier.padding(12.dp)
        ) {
            if (source.name != null) {
                var name by remember { mutableStateOf(source.name) }
                val minLines = 2
                Text(
                    text = name,
                    style = MaterialTheme.typography.headlineSmall,
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
                    modifier = Modifier
                        .height(32.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = category,
                        modifier = Modifier.padding(4.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
