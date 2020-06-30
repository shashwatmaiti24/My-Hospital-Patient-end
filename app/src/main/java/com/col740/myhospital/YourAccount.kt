package com.col740.myhospital

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.expandablelistview.expandableListView
import kotlinx.android.synthetic.main.searchbycategory.*

class YourAccount : AppCompatActivity() {

    val header: MutableList<String> = ArrayList()
    val body: MutableList<MutableList<String>> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.searchbycategory)
        toolbar3.title = "Your Account"

        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser: FirebaseUser? = firebaseAuth.getCurrentUser()
        val patientid = firebaseUser!!.email!!.split(".").toTypedArray()[0]
        var Prescriptions: MutableList<String> = ArrayList()
        val database =
            FirebaseDatabase.getInstance("https://my-hospital-fce56.firebaseio.com/")
        val prescriptions =
            database.getReferenceFromUrl("https://my-hospital-fce56.firebaseio.com/Prescription/$patientid")
        prescriptions.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(!Prescriptions.isEmpty()){
                    Prescriptions = ArrayList()}
                val e = dataSnapshot.child("elements").value.toString().toInt()
                for (i in 0 until e) {
                    Prescriptions.add("Prescription "+ (i+1))
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })


        val AccountSettings: MutableList<String> = ArrayList()
        AccountSettings.add("Change User Details")
        AccountSettings.add("Delete Account")

        header.add("Prescriptions")
        header.add("Account Settings")

        body.add(Prescriptions)
        body.add(AccountSettings)

        expandableListView.setAdapter(ExpandableListAdapter(this,expandableListView, header, body))
    }
}