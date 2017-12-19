package com.alokomkar.mashup.download


import android.os.Environment
import com.alokomkar.mashup.MashUpApplication
import com.alokomkar.mashup.songs.SongsView
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.File
import okio.Okio
import java.io.IOException


/**
 * Created by Alok on 19/12/17.
 */
class DownloadsPresenter( private val songsView : SongsView ) : Observer<File> {

    private val mCompositeDisposable : CompositeDisposable = CompositeDisposable()

    fun downloadFile( fileUrl : String, fileName : String ) {
        MashUpApplication.getDownloadFileApi()!!
                .downloadFile(fileUrl)
                .flatMap(processResponse(fileName))
                .subscribeOn(Schedulers.io())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(this)
    }

    private fun processResponse(fileName: String): Function1<Response<ResponseBody>, Observable<File>> {
        return object : Function1<Response<ResponseBody>, Observable<File>> {
            override fun invoke(responseBodyResponse: Response<ResponseBody>): Observable<File> {
                return saveToDisk(fileName, responseBodyResponse)
            }
        }
    }

    private fun saveToDisk( fileName: String, response: Response<ResponseBody> ): Observable<File> {
        return Observable.create { emitter ->
            try {

                val destinationFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absoluteFile, fileName + ".mp3");
                val bufferedSink = Okio.buffer(Okio.sink(destinationFile))

                bufferedSink.writeAll(response.body()!!.source())
                bufferedSink.close()

                emitter.onNext(destinationFile)
                emitter.onComplete()

            } catch (e: IOException) {
                e.printStackTrace()
                emitter.onError(e)
            }
        }
    }

    override fun onNext(t: File) {
        songsView.hideDownloadProgress(t.nameWithoutExtension)
    }

    override fun onComplete() {

    }

    override fun onError(e: Throwable) {
        songsView.showError(e.localizedMessage)
    }

    override fun onSubscribe(d: Disposable) {
        mCompositeDisposable.add(d)
    }

    fun onDestroy() {
        mCompositeDisposable.clear()
    }


}