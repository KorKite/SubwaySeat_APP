package com.example.subwayseat

val STATION_INDEX = hashMapOf("당고개" to 0, "상계" to 1, "노원" to 2, "창동" to 3, "쌍문" to 4, "수유" to 5, "미아" to 6, "미아사거리" to 7, "길음" to 8, "성신여대입구" to 9, "한성대입구" to 10, "혜화" to 11, "동대문" to 12, "동대문역사문화공원" to 13, "충무로" to 14, "명동" to 15, "회현" to 16, "서울역" to 17, "숙대입구" to 18, "삼각지" to 19, "신용산" to 20, "이촌" to 21, "동작" to 22, "총신대입구(이수)" to 23, "사당" to 24, "남태령" to 25, "선바위" to 26, "경마공원" to 27, "대공원" to 28, "과천" to 29, "정부과천청사" to 30, "인덕원" to 31, "평촌" to 32, "범계" to 33, "금정" to 34, "산본" to 35, "수리산" to 36, "대야미" to 37, "반월" to 38, "상록수" to 39, "한대앞" to 40, "중앙" to 41, "고잔" to 42, "초지" to 43, "안산" to 44, "신길온천" to 45 ,"정왕" to 46, "오이도" to 47)
val STATION_LIST = STATION_INDEX.keys

fun calculateTime(now:Int, dest:Int):Int{
    var timeleft = arrayOf(
        2,2,2,2,3,2,2,2,3,2,2,2,2,2,1,2,2,2,2,1,2,4,3,2,1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2)
    var time:Int = 0
    if (now < dest){
        for (i:Int in now..dest){
            time += timeleft[i]
        }
    }
    else{
        for(i: Int in dest..now){
            time += timeleft[i]
        }

    }
    return time
}