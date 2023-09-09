package com.example.eletriccar.data

import com.example.eletriccar.domain.Car
import retrofit2.Call
import retrofit2.http.GET

interface CarAPI {

    @GET("cars.json")
    fun getAllCars() : Call<List<Car>>

}