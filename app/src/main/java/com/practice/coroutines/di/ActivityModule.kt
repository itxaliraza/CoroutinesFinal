package com.practice.coroutines.di

import android.app.Activity
import android.content.Context
import com.practice.coroutines.databinding.ActivityGalleryBinding
import com.practice.coroutines.databinding.ActivityMainBinding
import com.practice.coroutines.databinding.ActivityMultipleQuotesBinding
import com.practice.coroutines.databinding.ActivityQuoteOfTheDayBinding
import com.practice.coroutines.databinding.ActivitySupervisorBinding
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object ActivityModule {


    @ActivityScoped
    @Provides
    fun provideActivityGalleryBinding(@ActivityContext appContext: Context) =
        ActivityGalleryBinding.inflate((appContext as Activity).layoutInflater)


    @ActivityScoped
    @Provides
    fun provideActivityMainBinding(@ActivityContext appContext: Context) =
        ActivityMainBinding.inflate((appContext as Activity).layoutInflater)

    @ActivityScoped
    @Provides
    fun provideActivityMultipleQuotesBinding(@ActivityContext appContext: Context) =
        ActivityMultipleQuotesBinding.inflate((appContext as Activity).layoutInflater)

    @ActivityScoped
    @Provides
    fun provideActivityQuoteOfTheDayBinding(@ActivityContext appContext: Context) =
        ActivityQuoteOfTheDayBinding.inflate((appContext as Activity).layoutInflater)

    @ActivityScoped
    @Provides
    fun provideActivitySupervisorBinding(@ActivityContext appContext: Context) =
        ActivitySupervisorBinding.inflate((appContext as Activity).layoutInflater)


}