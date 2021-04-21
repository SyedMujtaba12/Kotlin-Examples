package com.example.acronym.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.acronym.databinding.AcronymListBinding
import com.example.acronym.models.AcronymData
import com.example.acronym.models.AcronymDataItem
import com.example.acronym.models.Lf

class AcronymAdapter(val acronymList:MutableList<Lf>):RecyclerView.Adapter<AcronymAdapter.AcronymViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AcronymViewHolder {
        return AcronymViewHolder(AcronymListBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: AcronymViewHolder, position: Int) {
        var item: Lf? = acronymList[position]
        holder.binding?.apply {
            tvAcronym.text = item?.lf
        }
    }

    override fun getItemCount(): Int {
        return acronymList?.size
    }

     inner class AcronymViewHolder(mBinding: AcronymListBinding):RecyclerView.ViewHolder(mBinding.root){
         var binding:AcronymListBinding? =  null
         init {
             binding = mBinding
         }
     }

}