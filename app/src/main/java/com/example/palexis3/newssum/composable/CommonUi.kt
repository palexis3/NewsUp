package com.example.palexis3.newssum.composable

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight

@Composable
fun LoadingIcon(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxWidth()
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun TitleHeader(@StringRes title: Int) {
    Text(
        text = stringResource(id = title),
        style = MaterialTheme.typography.h4,
        fontWeight = FontWeight.ExtraBold
    )
}

@Composable
fun ErrorText(
    @StringRes title: Int
) {
    Text(
        text = stringResource(id = title),
        style = MaterialTheme.typography.body1,
        fontWeight = FontWeight.Bold
    )
}
