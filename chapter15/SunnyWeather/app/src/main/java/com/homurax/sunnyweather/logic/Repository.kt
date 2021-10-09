package com.homurax.sunnyweather.logic

import androidx.lifecycle.liveData
import com.homurax.sunnyweather.logic.dao.PlaceDao
import com.homurax.sunnyweather.logic.model.Place
import com.homurax.sunnyweather.logic.model.Weather
import com.homurax.sunnyweather.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

// 在logic包下新建一个Repository单例类，作为仓库层的统一封装入口
// 一般在仓库层中定义的方法，为了能将异步获取的数据以响应式编程的方式通知给上一层，
// 通常会返回一个LiveData对象。LiveData对象方便被ModelView观察和处理。
object Repository {

    fun savePlace(place: Place) = PlaceDao.savePlace(place)

    fun getSavedPlace() = PlaceDao.getSavedPlace()

    fun isPlaceSaved() = PlaceDao.isPlaceSaved()

//    fun searchPlaces(query: String) = fire(Dispatchers.IO) {
//        val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
//        if (placeResponse.status == "ok") {
//            val places = placeResponse.places
//            Result.success(places)
//        } else {
//            Result.failure(RuntimeException("response status is ${placeResponse.status}"))
//        }
//    }

//  liveData()函数是lifecycle-livedata-ktx库提供的一个非常强大且好用的功能，
//  它可以自动构建并返回一个LiveData对象，然后在它的代码块中 提供一个挂起函数的上下文，这样我们就可以在liveData()函数的代码块中调用任意的挂起函数了。
    fun searchPlaces(query: String) = liveData(Dispatchers.IO) {
        val result = try {
            val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
            if (placeResponse.status == "ok") {
                val places = placeResponse.places

                // 如果服务器响应的状态是ok，那么就使用Kotlin内置的Result.success()方法来包装获取的城市数据列表 ????
                Result.success(places)
            } else {
                // 返回异常
                Result.failure(RuntimeException("response status is ${placeResponse.status}"))
            }
        } catch (e: Exception) {
            Result.failure<List<Place>>(e)
        }
        // emit()方法将包装的结果发射出去，这个emit()方法其实类似于调用LiveData的setValue()方法来通知数据变化
        emit(result)
    }

    fun refreshWeather(lng: String, lat: String, placeName: String) = fire(Dispatchers.IO) {
        coroutineScope {
            val deferredRealtime = async {
                SunnyWeatherNetwork.getRealtimeWeather(lng, lat)
            }
            val deferredDaily = async {
                SunnyWeatherNetwork.getDailyWeather(lng, lat)
            }
            val realtimeResponse = deferredRealtime.await()
            val dailyResponse = deferredDaily.await()
            if (realtimeResponse.status == "ok" && dailyResponse.status == "ok") {
                val weather = Weather(realtimeResponse.result.realtime, dailyResponse.result.daily)
                Result.success(weather)
            } else {
                Result.failure(
                    RuntimeException(
                        "realtime response status is ${realtimeResponse.status}" +
                                "daily response status is ${dailyResponse.status}"
                    )
                )
            }
        }
    }

//  liveData()函数是lifecycle-livedata-ktx库提供的一个非常强大且好用的功能，
//  它可以自动构建并返回一个LiveData对象，然后在它的代码块中 提供一个挂起函数的上下文，这样我们就可以在liveData()函数的代码块中调用任意的挂起函数了。
    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData<Result<T>>(context) {
            val result = try {
                block()
            } catch (e: Exception) {
                Result.failure<T>(e)
            }
            emit(result)
        }

}