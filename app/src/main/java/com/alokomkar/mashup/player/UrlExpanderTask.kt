package com.alokomkar.mashup.player

import android.os.AsyncTask
import com.alokomkar.mashup.MashUpApplication
import com.alokomkar.mashup.songs.Songs
import java.net.HttpURLConnection
import java.net.Proxy
import java.net.URL

/**
 * Created by Alok Omkar on 2017-12-19.
 */
class UrlExpanderTask( private val playerView: PlayerView ) : AsyncTask<Songs, Void, Songs>() {

    override fun onCancelled(result: Songs?) {
        super.onCancelled(result)
        playerView.hideProgress()
        playerView.showError("Error resolving url")
    }

    override fun onCancelled() {
        super.onCancelled()
        playerView.hideProgress()
        playerView.showError("Error resolving url")
    }

    override fun onPostExecute(result: Songs?) {
        super.onPostExecute(result)
        playerView.onSuccess(result!!)
    }

    override fun onPreExecute() {
        super.onPreExecute()
        playerView.showProgress("Loading")
    }

    override fun doInBackground(vararg songs: Songs?): Songs {
        val mediaURL = URL(songs[0]!!.url)
        // open connection
        val httpURLConnection = mediaURL.openConnection(Proxy.NO_PROXY) as HttpURLConnection
        // stop following browser redirect
        httpURLConnection.instanceFollowRedirects = false
        // extract location header containing the actual destination URL
        val expandedURL = httpURLConnection.getHeaderField("Location")
        httpURLConnection.disconnect()
        val proxyURL = MashUpApplication.instance.getProxy().getProxyUrl(expandedURL)
        songs[0]!!.expandedUrl = proxyURL
        return songs[0]!!
    }


}