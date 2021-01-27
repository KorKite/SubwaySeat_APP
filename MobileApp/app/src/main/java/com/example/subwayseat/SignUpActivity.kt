package com.example.subwayseat

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {
    private var email_join: EditText? = null
    private var pwd_join: EditText? = null
    private var name_join: EditText? = null
    private var btn: Button? = null
    var firebaseAuth: FirebaseAuth? = null
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference = firebaseDatabase.reference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        email_join = findViewById<View>(R.id.editText_email) as EditText
        pwd_join = findViewById<View>(R.id.editText_passWord) as EditText
        name_join = findViewById<View>(R.id.editText_name) as EditText
        btn = findViewById<View>(R.id.btn_join) as Button
        firebaseAuth = FirebaseAuth.getInstance()
        btn!!.setOnClickListener {
            val email = email_join!!.text.toString().trim { it <= ' ' }
            val pwd = pwd_join!!.text.toString().trim { it <= ' ' }
            val name = name_join!!.text.toString().trim { it <= ' ' }
            //공백인 부분을 제거하고 보여주는 trim();
            firebaseAuth!!.createUserWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(
                    this@SignUpActivity,
                    object : OnCompleteListener<AuthResult?> {
                        override fun onComplete(task: Task<AuthResult?>) {
                            println(email)
                            if (task.isSuccessful()) {
                                val database = FirebaseDatabase.getInstance()
                                val myRef = database.getReference()
                                myRef.child("User").child(email).setValue(name)
                                val intent =
                                    Intent(this@SignUpActivity, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this@SignUpActivity, "등록 에러", Toast.LENGTH_SHORT)
                                    .show()
                                return
                            }
                        }
                    })
        }
    }
}