package com.practice.coroutines.di

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.practice.coroutines.data.HttpRoutes
import com.practice.coroutines.ui.quotes.data.api.QuotesApi
import com.practice.coroutines.ui.quotes.data.repo.SingleQuoteRepositoryImpl
import com.practice.coroutines.ui.quotes.domain.repo.SingleQuotesrepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideRequestManager(
        @ApplicationContext appContext: Context
    ): RequestManager = Glide.with(appContext)


    @Provides
    @Singleton
    fun provideRetrfit() = Retrofit.Builder().baseUrl(HttpRoutes.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()).build()

    @Provides
    @Singleton
    fun provideQuotesApi(retrofit: Retrofit) = retrofit.create(QuotesApi::class.java)

    @Provides
    @Singleton
    fun provideQuotesRepository(quotesApi: QuotesApi): SingleQuotesrepository =
        SingleQuoteRepositoryImpl(quotesApi)


}