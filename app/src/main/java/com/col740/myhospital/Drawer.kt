package com.col740.myhospital

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.drawer.*
import kotlinx.android.synthetic.main.drawer_header.view.*
import java.io.File


class Drawer:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawer)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        drawerrecycler.layoutManager = layoutManager
        val adapter = DrawerAdapter(this,Supplier_Drawer.draweritems)
        drawerrecycler.adapter = adapter
        val info = intent.getStringArrayExtra("info")
        drawer_header.name.setText(info[0]);
        drawer_header.email.setText(info[1]);
        var mImageRef = FirebaseStorage.getInstance().getReference("images/"+info[1].split(".").toTypedArray()[0])
        val file:File = File.createTempFile("image","jpg")
        mImageRef.getFile(file)
            .addOnSuccessListener(object: OnSuccessListener<FileDownloadTask.TaskSnapshot> {
                override fun onSuccess(tasksnapshot: FileDownloadTask.TaskSnapshot) {
                    val bm = BitmapFactory.decodeFile(file.absolutePath)
                    drawer_header.propic.setImageBitmap(bm)
                }
            })
    }
}