package com.example.acronym.view.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.databinding.adapters.SearchViewBindingAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.acronym.R
import com.example.acronym.databinding.ActivityMainBinding
import com.example.acronym.models.Lf
import com.example.acronym.repository.AcronymRepo
import com.example.acronym.util.Constant
import com.example.acronym.util.Resource
import com.example.acronym.view.adapter.AcronymAdapter
import com.example.acronym.view.viewmodel.AcronymViewModel
import com.example.acronym.view.viewmodel.ViewModelProviderFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    lateinit var acronymViewModel:AcronymViewModel
    lateinit var acronymAdapter: AcronymAdapter
    var job:Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = AcronymRepo()
        val viewModelProviderFactory = ViewModelProviderFactory(application,repository)
        acronymViewModel = ViewModelProvider(this,viewModelProviderFactory).get(AcronymViewModel::class.java)
        setupRecyclerView()

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(!TextUtils.isEmpty(query) && query?.length!! < 2){
                    showErrorMessage("please enter min 2 char value")
                    //Toast.makeText(this@MainActivity,"please enter min 2 char value",Toast.LENGTH_SHORT).show()
                }else{
                    job?.cancel()
                    job = MainScope().launch {
                        delay(Constant.SEARCH_ACRONYM_TIME_DELAY)
                        acronymViewModel.searchAcronym(query.toString())
                    }
                }
                return false;
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                setAdapter(mutableListOf<Lf>())
                return false;
            }
        })


        acronymViewModel.searchAcronym.observe(this, Observer {
            when(it){
                is Resource.Success ->{
                    hideProgressBar()

                    var list:MutableList<Lf> = if(it.data !=null && it.data.size > 0) it.data?.get(0)!!.lfs else{
                        Log.e("com.example.acronym","Size------ ")
                        showErrorMessage("No Record Found")
                        mutableListOf()
                    }

                    Log.e("com.example.acronym","Size------ " + list.size)
                    setAdapter(list)
                }

                is Resource.Error -> {
                    hideProgressBar()
                    it.message?.let { message ->
                        showErrorMessage(message)
                    }
                }

                is Resource.Loading -> {
                    setAdapter(mutableListOf<Lf>())
                    showProgressBar()
                }
            }
        })

    }

    private fun setupRecyclerView() {
        binding.recycleView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }
    private fun setAdapter(list: MutableList<Lf>){
        acronymAdapter = AcronymAdapter(list)
        binding.recycleView.apply {
            adapter = acronymAdapter
        }
    }
    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showErrorMessage(msg:String){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
    }

}