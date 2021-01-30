package com.example.subwayseat

fun calculateTime(now:Int, dest:Int):Int{
    println("출발 인덱스 "+now.toString()+" 도착 "+dest.toString())
    var timeleft = arrayOf(
        2,2,2,2,3,2,2,2,3,2,2,2,2,2,1,2,2,2,2,1,2,4,3,2,1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2)
    var time:Int = 0
    if (now < dest){
        for (i:Int in now..dest-1){
            time += timeleft[i]
        }
    }
    else{
        for(i: Int in dest..now-1){
            time += timeleft[i]
        }

    }
    return time
}


fun minuteColor(min:Int): Int {
    if (min > 10) {
        return R.drawable.seat_red
    } else if (min > 5) {
        return R.drawable.seat_yellow
    } else if (min <= 0){
        return R.drawable.seat_blu
    }
    else {
        return R.drawable.seat_green
    }
}