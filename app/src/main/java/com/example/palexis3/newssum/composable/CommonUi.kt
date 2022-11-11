package com.example.palexis3.newssum.composable

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.example.palexis3.newssum.R
import com.example.palexis3.newssum.helper.formatToReadableDate
import com.example.palexis3.newssum.helper.toDate
import com.example.palexis3.newssum.models.news_api.NewsApiArticle
import com.example.palexis3.newssum.state.ArticlesState

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

/**
 * The ShowArticleState and ArticleCard composable are in the common section because
 * they will be used in the HeadlineScreen and NewsSourceDetailsScreen
 */
@Composable
fun ShowArticlesState(
    articlesState: ArticlesState,
    articleSelected: (NewsApiArticle) -> Unit
) {
    when (val state = articlesState.articles) {
        is Loading -> {}
        is Fail -> {
            Box {
                ErrorText(title = R.string.articles_error)
            }
        }
        is Success -> {
            LazyColumn {
                val items = state.invoke()
                if (items.isEmpty()) {
                    item {
                        ErrorText(title = R.string.articles_error)
                    }
                } else {
                    items(items, itemContent = { article ->
                        ArticleCard(
                            newsApiArticle = article,
                            articleSelected = articleSelected
                        )
                    })
                }
            }
        }
        else -> {}
    }
}

@Composable
fun ArticleCard(
    newsApiArticle: NewsApiArticle,
    articleSelected: (NewsApiArticle) -> Unit
) {
    Card(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .padding(12.dp)
            .clickable { articleSelected(newsApiArticle) },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 10.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            val urlImage = newsApiArticle.urlToImage ?: ""
            if (urlImage.isNotEmpty()) {
                AsyncImage(
                    model = urlImage,
                    contentDescription = "Headline Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                )
                Spacer(Modifier.height(8.dp))
            }

            Column(modifier = Modifier.padding(12.dp)) {
                val title = newsApiArticle.title ?: ""
                if (title.isNotEmpty()) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(4.dp))
                }

                val publishedAt = newsApiArticle.publishedAt ?: ""
                if (publishedAt.isNotEmpty()) {
                    val date = publishedAt.toDate().formatToReadableDate()
                    Text(text = date, style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.height(4.dp))
                }

                val description = newsApiArticle.description ?: ""
                if (description.isNotEmpty()) {
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
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
