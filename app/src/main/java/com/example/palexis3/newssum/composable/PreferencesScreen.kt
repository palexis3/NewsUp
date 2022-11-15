package com.example.palexis3.newssum.composable

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.palexis3.newssum.R
import com.example.palexis3.newssum.viewmodels.PreferencesViewModel

@Composable
fun PreferencesScreen(
    preferencesViewModel: PreferencesViewModel = hiltViewModel()
) {
    val countryMap: Map<String, String> = preferencesViewModel.countryMap
    val languageMap: Map<String, String> = preferencesViewModel.languageMap

    val country by preferencesViewModel.country.collectAsState()
    val language by preferencesViewModel.language.collectAsState()

    var expandCountrySelectionBox by remember { mutableStateOf(false) }
    var expandLanguageSelectionBox by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TitleHeader(
            modifier = Modifier.align(CenterHorizontally),
            title = R.string.preferences
        )

        Row(
            modifier = Modifier
                .clickable { expandCountrySelectionBox = true }
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = stringResource(id = R.string.country),
                    style = MaterialTheme.typography.bodyMedium
                )
                val countryVal = country ?: ""
                if (countryVal.isNotEmpty()) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = countryVal,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }

            Icon(imageVector = Icons.Filled.ArrowForward, contentDescription = "Select country")
        }

        Divider(Modifier.fillMaxWidth())

        Row(
            modifier = Modifier
                .clickable { expandLanguageSelectionBox = true }
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = stringResource(id = R.string.language),
                    style = MaterialTheme.typography.bodyMedium
                )
                val languageVal = language ?: ""
                if (languageVal.isNotEmpty()) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = languageVal,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }

            Icon(imageVector = Icons.Filled.ArrowForward, contentDescription = "Select language")
        }

        if (expandCountrySelectionBox) {
            Box {
                ShowItemsList(
                    title = R.string.country,
                    list = countryMap.keys.toList(),
                    currentItem = country,
                    selectedItem = { item ->
                        preferencesViewModel.setCountry(item)
                    },
                    closeScreen = { expandCountrySelectionBox = false }
                )
            }
        }

        if (expandLanguageSelectionBox) {
            Box {
                ShowItemsList(
                    title = R.string.language,
                    list = languageMap.keys.toList(),
                    currentItem = language,
                    selectedItem = { item ->
                        preferencesViewModel.setLanguage(item)
                    },
                    closeScreen = { expandLanguageSelectionBox = false }
                )
            }
        }
    }
}

@Composable
fun ShowItemsList(
    @StringRes title: Int,
    list: List<String>,
    currentItem: String?,
    selectedItem: (String) -> Unit,
    closeScreen: () -> Unit
) {
    LazyColumn {
        item {
            Row(
                modifier = Modifier.padding(start = 12.dp),
                verticalAlignment = CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(onClick = closeScreen) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close"
                    )
                }
                TitleHeader(title = title)
            }

            Spacer(Modifier.height(20.dp))

            list.forEachIndexed { index, item ->
                Column(
                    modifier = Modifier
                        .clickable { selectedItem(item) }
                        .padding(12.dp)
                ) {
                    Row(
                        horizontalArrangement = SpaceBetween,
                        verticalAlignment = CenterVertically
                    ) {
                        Text(
                            text = item,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        if (currentItem == item) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Selected item"
                            )
                        }
                    }

                    if (index < list.size - 1) {
                        Divider(Modifier.fillMaxWidth())
                    }
                }
            }
        }
    }
}
