package com.col740.myhospital

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_draweritem.view.*
import kotlinx.android.synthetic.main.list_item.view.*

class PresAdapter(val context: Context, val presitems : List<PresItem>): RecyclerView.Adapter<PresAdapter.MyViewHolder>() {
    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var currentitem : PresItem? = null
        var currentposition : Int = 0

        fun setData(item: PresItem?, pos: Int){
            itemView.medicine.text = item!!.medicine
            itemView.dosage.text = item!!.dosage
            currentitem = item
            currentposition = pos
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return presitems.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val hobby = presitems[position]
        holder.setData(hobby, position)
    }
}