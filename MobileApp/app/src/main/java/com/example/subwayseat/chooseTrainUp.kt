package com.example.subwayseat

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_choose_train_up.*
val STATION_LOCATION_X = hashMapOf("당고개" to 110f, "상계" to 115f, "노원" to 120f, "창동" to 125f, "쌍문" to 128f, "수유" to 132f, "미아" to 138f, "미아사거리" to 143f, "길음" to 128f, "성신여대입구" to 132f, "한성대입구" to 138f)
val STATION_LOCATION_Y = hashMapOf("당고개" to 250f, "상계" to 250f, "노원" to 250f, "창동" to 250f, "쌍문" to 250f, "수유" to 250f, "미아" to 250f, "미아사거리" to 250f, "길음" to 250f, "성신여대입구" to 250f, "한성대입구" to 250f)

class chooseTrainUp : AppCompatActivity() {
    var handler = Handler()
    var runnable = Runnable{}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_train_up)
//        btn_move(btn_train1, 56f, 625f, 2500L)
//        btn_train1.layout
        val STATION = listOf<Button>(btn_train1, btn_train2, btn_train3, btn_train4, btn_train5, btn_train6, btn_train7, btn_train8, btn_train9, btn_train10, btn_train11, btn_train12, btn_train13, btn_train14, btn_train15, btn_train16, btn_train17, btn_train18, btn_train19, btn_train20, btn_train21)

        //현재 상행 중에 운행중인 열차 확인
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference()
        val db_station_info = myRef.child("SubwayLocation").child("4호선").child("상행")
        db_station_info.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                println("database error!")
            }

            override fun onDataChange(p0: DataSnapshot) {
                var i = 0
                println("총 운행중인 열차는 "+p0.childrenCount)
                for (snapshot in p0.children) {
                    val stt = snapshot.child("현재역").value.toString()
                    println(stt.toString())
                    if (stt in STATION_LOCATION_X.keys) {
                        btn_move(
                            STATION[i],
                            STATION_LOCATION_X[stt]!!,
                            STATION_LOCATION_Y[stt]!!,
                            2500L
                        )
                        STATION[i].setBackgroundResource(R.drawable.train)
                        println("열차 " + snapshot.child("현재역").value+STATION_LOCATION_X[stt].toString())
                        i = i + 1
                    }
                }
            }
        })
        println(STATION_LOCATION_X)
    }

    fun btn_move(btn: Button, posix: Float, posiy:Float, duration1:Long){
        runnable = object:Runnable{
            override fun run(){
                ObjectAnimator.ofFloat(btn, "translationX", posix).apply{
                    duration = duration1
                    start()
                }
                ObjectAnimator.ofFloat(btn, "translationY", posiy).apply{
                    duration = duration1
                    start()
                }
                handler.postDelayed(runnable, duration1)
            }
        }
        handler.post(runnable)
    }
}