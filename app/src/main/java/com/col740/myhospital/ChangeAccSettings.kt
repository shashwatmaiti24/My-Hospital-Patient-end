package com.col740.myhospital

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.changeaccsettings.*
import kotlinx.android.synthetic.main.changeaccsettings.chooseprofilepicture
import kotlinx.android.synthetic.main.changeaccsettings.dob
import kotlinx.android.synthetic.main.changeaccsettings.mobilenumber
import kotlinx.android.synthetic.main.changeaccsettings.name
import kotlinx.android.synthetic.main.changeaccsettings.password
import kotlinx.android.synthetic.main.changeaccsettings.retypepassword
import kotlinx.android.synthetic.main.changeaccsettings.submit
import java.io.File


class ChangeAccSettings : AppCompatActivity() {
    var filePath: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.changeaccsettings)
        toolbar4.title = "Change Account Settings"
        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser: FirebaseUser? = firebaseAuth.getCurrentUser()
        val database = FirebaseDatabase.getInstance("https://my-hospital-fce56.firebaseio.com/")
        val patientid = firebaseUser!!.email!!.split(".").toTypedArray()[0]
        val patient =
            database.getReferenceFromUrl("https://my-hospital-fce56.firebaseio.com/Patient/$patientid")
        var mImageRef = FirebaseStorage.getInstance().getReference("images/"+patientid)
        val file: File = File.createTempFile(patientid,"jpg")
        mImageRef.getFile(file)
            .addOnSuccessListener(object: OnSuccessListener<FileDownloadTask.TaskSnapshot> {
                override fun onSuccess(tasksnapshot: FileDownloadTask.TaskSnapshot) {
                    val bm = BitmapFactory.decodeFile(file.absolutePath)
                    imageView5.setImageBitmap(bm)
                }
            })
        chooseprofilepicture.setOnClickListener {
            val intent:Intent = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent, "Choose Profile Picture"),1)
        }
        submit.setOnClickListener{
            if(password.text.toString()==retypepassword.text.toString()){
                val alert:AlertDialog.Builder
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
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
                            if(name.text.toString()!=""){
                                patient.child("Name").setValue(name.text.toString())
                            }
                            if(mobilenumber.text.toString()!=""){
                                patient.child("Phone").setValue(mobilenumber.text.toString())
                            }
                            if(dob.text.toString()!=""){
                                patient.child("DOB").setValue(dob.text.toString())
                            }
                            if(password.text.toString()!=""){
                                var newPassword = password.text.toString();
                                firebaseUser.updatePassword(newPassword)
                            }
                        }
                    }
                }
            }else{
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode==1 && resultCode==RESULT_OK) && (data!=null && data.data!=null)){
            filePath = data.data
            val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
            imageView5.setImageBitmap(bitmap)
        }
    }
}