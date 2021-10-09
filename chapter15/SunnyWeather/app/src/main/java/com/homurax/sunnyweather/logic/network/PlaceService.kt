package com.homurax.sunnyweather.logic.network

import com.homurax.sunnyweather.SunnyWeatherApplication
import com.homurax.sunnyweather.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceService {

    // 声明了一个@GET注解，这样当调用 searchPlaces()方法的时候
    // Retrofit就会自动发起一条GET请求，去访问@GET注解中配置的地址
    @GET("v2/place?token=${SunnyWeatherApplication.TOKEN}&lang=zh_CN")
    // 搜索城市数据的API中只有query这个参数是需要动态指定的，我们使用@Query注解的方式来进行实现，
    // 另外两个参数是不会变的，因此固定写在@GET注解中即可。
    fun searchPlaces(@Query("query") query: String): Call<PlaceResponse>

}