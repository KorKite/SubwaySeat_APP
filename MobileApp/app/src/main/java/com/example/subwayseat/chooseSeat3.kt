package com.example.subwayseat

import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_choose_seat1.*
import kotlinx.android.synthetic.main.activity_choose_seat2.*
import kotlinx.android.synthetic.main.activity_choose_seat3.*
import kotlinx.android.synthetic.main.check_popup_layout.*


class chooseSeat3 : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference()
    var isrunning = true
    var tmp_left = -1

    fun return_intent(){
        val nextIntent = Intent(this, inputDestination::class.java)
        startActivity(nextIntent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_seat3)

        var location_info2: ArrayList<Int>  = ArrayList()
        var block:String = ""

        if (intent.hasExtra("intentKey")) {
            val location_info:ArrayList<Int> = intent.getSerializableExtra("intentKey") as ArrayList<Int>
            val loc1 = location_info.get(1)
            val loc2 = location_info.get(2)
            tv_location8.text = loc1.toString()+ "-4"
            tv_location9.text = loc1.toString()+ "-4"
            block = loc1.toString()
            //다음 인텐트로 보낼 location info
            location_info2.add(0, CURRENT_TRAIN_NO)
            location_info2.add(1, loc1)
            location_info2.add(2, loc2)

        }


        btn_seat35.setOnClickListener{seatPopup(35, block, btn_seat35)}
        btn_seat36.setOnClickListener{seatPopup(36, block, btn_seat36)}
        btn_seat37.setOnClickListener{seatPopup(37, block, btn_seat37)}
        btn_seat38.setOnClickListener{seatPopup(38, block, btn_seat38)}
        btn_seat39.setOnClickListener{seatPopup(39, block, btn_seat39)}
        btn_seat40.setOnClickListener{seatPopup(40, block, btn_seat40)}
        btn_seat41.setOnClickListener{seatPopup(41, block, btn_seat41)}
        btn_seat42.setOnClickListener{seatPopup(42, block, btn_seat42)}
        btn_seat43.setOnClickListener{seatPopup(43, block, btn_seat43)}
        btn_seat44.setOnClickListener{seatPopup(44, block, btn_seat44)}
        btn_seat45.setOnClickListener{seatPopup(45, block, btn_seat45)}
        btn_seat46.setOnClickListener{seatPopup(46, block, btn_seat46)}
        btn_seat47.setOnClickListener{seatPopup(47, block, btn_seat47)}
        btn_seat48.setOnClickListener{seatPopup(48, block, btn_seat48)}
        btn_seat49.setOnClickListener{seatPopup(49, block, btn_seat49)}
        btn_seat50.setOnClickListener{seatPopup(50, block, btn_seat50)}
        btn_seat51.setOnClickListener{seatPopup(51, block, btn_seat51)}
        btn_seat52.setOnClickListener{seatPopup(52, block, btn_seat52)}
        btn_seat53.setOnClickListener{seatPopup(53, block, btn_seat53)}
        btn_seat54.setOnClickListener{seatPopup(54, block, btn_seat54)}



        //firebase에서 정보 받아와서 좌석 color setting
        //TO-DO 현재 기기에서 파이어베이스에 업데이트를 했다면, 내린 후에 firebase에서 정보 삭제하기

        var seatMap:HashMap<Int, String> = hashMapOf()//seat number : destination
        if (block != ""){
            val db_seat_info = myRef.child("SeatStatus")
            db_seat_info.child(line_no).child(line_updown).child(CURRENT_TRAIN_NO.toString()).child("block"+block).addChildEventListener(object :
                ChildEventListener {
                override fun onChildAdded(p0: DataSnapshot, previousChildName: String?) {
                    val seat = p0.key.toString().substring(4).toString().toInt()
                    if (seat>34 && seat<55){
                        for (snapshot in p0.children) {
                            println(snapshot.value)
                            if (snapshot.key == "dst"){
                                val dst:String = snapshot.value.toString()
                                seatMap[seat] = dst
                            }
                        }
                    }
                }

                override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    println("database error!")
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    println("child moved!")
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    println("child changed!")
                    for (snap in snapshot.children) {
                        val seat = snap.key.toString().subSequence(5,-1).toString().toInt()
                        val dst: String = snap.child("dst").value.toString()
                        seatMap[seat] = dst
                    }
                }
            })
        }


        //현재 열차 내의 좌석(firebase에 정보가 있는)과 현재 열차의 위치와 비교해서 calculate time한 후, 좌석 색깔 업데이트
        //다른 스레드 사용 - 15초마다 업데이트 해주는 새로운 스레드 생성
        class ThreadClass:Thread(){
            override fun run(){
                val btnList = listOf<Button>(btn_seat35, btn_seat36, btn_seat37, btn_seat38, btn_seat39, btn_seat40, btn_seat41, btn_seat42, btn_seat43, btn_seat44, btn_seat45, btn_seat46, btn_seat47, btn_seat48, btn_seat49, btn_seat50, btn_seat51, btn_seat52, btn_seat53, btn_seat54)
                val db_seat_info = myRef.child("SubwayLocation").child(line_no).child(line_updown)
                var current_station:Int = -1
                while(isrunning){
                    db_seat_info.addValueEventListener(object: ValueEventListener{
                        override fun onCancelled(databaseError: DatabaseError) {

                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            println("쓰레드 working" + line_updown)
                            for (p0 in snapshot.children){
                                var left:Int
                                println(p0.key)
                                if (p0.key == CURRENT_TRAIN_NO.toString()) {
                                    println("catch it")
                                    current_station = STATION_INDEX[p0.child("현재역").value.toString()]!!
                                    var stn = p0.child("현재역").value.toString()
                                    var stt = p0.child("열차출발여부").value.toString()
//                                    println("좌석"+seatMap)

                                    //현재 열차의 위치가 사용자의 목적지 -1 이라면, 푸시 알림 시작
                                    if (line_updown=="상행" && STATION_INDEX[stn]!! == STATION_INDEX[input_dst]!! +1 && PUSHALARM == 1){
                                        dstPushAlarm()
                                        PUSHALARM = -1
                                    }
                                    if (line_updown=="하행" && STATION_INDEX[stn]!! +1 == STATION_INDEX[input_dst] && PUSHALARM == 1){
                                        dstPushAlarm()
                                        PUSHALARM = -1
                                    }

                                    //2. 현재 열차의 위치에서 모든 hashmap(현재 열차의 정보가 있는 좌석)의 도착지와의 거리를 계산
                                    if (seatMap.size > 0) {
                                        for (seatId in seatMap.keys) {
//                                            println("seatId ::$seatId")
//                                            println("도착역 ::" + seatMap[seatId])
//                                            println("현재 위치 ::" + p0.child("현재역").value.toString())

                                            left = calculateTime(
                                                current_station,
                                                STATION_INDEX[seatMap[seatId]]!! - 1
                                            )

                                            if (left==0){
                                                seatMap.remove(seatId)
                                                return_intent()
                                            }
                                            println("현재 열차 " + p0.child("현재역").value.toString() + "에서 " +seatMap[seatId] + "까지 잔여 시간 :" + left)
                                            runOnUiThread {
                                                //화면 상단에 열차의 현재 위치 업데이트 "00역 진입"
                                                train_current6.text = stn+" "+stt
                                                //3. 남은 시간에 따라 좌석 color update
                                                btnList[seatId-35].setBackgroundResource(minuteColor(left))
                                            }
                                        }}
                                    else{
                                        runOnUiThread {
                                            //화면 상단에 열차의 현재 위치 업데이트 "00역 진입"
                                            train_current6.text = stn + " " + stt
                                        }
                                    }
                                }
                            }
                        }

                    })


                    SystemClock.sleep(15000)
                }
            }
        }

        //스레드
        val thread = ThreadClass()
        thread.start()


        //이전 버튼
        btn_prev3.setOnClickListener {
            val nextIntent = Intent(this, chooseSeat2::class.java)
            nextIntent.putExtra("intentKey", location_info2)
            startActivity(nextIntent)

        }
    }
    //뒤로 가기 버튼 -> 열차 선택하는 화면으로 (chooseTrainUp activity)
    override fun onBackPressed() {
        startActivity(Intent(this, inputDestination::class.java))
        CURRENT_TRAIN_NO = -1
        isrunning = false
        finish()
    }


    // 1. 좌석 버튼 클릭시 나타나는 팝업창
    private fun seatPopup(seatNum2: Int, block: String, btn: Button){
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.train_info_popup_layout, null)
        val tv_popup_info = view.findViewById<TextView>(R.id.tv_popup_info)
        tv_popup_info.text = seatNum2.toString() +"번 좌석을 선택하시겠습니까?"


        //팝업창
        val alertDialog = AlertDialog.Builder(this)
            .setPositiveButton("확인"){dialog, which ->
//                button.setBackgroundResource(R.drawable.seat_red)
                println("좌석 number : "+seatNum2)
                checkPopup(seatNum2.toString(), input_dst, block, btn)
            }
            .setNeutralButton("취소", null)
            .create()

        alertDialog.setView(view)
        alertDialog.show()
    }


    // 2. seatPopup 팝업창 -> 확인 버튼 클릭 시 도착역 등 정보 확인 용 새로운 팝업창 나타남
    private fun checkPopup(seat_num:String, dst:String, block: String, btn: Button){
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.check_popup_layout, null)
        val et_dst = view.findViewById<TextView>(R.id.et_dst)
        val spn_station:Spinner = view.findViewById<Spinner>(R.id.spn_station2)
        val tv_left_info = view.findViewById<TextView>(R.id.tv_left_info)
        var cb_alarm = view.findViewById<CheckBox>(R.id.cb_alarm)
        val tv_seat1 = view.findViewById<TextView>(R.id.tv_seat1)
        val tv_dst_info = view.findViewById<TextView>(R.id.tv_dst_info)
        var final_dst:String = dst
        var exist = 0

        tv_seat1.setText(seat_num)


        //입력받은 하차역으로 팝업창의 하차역 기본 설정
        et_dst.setText("   "+dst)


        var stt_now = -1
        val stt_dst = STATION_INDEX[final_dst]!! -1


        // 도착역까지 남은 시간 계산
        // 1) 현재 열차의 위치 받아오기
        val db_station_info = myRef.child("SubwayLocation").child(line_no).child(line_updown)
        db_station_info.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                println("database error!")
            }

            override fun onDataChange(p0: DataSnapshot) {
//                        println("p0 " + p0.toString())
                for (snapshot in p0.children) {
//                            println(final_dst + "  " + snapshot.key)
//                            println(STATION_INDEX[snapshot.child("현재역").value.toString()])
//                            println(STATION_INDEX[snapshot.child("현재역").toString().toInt()])
                    if (snapshot.key == CURRENT_TRAIN_NO.toString()) {
                        stt_now = STATION_INDEX[snapshot.child("현재역").value.toString()]!!

                        //2) cost_time 계산
                        println(snapshot.child("현재역").value.toString())
//                        println("calculate time index : "+stt_dst.toString()+" "+stt_now.toString())
                        val cost_time = stt_dst?.let { it1 -> calculateTime(stt_now, it1) }
                        tmp_left = cost_time!!
//                        println("남았음 "+cost_time.toString())


                        tv_left_info.text = final_dst + " 도착까지 " + cost_time + "분 남았습니다."
                    }
                }

            }
        })


        //팝업창
        val alertDialog = AlertDialog.Builder(this)
            .setPositiveButton("확인") { dialog, which ->

                // 1) 1개 역 전에 알림 여부 체크박스
                if (cb_alarm.isChecked){
                    PUSHALARM = 1
                }
                else{
                    PUSHALARM = 0
                }

                // To2) firebase에서 좌석 정보 업데이트 - user 정보, 좌석 정보, 도착역
                updateSeatInfo(block, seat_num, 1, final_dst, btn)

            }

            .setNeutralButton("취소", null)
            .create()

        alertDialog.setView(view)
        alertDialog.show()

    }



    // 3. checkPopup 팝업창 -> 확인 버튼 클릭 시 firebase에 좌석 정보 업데이트
    private fun updateSeatInfo(block:String, seatNum:String, status:Int, dst:String, btn: Button) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference()
        val seatRef = myRef.child("SeatStatus").child(line_no).child(line_updown).child(CURRENT_TRAIN_NO.toString()).child("block"+block).child("seat"+seatNum)
        val updateInfo = seatInfo(USEREMAIL, status, dst)
        seatRef.setValue(updateInfo)
        btn.setBackgroundResource(minuteColor(tmp_left))
        tv_myseat6.setText(seatNum+"번 좌석")
    }



    // 4. 푸시 알림 function
    private fun dstPushAlarm() {
        // 해당 열차가 입력받은 도착역 1정거장 전에 도착했는지 확인
        //firebase ondatachange
        // 푸시 알림
        var title = "subwaySeat"
        var content = "하차 1 정거장 전입니다"
        var bitmap = BitmapFactory.decodeResource(resources, R.drawable.subwayseat)

        val NOTIFICAION_ID = 1001
        var builder = getNotificationBuilder("style","style Notification")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(content)
            .setAutoCancel(true)
            .setLargeIcon(bitmap)
            .setShowWhen(true)
            .setTimeoutAfter(5000L)
            .setColor(ContextCompat.getColor(this, R.color.colorAccent))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        var notification = builder.build()

        var manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(10 ,notification)
        NotificationManagerCompat.from(this).notify(NOTIFICAION_ID, builder.build())

//        val vib :(Vibrator) = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator;
//        val vib_pattern:LongArray = [100, 1000]
//        vib.vibrate(vib_pattern,-1);

    }
    //푸시 알림 채널 설정
    fun getNotificationBuilder(id:String, name:String): NotificationCompat.Builder {
        var builder: NotificationCompat.Builder? = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            var channel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH)

            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            manager.createNotificationChannel(channel)

            builder = NotificationCompat.Builder(this, id)
        } else {
            builder = NotificationCompat.Builder(this)
        }
        return builder
    }


}