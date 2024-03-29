package com.example.palexis3.newssum.composable.articles

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.palexis3.newssum.R
import com.example.palexis3.newssum.composable.CategoryOutlinedText
import com.example.palexis3.newssum.helper.formatToReadableDate
import com.example.palexis3.newssum.helper.toDateEmptySpace
import com.example.palexis3.newssum.models.news_data.NewsDataArticle
import com.example.palexis3.newssum.viewmodels.ArticleViewModel
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun NewsDataArticleDetailsScreen(
    articleViewModel: ArticleViewModel,
    closeScreen: () -> Unit,
    goToWebView: (String) -> Unit,
    title: (String) -> Unit
) {
    val article by remember { articleViewModel.currentNewsDataArticle }

    // Close the screen automatically if the article is null
    article?.let { item ->
        ShowNewsDataArticleState(
            item,
            goToWebView,
            title
        )
    } ?: closeScreen()
}

@Composable
fun ShowNewsDataArticleState(
    newsDataArticle: NewsDataArticle,
    goToWebView: (String) -> Unit,
    screenTitle: (String) -> Unit
) {
    LazyColumn {
        item {
            Box(Modifier.fillMaxWidth()) {
                val imageUrl = newsDataArticle.image_url
                if (imageUrl != null) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Article Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        contentScale = ContentScale.Crop,
                        alignment = Center,
                        error = painterResource(R.drawable.not_found)
                    )
                }
            }
            Spacer(Modifier.height(4.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                val title = newsDataArticle.title ?: ""
                screenTitle(title)

                val publishedAt = newsDataArticle.pubDate ?: ""
                if (publishedAt.isNotEmpty()) {
                    val date = publishedAt.toDateEmptySpace().formatToReadableDate()
                    Text(
                        text = date,
                        style = MaterialTheme.typography.bodySmall,
                        fontStyle = FontStyle.Italic
                    )
                    Spacer(Modifier.height(2.dp))
                }

                val creator = newsDataArticle.creator?.get(0) ?: ""
                if (creator.isNotEmpty()) {
                    val authorText = "Written by: $creator"
                    Text(
                        text = authorText,
                        style = MaterialTheme.typography.bodySmall,
                        fontStyle = FontStyle.Italic
                    )
                    Spacer(Modifier.height(8.dp))
                }

                val keywords = newsDataArticle.keywords ?: listOf()
                if (keywords.isNotEmpty()) {
                    FlowRow {
                        keywords.forEach { item ->
                            CategoryOutlinedText(
                                category = item,
                                modifier = Modifier.padding(end = 4.dp, bottom = 4.dp)
                            )
                        }
                    }
                    Spacer(Modifier.height(4.dp))
                }

                val articleUrl = newsDataArticle.link ?: ""
                if (articleUrl.isNotEmpty()) {
                    Spacer(Modifier.height(4.dp))
                    ElevatedButton(
                        modifier = Modifier.align(End),
                        elevation = ButtonDefaults.buttonElevation(6.dp),
                        onClick = { goToWebView(articleUrl) }
                    ) {
                        Text(text = stringResource(id = R.string.open_web_version))
                    }
                }

                Spacer(Modifier.height(20.dp))

                val content = newsDataArticle.content ?: ""
                val description = newsDataArticle.description ?: ""

                Box(modifier = Modifier.fillMaxWidth()) {
                    if (content.isNotEmpty()) {
                        Text(text = content, style = MaterialTheme.typography.bodyLarge)
                    } else if (description.isNotEmpty()) {
                        Text(text = description, style = MaterialTheme.typography.bodyLarge)
                    } else {
                        Text(
                            text = stringResource(id = R.string.article_content_error),
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.align(Center)
                        )
                    }
                }
            }
        }
    }
}
