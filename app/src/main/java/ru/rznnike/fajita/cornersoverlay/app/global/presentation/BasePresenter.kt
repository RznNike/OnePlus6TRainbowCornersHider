package ru.rznnike.fajita.cornersoverlay.app.global.presentation

import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.koin.core.KoinComponent

open class BasePresenter<View : MvpView> : MvpPresenter<View>(), KoinComponent {
    private val disposables = CompositeDisposable()

    protected fun Disposable.unsubscribeOnDestroy() {
        disposables.add(this)
    }

    override fun onDestroy() {
        disposables.clear()
    }
}
