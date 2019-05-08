package com.ebnbin.eb.async

import com.ebnbin.eb.githubapi.GitHubApi
import com.ebnbin.eb.githubapi.model.PutContentsRequest
import com.ebnbin.eb.library.Libraries
import com.ebnbin.eb.util.BuildHelper
import com.ebnbin.eb.util.DataHelper
import com.ebnbin.eb.util.DeviceHelper
import com.ebnbin.eb.util.TimeHelper
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
            .getContentsFile("${BuildHelper.simpleApplicationId}$path")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                val content = DataHelper.base64Decode(it.content)
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
            .getContentsDirectory("${BuildHelper.simpleApplicationId}$dir")
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .flatMap {
                val putContentsRequest = PutContentsRequest()
                putContentsRequest.message =
                    "${BuildHelper.simpleApplicationId} ${BuildHelper.versionName} ${DeviceHelper.DEVICE_ID}"
                putContentsRequest.content = DataHelper.base64Encode(Libraries.gson.toJson(t))
                putContentsRequest.sha = it.firstOrNull { content -> content.name == name }?.sha
                GitHubApi.api.putContents("${BuildHelper.simpleApplicationId}$path", putContentsRequest)
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
}
