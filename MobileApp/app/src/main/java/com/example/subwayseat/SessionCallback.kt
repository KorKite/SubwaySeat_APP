package com.example.subwayseat

import android.util.Log
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


            override fun onSuccess(result: MeV2Response?) {
                println("tlqkf")
                Log.e("SessionCallback :: ", "onSuccess")
                Log.i("Log", "아이디 : ${result!!.id}")
                checkNotNull(result) { "session response null" }
            }

        })
    }


}