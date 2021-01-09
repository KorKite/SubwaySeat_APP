package com.example.subwayseat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
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

        spn_station_stt.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, station_list)

        //아이템 선택 리스너
        spn_station_stt.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                println("nothing selected!")
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                tv_station_stt.setText("   "+station_list[position].toString())
                input_stt = station_list[position]
            }

        }


        //도착역 선택할 수 있는 스피너
        spn_station_dst.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, station_list)

        //아이템 선택 리스너
        spn_station_dst.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                println("nothing selected!")
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                tv_station_dst.setText("   "+station_list[position].toString())
                input_dst = station_list[position]

            }

        }



        btn_next.setOnClickListener{
            getLine(input_stt, input_dst)
            var nextIntent = Intent(this, chooseTrainUp::class.java)
            startActivity(nextIntent)
        }



    }
}


fun getLine(stt:String, dst:String){
    if (STATION_INDEX[stt]!! < STATION_INDEX[dst]!!){
        line_updown = "상행"
    }
    else{
        line_updown = "하행"
    }
}