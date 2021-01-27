package com.example.subwayseat

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {
    private var join: Button? = null
    private var login: Button? = null
    private var email_login: EditText? = null
    private var pwd_login: EditText? = null
    var firebaseAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        join = findViewById<View>(R.id.btn_signup) as Button
        login = findViewById<View>(R.id.btn_login) as Button
        email_login = findViewById<View>(R.id.edittext_email) as EditText
        pwd_login = findViewById<View>(R.id.edittext_password) as EditText
        firebaseAuth = FirebaseAuth.getInstance() //firebaseAuth의 인스턴스를 가져옴


        login!!.setOnClickListener {
            val email = email_login!!.text.toString().trim { it <= ' ' }
            val pwd = pwd_login!!.text.toString().trim { it <= ' ' }
            //String형 변수 email.pwd(edittext에서 받오는 값)으로 로그인하는것
            firebaseAuth!!.signInWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(this@LoginActivity) { task ->
                    if (task.isSuccessful) { //성공했을때

                        val intent = Intent(this@LoginActivity, inputDestination::class.java)
                        startActivity(intent)
                    } else { //실패했을때
                        Toast.makeText(this@LoginActivity, "로그인 오류", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        join!!.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}