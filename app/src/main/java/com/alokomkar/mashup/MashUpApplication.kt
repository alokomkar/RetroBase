package com.alokomkar.mashup

import android.app.Application
import com.alokomkar.mashup.base.NetModule
import com.alokomkar.mashup.songs.SongsAPI

/**
 * Created by Alok Omkar on 2017-12-16.
 */
class MashUpApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {

        var songsAPI: SongsAPI?= null
        fun getSongsApI() : SongsAPI? {
            if( songsAPI == null ) songsAPI = NetModule(instance).songsAPI
            return songsAPI
        }

        lateinit var instance: MashUpApplication
            private set
    }
}