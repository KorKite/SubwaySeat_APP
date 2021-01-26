package com.example.subwayseat

import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import com.kakao.auth.ISessionCallback
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeV2ResponseCallback
import com.kakao.usermgmt.response.MeV2Response
import com.kakao.util.exception.KakaoException


class SessionCallback : ISessionCallback {

    override fun onSessionOpenFailed(exception: KakaoException?) {
        Log.e("Log", "Session Call back :: onSessionOpenFailed: ${exception?.message}")
    }

    override fun onSessionOpened() {
        UserManagement.getInstance().me(object : MeV2ResponseCallback() {

            override fun onFailure(errorResult: ErrorResult?) {
                Log.i("Log", "Session Call back :: on failed ${errorResult?.errorMessage}")
            }

            override fun onSessionClosed(errorResult: ErrorResult?) {
                Log.i("Log", "Session Call back :: onSessionClosed ${errorResult?.errorMessage}")
            }

/*            override fun onSuccess(result: MeV2Response?) {
                var nextIntent = Intent(this, inputDestination::class.java)
                nextIntent.putExtra("id", result?.id)
                checkNotNull(result) { "session response null" }
                startActivity(nextIntent)
            }*/

            override fun onSuccess(result: MeV2Response) {
//                val nextIntent = Intent(LoginActivity::class.java, inputDestination::class.java)
//                nextIntent.putExtra("id", result.id)
//                checkNotNull(result) { "session response null" }
//                startActivity(nextIntent)

            }


/*            override fun onSuccess(result: MeV2Response?) {

                var nextIntent = Intent(this, inputDestination::class.java)
                nextIntent.putExtra("id", result?.id)

                Log.e("SessionCallback :: ", "onSuccess")
                println("dk tlqkfwlsWk...")
                val id = result?.id
                Log.i("Log", "아이디 : ${result!!.id}")
                println(id)
                Log.i("Log", "이메일 : ${result.kakaoAccount.email}")
                Log.i("Log", "프로필 이미지 : ${result.profileImagePath}")
                checkNotNull(result) { "session response null" }
                startActivity(nextIntent)
            }*/


            /*override fun onSuccess(result: MeV2Response) {
                val intent = Intent(getApplicationContext(), Main2Activity::class.java)
                intent.putExtra("name", result.nickname)
                intent.putExtra("profile", result.profileImagePath)
               
               
                startActivity(intent)
                finish()
            }*/

        })
    }


}