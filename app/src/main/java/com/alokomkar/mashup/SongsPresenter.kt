package com.alokomkar.mashup

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlin.collections.ArrayList

/**
 * Created by Alok Omkar on 2017-12-16.
 */
class SongsPresenter(private val songsView : SongsView ) : Observer<ArrayList<Songs>> {

    private val mCompositeDisposable : CompositeDisposable = CompositeDisposable()

    fun getSongs() {
        songsView.showProgress("Loading")
        MashUpApplication.getSongsApI()!!
                .songs
                .subscribeOn(Schedulers.io())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe( this )
    }

    /**
     * Provides the Observer with a new item to observe.
     *
     *
     * The [Observable] may call this method 0 or more times.
     *
     *
     * The `Observable` will not call this method again after it calls either [.onComplete] or
     * [.onError].
     *
     * @param t
     * the item emitted by the Observable
     */
    override fun onNext(t: ArrayList<Songs>) {
        songsView.onSuccess(t)
        songsView.hideProgress()
    }

    /**
     * Notifies the Observer that the [Observable] has finished sending push-based notifications.
     *
     *
     * The [Observable] will not call this method if it calls [.onError].
     */
    override fun onComplete() {
        songsView.hideProgress()
    }

    /**
     * Provides the Observer with the means of cancelling (disposing) the
     * connection (channel) with the Observable in both
     * synchronous (from within [.onNext]) and asynchronous manner.
     * @param d the Disposable instance whose [Disposable.dispose] can
     * be called anytime to cancel the connection
     * @since 2.0
     */
    override fun onSubscribe(d: Disposable) {
        mCompositeDisposable.add(d)
    }

    /**
     * Notifies the Observer that the [Observable] has experienced an error condition.
     *
     *
     * If the [Observable] calls this method, it will not thereafter call [.onNext] or
     * [.onComplete].
     *
     * @param e
     * the exception encountered by the Observable
     */
    override fun onError(e: Throwable) {
        songsView.showError(e.message!!)
    }

    fun onDestroy() {
        mCompositeDisposable.clear()
    }
}