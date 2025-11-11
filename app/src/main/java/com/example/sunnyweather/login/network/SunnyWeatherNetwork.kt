package com.example.sunnyweather.login.network

import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response
import retrofit2.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * 这里是把传统的 Retrofit 网络回调(Callback)接口，封装成了可以用 suspend 的协程函数。
 * 其核心目标是：让网络请求能直接用[同步写法]调用，但是底层仍是异步执行
 *
 * 从 placeService.searchPlaces("Zhongshan").enqueue(object: Callback<PlaceResponse>){...})
 * 通过封装能在协程里直接写出同步风格的代码： val place = SunnyWeatherNetWork.searchPlaces("Zhongshan")
 */

object SunnyWeatherNetwork {
    private val placeService = ServiceCreator.create<PlaceService>()  // 创建 retrofit 的接口实现类
    suspend fun searchPlaces(query: String) = placeService.searchPlaces(query).await()

    private suspend fun <T> Call<T>.await():T{
        return suspendCoroutine{continuation ->
            enqueue(object : Callback<T>{  //enqueue 是 Retrofit 的异步请求方法，他会在请求结束后调用回调

                override fun onResponse(call: Call<T?>, response: Response<T?>) {
                    val responseBody = response.body()
                    if(responseBody != null) continuation.resume(responseBody)
                    else continuation.resumeWithException(RuntimeException("response body is null"))
                }
                override fun onFailure(call: Call<T?>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }

}