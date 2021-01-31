package com.example.Want2Seat

/*
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.kakao.auth.Session
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.LogoutResponseCallback
import com.kakao.usermgmt.callback.UnLinkResponseCallback
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity_before : AppCompatActivity() {

    private var callback: SessionCallback = SessionCallback()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_before)

        Session.getCurrentSession().addCallback(callback)

        Logout.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("로그아웃 하시겠습니까?")
            builder.setPositiveButton("확인") { dialogInterface, i ->
                UserManagement.getInstance().requestLogout(object : LogoutResponseCallback() {
                    override fun onCompleteLogout() {

                    }
                })
                dialogInterface.dismiss()
            }
            builder.setNegativeButton("취소") { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

        // 앱연결 해제
        Link_out.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("앱 연결을 해제하시겠습니까?")
            builder.setPositiveButton("확인"){ dialogInterface, i ->
                UserManagement.getInstance().requestUnlink(object : UnLinkResponseCallback() {
                    override fun onFailure(errorResult: ErrorResult?) {
                        Log.i("Log", errorResult!!.toString())
                    }

                    override fun onSessionClosed(errorResult: ErrorResult) {
                    }

                    override fun onNotSignedUp() {
                    }

                    override fun onSuccess(userId: Long?) {
                        Log.i("Log", userId.toString())
                    }
                })
                dialogInterface.dismiss()
            }
            builder.setNegativeButton("취소") { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

    }

    @SuppressLint("MissingSuperCall")
    override fun onDestroy() {
        Session.getCurrentSession().removeCallback(callback)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            Log.i("Log", "session get current session")
            println("dkseho tlqkf....")
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    /*
    fun onSessionOpened() {
        requestMe()
    }

    // 로그인에 실패한 상태
    fun onSessionOpenFailed(exception: KakaoException) {
        Log.e("SessionCallback :: ", "onSessionOpenFailed : " + exception.message)
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
*/

}

*/





