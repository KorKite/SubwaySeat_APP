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
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_choose_train_up.*

var CURRENT_TRAIN_NO = -1
var btn_stt_hashmap : MutableMap<Button, Int> = mutableMapOf()
var LOCATION: HashMap<Int, String> = hashMapOf()
var SELECTED_TRAIN = -1
var SELECTED_TRAIN_LOCATION_INDEX = -1
public lateinit var PREVIOUS_SELECTED : Button
public lateinit var public_db_station_info_listener:ValueEventListener
val database = FirebaseDatabase.getInstance()
val myRef = database.getReference()
public val public_db_station_info = myRef.child("SubwayLocation").child(line_no).child(line_updown)

class chooseTrainUp : AppCompatActivity() {
    var trainLocation:HashMap<Int, Int> = hashMapOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_train_up)
//        btn_move(btn_train1, 56f, 625f, 2500L)
//        btn_train1.layout
        val STATION = listOf<Button>(btn_train1, btn_train2, btn_train3, btn_train4, btn_train5, btn_train6, btn_train7, btn_train8, btn_train9, btn_train10, btn_train11, btn_train12, btn_train13, btn_train14, btn_train15, btn_train16, btn_train17, btn_train18, btn_train19, btn_train20, btn_train21, btn_train22, btn_train23, btn_train24, btn_train25, btn_train26, btn_train27, btn_train28, btn_train29, btn_train30, btn_train31, btn_train32, btn_train33, btn_train34, btn_train35, btn_train36, btn_train37, btn_train38, btn_train39, btn_train40)
        var image:Int = R.drawable.train_down

        //현재 출발역 표시 화살표 위치 지정
        img_arrow.visibility = View.VISIBLE
        if (line_updown=="상행") {
            if ((STATION_INDEX[input_stt]!! >= 0 && STATION_INDEX[input_stt]!! < 11) || (STATION_INDEX[input_stt]!! >= 23 && STATION_INDEX[input_stt]!! < 36)) {
                img_arrow.setBackgroundResource(R.drawable.arrow_left)
            } else {
                img_arrow.setBackgroundResource(R.drawable.arrow_right)
            }
        }
        else {
            if ((STATION_INDEX[input_stt]!! >= 0 && STATION_INDEX[input_stt]!! < 11) || (STATION_INDEX[input_stt]!! >= 23 && STATION_INDEX[input_stt]!! < 36)) {
                img_arrow.setBackgroundResource(R.drawable.arrow_right)
            } else {
                img_arrow.setBackgroundResource(R.drawable.arrow_left)
            }
        }
        val objectAnimator_x = ObjectAnimator.ofFloat(img_arrow, "translationX",  STATION_LOCATION_X[input_stt]!!)
        objectAnimator_x.duration = 50
        objectAnimator_x.start()

        val objectAnimator_y = ObjectAnimator.ofFloat(img_arrow, "translationY",  STATION_LOCATION_Y[input_stt]!!+35)
        objectAnimator_y.duration = 50
        objectAnimator_y.start()




        //현재 상행 중에 운행중인 열차 확인
        public_db_station_info_listener = public_db_station_info.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                println("database error!")
            }

            override fun onDataChange(p0: DataSnapshot) {
                var i = 0
                trainLocation = hashMapOf()
                println("총 운행중인 열차는 "+p0.childrenCount)
                println(STATION_LOCATION_X.keys)
                btn_stt_hashmap = mutableMapOf()

                for (snapshot in p0.children) {
                    val stt = snapshot.child("현재역").value.toString()
                    val train_no:Int = snapshot.key.toString().toInt() as Int
                    trainLocation[train_no] = STATION_INDEX[stt]!!
                    LOCATION[train_no] = stt + " " + snapshot.child("열차출발여부").value.toString()

                    if (STATION_LOCATION_X.keys.contains(stt)) {
                        btn_stt_hashmap.put(STATION[i], train_no)

                        // 열차 button 표시(x,y 좌표 이동)
                        val objectAnimator_x = ObjectAnimator.ofFloat(STATION[i], "translationX",  STATION_LOCATION_X[stt]!!)
                        objectAnimator_x.duration = 50
                        objectAnimator_x.start()

                        val objectAnimator_y = ObjectAnimator.ofFloat(STATION[i], "translationY",  STATION_LOCATION_Y[stt]!!)
                        objectAnimator_y.duration = 50
                        objectAnimator_y.start()


                        STATION[i].visibility = View.VISIBLE
                        println(stt+" "+train_no)
                        if (train_no != SELECTED_TRAIN){
                            STATION[i].setBackgroundResource(image)
                        }
                        else{
                            STATION[i].setBackgroundResource(R.drawable.train_select)
                        }

//                        println("열차 " + snapshot.child("현재역").value+STATION_LOCATION_X[stt].toString())
                        i = i + 1
                    }
                }
            }
        })



        //button에 리스너 달기
        btn_train1.setOnClickListener{selectTrain(btn_train1, btn_stt_hashmap[btn_train1]!!)}
        btn_train2.setOnClickListener{selectTrain(btn_train2, btn_stt_hashmap[btn_train2]!!)}
        btn_train3.setOnClickListener{selectTrain(btn_train3, btn_stt_hashmap[btn_train3]!!)}
        btn_train4.setOnClickListener{selectTrain(btn_train4, btn_stt_hashmap[btn_train4]!!)}
        btn_train5.setOnClickListener{selectTrain(btn_train5, btn_stt_hashmap[btn_train5]!!)}
        btn_train6.setOnClickListener{selectTrain(btn_train6, btn_stt_hashmap[btn_train6]!!)}
        btn_train7.setOnClickListener{selectTrain(btn_train7, btn_stt_hashmap[btn_train7]!!)}
        btn_train8.setOnClickListener{selectTrain(btn_train8, btn_stt_hashmap[btn_train8]!!)}
        btn_train9.setOnClickListener{selectTrain(btn_train9, btn_stt_hashmap[btn_train9]!!)}
        btn_train10.setOnClickListener{selectTrain(btn_train10, btn_stt_hashmap[btn_train10]!!)}
        btn_train11.setOnClickListener{selectTrain(btn_train11, btn_stt_hashmap[btn_train11]!!)}
        btn_train12.setOnClickListener{selectTrain(btn_train12, btn_stt_hashmap[btn_train12]!!)}
        btn_train13.setOnClickListener{selectTrain(btn_train13, btn_stt_hashmap[btn_train13]!!)}
        btn_train14.setOnClickListener{selectTrain(btn_train14, btn_stt_hashmap[btn_train14]!!)}
        btn_train15.setOnClickListener{selectTrain(btn_train15, btn_stt_hashmap[btn_train15]!!)}
        btn_train16.setOnClickListener{selectTrain(btn_train16, btn_stt_hashmap[btn_train16]!!)}
        btn_train17.setOnClickListener{selectTrain(btn_train17, btn_stt_hashmap[btn_train17]!!)}
        btn_train18.setOnClickListener{selectTrain(btn_train18, btn_stt_hashmap[btn_train18]!!)}
        btn_train19.setOnClickListener{selectTrain(btn_train19, btn_stt_hashmap[btn_train19]!!)}
        btn_train20.setOnClickListener{selectTrain(btn_train20, btn_stt_hashmap[btn_train20]!!)}
        btn_train21.setOnClickListener{selectTrain(btn_train21, btn_stt_hashmap[btn_train21]!!)}
        btn_train22.setOnClickListener{selectTrain(btn_train22, btn_stt_hashmap[btn_train22]!!)}
        btn_train23.setOnClickListener{selectTrain(btn_train23, btn_stt_hashmap[btn_train23]!!)}
        btn_train24.setOnClickListener{selectTrain(btn_train24, btn_stt_hashmap[btn_train24]!!)}
        btn_train25.setOnClickListener{selectTrain(btn_train25, btn_stt_hashmap[btn_train25]!!)}
        btn_train26.setOnClickListener{selectTrain(btn_train26, btn_stt_hashmap[btn_train26]!!)}
        btn_train27.setOnClickListener{selectTrain(btn_train27, btn_stt_hashmap[btn_train27]!!)}
        btn_train28.setOnClickListener{selectTrain(btn_train28, btn_stt_hashmap[btn_train28]!!)}
        btn_train29.setOnClickListener{selectTrain(btn_train29, btn_stt_hashmap[btn_train29]!!)}
        btn_train30.setOnClickListener{selectTrain(btn_train30, btn_stt_hashmap[btn_train30]!!)}
        btn_train31.setOnClickListener{selectTrain(btn_train31, btn_stt_hashmap[btn_train31]!!)}
        btn_train32.setOnClickListener{selectTrain(btn_train32, btn_stt_hashmap[btn_train32]!!)}
        btn_train33.setOnClickListener{selectTrain(btn_train33, btn_stt_hashmap[btn_train33]!!)}
        btn_train34.setOnClickListener{selectTrain(btn_train34, btn_stt_hashmap[btn_train34]!!)}
        btn_train35.setOnClickListener{selectTrain(btn_train35, btn_stt_hashmap[btn_train35]!!)}
        btn_train36.setOnClickListener{selectTrain(btn_train36, btn_stt_hashmap[btn_train36]!!)}
        btn_train37.setOnClickListener{selectTrain(btn_train37, btn_stt_hashmap[btn_train37]!!)}
        btn_train38.setOnClickListener{selectTrain(btn_train38, btn_stt_hashmap[btn_train38]!!)}
        btn_train39.setOnClickListener{selectTrain(btn_train39, btn_stt_hashmap[btn_train39]!!)}
        btn_train40.setOnClickListener{selectTrain(btn_train40, btn_stt_hashmap[btn_train40]!!)}


        //현재 열차 앞에 위치 선택 스피너 관련(ex. 4-1, 10-2)
        //어답터 설정 - 안드로이드에서 제공하는 어답터를 연결
        var enter_location1 = arrayOf(1,2,3,4,5,6,7,8,9,10)
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
            val tv_popup_info = view.findViewById<TextView>(R.id.tv_popup_info)
            if (CURRENT_TRAIN_NO==-1){
                //팝업창
                val alertDialog = AlertDialog.Builder(this)
                    .setPositiveButton("확인", null)
                    .create()

                alertDialog.setView(view)
                alertDialog.show()
            }
            else if (line_updown == "상행" && SELECTED_TRAIN_LOCATION_INDEX < STATION_INDEX[input_dst]!!){
                tv_popup_info.setText("선택하신 열차의 위치가 도착역을 지났습니다.\n 열차를 다시 선택하세요.")
                //팝업창
                val alertDialog = AlertDialog.Builder(this)
                    .setPositiveButton("확인", null)
                    .create()

                alertDialog.setView(view)
                alertDialog.show()
            }
            else if (line_updown == "하행" && SELECTED_TRAIN_LOCATION_INDEX > STATION_INDEX[input_dst]!!){
                tv_popup_info.setText("선택하신 열차의 위치가 도착역을 지났습니다.\n 열차를 다시 선택하세요.")
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
                public_db_station_info.removeEventListener(public_db_station_info_listener)
                startActivity(nextIntent)
            }
            }

    }


    fun selectTrain(btn_train:Button, train_no:Int){
        CURRENT_TRAIN_NO = train_no
        tv_train_select.text = "탑승할 열차는 "+train_no.toString()+"번 열차입니다"
        if (SELECTED_TRAIN != -1){
            PREVIOUS_SELECTED.setBackgroundResource(R.drawable.train_down)
        }
        SELECTED_TRAIN = train_no
        PREVIOUS_SELECTED = btn_train
        SELECTED_TRAIN_LOCATION_INDEX = trainLocation[SELECTED_TRAIN]!!
        btn_train.setBackgroundResource(R.drawable.train_select)
    }

    //뒤로 가기 버튼 -> 열차 출발역 도착역 선택 화면
    override fun onBackPressed() {
        startActivity(Intent(this, inputDestination::class.java))
        finish()
    }

}

