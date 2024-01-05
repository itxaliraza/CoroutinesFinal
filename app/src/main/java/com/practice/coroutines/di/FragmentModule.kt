package com.practice.coroutines.di

import android.app.Activity
import android.content.Context
import com.practice.coroutines.databinding.FragmentMediaBinding
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.FragmentScoped

@InstallIn(FragmentComponent::class)
@Module
object FragmentModule {

    @FragmentScoped
    @Provides
    fun provideFragmentMediaBinding(@ActivityContext appContext: Context) =
        FragmentMediaBinding.inflate((appContext as Activity).layoutInflater)

}