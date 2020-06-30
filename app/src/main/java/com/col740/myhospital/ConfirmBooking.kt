package com.col740.myhospital

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.confirmbooking.*

class ConfirmBooking: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.confirmbooking)
        toolbar2.title = "Confirm Booking"
        val patientid = intent.getStringExtra("patientid")
        val docid = intent.getStringExtra("docid")
        val slotid = intent.getStringExtra("slotid")
        println(slotid)
        val problem = intent.getStringExtra("problem")
        val database = FirebaseDatabase.getInstance("https://my-hospital-fce56.firebaseio.com/")
        val patient =
            database.getReferenceFromUrl("https://my-hospital-fce56.firebaseio.com/Patient/$patientid")
        val doctor =
        database.getReferenceFromUrl("https://my-hospital-fce56.firebaseio.com/Doctor/$docid")
        doctor.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(data: DataSnapshot) {
                doctorname.text = data.child("Name").value.toString()
                speciality.text = data.child("Specialisation").value.toString()
     //           println("____________________")
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
        val setappointment =
            database.getReferenceFromUrl("https://my-hospital-fce56.firebaseio.com/Doctorbookings/$docid/$slotid")
        var bookings = 0
        setappointment.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(data: DataSnapshot) {
                selectedslot.text = data.child("timing").value.toString()
                bookings = data.child("bookedby").child("elements").value.toString().toInt()
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
        patproblem.text = problem
        confirm.setOnClickListener {
            val intent = Intent(this@ConfirmBooking, BookingConfirm::class.java)
            intent.putExtra("bookings", bookings)
            intent.putExtra("docid", docid)
            intent.putExtra("patientid", patientid)
            intent.putExtra("slotid", slotid)
            intent.putExtra("problem", problem)
            startActivity(intent, null)
            finish()
        }
    }
}