package com.player.v2player.di

import android.app.Application
import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import com.player.v2player.data.repository.VideoRepositoryImpl
import com.player.v2player.domain.repository.VideoRepository
import com.player.v2player.domain.usecase.VideoUseCase
import com.player.v2player.domain.usecase.VideosByFolder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PlayerModule {

    @Provides
    @Singleton
    fun provideVideoPlayer(app: Application): ExoPlayer {
        return ExoPlayer.Builder(app)
            .build()
    }

    @Provides
    @Singleton
    fun playerContext(app: Application) : Context{
        return app
    }
    @Provides
    @Singleton
    fun provideVideoRepository(): VideoRepository {
        return VideoRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideVideoUseCase(repository: VideoRepository): VideoUseCase {
        return VideoUseCase(
            getVideosByFolder= VideosByFolder(repository)
        )
    }

}