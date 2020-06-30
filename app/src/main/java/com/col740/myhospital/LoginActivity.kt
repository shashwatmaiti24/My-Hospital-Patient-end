package com.col740.myhospital

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    private var emailText: EditText? = null
    private var passwordText: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()
        emailText = findViewById<View>(R.id.editEmail) as EditText
        passwordText = findViewById<View>(R.id.editPass) as EditText


        val buttonLogin = findViewById<View>(R.id.buttonLoginIn) as Button

        logintoolbar.setTitle("Login")
        buttonLogin.setOnClickListener({ startSignIn() })
        signup.setOnClickListener {
            val intent = Intent(this, Signup::class.java)
            startActivity(intent)
        }
    }

    private fun startSignIn() {
        val email = emailText!!.text.toString()
        val password = passwordText!!.text.toString()
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this@LoginActivity, "Fields Empty", Toast.LENGTH_LONG).show()
        } else {
            mAuth!!.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Toast.makeText(this@LoginActivity, "Sign In Problem", Toast.LENGTH_LONG).show()
                } else {
                    val id = email.split(".").toTypedArray()[0]
                    println("ID $id")
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.putExtra("id", id)
                            startActivity(intent)
                }
            }
        }
    }
}
