package com.example.palexis3.newssum.repository

import com.example.palexis3.newssum.repository.article.ArticleRepository
import com.example.palexis3.newssum.repository.article.ArticleRepositoryImpl
import com.example.palexis3.newssum.repository.source.SourceRepository
import com.example.palexis3.newssum.repository.source.SourceRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
interface RepositoryModule {
    @Binds
    @Singleton
    fun bindArticleRepository(articleRepositoryImpl: ArticleRepositoryImpl): ArticleRepository

    @Binds
    @Singleton
    fun bindSourceRepository(sourceRepositoryImpl: SourceRepositoryImpl): SourceRepository
}