package com.example.acronym.view.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.acronym.models.AcronymData
import com.example.acronym.models.AcronymDataItem
import com.example.acronym.repository.AcronymRepo
import com.example.acronym.util.Resource
import com.example.acronym.util.Utility
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import java.lang.Exception

class AcronymViewModel(
    app:Application,
    val repo:AcronymRepo
):AndroidViewModel(app) {

    val searchAcronym: MutableLiveData<Resource<AcronymData>> = MutableLiveData()

    fun searchAcronym(searchQuery: String) = viewModelScope.launch {
        acronymAPICall(searchQuery)
    }

    private suspend fun acronymAPICall(searchQuery: String) {
        searchAcronym.postValue(Resource.Loading())
        try {
            if(Utility.isNetworkAvailable(getApplication())) {
                val response = repo.searchAcronym(searchQuery)
                searchAcronym.postValue(acronymAPIResponse(response))
            } else {
                searchAcronym.postValue(Resource.Error("No internet connection"))
            }
        } catch(t: Exception) {
            when(t) {
                is IOException -> searchAcronym.postValue(Resource.Error("Network Failure"))
                else -> {
                    Log.e("com.example.acronym","exception----- " +t.localizedMessage);
                    searchAcronym.postValue(Resource.Error(t.localizedMessage))
                }
            }
        }
    }

    private fun acronymAPIResponse(response: Response<AcronymData>): Resource<AcronymData>? {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse: AcronymData ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}