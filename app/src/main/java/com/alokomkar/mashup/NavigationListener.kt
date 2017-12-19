package com.alokomkar.mashup

import com.alokomkar.mashup.songs.Songs

/**
 * Created by Alok Omkar on 2017-12-16.
 */
interface NavigationListener {
    fun loadSongsFragment()
    fun loadPlayerFragment(selectedSong: Songs, allSongs : ArrayList<Songs> )
}