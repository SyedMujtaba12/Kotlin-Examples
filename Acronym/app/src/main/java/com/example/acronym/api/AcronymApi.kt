package com.example.acronym.api

import com.example.acronym.models.AcronymData
import com.example.acronym.models.AcronymDataItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AcronymApi {
    @GET("software/acromine/dictionary.py")
    suspend fun getAcronym(@Query("sf")str:String):Response<AcronymData>
}