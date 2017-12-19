package com.alokomkar.mashup.download

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.os.Environment
import android.util.Log

import com.alokomkar.mashup.MashUpApplication
import com.alokomkar.mashup.songs.Songs
import com.alokomkar.mashup.songs.SongsView

import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.Proxy
import java.net.URL
import java.net.URLConnection

/**
 * Created by Alok on 19/12/17.
 */

@SuppressLint("StaticFieldLeak")
internal class DownloadFileTask( private val context: Context, val songsView: SongsView ) : AsyncTask<Songs, String, String?>() {
    private var mProgressDialog: ProgressDialog? = null
    private var mSong : Songs ?= null

    override fun onPreExecute() {
        super.onPreExecute()
        mProgressDialog = ProgressDialog(context)
        mProgressDialog!!.setMessage("Downloading file..")
        mProgressDialog!!.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        mProgressDialog!!.setCancelable(false)
        mProgressDialog!!.show()
    }

    private lateinit var file: File

    override fun doInBackground(vararg aurl: Songs): String? {
        var count: Int
        var filename = ""
        mSong = aurl[0];
        try {

            val mediaURL = URL(aurl[0].url)
            // open connection
            val httpURLConnection = mediaURL.openConnection(Proxy.NO_PROXY) as HttpURLConnection
            // stop following browser redirect
            httpURLConnection.instanceFollowRedirects = false
            // extract location header containing the actual destination URL
            val expandedURL = httpURLConnection.getHeaderField("Location")
            Log.d("URL", "Song : " + expandedURL)
            httpURLConnection.disconnect()


            val url = URL(expandedURL)
            val conexion = url.openConnection()
            conexion.connect()
            val lenghtOfFile = conexion.contentLength
            val `is` = url.openStream()
            val testDirectory = File(Environment.getExternalStorageDirectory().toString() + "/Folder")
            if (!testDirectory.exists()) {
                testDirectory.mkdir()
            }
            aurl[0].fileName = aurl[0].song.replace("\\s".toRegex(), "_")
            file = File(testDirectory.toString() + "/" + aurl[0].fileName + ".mp3")
            val fos = FileOutputStream(testDirectory.toString() + "/" + aurl[0].fileName + ".mp3")
            filename = file.absolutePath
            val data = ByteArray(1024)
            var total: Long = 0
            var progress = 0
            count = `is`.read(data)
            while ((count) != -1) {
                total += count.toLong()
                val progress_temp = total.toInt() * 100 / lenghtOfFile
                publishProgress("" + progress_temp)
                if (progress_temp % 10 == 0 && progress != progress_temp) {
                    progress = progress_temp
                }
                fos.write(data, 0, count)
                count = `is`.read(data)
            }
            `is`.close()
            fos.close()

        } catch (e: Exception) {
        }

        return filename

    }

    override fun onProgressUpdate(vararg progress: String) {
        Log.d("ANDRO_ASYNC", progress[0])
        mProgressDialog!!.progress = Integer.parseInt(progress[0])
    }

    override fun onPostExecute(unused: String?) {
        mProgressDialog!!.dismiss()
        if( unused != null ) {
            songsView.onDownloadSuccess(mSong!!, file)
        }
    }
}
