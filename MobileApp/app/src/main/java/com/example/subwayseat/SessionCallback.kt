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
        requestMe()
/*
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

        })*/
    }

    // 사용자 정보 요청
    fun requestMe() {
        // 사용자정보 요청 결과에 대한 Callback
        UserManagement.getInstance().me(object : MeV2ResponseCallback() {
            // 세션 오픈 실패. 세션이 삭제된 경우,
            override fun onSessionClosed(errorResult: ErrorResult) {
                Log.e("SessionCallback :: ", "onSessionClosed : " + errorResult.errorMessage)
            }

            // 회원이 아닌 경우,
            //override fun onNotSignedUp() {
            //    Log.e("SessionCallback :: ", "onNotSignedUp")
            //}

            // 사용자 정보 요청 실패
            override fun onFailure(errorResult: ErrorResult) {
                Log.e("SessionCallback :: ", "onFailure : " + errorResult.errorMessage)
            }

            override fun onSuccess(result: MeV2Response?) {
                println("tlqkfwlsWk......")
                Log.e("SessionCallback :: ", "onSuccess")
                val id = result?.id
                Log.e("Profile : ", id.toString() + "")
            }
        })
    }


}