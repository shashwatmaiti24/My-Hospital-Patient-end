package com.col740.myhospital

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.booking.*


class Booking: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.booking)
        val firebaseAuth:FirebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser: FirebaseUser? = firebaseAuth.getCurrentUser()
        val email = firebaseUser?.email
        val patientid = email!!.split(".").toTypedArray().get(0)
        val docid = intent.getStringExtra("doc")
        val slot:Int = intent.getIntExtra("slot", -1)
        val slotid = "slot"+(slot).toString()
        toolbar.setTitle("Book Appointment")
        book.setOnClickListener {
            val intent = Intent(this, ConfirmBooking::class.java)
            if (problem.text.toString()!="") {
                val database = FirebaseDatabase.getInstance("https://my-hospital-fce56.firebaseio.com/")
                val patient =
                    database.getReferenceFromUrl("https://my-hospital-fce56.firebaseio.com/Patient/$patientid")
                patient.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(data: DataSnapshot) {
                        val appointment = data.child("Appointment").value
                        println(appointment)
                        if(appointment==null) {
                            intent.putExtra("patientid", patientid)
                            intent.putExtra("docid", docid)
                            intent.putExtra("slotid", slotid)
                            intent.putExtra("problem", problem.text.toString())
                            startActivity(intent, null)
                            finish()
                        }else{
                            Toast.makeText(this@Booking, "Please complete your previous Booking by going into Your Booking", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onCancelled(databaseError: DatabaseError) {}
                })
            }else{
                Toast.makeText(this, "Please describe your Problem", Toast.LENGTH_SHORT).show()
            }
        }
    }
}