package com.col740.myhospital

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item.view.*

class HomeAdapter(val context: Context, val homeitems : List<HomeItem>): RecyclerView.Adapter<HomeAdapter.MyViewHolder>() {
    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var currentitem : HomeItem? = null
        var currentposition : Int = 0

        init {
            itemView.setOnClickListener{
 //               Toast.makeText(context, currentitem!!.medicine +" was Clicked", Toast.LENGTH_SHORT).show()
            }
        }
        fun setData(item: HomeItem?, pos: Int){
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
        return homeitems.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val hobby = homeitems[position]
        holder.setData(hobby, position)
    }
}