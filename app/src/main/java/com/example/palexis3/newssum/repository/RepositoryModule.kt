package com.example.palexis3.newssum.repository

import com.airbnb.mvrx.hilt.MavericksViewModelComponent
import com.example.palexis3.newssum.repository.article.ArticleRepository
import com.example.palexis3.newssum.repository.article.ArticleRepositoryImpl
import com.example.palexis3.newssum.repository.source.NewsSourcesRepository
import com.example.palexis3.newssum.repository.source.NewsSourcesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn

@Module
@InstallIn(MavericksViewModelComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindArticleRepository(impl: ArticleRepositoryImpl): ArticleRepository

    @Binds
    abstract fun bindNewsSourceRepository(impl: NewsSourcesRepositoryImpl): NewsSourcesRepository
}
