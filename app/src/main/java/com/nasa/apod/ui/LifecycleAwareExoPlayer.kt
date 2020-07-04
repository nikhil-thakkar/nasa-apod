package com.nasa.apod.ui

import android.content.Context
import android.net.Uri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util


class LifecycleAwareExoPlayer(
    private val appContext: Context,
    private var exoPlayer: ExoPlayer? = null,
    private val url: String = "https://storage.googleapis.com/exoplayer-test-media-0/play.mp3"
) :
    LifecycleObserver {

    private var isPlayerReady: Boolean = false
    private var playbackPosition: Long = 0
    private var currentWindow: Int = 0

    private fun initializePlayer() {
        val uri = Uri.parse(url)
        val mediaSource = buildMediaSource(uri)
        exoPlayer?.apply {
            prepare(mediaSource)
            playWhenReady = isPlayerReady
        }
    }


    private fun buildMediaSource(uri: Uri?): MediaSource {
        val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(appContext, "exoplayer-codelab")
        return ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(uri)
    }

    private fun releasePlayer() {
        exoPlayer?.apply {
            isPlayerReady = playWhenReady
            playbackPosition = currentPosition
            currentWindow = currentWindowIndex
            release()
            exoPlayer = null
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        if (Util.SDK_INT >= 24) {
            initializePlayer()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        if ((Util.SDK_INT < 24 || exoPlayer == null)) {
            initializePlayer()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }
}