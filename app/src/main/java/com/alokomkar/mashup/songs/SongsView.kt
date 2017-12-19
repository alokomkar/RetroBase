package com.alokomkar.mashup.songs

import com.alokomkar.mashup.base.BaseView
import java.io.File

/**
 * Created by Alok Omkar on 2017-12-16.
 */
interface SongsView : BaseView {
    fun onSuccess( songsList : ArrayList<Songs> )
    fun onDownloadSuccess( fileUrl : String, downloadedFile : File )
    fun showDownloadProgress( fileUrl: String )
    fun hideDownloadProgress( fileUrl: String )
    fun onSongSelect( songIndex : Int, action : String )
}