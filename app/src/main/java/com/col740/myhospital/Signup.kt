package com.col740.myhospital

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.signup.*

class Signup: AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    var filePath: Uri? = null
    private var storage:FirebaseStorage? = null
    private var storageReference:StorageReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup)
        toolbar4.title = "Sign Up"
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.getReference()
        val database = FirebaseDatabase.getInstance("https://my-hospital-fce56.firebaseio.com/")
        val patients = database.getReferenceFromUrl("https://my-hospital-fce56.firebaseio.com/Patient")
        val prescriptions = database.getReferenceFromUrl("https://my-hospital-fce56.firebaseio.com/Prescription")
        chooseprofilepicture.setOnClickListener {
            val intent:Intent = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent, "Choose Profile Picture"),1)
        }

        loginbutton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            ContextCompat.startActivity(this, intent, null)
        }

        submit.setOnClickListener {
            if (name.text.toString()!=""){
                val emailid = email.text.toString()
                val emailsplit = emailid.split(".").toTypedArray()
                if ((email.text.toString()!="") and (emailsplit[0].split("@").toTypedArray().size==2) and (emailsplit.size==2)) {
                    if(mobilenumber.text.toString()!="") {
                        if(dob.text.toString()!="") {
                            if (password.text.toString()!="") {
                                if(retypepassword.text.toString()==password.text.toString()) {
                                    mAuth!!.createUserWithEmailAndPassword(emailid, password.text.toString()).addOnCompleteListener(this@Signup,
                                            OnCompleteListener<AuthResult?> { task ->
                                        if (task.isSuccessful) { // Sign in success, update UI with the signed-in user's information
                                            //Log.d(TAG, "createUserWithEmail:success");
                                            patients.child(emailsplit[0]).child("Name")
                                                .setValue(name.text.toString())
                                            patients.child(emailsplit[0]).child("E-mail")
                                                .setValue(emailid)
                                            patients.child(emailsplit[0]).child("Phone")
                                                .setValue(mobilenumber.text.toString())
                                            patients.child(emailsplit[0]).child("DOB")
                                                .setValue(dob.text.toString())
                                            prescriptions.child(emailsplit[0]).child("elements").setValue(0)
                                            patients.addListenerForSingleValueEvent(object :
                                                ValueEventListener {
                                                override fun onDataChange(data: DataSnapshot) {
                                                    val e = data.child("elements").value.toString().toInt()
                                                    patients.child("elements").setValue(e+1)
                                                }
                                                override fun onCancelled(databaseError: DatabaseError) {}
                                            })
                                            prescriptions.addListenerForSingleValueEvent(object :
                                                ValueEventListener {
                                                override fun onDataChange(data: DataSnapshot) {
                                                    val e = data.child("elements").value.toString().toInt()
                                                    prescriptions.child("elements").setValue(e+1)
                                                    val intent = Intent(this@Signup, MainActivity::class.java)
                                                    intent.putExtra("id", emailsplit[0])
                                                    ContextCompat.startActivity(this@Signup, intent, null)
                                                }
                                                override fun onCancelled(databaseError: DatabaseError) {}
                                            })
                                            if(filePath!=null){
                                                val progressdialog:ProgressDialog = ProgressDialog(this)
                                                progressdialog.setTitle("Uploading")
                                                progressdialog.show()
                                                val reference:StorageReference = storageReference!!.child("images/"+ emailsplit[0])
                                                reference.putFile(filePath!!)
                                                    .addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot>(){
                                                        progressdialog.dismiss()
                                                        Toast.makeText(this, "Image Uploaded", Toast.LENGTH_SHORT).show()
                                                    })


                                            }
                                        } else {
                                            Toast.makeText(this@Signup, task.exception.toString(), Toast.LENGTH_SHORT).show()
                                        }
                                    })
                                }else{
                                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                                }
                            }else{
                                Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show()
                            }
                        }else{
                            Toast.makeText(this, "Please enter Date of Birth", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText(this, "Please enter a Mobile Number", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this, "Please enter valid Email", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Toast.makeText(this, "Press Back Button Again to Exit", Toast.LENGTH_LONG).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode==1 && resultCode==RESULT_OK) && (data!=null && data.data!=null)){
            filePath = data.data
            val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
            propic.setImageBitmap(bitmap)
        }
    }
}