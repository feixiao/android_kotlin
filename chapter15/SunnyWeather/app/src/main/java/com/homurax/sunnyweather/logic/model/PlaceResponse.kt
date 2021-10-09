package com.homurax.sunnyweather.logic.model

import com.google.gson.annotations.SerializedName

data class Location(val lng: String, val lat: String)

// @SerializedName JSON字段和变量建立联系
data class Place(val name: String, val location: Location,
            @SerializedName("formatted_address") val address: String)

data class PlaceResponse(val status: String, val places: List<Place>)