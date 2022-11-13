package com.example.palexis3.newssum.composable

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

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
fun TitleHeader(modifier: Modifier = Modifier, @StringRes title: Int) {
    Text(
        modifier = modifier,
        text = stringResource(id = title),
        style = MaterialTheme.typography.headlineLarge,
        fontWeight = FontWeight.ExtraBold
    )
}

@Composable
fun ErrorText(
    modifier: Modifier = Modifier,
    @StringRes title: Int
) {
    Text(
        modifier = modifier,
        text = stringResource(id = title),
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun CategoryOutlinedText(category: String, modifier: Modifier = Modifier) {
    OutlinedCard(
        border = BorderStroke(
            width = 1.dp, color = Color.Black
        ),
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
    ) {
        Text(
            text = category,
            modifier = Modifier.padding(8.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
