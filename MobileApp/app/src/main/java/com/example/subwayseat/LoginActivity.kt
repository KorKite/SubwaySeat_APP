package com.example.subwayseat

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
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

var USERNAME = ""




class LoginActivity : AppCompatActivity() {
    private var join: Button? = null
    private var login: Button? = null
    private var email_login: EditText? = null
    private var pwd_login: EditText? = null
    var firebaseAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //자동 로그인 관련 변수들
        var contextKey = "login"
        var sharedPreferences = getSharedPreferences(contextKey, Context.MODE_PRIVATE)
        var editor = sharedPreferences.edit()


        firebaseAuth = FirebaseAuth.getInstance() //firebaseAuth의 인스턴스를 가져옴

        //로그인 정보가 저장되어 있다면
        if (sharedPreferences.getString("email","")?.length!! > 0){
            edittext_email.setText(sharedPreferences.getString("email",""))
            edittext_password.setText(sharedPreferences.getString("pwd",""))
        }

        //로그인 function
        btn_login.setOnClickListener {
            val email = edittext_email.text.toString()
            val pwd = edittext_password.text.toString()
            if (email.length == 0) {
                Toast.makeText(this@LoginActivity, "이메일을 입력하세요!", Toast.LENGTH_SHORT).show()
            } else if (pwd.length == 0) {
                Toast.makeText(this@LoginActivity, "이메일을 입력하세요!", Toast.LENGTH_SHORT).show()
            } else {
                //String형 변수 email.pwd(edittext에서 받오는 값)으로 로그인하는것
                firebaseAuth!!.signInWithEmailAndPassword(email, pwd)
                    .addOnCompleteListener(this@LoginActivity) { task ->
                        if (task.isSuccessful) { //성공했을때
                            //자동로그인 체크 시
                            if (cb_login.isChecked){
                                editor.putString("email", email)
                                editor.putString("pwd", pwd)
                                editor.commit()
                            }

                            val db_userinfo = FirebaseDatabase.getInstance().getReference().child("UserInfo")
                            db_userinfo.addListenerForSingleValueEvent(object : ValueEventListener{
                                override fun onCancelled(p0: DatabaseError) {
                                    println("database error!")
                                }

                                override fun onDataChange(p0: DataSnapshot) {
                                    for (snapshot in p0.children) {
                                        if (snapshot.child("email").value == email){
                                            USERNAME = snapshot.child("name").value.toString()
                                            val intent = Intent(this@LoginActivity, inputDestination::class.java)
                                            startActivity(intent)
//                                            Toast.makeText(this, USERNAME.toString()+"님 환영합니다", Toast.LENGTH_SHORT).show()
                                        }
                                    }

                                }
                            })




                        } else { //실패했을때
                            Toast.makeText(
                                this@LoginActivity,
                                "이메일 또는 비밀번호 오류입니다!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }

        btn_signup.setOnClickListener{
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
        }
    }




}