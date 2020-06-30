package com.col740.myhospital

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import android.os.SystemClock

class BookingConfirm: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
     //   println("___________________________")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bookingconfirm)
        SystemClock.sleep(2000)
        val patientid = intent.getStringExtra("patientid")
        val docid = intent.getStringExtra("docid")
        val slotid = intent.getStringExtra("slotid")
        val bookings = intent.getIntExtra("bookings", 0)
        val problem = intent.getStringExtra("problem")
        val database = FirebaseDatabase.getInstance("https://my-hospital-fce56.firebaseio.com/")
        val patient =
            database.getReferenceFromUrl("https://my-hospital-fce56.firebaseio.com/Patient/$patientid")
        val setappointment =
            database.getReferenceFromUrl("https://my-hospital-fce56.firebaseio.com/Doctorbookings/$docid/$slotid")
        setappointment.child("bookedby").child((bookings + 1).toString())
            .child("patientid").setValue(patientid)
        setappointment.child("bookedby").child((bookings + 1).toString())
            .child("problem").setValue(problem)
        setappointment.child("bookedby").child("elements").setValue(
            bookings + 1
        )
        patient.child("Appointment").setValue(docid)
    }
}