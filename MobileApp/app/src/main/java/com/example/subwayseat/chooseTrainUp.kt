package com.example.subwayseat

import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_choose_train_up.*

var CURRENT_TRAIN_NO = -1
var btn_stt_hashmap : MutableMap<Button, Int> = mutableMapOf()

class chooseTrainUp : AppCompatActivity() {
    var handler = Handler()
    var runnable = Runnable{}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_train_up)
//        btn_move(btn_train1, 56f, 625f, 2500L)
//        btn_train1.layout
        val STATION = listOf<Button>(btn_train1, btn_train2, btn_train3, btn_train4, btn_train5, btn_train6, btn_train7, btn_train8, btn_train9, btn_train10, btn_train11, btn_train12, btn_train13, btn_train14, btn_train15, btn_train16, btn_train17, btn_train18, btn_train19, btn_train20, btn_train21, btn_train22, btn_train23, btn_train24, btn_train25, btn_train26, btn_train27, btn_train28, btn_train29, btn_train30)
        var image:Int = -1
        if (line_updown=="상행"){
            image = R.drawable.train_up
        }
        else{
            image = R.drawable.train_down
        }
        //현재 상행 중에 운행중인 열차 확인

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference()
        val db_station_info = myRef.child("SubwayLocation").child(line_no).child(line_updown)
        db_station_info.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                println("database error!")
            }

            override fun onDataChange(p0: DataSnapshot) {
                var i = 0
                println("총 운행중인 열차는 "+p0.childrenCount)
//                println(STATION_LOCATION_X.keys)
                btn_stt_hashmap = mutableMapOf()

                for (snapshot in p0.children) {
                    val stt = snapshot.child("현재역").value.toString()
                    val train_no:Int = snapshot.key.toString().toInt() as Int
//                    println(stt.toString())
                    //현재 가지고 있는 역 정보(4호선)에 해당 역이 존재하면
                    if (STATION_LOCATION_X.keys.contains(stt)) {
                        btn_stt_hashmap.put(STATION[i], train_no)

                        val objectAnimator_x = ObjectAnimator.ofFloat(STATION[i], "translationX",  STATION_LOCATION_X[stt]!!)
                        objectAnimator_x.duration = 50
                        objectAnimator_x.start()

                        val objectAnimator_y = ObjectAnimator.ofFloat(STATION[i], "translationY",  STATION_LOCATION_Y[stt]!!)
                        objectAnimator_y.duration = 50
                        objectAnimator_y.start()

//                        STATION[i].text = stt

                        STATION[i].visibility = View.VISIBLE
                        STATION[i].setBackgroundResource(image)
//                        println("열차 " + snapshot.child("현재역").value+STATION_LOCATION_X[stt].toString())
                        i = i + 1
                    }
                }
            }
        })



        //button에 리스너 달기
        btn_train1.setOnClickListener{selectTrain(btn_stt_hashmap[btn_train1]!!)}
        btn_train2.setOnClickListener{selectTrain(btn_stt_hashmap[btn_train2]!!)}
        btn_train3.setOnClickListener{selectTrain(btn_stt_hashmap[btn_train3]!!)}
        btn_train4.setOnClickListener{selectTrain(btn_stt_hashmap[btn_train4]!!)}
        btn_train5.setOnClickListener{selectTrain(btn_stt_hashmap[btn_train5]!!)}
        btn_train6.setOnClickListener{selectTrain(btn_stt_hashmap[btn_train6]!!)}
        btn_train7.setOnClickListener{selectTrain(btn_stt_hashmap[btn_train7]!!)}
        btn_train8.setOnClickListener{selectTrain(btn_stt_hashmap[btn_train8]!!)}
        btn_train9.setOnClickListener{selectTrain(btn_stt_hashmap[btn_train9]!!)}
        btn_train10.setOnClickListener{selectTrain(btn_stt_hashmap[btn_train10]!!)}
        btn_train11.setOnClickListener{selectTrain(btn_stt_hashmap[btn_train11]!!)}
        btn_train12.setOnClickListener{selectTrain(btn_stt_hashmap[btn_train12]!!)}
        btn_train13.setOnClickListener{selectTrain(btn_stt_hashmap[btn_train13]!!)}
        btn_train14.setOnClickListener{selectTrain(btn_stt_hashmap[btn_train14]!!)}
        btn_train15.setOnClickListener{selectTrain(btn_stt_hashmap[btn_train15]!!)}
        btn_train16.setOnClickListener{selectTrain(btn_stt_hashmap[btn_train16]!!)}
        btn_train17.setOnClickListener{selectTrain(btn_stt_hashmap[btn_train17]!!)}
        btn_train18.setOnClickListener{selectTrain(btn_stt_hashmap[btn_train18]!!)}
        btn_train19.setOnClickListener{selectTrain(btn_stt_hashmap[btn_train19]!!)}
        btn_train20.setOnClickListener{selectTrain(btn_stt_hashmap[btn_train20]!!)}
        btn_train21.setOnClickListener{selectTrain(btn_stt_hashmap[btn_train21]!!)}
        btn_train22.setOnClickListener{selectTrain(btn_stt_hashmap[btn_train22]!!)}
        btn_train23.setOnClickListener{selectTrain(btn_stt_hashmap[btn_train23]!!)}
        btn_train24.setOnClickListener{selectTrain(btn_stt_hashmap[btn_train24]!!)}
        btn_train25.setOnClickListener{selectTrain(btn_stt_hashmap[btn_train25]!!)}
        btn_train26.setOnClickListener{selectTrain(btn_stt_hashmap[btn_train26]!!)}
        btn_train27.setOnClickListener{selectTrain(btn_stt_hashmap[btn_train27]!!)}
        btn_train28.setOnClickListener{selectTrain(btn_stt_hashmap[btn_train28]!!)}
        btn_train29.setOnClickListener{selectTrain(btn_stt_hashmap[btn_train29]!!)}
        btn_train30.setOnClickListener{selectTrain(btn_stt_hashmap[btn_train30]!!)}



        //현재 열차 앞에 위치 선택 스피너 관련(ex. 4-1, 8-2)
        //어답터 설정 - 안드로이드에서 제공하는 어답터를 연결
        var enter_location1 = arrayOf(1,2,3,4,5,6,7,8)
        var enter_location2 = arrayOf(1,2,3,4)
        spn_location1.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, enter_location1)

        //아이템 선택 리스너
        spn_location1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                println("nothing selected!")
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                tv_location1.text = enter_location1[position].toString()

            }

        }

        spn_location2.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, enter_location2)

        //아이템 선택 리스너
        spn_location2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                println("nothing selected!")
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                tv_location2.text = enter_location2[position].toString()

            }

        }

        btn_location.setOnClickListener{
            val location1 = tv_location1.text.toString().toInt()
            val location2 = tv_location2.text.toString().toInt()
            var nextIntent = Intent(this, chooseSeat2::class.java)

            if (location2==1){
                nextIntent = Intent(this, chooseSeat1::class.java)
            }
            else if(location2==4){
                nextIntent = Intent(this, chooseSeat3::class.java)
            }

            val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.train_info_popup_layout, null)
            if (CURRENT_TRAIN_NO==-1){
                //팝업창
                val alertDialog = AlertDialog.Builder(this)
                    .setPositiveButton("확인", null)
                    .create()

                alertDialog.setView(view)
                alertDialog.show()
            }
            else{
                var location_info: ArrayList<Int>  = ArrayList()
                location_info.add(0, CURRENT_TRAIN_NO)
                location_info.add(1, location1)
                location_info.add(2, location2)
                nextIntent.putExtra("intentKey", location_info)
                startActivity(nextIntent)
            }
            }

    }


    fun selectTrain(train_no:Int){
        CURRENT_TRAIN_NO = train_no
        tv_train_select.text = "탑승할 열차는 "+train_no.toString()+"번 열차입니다"

    }

    //뒤로 가기 버튼 -> 열차 출발역 도착역 선택 화면
    override fun onBackPressed() {
        startActivity(Intent(this, inputDestination::class.java))
        finish()
    }

}

