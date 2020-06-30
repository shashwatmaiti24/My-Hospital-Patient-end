package com.col740.myhospital

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.itemview.*
import java.io.File

class ItemActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.itemview)
        val spinner: Spinner = this.findViewById(R.id.spinner)
        val id: String = intent.getStringExtra("id")
        val database = FirebaseDatabase.getInstance("https://my-hospital-fce56.firebaseio.com/")
        val doctor =
            database.getReferenceFromUrl("https://my-hospital-fce56.firebaseio.com/Doctor/$id")
        var slot: Int? = null
        var mImageRef = FirebaseStorage.getInstance().getReference("images/"+id)
        val file: File = File.createTempFile(id,"jpg")
        mImageRef.getFile(file)
            .addOnSuccessListener(object: OnSuccessListener<FileDownloadTask.TaskSnapshot> {
                override fun onSuccess(tasksnapshot: FileDownloadTask.TaskSnapshot) {
                    val bm = BitmapFactory.decodeFile(file.absolutePath)
                    imageView2.setImageBitmap(bm)
                }
            })
        doctor.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(data: DataSnapshot) {
                name.text = data.child("Name").value.toString()
                ratingBar.rating = data.child("Rating").value.toString().toFloat()
                specialization.text = data.child("Specialisation").value.toString()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        val doctorbooking =
                    database.getReferenceFromUrl("https://my-hospital-fce56.firebaseio.com/Doctorbookings/$id")
        val slotarray:MutableList<String> = ArrayList()
        slotarray.add("Please select a slot")
        doctorbooking.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (d in dataSnapshot.children) {
                    if(d.key!="elements") {
                        if (d.child("maxbooking").value.toString().toInt() - d.child("bookedby").child(
                                "elements"
                            ).value.toString().toInt() > 0
                        ) {
                            slotarray.add(
                                d.child("timing").value.toString()
                            )
                        }
                    }
                }
                val adapter = ArrayAdapter(
                    this@ItemActivity,
                    android.R.layout.simple_spinner_item, slotarray
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

            book.setOnClickListener {
                val intent = Intent(this, Booking::class.java)
                if (slot != 0) {
                    intent.putExtra("doc", id)
                    intent.putExtra("slot", slot!!)
                    ContextCompat.startActivity(this, intent, null)
                }else{
                    Toast.makeText(this, "Please select a slot", Toast.LENGTH_SHORT).show()
                }
             }


  spinner.onItemSelectedListener = object:
        AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                slot = position
            }
        }
    }
}

