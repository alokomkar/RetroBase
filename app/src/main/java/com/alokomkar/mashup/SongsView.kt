package com.alokomkar.mashup

import com.alokomkar.mashup.base.BaseView

/**
 * Created by Alok Omkar on 2017-12-16.
 */
interface SongsView : BaseView {
    fun onSuccess( songsList : ArrayList<Songs> )
    fun onSongSelect( songIndex : Int )
}