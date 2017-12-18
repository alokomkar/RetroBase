package com.alokomkar.mashup

/**
 * Created by Alok Omkar on 2017-12-16.
 */
interface NavigationListener {
    fun loadSongsFragment()
    fun loadPlayerFragment( selectedSong: Songs, allSongs : ArrayList<Songs> )
}