package com.col740.myhospital

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.yourbooking.*

class YourBooking : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser: FirebaseUser? = firebaseAuth.getCurrentUser()
        val patientid = firebaseUser!!.email!!.split(".").toTypedArray()[0]
        super.onCreate(savedInstanceState)
        setContentView(R.layout.yourbooking)
        toolbar5.title = "Your Booking"
        var userrating:Int? = null
        val database = FirebaseDatabase.getInstance("https://my-hospital-fce56.firebaseio.com/")
        val patient =
            database.getReferenceFromUrl("https://my-hospital-fce56.firebaseio.com/Patient/$patientid")
        patient.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) { // This method is called once with the initial value and again
                if(dataSnapshot.child("Appointment").value==null){
                    Toast.makeText(this@YourBooking, "No Booking Found", Toast.LENGTH_SHORT).show()
                    finish()
                }
                val docid = dataSnapshot.child("Appointment").value.toString()
                val doctor =
                    database.getReferenceFromUrl("https://my-hospital-fce56.firebaseio.com/Doctor/$docid")
                doctor.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) { // This method is called once with the initial value and again
                        drname.text = "Dr. "+dataSnapshot.child("Name").value.toString()
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        rateappointment.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            userrating = rating.toInt()
        }
        completebooking.setOnClickListener {
            if(userrating!=null) {
                patient.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) { // This method is called once with the initial value and again
                        val docid = dataSnapshot.child("Appointment").value.toString()
                        val doctor =
                            database.getReferenceFromUrl("https://my-hospital-fce56.firebaseio.com/Doctor/$docid")
                        doctor.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) { // This method is called once with the initial value and again
                                val prevbooking = dataSnapshot.child("Booking").value.toString().toInt()
                                val prevrating = dataSnapshot.child("Rating").value.toString().toFloat()
                                val newrating:Float = (prevrating*prevbooking+ userrating!!)/(prevbooking+1)
                                doctor.child("Rating").setValue(newrating)
                                doctor.child("Booking").setValue(prevbooking+1)
                                patient.child("Appointment").setValue(null)
                                finish()
                            }

                            override fun onCancelled(databaseError: DatabaseError) {}
                        })
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })
            }else {
                patient.child("Appointment").setValue(null)
                finish()
            }
        }
    }
}