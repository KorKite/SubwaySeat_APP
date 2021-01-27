package com.example.subwayseat

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.subwayseat.R.color.colorRed
import kotlinx.android.synthetic.main.activity_input_destination.*
var input_dst = ""
var input_stt = ""
var line_updown = ""
var line_no = ""
class inputDestination : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_destination)


        //지하철 호선 선택할 수 있는 스피너
        val line_list = arrayOf("4호선")
        spn_line.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, line_list)

        //아이템 선택 리스너
        spn_line.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                println("nothing selected!")
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                tv_line.setText("   "+line_list[position].toString())
                line_no = line_list[position]

            }

        }




        //출발역 선택할 수 있는 스피너
        val station_list = STATION_LOCATION_X.keys.toTypedArray()
        station_list.sort()
        var stationStart = arrayOf("출발역 선택" ) + station_list

        spn_station_stt.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, stationStart)
        spn_station_stt.setSelection(0)

        //아이템 선택 리스너
        spn_station_stt.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                println("nothing selected!")
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                tv_station_stt.setText("   "+stationStart[position].toString())
                input_stt = stationStart[position]
            }

        }


        //도착역 선택할 수 있는 스피너
        var stationDst = arrayOf("도착역 선택" ) + station_list
        spn_station_dst.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, stationDst)

        //아이템 선택 리스너
        spn_station_dst.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                println("nothing selected!")
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                tv_station_dst.setText("   "+stationDst[position].toString())
                input_dst = stationDst[position]

            }

        }



        btn_next.setOnClickListener{
            if (input_stt == "출발역 선택"){
                tv_station_info.setTextColor(resources.getColor(R.color.colorBlack))
                tv_station_info1.setTextColor(resources.getColor(R.color.colorRed))
            }
            else if(input_dst == "도착역 선택"){
                tv_station_info1.setTextColor(resources.getColor(R.color.colorBlack))
                tv_station_info.setTextColor(resources.getColor(R.color.colorRed))
            }
            else {
                tv_station_info1.setTextColor(resources.getColor(R.color.colorBlack))
                tv_station_info.setTextColor(resources.getColor(R.color.colorBlack))
                getLine(input_stt, input_dst)
                if (input_stt == input_dst) {
                    tv_alert.setTextColor(Color.parseColor("#FD0303"))
                } else {
                    var nextIntent = Intent(this, chooseTrainUp::class.java)
                    startActivity(nextIntent)
                }
            }
        }



    }
}


fun getLine(stt:String, dst:String){
    println("출발역 "+stt+STATION_INDEX[stt])
    println("도착역 "+dst+STATION_INDEX[dst])
    if (STATION_INDEX[stt]!! > STATION_INDEX[dst]!!){
        line_updown = "상행"
    }
    else{
        line_updown = "하행"
    }
}