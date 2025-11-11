package com.example.sunnyweather.login

import androidx.lifecycle.liveData
import com.example.sunnyweather.login.model.Place
import com.example.sunnyweather.login.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

object Repository {
    fun searchPlaces(query: String) = liveData(Dispatchers.IO) { //将liveData()函数的线程参数类型指定成了 Dispatchers.IO，这样代码块中的所有代码就都运行在子线程中了。众所周知，Android是 不允许在主线程中进行网络请求的
        val result = try {
            val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
            if (placeResponse.status == "ok"){
                val places = placeResponse.places
                Result.success(places)
            }else{
                Result.failure(RuntimeException("response status is ${placeResponse.status}"))
            }
        }catch (e: Exception) {
            Result.failure<List<Place>>(e)
        }
        emit(result)  // emit()方法负责将包装的结果发射出去，类似于调用 LiveData 的 setValue()方法来通知数据变化，只不过这里无法直接取得返回的 LiveData 对象，所以提供了这样一个替代方法
    }


}