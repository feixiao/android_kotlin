package com.homurax.sunnyweather.logic.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// 创建一个Retrofit构建

object ServiceCreator {

    private const val BASE_URL = "https://api.caiyunapp.com/"

//  需要先使用Retrofit.Builder构建出一个
//  Retrofit对象，然后再调用Retrofit对象的create()方法创建动态代理对象
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    inline fun <reified T> create(): T = create(T::class.java)

}
