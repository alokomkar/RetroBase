package com.alokomkar.mashup.player

import com.alokomkar.mashup.base.BaseView
import com.alokomkar.mashup.songs.Songs

/**
 * Created by Alok Omkar on 2017-12-19.
 */
interface PlayerView : BaseView {
    fun onSuccess( songs: Songs)
}