package com.col740.myhospital

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.itemview.*
import kotlinx.android.synthetic.main.list_searchitem.view.*
import java.io.File

class SearchAdapter(val context: Context, val searchitems : MutableList<SearchItem>) : RecyclerView.Adapter<SearchAdapter.MyViewHolder>()  {
   inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var currentitem : SearchItem? = null
        var currentposition : Int = 0

        init {
            itemView.setOnClickListener{
//                Toast.makeText(context, currentitem!!.title +" was Clicked", Toast.LENGTH_SHORT).show()
                val intent = Intent(context, ItemActivity::class.java)
                val database = FirebaseDatabase.getInstance("https://my-hospital-fce56.firebaseio.com/")
                val id = currentitem!!.id
//                val doctorbooking =
//                    database.getReferenceFromUrl("https://my-hospital-fce56.firebaseio.com/Doctorbookings/$id")
//                val slotarray:MutableList<String> = ArrayList()
//                doctorbooking.addListenerForSingleValueEvent(object : ValueEventListener {
//                    override fun onDataChange(dataSnapshot: DataSnapshot) { // This method is called once with the initial value and again
//// whenever data at this location is updated.
////                s = Array<String>(l.toInt() + 1){""}
////                email = Array<String>(l.toInt() + 1){""}
//
//                        println("-------------------------------------------5")
////                        var j = 0
//                        for (d in dataSnapshot.children) {
////                    println(d.child("Name").getValue(String::class.java).toString())
////                    println(d.key.toString())
////                    println("_*_*_*_*_*_*_*_*_*__*_*__*_*__*_*")
//                            //       if(d.child("Name").value!=null) {
//                            //            slotarray.add(
//                            slotarray.add(
//                                d.child("timing").value.toString()
//                                   )
//                            //           )
//////                    email[j] = d.key.toString()
////                            j++
//                        }
//                        //                for (int i=1;i<(int)l;i++){
////                    DataSnapshot d=dataSnapshot.child(Integer.toString(i));
////                    s[i]=d.child("Name").getValue(String.class)+" - "+d.child("Specialisation").getValue(String.class)+" - "+d.child("Hospital").getValue(String.class);
////                    email[i]=d.child("E-mail").getValue(String.class);
////                }
//                    }
//
//                    override fun onCancelled(databaseError: DatabaseError) {}
//                })
//                var slotarr:Array<String?> = arrayOfNulls<String>(slotarray.size)
//                var j = 0
//                for (k in slotarray){
//                    slotarr[j] = k
//                    println(k)
//                    j++
//                }
//                intent.putExtra("slotarray", slotarr)
                intent.putExtra("id", id)
                ContextCompat.startActivity(context, intent, null)
            }
        }
        fun setData(item: SearchItem?, pos: Int){
            itemView.textView3.text = item!!.title
            currentitem = item
            currentposition = pos
            var mImageRef = FirebaseStorage.getInstance().getReference("images/"+item!!.id)
            val file: File = File.createTempFile(item!!.id,"jpg")
            mImageRef.getFile(file)
                .addOnSuccessListener(object: OnSuccessListener<FileDownloadTask.TaskSnapshot> {
                    override fun onSuccess(tasksnapshot: FileDownloadTask.TaskSnapshot) {
                        val bm = BitmapFactory.decodeFile(file.absolutePath)
                        itemView.docpick.setImageBitmap(bm)
                    }
                })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_searchitem, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return searchitems.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val SearchItem = searchitems[position]
        holder.setData(SearchItem, position)

    }
}