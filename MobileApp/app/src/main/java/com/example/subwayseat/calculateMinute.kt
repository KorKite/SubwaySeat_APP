package com.example.subwayseat

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