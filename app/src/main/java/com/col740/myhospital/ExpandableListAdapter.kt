package com.col740.myhospital

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity

class ExpandableListAdapter(var context: Context,var expandableListView : ExpandableListView,var header : MutableList<String>, var body : MutableList<MutableList<String>>) : BaseExpandableListAdapter(){
    override fun getGroup(groupPosition: Int): String {
        return header[groupPosition]
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View? {
        var convertView = convertView
        if(convertView == null){
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.layout_group,null)
        }
        val title = convertView?.findViewById<TextView>(R.id.tv_title)
        title?.text = getGroup(groupPosition)
        title?.setOnClickListener {
            if(expandableListView.isGroupExpanded(groupPosition))
                expandableListView.collapseGroup(groupPosition)
            else
                expandableListView.expandGroup(groupPosition)
        }
        return convertView
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return body[groupPosition].size
    }

    override fun getChild(groupPosition: Int, childPosition: Int): String {
        return body[groupPosition][childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View? {
        var convertView = convertView
        if(convertView == null){
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.layout_child,null)
        }
        val title = convertView?.findViewById<TextView>(R.id.tv_title)
        title?.text = getChild(groupPosition,childPosition)
        title?.setOnClickListener {
            if(getChild(groupPosition,childPosition)=="Change User Details"){
                val intent = Intent(context, ChangeAccSettings::class.java)
                startActivity(context,  intent, null)
            }else if(getChild(groupPosition,childPosition).split(" ").toTypedArray()[0]=="Prescription"){
                val intent = Intent(context, Prescription::class.java)
                intent.putExtra("presid",getChild(groupPosition,childPosition).split(" ").toTypedArray()[1])
                startActivity(context,  intent, null)
            }else if(getChild(groupPosition,childPosition)=="Delete Account"){
                val intent = Intent(context, DeleteAccount::class.java)
                startActivity(context,  intent, null)
            }else{
                val intent = Intent(context, SearchActivity::class.java)
                intent.putExtra("speciality",getChild(groupPosition, childPosition))
                startActivity(context, intent, null)
            }
        }
        return convertView
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return header.size
    }

}