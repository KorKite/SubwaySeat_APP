package com.example.Want2Seat

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
import android.widget.*
import androidx.core.app.NotificationCompat
import com.example.subwayseat.STATION_INDEX
import com.example.subwayseat.seatInfo
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_choose_seat1.*

var CURRENT_SEAT = ""
var PUSHALARM = -1
public lateinit var CURRENT_BUTTON :Button
var BEFORE_SEAT = ""

class chooseSeat1 : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference()
    val db_train_info = myRef.child("SubwayLocation").child(line_no).child(line_updown)
    val db_station_info = myRef.child("SubwayLocation").child(line_no).child(line_updown)
    val db_seat_info = myRef.child("SeatStatus")

    private lateinit var db_train_info_listener:ValueEventListener
    private lateinit var db_seat_info_listener:ChildEventListener


    var tmp_left = -1

    fun remove_listener() {
        db_train_info.removeEventListener(db_train_info_listener)
        db_seat_info.removeEventListener(db_seat_info_listener)
    }

    fun return_intent() {
        val nextIntent = Intent(this, inputDestination::class.java)
        startActivity(nextIntent)
    }

    fun toastDst() {
        Toast.makeText(this, "목적지에 도착했습니다. 이용해주셔서 감사합니다.", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_seat1)



        var location_info2: ArrayList<Int> = ArrayList()
        var block: String = ""

        //현재 기기의 좌석 정보 가지고 있다면
        if (CURRENT_SEAT != ""){
            tv_myseat2.setText(CURRENT_SEAT + "번 좌석")
        }

        if (intent.hasExtra("intentKey")) {
            val location_info: ArrayList<Int> = intent.getSerializableExtra("intentKey") as ArrayList<Int>
            val loc1 = location_info.get(1)
            val loc2 = location_info.get(2)
            tv_location.text = loc1.toString() + "-1"
            tv_location3.text = loc1.toString() + "-1"
            block = loc1.toString()
            //다음 인텐트로 보낼 location info
            location_info2.add(0, CURRENT_TRAIN_NO)
            location_info2.add(1, loc1)
            location_info2.add(2, loc2)

        }

        btn_seat1.setOnClickListener { seatPopup(1, block, btn_seat1) }
        btn_seat2.setOnClickListener { seatPopup(2, block, btn_seat2) }
        btn_seat3.setOnClickListener { seatPopup(3, block, btn_seat3) }
        btn_seat4.setOnClickListener { seatPopup(4, block, btn_seat4) }
        btn_seat5.setOnClickListener { seatPopup(5, block, btn_seat5) }
        btn_seat6.setOnClickListener { seatPopup(6, block, btn_seat6) }
        btn_seat7.setOnClickListener { seatPopup(7, block, btn_seat7) }
        btn_seat8.setOnClickListener { seatPopup(8, block, btn_seat8) }
        btn_seat9.setOnClickListener { seatPopup(9, block, btn_seat9) }
        btn_seat10.setOnClickListener { seatPopup(10, block, btn_seat10) }
        btn_seat11.setOnClickListener { seatPopup(11, block, btn_seat11) }
        btn_seat12.setOnClickListener { seatPopup(12, block, btn_seat12) }
        btn_seat13.setOnClickListener { seatPopup(13, block, btn_seat13) }
        btn_seat14.setOnClickListener { seatPopup(14, block, btn_seat14) }
        btn_seat15.setOnClickListener { seatPopup(15, block, btn_seat15) }
        btn_seat16.setOnClickListener { seatPopup(16, block, btn_seat16) }
        btn_seat17.setOnClickListener { seatPopup(17, block, btn_seat17) }
        btn_seat18.setOnClickListener { seatPopup(18, block, btn_seat18) }
        btn_seat19.setOnClickListener { seatPopup(19, block, btn_seat19) }
        btn_seat20.setOnClickListener { seatPopup(20, block, btn_seat20) }

        //firebase에서 정보 받아와서 좌석 color setting
        //TO-DO 현재 기기에서 파이어베이스에 업데이트를 했다면, 내린 후에 firebase에서 정보 삭제하기
        var seatMap: HashMap<Int, String> = hashMapOf()// seat number : destination
        if (block != "") {
            db_seat_info_listener = db_seat_info.child(line_no).child(line_updown).child(CURRENT_TRAIN_NO.toString())
                .child("block" + block).addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(p0: DataSnapshot, previousChildName: String?) {
                    val seat = p0.key.toString().substring(4).toString().toInt()
                    if (seat > 0 && seat < 21) {
                        for (snapshot in p0.children) {
                            println(snapshot)
                            if (snapshot.key == "dst") {
                                val dst: String = snapshot.value.toString()
                                seatMap[seat] = dst
                                println("catch : " + seat + " : " + dst)
                            }
                        }

                        //자리 옮겼을 경우에 원래 seat 정보 제거
                        if (BEFORE_SEAT != ""){
                            seatMap.remove(BEFORE_SEAT.toString().toInt())
                            BEFORE_SEAT = ""
                        }
                        println("seatMap 정보 "+seatMap)
                    }
                }

                override fun onChildRemoved(p0: DataSnapshot) {
                    val seat = p0.key.toString().substring(4).toString().toInt()
                    if (seat > 0 && seat < 21) {
                        for (snapshot in p0.children) {
                            println(snapshot)
                            if (snapshot.key == "dst") {
                                val dst: String = snapshot.value.toString()
                                seatMap[seat] = dst
                                println("catch : " + seat + " : " + dst)
                            }
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    println("database error!")
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    println("child moved!")
                }

                override fun onChildChanged(p0: DataSnapshot, previousChildName: String?) {
//                    seatMap = hashMapOf()
                    val seat = p0.key.toString().substring(4).toString().toInt()
                    for (snap in p0.children) {
                        val seat = snap.key.toString().toInt()
                        val dst: String = snap.child("dst").value.toString()
                        seatMap[seat] = dst
                    }
                }
            })
        }




        //현재 열차 내의 좌석(firebase에 정보가 있는)과 현재 열차의 '위치'와 비교해서 calculate time한 후, 좌석 색깔 업데이트
        //다른 스레드 사용 - 15초마다 업데이트 해주는 새로운 스레드 생성
        class ThreadClass : Thread() {
            @Volatile var isrunning = true
            override fun run() {
                val btnList = listOf<Button>(btn_seat1, btn_seat2, btn_seat3, btn_seat4, btn_seat5, btn_seat6, btn_seat7, btn_seat8, btn_seat9, btn_seat10, btn_seat11, btn_seat12, btn_seat13, btn_seat14, btn_seat15, btn_seat16, btn_seat17, btn_seat18, btn_seat19, btn_seat20)
                var current_station: Int = -1
                var firstTime = 1
                while (isrunning) {
                    db_train_info_listener = db_train_info.addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(databaseError: DatabaseError) {

                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            println("쓰레드 working" + line_updown)
                            for (p0 in snapshot.children) {
                                var left: Int
                                if (p0.key == CURRENT_TRAIN_NO.toString()) {
                                    current_station = STATION_INDEX[p0.child("현재역").value.toString()]!!
                                    var stn = p0.child("현재역").value.toString()
                                    var stt = p0.child("열차출발여부").value.toString()
                                    runOnUiThread {
                                        //화면 상단에 열차의 현재 위치 업데이트 "00역 진입"
                                        train_current2.text = stn + " " + stt
                                    }

                                    //현재 열차의 위치가 사용자의 목적지 -1 이라면, 푸시 알림 시작
                                    if (line_updown == "상행" && STATION_INDEX[stn]!! == STATION_INDEX[input_dst]!! + 1 && PUSHALARM == 1) {
                                        dstPushAlarm()
                                        PUSHALARM = -1
                                    }
                                    if (line_updown == "하행" && STATION_INDEX[stn]!! + 1 == STATION_INDEX[input_dst] && PUSHALARM == 1) {
                                        dstPushAlarm()
                                        PUSHALARM = -1
                                    }

                                    //2. 현재 열차의 위치에서 모든 hashmap(현재 열차의 정보가 있는 좌석)의 도착지와의 거리를 계산
                                    if (seatMap.size > 0) {
                                        var removeIdList : ArrayList<Int> = ArrayList()
                                        for (seatId in seatMap.keys) {
                                            println("seatId : " + seatId)
                                            left = calculateTime(
                                                current_station,
                                                STATION_INDEX[seatMap[seatId]]!!
                                            )

                                            println("현재 열차 " + p0.child("현재역").value.toString() + "에서 " + seatMap[seatId] + "까지 잔여 시간 :" + left)

                                            // 잔여시간이 0인 좌석 정보는 hashmap에서 remove
                                            if (left == 0) {
                                                removeIdList.add(seatId)
                                                //현재 기기가 입력한 목적지에 도착하면, 목적지 선택 화면으로 return
                                                if (input_dst == p0.child("현재역").value.toString()) {
                                                    toastDst()
                                                    db_train_info.removeEventListener(this)
                                                    remove_listener()
                                                    isrunning = false
                                                    CURRENT_SEAT = ""
                                                    CURRENT_TRAIN_NO = -1
                                                    SELECTED_TRAIN = -1
                                                    return_intent()
                                                }
                                            }
                                            else if (line_updown == "상행" && current_station < STATION_INDEX[seatMap[seatId]]!!){
                                                removeIdList.add(seatId)
                                            }
                                            else if (line_updown == "하행" && current_station > STATION_INDEX[seatMap[seatId]]!!){
                                                removeIdList.add(seatId)
                                            }
                                            runOnUiThread {
                                                //화면 상단에 열차의 현재 위치 업데이트 "00역 진입"
                                                train_current2.text = stn + " " + stt
                                                //3. 남은 시간에 따라 좌석 color update
                                                btnList[seatId - 1].setBackgroundResource(
                                                    minuteColor(left)
                                                )
                                            }
                                        }
                                        if (removeIdList.size > 0){
                                            println("remove list "+removeIdList)
                                            for (id in removeIdList){
                                                seatMap.remove(id)
                                                btnList[id -1].setBackgroundResource(
                                                    minuteColor(-1)
                                                )
                                            }
                                            removeIdList = ArrayList()
                                            println("after remove"+seatMap)
                                        }
                                    } else {
                                        runOnUiThread {
                                            //화면 상단에 열차의 현재 위치 업데이트 "00역 진입"
                                            train_current2.text = stn + " " + stt
                                        }
                                    }
                                }
                            }
                        }

                    })

                    if (firstTime == 1) {
                        SystemClock.sleep(100)
                        firstTime = 0
                    } else {
                        SystemClock.sleep(1000)
                    }
                }
                println("스레드 멈춤")
                public_db_station_info.removeEventListener(public_db_station_info_listener)
            }
        }

        //스레드
        val thread = ThreadClass()
        thread.start()



        //다음 버튼
        btn_next1.setOnClickListener {
            val nextIntent = Intent(this, chooseSeat2::class.java)
            nextIntent.putExtra("intentKey", location_info2)
            startActivity(nextIntent)
        }


    }

    //뒤로 가기 버튼 -> 열차 선택하는 화면으로 (chooseTrainUp activity)
    override fun onBackPressed() {
        remove_listener()
        CURRENT_SEAT = ""
        CURRENT_TRAIN_NO = -1
        SELECTED_TRAIN = -1
        return_intent()
        finish()
    }


    // 1. 좌석 버튼 클릭시 나타나는 팝업창
    private fun seatPopup(seatNum2: Int, block: String, btn: Button) {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.train_info_popup_layout, null)
        val tv_popup_info = view.findViewById<TextView>(R.id.tv_popup_info)
        tv_popup_info.text = seatNum2.toString() + "번 좌석을 선택하시겠습니까?"


        //팝업창
        val alertDialog = AlertDialog.Builder(this)
            .setPositiveButton("확인") { dialog, which ->
                checkPopup(seatNum2.toString(), input_dst, block, btn)
            }
            .setNeutralButton("취소", null)
            .create()

        alertDialog.setView(view)
        alertDialog.show()
    }


    // 2. seatPopup 팝업창 -> 확인 버튼 클릭 시 도착역 등 정보 확인 용 새로운 팝업창 나타남
    private fun checkPopup(seat_num: String, dst: String, block: String, btn: Button) {

        //이미 현재 사용자가 좌석에 앉아있다면 (좌석 정보가 존재한다면)
        if (CURRENT_SEAT != "") {
            val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val info_view = inflater.inflate(R.layout.train_info_popup_layout, null)
            val tv_popup_info = info_view.findViewById<TextView>(R.id.tv_popup_info)
            tv_popup_info.text = CURRENT_SEAT + "번 좌석에 착석 중입니다.\n 자리를 이동하시겠습니까?"
            val alertDialog = AlertDialog.Builder(this)
                .setPositiveButton("확인") { dialog, which ->
                    //자리 이동 버튼 누르면
                    var final_dst: String = dst
                    // firebase에서 좌석 정보 업데이트 - user 정보, 좌석 정보, 도착역
                    updateCurrentSeatInfo(block, seat_num, 1, final_dst, btn)
                }
                .setNeutralButton("취소", null)
                .create()
            alertDialog.setView(info_view)
            alertDialog.show()
        } else {
            val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.check_popup_layout, null)
            val et_dst = view.findViewById<TextView>(R.id.et_dst)
//            val spn_station: Spinner = view.findViewById<Spinner>(R.id.spn_station2)
            val tv_left_info = view.findViewById<TextView>(R.id.tv_left_info)
            var cb_alarm = view.findViewById<CheckBox>(R.id.cb_alarm)
            val tv_seat1 = view.findViewById<TextView>(R.id.tv_seat1)
            val tv_dst_info = view.findViewById<TextView>(R.id.tv_dst_info)
            var final_dst: String = dst
            var exist = 0

            tv_seat1.setText(seat_num)

            //입력받은 하차역으로 팝업창의 하차역 기본 설정
            et_dst.setText("   " + dst)

            var stt_now = -1
            val stt_dst = STATION_INDEX[final_dst]!!


            // 도착역까지 남은 시간 계산
            // 1) 현재 열차의 위치 받아오기
            db_station_info.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    println("database error!")
                }

                override fun onDataChange(p0: DataSnapshot) {
                    for (snapshot in p0.children) {
                        if (snapshot.key == CURRENT_TRAIN_NO.toString()) {
                            stt_now = STATION_INDEX[snapshot.child("현재역").value.toString()]!!

                            //2) cost_time 계산
                            val cost_time = stt_dst?.let { it1 -> calculateTime(stt_now, it1) }
                            tmp_left = cost_time!!


                            tv_left_info.text = final_dst + " 도착까지 " + cost_time + "분 남았습니다."
                        }
                    }

                }
            })

            //팝업창
            val alertDialog = AlertDialog.Builder(this)
                .setPositiveButton("확인") { dialog, which ->
                    // 1) 1개 역 전에 알림 여부 체크박스
                    if (cb_alarm.isChecked) {
                        PUSHALARM = 1
                    } else {
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
    }


    // 3. checkPopup 팝업창 -> 확인 버튼 클릭 시 firebase에 좌석 정보 업데이트
    private fun updateSeatInfo(
        block: String,
        seatNum: String,
        status: Int,
        dst: String,
        btn: Button
    ) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference()
        val seatRef = myRef.child("SeatStatus").child(line_no).child(line_updown)
            .child(CURRENT_TRAIN_NO.toString()).child("block" + block).child("seat" + seatNum)
        val updateInfo = seatInfo(USEREMAIL, status, dst)
        input_dst = dst
        CURRENT_SEAT = seatNum
        seatRef.setValue(updateInfo)
        tv_myseat2.setText(seatNum + "번 좌석")
        btn.setBackgroundResource(minuteColor(tmp_left))
        CURRENT_BUTTON = btn
    }

    // 3-2. checkPopup 팝업창 -> 확인 버튼 클릭 시 firebase에 좌석 정보 업데이트(좌석 이동)
    private fun updateCurrentSeatInfo(
        block: String,
        seatNum: String,
        status: Int,
        dst: String,
        btn: Button
    ) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference()
        var seatRef = myRef.child("SeatStatus").child(line_no).child(line_updown)
            .child(CURRENT_TRAIN_NO.toString()).child("block" + block).child("seat" + CURRENT_SEAT)
        val updateInfo = seatInfo(USEREMAIL, status, dst)
        // 기존의 정보 remove
