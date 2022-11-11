package com.example.palexis3.newssum.networking

import com.example.palexis3.newssum.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

private const val NEWS_API_BASE_URL = "https://newsapi.org/"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideNewsApi(): NewsApi {
        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()

        val loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val authInterceptor = Interceptor { chain ->
            val headerRequest = chain.request().newBuilder().addHeader("X-Api-Key", BuildConfig.NEWS_API_KEY).build()
            chain.proceed(headerRequest)
        }
        val client: OkHttpClient = OkHttpClient.Builder().apply {
            addInterceptor(loggingInterceptor)
            addInterceptor(authInterceptor)
        }.build()

        return Retrofit.Builder()
            .baseUrl(NEWS_API_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
            .client(client)
            .build()
            .create(NewsApi::class.java)
    }
}
