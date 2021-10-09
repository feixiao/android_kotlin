package com.homurax.sunnyweather.ui.weather

import androidx.lifecycle.*
import com.homurax.sunnyweather.logic.Repository
import com.homurax.sunnyweather.logic.model.Location

class WeatherViewModel : ViewModel() {

    private val locationLiveData = MutableLiveData<Location>()

    var locationLng = ""

    var locationLat = ""

    var placeName = ""

    // Transformations的switchMap()方法来观察这个对象，并在switchMap()方法的转换函数中调用仓库层中定义的refreshWeather()方法。
    // 这样仓库层返回的LiveData对象就可以转换成一个可供Activity观察的LiveData对象了。
    val weatherLiveData = Transformations.switchMap(locationLiveData) { location ->
        Repository.refreshWeather(location.lng, location.lat, placeName)
    }

    fun refreshWeather(lng: String, lat: String) {

        // 这里定义了一个refreshWeather()方法来刷新天气信息，并将传入的经纬度参数封装成一个Location对象后赋值给locationLiveData对象，
        // 然后使用Transformations的switchMap()方法来观察这个对象，并在switchMap()方法的转换函数中调用仓库层中定义的refreshWeather()方法。
        // 这样仓库层返回的LiveData对象就可以转换成一个可供Activity观察的LiveData对象了。
        locationLiveData.value = Location(lng, lat)
    }

}