//        seatMap.remove(CURRENT_SEAT)
        seatRef.removeValue()
        println("기존 버튼"+CURRENT_BUTTON)
        CURRENT_BUTTON.setBackgroundResource(minuteColor(-1))
        BEFORE_SEAT = CURRENT_SEAT

        // 새로운 정보 update
        CURRENT_SEAT = seatNum
        seatRef = myRef.child("SeatStatus").child(line_no).child(line_updown)
            .child(CURRENT_TRAIN_NO.toString()).child("block" + block).child("seat" + CURRENT_SEAT)
        seatRef.setValue(updateInfo)

        input_dst = dst
        tv_myseat2.setText(seatNum + "번 좌석")
        btn.setBackgroundResource(minuteColor(tmp_left))
        CURRENT_BUTTON = btn
    }

    // 4. 푸시 알림 function
    private fun dstPushAlarm() {
        // 해당 열차가 입력받은 도착역 1정거장 전에 도착했는지 확인
        // 푸시 알림
        var title = "Want2Seat"
        var content = "하차 1 정거장 전입니다"
        var bitmap = BitmapFactory.decodeResource(resources, R.drawable.w2s_logo)

        val NOTIFICAION_ID = 1001
        var builder = getNotificationBuilder("style", "style Notification")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(content)
            .setAutoCancel(true)
            .setLargeIcon(bitmap)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        var notification = builder.build()

        var manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(10, notification)
//        NotificationManagerCompat.from(this).notify(NOTIFICAION_ID, builder.build())
//        manager.cancelAll()
//        val vib :(Vibrator) = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator;
//        val vib_pattern:LongArray = [100, 1000]
//        vib.vibrate(vib_pattern,-1);

    }

    //푸시 알림 채널 설정
    fun getNotificationBuilder(id: String, name: String): NotificationCompat.Builder {
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