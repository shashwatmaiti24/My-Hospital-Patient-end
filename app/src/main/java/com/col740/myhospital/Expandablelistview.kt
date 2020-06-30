package com.col740.myhospital

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.expandablelistview.expandableListView
import kotlinx.android.synthetic.main.searchbycategory.*

class Expandablelistview : AppCompatActivity() {

    val header: MutableList<String> = ArrayList()
    val body: MutableList<MutableList<String>> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.searchbycategory)
        toolbar3.title = "Search by Category"


        val Doctors: MutableList<String> = ArrayList()
        Doctors.add("Dentist")
        Doctors.add("Orthopaedic")
        Doctors.add("Surgeon")
        Doctors.add("Vet")


        val Labs: MutableList<String> = ArrayList()
     //   Labs.add("Blood Test")
     //   Labs.add("ECG")

        header.add("Doctors")
     //   header.add("Labs")

        body.add(Doctors)
    //    body.add(Labs)

        expandableListView.setAdapter(ExpandableListAdapter(this,expandableListView, header, body))
    }
}