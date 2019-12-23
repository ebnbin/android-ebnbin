package com.ebnbin.eb2.async

import com.ebnbin.eb.EBApp
import com.ebnbin.eb.versionCode
import com.ebnbin.eb.library.Libraries
import com.ebnbin.eb.util.DeviceUtil
import com.ebnbin.eb.base64Decode
import com.ebnbin.eb.base64EncodeToString
import com.ebnbin.eb.util.ebnbinApplicationId
import com.ebnbin.eb2.githubapi.GitHubApi
import com.ebnbin.eb2.githubapi.model.PutContentsRequest
import com.ebnbin.eb2.util.TimeHelper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class AsyncHelper {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun clear() {
        compositeDisposable.clear()
    }

    fun add(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    fun <T> observable(
        observable: Observable<T>,
        onNext: ((T) -> Unit)? = null,
        onError: ((Throwable) -> Unit)? = null,
        onComplete: (() -> Unit)? = null,
        onSubscribe: ((Disposable) -> Unit)? = null
    ): Disposable {
        return observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { onNext?.invoke(it) },
                { onError?.invoke(it) },
                { onComplete?.invoke() },
                {
                    compositeDisposable.add(it)
                    onSubscribe?.invoke(it)
                }
            )
    }

    private val loadings: LinkedHashMap<String, Loading<*>> = LinkedHashMap()

    fun <T> load(
        observable: Observable<T>,
        loading: Loading<T>? = null,
        onSuccess: ((T) -> Unit)? = null,
        onFailure: ((Throwable) -> Unit)? = null,
        onStart: ((Disposable) -> Unit)? = null
    ): Disposable {
        val key = TimeHelper.nano().toString()
        return observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    loading?.onSuccess(key, loadings, it)
                    onSuccess?.invoke(it)
                },
                {
                    loading?.onFailure(key, loadings, it) {
                        load(observable, loading, onSuccess, onFailure, onStart)
                    }
                    onFailure?.invoke(it)
                },
                {
                },
                {
                    loading?.onStart(key, loadings, it)
                    onStart?.invoke(it)
                }
            )
    }

    fun <T> task(
        task: () -> T,
        loading: Loading<T>? = null,
        onSuccess: ((T) -> Unit)? = null,
        onFailure: ((Throwable) -> Unit)? = null,
        onStart: ((Disposable) -> Unit)? = null
    ): Disposable {
        return load(
            Observable.create<T> {
                try {
                    val result = task()
                    if (!it.isDisposed) {
                        it.onNext(result)
                        it.onComplete()
                    }
                } catch (throwable: Throwable) {
                    if (!it.isDisposed) {
                        it.onError(throwable)
                    }
                }
            },
            loading, onSuccess, onFailure, onStart
        )
    }

    fun <T> githubGetJson(
        classOfT: Class<T>,
        path: String,
        loading: Loading<T>? = null,
        onSuccess: ((T) -> Unit)? = null,
        onFailure: ((Throwable) -> Unit)? = null,
        onStart: ((Disposable) -> Unit)? = null
    ): Disposable {
        val key = TimeHelper.nano().toString()
        return GitHubApi.api
            .getContentsFile("${EBApp.instance.ebnbinApplicationId}$path")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                val content = String(it.content.base64Decode())
                val t = Libraries.gson.fromJson<T>(content, classOfT)
                t
            }
            .subscribe(
                {
                    loading?.onSuccess(key, loadings, it)
                    onSuccess?.invoke(it)
                },
                {
                    loading?.onFailure(key, loadings, it) {
                        githubGetJson(classOfT, path, loading, onSuccess, onFailure, onStart)
                    }
                    onFailure?.invoke(it)
                },
                {
                },
                {
                    loading?.onStart(key, loadings, it)
                    onStart?.invoke(it)
                }
            )
    }

    fun <T> githubPutJson(
        path: String,
        t: T,
        loading: Loading<Unit>? = null,
        onSuccess: (() -> Unit)? = null,
        onFailure: ((Throwable) -> Unit)? = null,
        onStart: ((Disposable) -> Unit)? = null
    ): Disposable {
        val key = TimeHelper.nano().toString()
        val dir = path.substringBeforeLast("/")
        val name = path.substringAfterLast("/")
        return GitHubApi.api
            .getContentsDirectory("${EBApp.instance.ebnbinApplicationId}$dir")
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .flatMap {
                val putContentsRequest = PutContentsRequest()
                putContentsRequest.message =
                    "${EBApp.instance.ebnbinApplicationId} ${EBApp.instance.versionCode} ${DeviceUtil.DEVICE_ID}"
                putContentsRequest.content = Libraries.gson.toJson(t).toByteArray().base64EncodeToString()
                putContentsRequest.sha = it.firstOrNull { content -> content.name == name }?.sha
                GitHubApi.api.putContents("${EBApp.instance.ebnbinApplicationId}$path", putContentsRequest)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                Unit
            }
            .subscribe(
                {
                    loading?.onSuccess(key, loadings, it)
                    onSuccess?.invoke()
                },
                {
                    loading?.onFailure(key, loadings, it) {
                        githubPutJson(path, t, loading, onSuccess, onFailure, onStart)
                    }
                    onFailure?.invoke(it)
                },
                {
                },
                {
                    loading?.onStart(key, loadings, it)
                    onStart?.invoke(it)
                }
            )
    }

    companion object {
        val global: AsyncHelper = AsyncHelper()
    }
}
