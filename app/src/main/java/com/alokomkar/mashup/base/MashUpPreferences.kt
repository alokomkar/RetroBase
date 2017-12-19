package com.alokomkar.mashup.base

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.alokomkar.mashup.songs.Songs
import com.google.gson.Gson
import java.io.File

/**
 * Created by Alok Omkar on 2017-12-19.
 */
class MashUpPreferences(context: Context ) {

    private val mSharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun addDownloadedFile( songs: Songs, songFile : File ) {
        mSharedPreferences.edit().putString(songs.url, songFile.absolutePath).apply()
    }

    fun getDownloadedFile( songs: Songs ) : String {
        return mSharedPreferences.getString(songs.url, "")
    }

    fun setFavorite( songs: Songs ) {
        val favoritesList = getFavorites()
        if( !favoritesList.contains(songs) )
            favoritesList.add(songs)
        mSharedPreferences.edit().putString("favorites", Gson().toJson(FavoritesList("favorites", favoritesList)).toString()).apply()
    }

    fun isFavorite( songs: Songs ) : Boolean {
        val favoritesList = getFavorites()
        return favoritesList.contains(songs)
    }

    private fun getFavorites(): ArrayList<Songs> {
        val userBuddies = mSharedPreferences.getString("favorites", "")
        if( userBuddies.isNullOrEmpty() ) {
            return ArrayList()
        }
        else
            return Gson().fromJson(userBuddies, FavoritesList()::class.java).songsList
    }

    inner class FavoritesList( var key : String = "favorites", var songsList: ArrayList<Songs> = ArrayList())

}