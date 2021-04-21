package com.example.acronym.view.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.acronym.repository.AcronymRepo

class ViewModelProviderFactory(
    val app:Application,
    val repo:AcronymRepo
):ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AcronymViewModel(app,repo) as T
    }
}