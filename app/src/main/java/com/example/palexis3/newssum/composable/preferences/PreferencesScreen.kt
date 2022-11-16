package com.example.palexis3.newssum.composable.preferences

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
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
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.palexis3.newssum.R
import com.example.palexis3.newssum.composable.TitleHeader
import com.example.palexis3.newssum.helper.EmptyScreenWidth
import com.example.palexis3.newssum.viewmodels.PreferencesViewModel

@Composable
fun PreferencesScreen(
    preferencesViewModel: PreferencesViewModel = hiltViewModel()
) {
    val country by preferencesViewModel.country.collectAsState()
    val language by preferencesViewModel.language.collectAsState()

    var expandCountrySelectionBox by remember { mutableStateOf(false) }
    var expandLanguageSelectionBox by remember { mutableStateOf(false) }

    Box {
        Column {
            TitleHeader(
                modifier = Modifier.align(CenterHorizontally),
                title = R.string.preferences
            )

            Spacer(Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expandCountrySelectionBox = true }
                    .padding(12.dp),
                verticalAlignment = CenterVertically,
                horizontalArrangement = SpaceBetween
            ) {
                Column {
                    Text(
                        text = stringResource(id = R.string.country),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.ExtraBold
                    )
                    val countryVal = country ?: ""
                    if (countryVal.isNotEmpty()) {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = countryVal,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                Icon(imageVector = Icons.Filled.ArrowForward, contentDescription = "Select country")
            }

            Divider(Modifier.fillMaxWidth())

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expandLanguageSelectionBox = true }
                    .padding(12.dp),
                verticalAlignment = CenterVertically,
                horizontalArrangement = SpaceBetween
            ) {
                Column {
                    Text(
                        text = stringResource(id = R.string.language),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.ExtraBold
                    )
                    val languageVal = language ?: ""
                    if (languageVal.isNotEmpty()) {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = languageVal,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                Icon(imageVector = Icons.Filled.ArrowForward, contentDescription = "Select language")
            }
        }

        if (expandCountrySelectionBox) {
            ShowItemsList(
                title = R.string.select_country,
                list = preferencesViewModel.countryMap.keys.toList(),
                currentItem = country,
                selectedItem = { item ->
                    preferencesViewModel.setCountry(item)
                },
                closeScreen = { expandCountrySelectionBox = false }
            )
        }

        if (expandLanguageSelectionBox) {
            ShowItemsList(
                title = R.string.select_language,
                list = preferencesViewModel.languageMap.keys.toList(),
                currentItem = language,
                selectedItem = { item ->
                    preferencesViewModel.setLanguage(item)
                },
                closeScreen = { expandLanguageSelectionBox = false }
            )
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
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        item {
            Row(
                modifier = Modifier.padding(start = 12.dp),
                verticalAlignment = CenterVertically,
                horizontalArrangement = SpaceBetween
            ) {
                IconButton(onClick = closeScreen) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close"
                    )
                }
                Spacer(Modifier.width(EmptyScreenWidth()))
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
                        modifier = Modifier.fillMaxWidth(),
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
