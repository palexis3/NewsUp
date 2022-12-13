package com.example.palexis3.newssum.viewmodels

import com.example.palexis3.newssum.repository.TestArticlesRepository
import com.example.palexis3.newssum.utils.MainDispatcherRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ArticlesViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val articlesRepository = TestArticlesRepository()

    private lateinit var viewModel: ArticleViewModel

    @Before
    fun setup() {
        viewModel = ArticleViewModel(articlesRepository)
    }

    // TODO: Add mavericks viewmodel testing
}