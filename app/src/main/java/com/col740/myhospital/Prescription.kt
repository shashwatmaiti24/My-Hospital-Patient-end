package com.col740.myhospital

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.prescription.*
import kotlinx.android.synthetic.main.toolbar.*

data class PresItem(var medicine:String, var dosage:String)
class Prescription : AppCompatActivity() {
    object Supplier_Pres{
        var presitems: MutableList<PresItem> = ArrayList()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.prescription)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        presrecycler.layoutManager = layoutManager
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        //   val im1: ImageView = toolbar.findViewById(R.id.logo)
        toolbar.title = "Prescription"
        val presid = intent.getStringExtra("presid")
        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser: FirebaseUser? = firebaseAuth.getCurrentUser()
        val patientid = firebaseUser!!.email!!.split(".").toTypedArray()[0]
        val database =
            FirebaseDatabase.getInstance("https://my-hospital-fce56.firebaseio.com/")
        val prescriptions =
            database.getReferenceFromUrl("https://my-hospital-fce56.firebaseio.com/Prescription/$patientid")
        prescriptions.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) { // This method is called once with the initial value and again
// whenever data at this location is updated.
//                s = Array<String>(l.toInt() + 1){""}
//                email = Array<String>(l.toInt() + 1){""}
//                var j = 0
                val e = dataSnapshot.child("elements").value.toString().toInt()
                if(!Supplier_Pres.presitems.isEmpty()){
                    Supplier_Pres.presitems = ArrayList()}
                for (d in dataSnapshot.child((e-presid.toInt()+1).toString()).children) {
//                    println(d.child("Name").getValue(String::class.java).toString())
//                    println(d.key.toString())
//                    println("_*_*_*_*_*_*_*_*_*__*_*__*_*__*_*")
//                    println(d.key)
                    if(d.key!="Doctor") {
                        Supplier_Pres.presitems.add(
                            PresItem(
                                d.key!!,
                                d.value.toString()
                            )
                        )
//                    email[j] = d.key.toString()
                        //                  j++
                    }
                }
                val adapter = PresAdapter(this@Prescription, Supplier_Pres.presitems)
                presrecycler.adapter = adapter
                //                for (int i=1;i<(int)l;i++){
//                    DataSnapshot d=dataSnapshot.child(Integer.toString(i));
//                    s[i]=d.child("Name").getValue(String.class)+" - "+d.child("Specialisation").getValue(String.class)+" - "+d.child("Hospital").getValue(String.class);
//                    email[i]=d.child("E-mail").getValue(String.class);
//                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

    }
}