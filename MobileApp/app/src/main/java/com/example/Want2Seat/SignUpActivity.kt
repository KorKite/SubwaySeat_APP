package com.example.Want2Seat

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.subwayseat.userInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_signup.*

class SignUpActivity : AppCompatActivity() {
    private var email_join: EditText? = null
    private var pwd_join: EditText? = null
    private var name_join: EditText? = null
    private var btn: Button? = null
    var firebaseAuth: FirebaseAuth? = null
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val db_userinfo = firebaseDatabase.getReference().child("UserInfo")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        firebaseAuth = FirebaseAuth.getInstance()
        btn_join.setOnClickListener {
            val email = editText_email.text.toString()
            val pwd = editText_passWord.text.toString()
            val name = editText_name.text.toString()
            var key = db_userinfo.push().key
            firebaseAuth!!.createUserWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(this) {
                    if (it.isSuccessful()) {
                        val database = FirebaseDatabase.getInstance()
                        val myRef = database.getReference()
                        db_userinfo.child(key!!).setValue(userInfo(email, name))
                        Toast.makeText(this, "회원가입을 축하합니다!", Toast.LENGTH_SHORT).show()
                        val intent =
                            Intent(this@SignUpActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@SignUpActivity, "등록 에러", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }
    }
}