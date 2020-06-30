package com.col740.myhospital

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.deleteaccount.*

class DeleteAccount : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.deleteaccount)
        toolbar6.title = "Delete Account"
        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser: FirebaseUser? = firebaseAuth.getCurrentUser()
        val database = FirebaseDatabase.getInstance("https://my-hospital-fce56.firebaseio.com/")
        val patientid = firebaseUser!!.email!!.split(".").toTypedArray()[0]
        val patients = database.getReferenceFromUrl("https://my-hospital-fce56.firebaseio.com/Patient")
        val prescriptions = database.getReferenceFromUrl("https://my-hospital-fce56.firebaseio.com/Prescription")
        var mImageRef = FirebaseStorage.getInstance().getReference("images/"+patientid)
        no.setOnClickListener {
            finish()
        }
        yes.setOnClickListener {
            val alert: AlertDialog.Builder
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP) {
                alert =
                    AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert)
            }else{
                alert =
                    AlertDialog.Builder(this)
            }
            val inflater: LayoutInflater = layoutInflater
            val view: View = inflater.inflate(R.layout.popup, null)
            alert.setView(view)
            alert.setCancelable(false)
            val dialog: AlertDialog = alert.create()
            dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
            dialog.show()
            val proceedbtn = view.findViewById<View>(R.id.proceed) as Button
            val oldpassword = view.findViewById<View>(R.id.passwordp) as EditText
            proceedbtn.setOnClickListener {
                val mAuth = FirebaseAuth.getInstance()
                mAuth!!.signInWithEmailAndPassword(patientid+".com", oldpassword.text.toString()).addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Toast.makeText(this, "Wrong Password", Toast.LENGTH_LONG).show()
                    } else {
                        patients.child(patientid).setValue(null)
                        prescriptions.child(patientid).setValue(null)
                        mImageRef.delete()
                        firebaseUser.delete()
                        patients.addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(data: DataSnapshot) {
                                val e = data.child("elements").value.toString().toInt()
                                patients.child("elements").setValue(e-1)
                            }
                            override fun onCancelled(databaseError: DatabaseError) {}
                        })
                        prescriptions.addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(data: DataSnapshot) {
                                val e = data.child("elements").value.toString().toInt()
                                prescriptions.child("elements").setValue(e-1)
                                val intent = Intent(this@DeleteAccount, Signup::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                ContextCompat.startActivity(this@DeleteAccount, intent, null)
                            }
                            override fun onCancelled(databaseError: DatabaseError) {}
                        })
                    }
                }
            }
        }
    }
}