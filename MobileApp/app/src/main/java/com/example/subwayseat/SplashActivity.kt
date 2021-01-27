package com.example.subwayseat


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView


//class SplashActivity : AppCompatActivity() {
//startActivity<LoginActivity>()

class SplashActivity : Activity() {
    private var fadeIn : Animation? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //서서히보이는 페이드인 애니메이션
        fadeIn = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in);

        try {
            Handler().postDelayed({
                //method
                var nextIntent = Intent(this, LoginActivity::class.java)
                startActivity(nextIntent)
                finish()
            }, 4000)

        }catch (e : Exception){
            e.printStackTrace()
        }
    }

    private fun fadeIn(view : TextView, duration: Int){
        Handler().postDelayed({
            view.visibility = View.VISIBLE
            view.startAnimation(fadeIn)
        }, duration.toLong())
    }

}