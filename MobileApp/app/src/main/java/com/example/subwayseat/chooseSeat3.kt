package com.example.subwayseat

import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_choose_seat1.*
import kotlinx.android.synthetic.main.activity_choose_seat2.*
import kotlinx.android.synthetic.main.activity_choose_seat3.*

class chooseSeat3 : AppCompatActivity() {
    // user가 탑승하고 있는 열차 정보 (TO-DO:이전 intent에서 받아와야 함)
    val TRAIN = 1000
    val BLOCK = "4-1"
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_seat3)

        btn_seat35.setOnClickListener{seatPopup(35, btn_seat35)}
        btn_seat36.setOnClickListener{seatPopup(36, btn_seat36)}
        btn_seat37.setOnClickListener{seatPopup(37, btn_seat37)}
        btn_seat38.setOnClickListener{seatPopup(38, btn_seat38)}
        btn_seat39.setOnClickListener{seatPopup(39, btn_seat39)}
        btn_seat40.setOnClickListener{seatPopup(40, btn_seat40)}
        btn_seat41.setOnClickListener{seatPopup(41, btn_seat41)}
        btn_seat42.setOnClickListener{seatPopup(42, btn_seat42)}
        btn_seat43.setOnClickListener{seatPopup(43, btn_seat43)}
        btn_seat44.setOnClickListener{seatPopup(44, btn_seat44)}
        btn_seat45.setOnClickListener{seatPopup(45, btn_seat45)}
        btn_seat46.setOnClickListener{seatPopup(46, btn_seat46)}
        btn_seat47.setOnClickListener{seatPopup(47, btn_seat47)}
        btn_seat48.setOnClickListener{seatPopup(48, btn_seat48)}
        btn_seat49.setOnClickListener{seatPopup(49, btn_seat49)}
        btn_seat50.setOnClickListener{seatPopup(50, btn_seat50)}
        btn_seat51.setOnClickListener{seatPopup(51, btn_seat51)}
        btn_seat52.setOnClickListener{seatPopup(52, btn_seat52)}
        btn_seat53.setOnClickListener{seatPopup(53, btn_seat53)}
        btn_seat54.setOnClickListener{seatPopup(54, btn_seat54)}


        //이전 버튼
        btn_prev3.setOnClickListener {
            val nextIntent = Intent(this, chooseSeat2::class.java)
            startActivity(nextIntent)

        }
    }


    // 1. 좌석 버튼 클릭시 나타나는 팝업창
    private fun seatPopup(seat_num: Int, button: Button){
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.seat_popup_layout, null)
        val tv_popup_info = view.findViewById<TextView>(R.id.tv_popup_info)
        tv_popup_info.text = seat_num.toString() +"번 좌석을 선택하시겠습니까?"

        //팝업창
        val alertDialog = AlertDialog.Builder(this)
            .setPositiveButton("확인"){dialog, which ->
                //                button.setBackgroundResource(R.drawable.seat_red)
                println("좌석 number : "+seat_num)
                checkPopup(seat_num.toString(), "서울역")
            }
            .setNeutralButton("취소", null)
            .create()

        alertDialog.setView(view)
        alertDialog.show()
    }


    // 2. seatPopup 팝업창 -> 확인 버튼 클릭 시 도착역 등 정보 확인 용 새로운 팝업창 나타남
    private fun checkPopup(seat_num:String, dst:String){
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.check_popup_layout, null)
        val et_dst = view.findViewById<EditText>(R.id.et_dst)
        val btn_dst = view.findViewById<Button>(R.id.btn_dst)
        val tv_left_info = view.findViewById<TextView>(R.id.tv_left_info)
        var cb_alarm = view.findViewById<CheckBox>(R.id.cb_alarm)
        val tv_seat1 = view.findViewById<TextView>(R.id.tv_seat1)
        val tv_dst_info = view.findViewById<TextView>(R.id.tv_dst_info)
        var final_dst:String = dst
        var exist = 0

        tv_seat1.setText(seat_num)


        //입력받은 하차역으로 팝업창의 하차역 기본 설정
        et_dst.setText(dst)
        tv_left_info.text = "확인 버튼을 누르세요"


        //역 입력 후 확인 버튼 -> 최종적으로 입력받은 하차역을 최종 하차역으로 설정
        btn_dst.setOnClickListener {
            var final_dst: String = dst
            final_dst = et_dst.text.toString()

            val line_updown = "상행"
            val train_no = 4136

            var stt_now = -1
            val stt_dst = STATION_INDEX[final_dst]


            // 입력받은 역이 존재한다면
            if (STATION_LIST.contains(final_dst)){
                exist = 1
                tv_dst_info.setText("")

                // 도착역까지 남은 시간 계산
                // 1) 현재 열차의 위치 받아오기
                val db_station_info = myRef.child("SubwayLocation").child("4호선").child(line_updown)
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
                            if (snapshot.key == train_no.toString()) {
                                stt_now = STATION_INDEX[snapshot.child("현재역").value.toString()]!!
                            }
                        }
                        //2) cost_time 계산
                        val cost_time = stt_dst?.let { it1 -> calculateTime(stt_now, it1) }

                        tv_left_info.text = final_dst + " 도착까지 " + cost_time + "분 남았습니다."
                    }
                })
            }
            else{ //역이 존재하지 않는다면
                tv_dst_info.setTextColor(Color.parseColor("#FE0000"))
                tv_dst_info.setText("존재하지 않는 역입니다. 다시 입력해주세요")
                tv_left_info.setText("")
            }
        }



        //팝업창
        val alertDialog = AlertDialog.Builder(this)
            .setPositiveButton("확인") { dialog, which ->
                if (exist == 1) { //하차역이 존재하는 역이라면
                    //                    startActivity(nextIntent)

                    // 1) 1개 역 전에 알림 여부 체크박스
                    cb_alarm.setOnCheckedChangeListener { buttonView, isChecked ->
                        if (isChecked) {
                            //알림 function
                            dstPushAlarm()
                        }
                    }
                    // To2) firebase에서 좌석 정보 업데이트 - user 정보, 좌석 정보, 도착역
                    updateSeatInfo(TRAIN, BLOCK, seat_num, "sange1104", 1, final_dst)
                }
                else{
                    dialog.dismiss()
                }
            }

            .setNeutralButton("취소", null)
            .create()

        alertDialog.setView(view)
        //        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        //            .setOnClickListener(View.OnClickListener() {
        //                if (exist==1)
        //                    alertDialog.dismiss();
        //            })

        alertDialog.show()

    }



    // 3. checkPopup 팝업창 -> 확인 버튼 클릭 시 firebase에 좌석 정보 업데이트
    private fun updateSeatInfo(train:Int, block:String, seat:String, userid:String, status:Int, dst:String) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference()
        val seatRef = myRef.child("SeatStatus").child("4호선").child(train.toString()).child(block)
        val updateInfo = seatInfo(userid, status, dst)
        seatRef.child(seat).setValue(updateInfo) //시간 계산 어디에서??
    }



    // 4. 푸시 알림 function
    private fun dstPushAlarm() {
        // 해당 열차가 입력받은 도착역 1정거장 전에 도착했는지 확인
        //firebase ondatachange
        // 푸시 알림
        var title = "subwaySeat"
        var content = "하차 1정거장 전입니다"
        //        var bitmap = BitmapFactory.decodeResource(resources, R.drawable.phone)

        val NOTIFICAION_ID = 1001
        var builder = getNotificationBuilder("style","style Notification")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(content)
            .setAutoCancel(true)
            //            .setLargeIcon(bitmap)
            .setShowWhen(true)
            .setColor(ContextCompat.getColor(this, R.color.colorAccent))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        var notification = builder.build()

        var manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(10 ,notification)
        //        NotificationManagerCompat.from(this).notify(NOTIFICAION_ID, builder.build())

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

