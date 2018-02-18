package com.example.sample.araexoplayer

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.SimpleExoPlayerView
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util

class ExoMainActivity : AppCompatActivity() {

    private lateinit var playerView: SimpleExoPlayerView
    private var player: SimpleExoPlayer? = null
    private var playbackPosition: Long = 0
    private var currentWindow: Int = 0
    private var playWhenReady: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exo_main)

        playerView = findViewById(R.id.video_view)
        initializerPlayer()
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
            initializerPlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        hideSystemUi()
        if (Util.SDK_INT < 23 || player == null) {
            initializerPlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            releasePlayer()
        }
    }

    private fun initializerPlayer() {
        player = ExoPlayerFactory.newSimpleInstance(
                DefaultRenderersFactory(this),
                DefaultTrackSelector(), DefaultLoadControl())
        playerView.player = player
        player?.playWhenReady = playWhenReady
        player?.seekTo(currentWindow, playbackPosition)

        val uri: Uri = Uri.parse(getString(R.string.app_name))
        val mediaResource: MediaSource = buildMediaSource(uri)
        player?.prepare(mediaResource, true, true)
    }

    private fun buildMediaSource(uri: Uri) : MediaSource {
        return ExtractorMediaSource.Factory(DefaultHttpDataSourceFactory("exoplayer-codelab"))
                .createMediaSource(uri)
    }

    private fun releasePlayer() {
        if (player != null) {
            playbackPosition = player?.currentPosition!!
            currentWindow = player?.currentWindowIndex!!
            playWhenReady = player?.playWhenReady!!
            player?.release()
            player = null
        }
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        playerView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }
}
