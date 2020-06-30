package com.col740.myhospital

import android.app.SearchManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*

data class HomeItem(var medicine:String, var dosage:String)
class MainActivity : AppCompatActivity() {
    object Supplier_Home{
        var homeitems: MutableList<HomeItem> = ArrayList()
    }
    private var id: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        homerecycler.layoutManager = layoutManager
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser: FirebaseUser? = firebaseAuth.getCurrentUser()
        id = firebaseUser!!.email!!.split(".").toTypedArray()[0]
        toolbar_drawer.setOnClickListener ({ startDrawer() })
        val database =
            FirebaseDatabase.getInstance("https://my-hospital-fce56.firebaseio.com/")
        val prescriptions =
            database.getReferenceFromUrl("https://my-hospital-fce56.firebaseio.com/Prescription")
        prescriptions.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(!Supplier_Home.homeitems.isEmpty()){
                    Supplier_Home.homeitems = ArrayList()}
                val e = dataSnapshot.child(id!!).child("elements").value.toString().toInt()
                for (d in dataSnapshot.child(id!!).child(e.toString()).children) {
                    println(d.key)
                    if(d.key!="Doctor") {
                        Supplier_Home.homeitems.add(
                            HomeItem(
                                d.key!!,
                                d.value.toString()
                            )
                        )
                    }
                }
                val adapter = HomeAdapter(this@MainActivity,Supplier_Home.homeitems)
                homerecycler.adapter = adapter
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = getMenuInflater()
        inflater.inflate(R.menu.moreoptions, menu)
        val searchView : SearchView =
            MenuItemCompat.getActionView(menu!!.findItem(R.id.search_button)) as SearchView;
        val searchManager : SearchManager = getSystemService(SEARCH_SERVICE) as SearchManager;
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText=="ok"){
                    return false
                }
                return true
            }
            override fun onQueryTextSubmit(query: String?): Boolean {
                val intent = Intent(this@MainActivity, SearchActivity::class.java)
                intent.putExtra("query", query)
                startActivity(intent)
                return true
            }
        })
        return true
    }


    private fun startDrawer() {
        val database = FirebaseDatabase.getInstance("https://my-hospital-fce56.firebaseio.com/")
        val intent = Intent(this, Drawer::class.java)
        val patient =
            database.getReferenceFromUrl("https://my-hospital-fce56.firebaseio.com/Patient/$id")
        patient.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(data: DataSnapshot) {
                val info =
                    arrayOf(
                        "Name",
                        "E-mail",
                        "Problem",
                        "Phone",
                        "Appointment"
                    )
                for (i in info.indices) {
                    println(data.child(info[i]).value.toString())
                    println("__________________________")
                    info[i] =
                        data.child(info[i]).value.toString()
                }
                intent.putExtra("info", info)
                startActivity(intent)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}