package com.homurax.sunnyweather.ui.place

import androidx.lifecycle.*
import com.homurax.sunnyweather.logic.Repository
import com.homurax.sunnyweather.logic.model.Place

class PlaceViewModel : ViewModel() {

    // LiveData是Jetpack提供的一种响应式编程组件，它可以包含任何类型的数据，并在数据发生变化的时候通知给观察者
    // MutableLiveData可变的LiveData
    private val searchLiveData = MutableLiveData<String>()

    val placeList = ArrayList<Place>()

    val placeLiveData = Transformations.switchMap(searchLiveData) { query ->
        Repository.searchPlaces(query)
    }

    // 没有直接调用仓库层中的searchPlaces()方法，而是将传入的搜索参数赋值给了一个searchLiveData对象，并使用Transformations的
    // switchMap()方法来观察这个对象，否则仓库层返回的LiveData对象将无法进行观察。
    fun searchPlaces(query: String) {
        searchLiveData.value = query
    }

    // 没有开启线程 不需要借助 LiveData 对象来观察
    fun savePlace(place: Place) = Repository.savePlace(place)

    fun getSavedPlace() = Repository.getSavedPlace()

    fun isPlaceSaved() = Repository.isPlaceSaved()

}