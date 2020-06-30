package com.col740.myhospital

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.searchview.*
import kotlinx.android.synthetic.main.toolbar.*

data class SearchItem(var title:String, var id:String)
class SearchActivity:AppCompatActivity() {
    object Supplier_Search{
        var searchitems: MutableList<SearchItem> = ArrayList()
    }
    private var id: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.searchview)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        searchrecycler.layoutManager = layoutManager
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val query = intent.getStringExtra("query")
        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser: FirebaseUser? = firebaseAuth.getCurrentUser()
        id = firebaseUser!!.email!!.split(".").toTypedArray()[0]
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        val speciality:String? = intent.getStringExtra("speciality")

        toolbar_drawer.setOnClickListener ({ startDrawer() })
        val database =
            FirebaseDatabase.getInstance("https://my-hospital-fce56.firebaseio.com/")
        val doctors =
            database.getReferenceFromUrl("https://my-hospital-fce56.firebaseio.com/Doctor")
        doctors.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(!Supplier_Search.searchitems.isEmpty()){
                    Supplier_Search.searchitems = ArrayList()}
                for (d in dataSnapshot.children) {
                    if(speciality==null) {
                        if (d.child("Name").value.toString().contains(query, ignoreCase = true)) {
                            Supplier_Search.searchitems.add(
                                SearchItem(
                                    d.child("Name").getValue(String::class.java).toString(),
                                    d.getKey()!!
                                )
                            )
                        }
                    }else{
                        if (d.child("Specialisation").value.toString() == speciality) {
                            Supplier_Search.searchitems.add(
                                SearchItem(
                                    d.child("Name").getValue(String::class.java).toString(),
                                    d.getKey()!!
                                )
                            )
                        }
                    }
                }
                val adapter = SearchAdapter(this@SearchActivity,Supplier_Search.searchitems)
                searchrecycler.adapter = adapter
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = getMenuInflater()
        inflater.inflate(R.menu.moreoptions, menu)
        // Retrieve the SearchView and plug it into SearchManager
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
                val intent = Intent(this@SearchActivity, SearchActivity::class.java)
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
        //            println(data.child(info[i]).value.toString())
        //            println("__________________________")
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