package com.example.palexis3.newssum

import android.app.Application
import android.content.Context
import com.example.palexis3.newssum.helper.PreferencesManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Singleton
    @Provides
    fun provideContext(application: Application): Context = application.applicationContext

    @Singleton
    @Provides
    fun preferencesManager(@ApplicationContext context: Context) = PreferencesManager(context)
}