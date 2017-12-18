package com.alokomkar.mashup

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.alokomkar.mashup.base.BaseFragment
import com.alokomkar.mashup.base.hide
import com.alokomkar.mashup.base.show
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_player.*

/**
 * Created by Alok Omkar on 2017-12-16.
 */
class PlayerFragment : BaseFragment(), MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {



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
    private lateinit var mMediaPlayer : MediaPlayer
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
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mSong = arguments!!.getParcelable<Songs>(BUNDLE_SONG)
        mSongsList = arguments!!.getParcelableArrayList(BUNDLE_SONGS_LIST)

        val mRequestOptions = RequestOptions()
        mRequestOptions.placeholder(R.mipmap.ic_launcher)
        mRequestOptions.fallback(R.mipmap.ic_launcher)

        Glide.with(context)
                .load(mSong.coverImage)
                .apply(mRequestOptions)
                .into(songsImageView)

        progressLayout.hide()
        mMediaPlayer = MediaPlayer()
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mMediaPlayer.setDataSource(mSong.url)
        try {
            mMediaPlayer.prepare()
            mMediaPlayer.setOnPreparedListener(this)
        } catch ( e : Exception ) {
            e.printStackTrace()
            progressLayout.hide()
            Toast.makeText(context, "Error : " + e.localizedMessage, Toast.LENGTH_LONG).show()
        }

        playPauseImageView.setOnClickListener{
            if( !mMediaPlayer.isPlaying ) {
                progressLayout.show()
                playPauseImageView.setImageDrawable( ContextCompat.getDrawable(context!!, android.R.drawable.ic_media_pause))
                mMediaPlayer.start()
            }
            else {
                playPauseImageView.setImageDrawable( ContextCompat.getDrawable(context!!, android.R.drawable.ic_media_play))
                mMediaPlayer.pause()
            }
        }

        mMediaPlayer.setOnErrorListener( this )
    }

    override fun onPrepared(mediaPlayer: MediaPlayer?) {
        progressLayout.hide()
        mediaPlayer!!.start()
        playPauseImageView.setImageDrawable( ContextCompat.getDrawable(context!!, android.R.drawable.ic_media_pause))
    }

    /**
     * Called when the Fragment is no longer resumed.  This is generally
     * tied to [Activity.onPause] of the containing
     * Activity's lifecycle.
     */
    override fun onPause() {
        super.onPause()
        if( mMediaPlayer != null && mMediaPlayer!!.isPlaying ) {
            mMediaPlayer!!.pause()
        }
    }

    override fun onError(p0: MediaPlayer?, p1: Int, p2: Int): Boolean {
        progressLayout.hide()
        return true
    }
}