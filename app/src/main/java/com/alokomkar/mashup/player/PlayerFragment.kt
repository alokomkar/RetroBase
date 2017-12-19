package com.alokomkar.mashup.player

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.alokomkar.mashup.base.BUNDLE_SONG
import com.alokomkar.mashup.base.BUNDLE_SONGS_LIST
import com.alokomkar.mashup.R
import com.alokomkar.mashup.base.BaseFragment
import com.alokomkar.mashup.base.hide
import com.alokomkar.mashup.base.show
import com.alokomkar.mashup.songs.Songs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_player.*

import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource


/**
 * Created by Alok Omkar on 2017-12-16.
 */
class PlayerFragment : BaseFragment(), PlayerView {

    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null (which
     * is the default implementation).  This will be called between
     * [.onCreate] and [.onActivityCreated].
     *
     *
     * If you return a View from here, you will later be called in
     * [.onDestroyView] when the view is being released.
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return Return the View for the fragment's UI, or null.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_player, container, false)
    }

    private lateinit var mSong : Songs
    private lateinit var mSongsList : ArrayList<Songs>
    /**
     * Called immediately after [.onCreateView]
     * has returned, but before any saved state has been restored in to the view.
     * This gives subclasses a chance to initialize themselves once
     * they know their view hierarchy has been completely created.  The fragment's
     * view hierarchy is not however attached to its parent at this point.
     * @param view The View returned by [.onCreateView].
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mSong = arguments!!.getParcelable(BUNDLE_SONG)
        mSongsList = arguments!!.getParcelableArrayList(BUNDLE_SONGS_LIST)

    }

    private fun initializePlayback(songs: Songs) {
        val uri = Uri.parse(songs.expandedUrl)
        val mediaSource = buildMediaSource(uri)
        mExoPlayer?.prepare(mediaSource, true, false)
    }

    private fun buildMediaSource(uri: Uri): ExtractorMediaSource {
        return ExtractorMediaSource(uri,
                DefaultHttpDataSourceFactory("ua"),
                DefaultExtractorsFactory(), null, null)
    }

    private var mExoPlayer: SimpleExoPlayer ?= null
    private var mPlayWhenReady: Boolean = true
    private var mCurrentWindow: Int = 0
    private var mPlaybackPosition: Long = 0

    private fun initializePlayer() {

        mExoPlayer = ExoPlayerFactory.newSimpleInstance(
                DefaultRenderersFactory(context),
                DefaultTrackSelector(), DefaultLoadControl())

        videoView.player = mExoPlayer

        mExoPlayer?.playWhenReady = mPlayWhenReady
        mExoPlayer?.seekTo(mCurrentWindow, mPlaybackPosition)
    }

    override fun onResume() {
        super.onResume()
        //hideSystemUi()
        if ( mExoPlayer == null ) {
            initializePlayer()
            resolveURL()
        }
    }

    private fun resolveURL() {
        UrlExpanderTask(this).execute(mSong)
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        videoView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    override fun onPause() {
        super.onPause()
        releasePlayer()
    }

    private fun releasePlayer() {
        if (mExoPlayer != null) {
            mPlaybackPosition = mExoPlayer?.currentPosition!!
            mCurrentWindow = mExoPlayer?.currentWindowIndex!!
            mPlayWhenReady = mExoPlayer?.playWhenReady!!
            mExoPlayer?.release()
            mExoPlayer = null
        }
    }

    override fun showProgress(message: String) {
        progressLayout.show()
    }

    override fun hideProgress() {
        progressLayout.hide()
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun onSuccess(songs: Songs) {
        initializePlayback(songs)
    }
}