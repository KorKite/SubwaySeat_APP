package com.example.subwayseat

import android.R
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kakao.auth.ErrorCode
import com.kakao.auth.ISessionCallback
import com.kakao.auth.Session
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeResponseCallback
import com.kakao.usermgmt.response.model.UserProfile
import com.kakao.util.exception.KakaoException
import com.kakao.util.helper.log.Logger
import java.security.MessageDigest
import java.util.*


class LoginActivity : AppCompatActivity() {
    private var callback //콜백 선언
            : SessionCallback? = null

    //유저프로필
    var token: String? = "" //카카오톡 토큰
    var name: String? = "" //카카오톡상 이름
    var email = "" //이메일(아이디와 조합해서 임시로만들어줌)
    var password = "dalkomdalkom@" //비번

    //파이어베이스 디비
    var db = FirebaseFirestore.getInstance()

    // Initialize Firebase Auth
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //카카오 로그인 콜백받기
        callback = SessionCallback()
        Session.getCurrentSession().addCallback(callback)
        //키값 알아내기(알아냈으면 등록하고 지워도 상관없다)
        appKeyHash

        //자기 카카오톡 프로필 정보 및 디비정보 쉐어드에 저장해놨던거 불러오기
        loadShared()
    }// TODO Auto-generated catch block

    //카카오 디벨로퍼에서 사용할 키값을 로그를 통해 알아낼 수 있다. (로그로 본 키 값을을 카카오 디벨로퍼에 등록해주면 된다.)
    private val appKeyHash: Unit
        private get() {
            try {
                val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
                for (signature in info.signatures) {
                    var md: MessageDigest
                    md = MessageDigest.getInstance("SHA")
                    md.update(signature.toByteArray())
                    val something = String(Base64.encode(md.digest(), 0))
                    Log.e("Hash key", something)
                }
            } catch (e: Exception) {
                // TODO Auto-generated catch block
                Log.e("name not found", e.toString())
            }
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        Session.getCurrentSession().removeCallback(callback)
    }

    private inner class SessionCallback : ISessionCallback {
        override fun onSessionOpened() {
            requestMe()
            //redirectSignupActivity();  // 세션 연결성공 시 redirectSignupActivity() 호출
        }

        override fun onSessionOpenFailed(exception: KakaoException) {
            if (exception != null) {
                Logger.e(exception)
            }
            setContentView(R.layout.activity_login) // 세션 연결이 실패했을때
        } // 로그인화면을 다시 불러옴
    }

    protected fun requestMe() { //유저의 정보를 받아오는 함수
        UserManagement.requestMe(object : MeResponseCallback() {
            override fun onFailure(errorResult: ErrorResult) {
                val message = "failed to get user info. msg=$errorResult"
                Logger.d(message)
                val result = ErrorCode.valueOf(errorResult.errorCode)
                if (result == ErrorCode.CLIENT_ERROR_CODE) {
                    finish()
                } else {
                    Log.d(TAG, "1")
                    redirectLoginActivity()
                }
            }

            override fun onSessionClosed(errorResult: ErrorResult) {
                redirectLoginActivity()
            }

            override fun onNotSignedUp() {} // 카카오톡 회원이 아닐 시 showSignup(); 호출해야함
            override fun onSuccess(userProfile: UserProfile) {  //성공 시 userProfile 형태로 반환
                Logger.d("UserProfile : $userProfile")
                Log.d(TAG, "유저가입성공")
                // Create a new user with a first and last name
                // 문자열과 토큰과 조합해서 임시로만들어 파이어베이스 이메일인증로그인에 사용
                email = "dalkom" + userProfile.uuid + "@dalkom.com"

                // 유저 카카오톡 아이디 디비에 넣음(첫가입인 경우에만 디비에저장)
                val user: MutableMap<String, String> = HashMap()
                token = userProfile.id.toString() + ""
                user["token"] = token + ""
                user["name"] = userProfile.nickname + userProfile.id
                user["isCreateAuth"] = "0" //0이면 구글AUTH 생성안한상태 1 이면 한상태
                db.collection("users")
                    .document(userProfile.id.toString() + "")
                    .set(user)
                    .addOnSuccessListener {
                        Log.d(TAG, "유저정보 디비삽입 성공")
                        saveShared(
                            userProfile.id.toString() + userProfile.uuid + "",
                            userProfile.nickname
                        )
                        checkIsFirstLogin() // 로그인 성공시 MainActivity로
                    }
            }
        })
    }

    //디비에 해당 유저가 이미가입되어있는지확인
    private fun checkIsFirstLogin() {
        Log.d(TAG, "checkIsFirstLogin 진입")
        db.collection("users").document(token!!).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document.exists()) { //가입되있으면 바로 로그인
                    Log.d(
                        TAG,
                        "DocumentSnapshot data: " + document.data
                    )
                    val isCreateAuth = document.getString("isCreateAuth")
                    if (isCreateAuth == "0") {
                        db.collection("users").document(token!!).update("isCreateAuth", 1)
                        createGooleEmailAuth()
                    } else {
                        loginWithGooleEmail()
                    }
                } else { //가입안되어있으면 회원가입 이메일인증하게함
                    Log.d(TAG, "No such document")
                    Toast.makeText(this@LoginActivity, "로그엔 오류", Toast.LENGTH_SHORT).show()
                }
            } else {
                Log.d(TAG, "get failed with ", task.exception)
            }
        }
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    private fun createGooleEmailAuth() {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this,
                OnCompleteListener<Any?> { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        //FirebaseUser user = mAuth.getCurrentUser();
                        loginWithGooleEmail()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            this@LoginActivity, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
    }

    private fun loginWithGooleEmail() {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this,
                OnCompleteListener<Any?> { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        redirectLoginActivity()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            this@LoginActivity, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    // ...
                })
    }

    protected fun redirectLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        startActivity(intent)
        finish()
    }

    /*쉐어드에 입력값 저장*/
    private fun saveShared(id: String, name: String) {
        val pref = getSharedPreferences("profile", MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString("token", id)
        editor.putString("name", name)
        editor.apply()
    }

    /*쉐어드값 불러오기*/
    private fun loadShared() {
        val pref = getSharedPreferences("profile", MODE_PRIVATE)
        token = pref.getString("token", "")
        name = pref.getString("name", "")
    }

    override fun onStop() {
        super.onStop()
    }

    companion object {
        const val TAG = "LoginActivityT"
    }
}

