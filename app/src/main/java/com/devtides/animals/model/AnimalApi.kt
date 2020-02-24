package com.devtides.animals.model

import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface AnimalApi {


    @GET(value="getKey")
    fun getApiKey(): Single<ApiKey>

    @FormUrlEncoded
    @POST(value="getAnimals")
    fun getAnimals(@Field(value="key") key : String): Single<List<Animal>>

}