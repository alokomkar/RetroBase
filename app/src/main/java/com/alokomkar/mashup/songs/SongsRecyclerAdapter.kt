package com.alokomkar.mashup.songs

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.alokomkar.mashup.R
import com.alokomkar.mashup.base.hide
import com.alokomkar.mashup.base.show
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

/**
 * Created by Alok Omkar on 2017-12-16.
 */

class SongsRecyclerAdapter(val songsList: ArrayList<Songs>, val songsView: SongsView) : RecyclerView.Adapter<SongsRecyclerAdapter.ViewHolder>() {

    private lateinit var mRequestOptions: RequestOptions
    private var mFilteredList : ArrayList<Songs> = ArrayList(songsList)

    @SuppressLint("CheckResult")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder? {
        mRequestOptions = RequestOptions()
        mRequestOptions.placeholder(R.mipmap.ic_launcher)
        mRequestOptions.fallback(R.mipmap.ic_launcher)

        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_song_list, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val songs = getItemAtPosition(position)

        holder.titleTextView.text = songs.song
        holder.artistTextView.text = songs.artists
        Glide.with(holder.itemView.context)
                .load(songs.coverImage)
                .apply(mRequestOptions)
                .into(holder.songsImageView)

        if( songs.isDownloading ) {
            holder.downloadBar.show()
            holder.downloadImageView.hide()
        }
        else {
            holder.downloadBar.hide()
            holder.downloadImageView.show()
        }

        if( songs.isDownloaded ) {
            holder.downloadImageView.hide()
        }


    }

    fun getItemAtPosition( position: Int ) : Songs {
        return mFilteredList[position]
    }

    fun filterList( searchString : String ) {
        mFilteredList.clear()
        if( searchString.isBlank() ) {
            mFilteredList.addAll(songsList)
        }
        else {
            for( song in songsList ) {
                if( song.artists.contains(searchString, true)
                        || song.song.contains(searchString, true) ) {
                    mFilteredList.add(song)
                }
            }
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mFilteredList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {


        val titleTextView : TextView = itemView.findViewById(R.id.titleTextView)
        val artistTextView : TextView = itemView.findViewById(R.id.artistTextView)
        val songsImageView : ImageView = itemView.findViewById(R.id.songsImageView)
        val downloadBar : ProgressBar = itemView.findViewById(R.id.downloadBar)
        val downloadImageView : ImageView = itemView.findViewById(R.id.downloadImageView)
        val playImageView : ImageView = itemView.findViewById(R.id.playImageView)

        init {
            titleTextView.setOnClickListener(this)
            artistTextView.setOnClickListener(this)
            songsImageView.setOnClickListener(this)
            playImageView.setOnClickListener(this)
            downloadImageView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            val position = adapterPosition
            if( position != RecyclerView.NO_POSITION ) {
                when( view!!.id ) {
                    R.id.downloadImageView -> {
                        songsView.onSongSelect(position, "download")
                    }
                    else -> {
                        songsView.onSongSelect(position, "play")
                    }
                }
            }
        }

    }

    fun showProgress(songIndex: Int) {
        songsList[songIndex].isDownloading = true
        notifyDataSetChanged()
    }

    fun showProgress(songIndex: Songs) {
        songIndex.isDownloading = true
        notifyDataSetChanged()
    }

    fun hideProgress(song: Songs) {
        /*val songIndex = songsList.indexOf(song)
        songsList[songIndex].isDownloading = false
        songsList[songIndex].isDownloaded = true*/
        notifyDataSetChanged()
    }
}
