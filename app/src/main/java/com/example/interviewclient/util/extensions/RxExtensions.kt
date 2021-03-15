package com.atom.myfirstkotlinproject.utils.extensions

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.ObservableSubscribeProxy
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author qiuyunfei
 * @description
 */
/**
 * rx自动切换线程
 */
fun <T> Observable<T>.switch(): Observable<T> =
    subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

/**
 * 生命周期管理
 */
fun <T> Observable<T>.autoDispose(context: Context): ObservableSubscribeProxy<T> =
    `as`(
        AutoDispose.autoDisposable(
            AndroidLifecycleScopeProvider
                .from(context as LifecycleOwner, Lifecycle.Event.ON_DESTROY)
        )
    )

fun <T> Observable<T>.switchAndAutoDispose(context: Context): ObservableSubscribeProxy<T> =
    switch()
        .autoDispose(context)