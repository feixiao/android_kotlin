package com.homurax.sunnyweather.ui.place

import androidx.lifecycle.*
import com.homurax.sunnyweather.logic.Repository
import com.homurax.sunnyweather.logic.model.Place

class PlaceViewModel : ViewModel() {

    // LiveData是Jetpack提供的一种响应式编程组件，它可以包含任何类型的数据，并在数据发生变化的时候通知给观察者
    // MutableLiveData可变的LiveData
    private val searchLiveData = MutableLiveData<String>()

    val placeList = ArrayList<Place>()

    // switchMap使用场景非常固定：
    // 如果ViewModel中的某个LiveData对象是调用另外的方法获取的，那么我们就可以借助switchMap()方法，
    // 将这个LiveData对象转换成另外一个可观察的LiveData对象。
//    switchMap()方法同样接收两个参数：
//    第一个参数传入我们新增的searchLiveData，switchMap()方法会对它进行观察；
//    第二个参数是一个转换函数，注意，我们必须在这个转换函数中返回一个LiveData对象，因为switchMap()方法的工作原理就是要将转换函数中返回
//    的LiveData对象转换成另一个可观察的LiveData对。
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