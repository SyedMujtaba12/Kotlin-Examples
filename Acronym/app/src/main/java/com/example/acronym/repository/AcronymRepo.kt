package com.example.acronym.repository

import com.example.acronym.api.RetrofitInstance

class AcronymRepo {
    suspend fun searchAcronym(queryString:String) = RetrofitInstance.api.getAcronym(queryString)
}