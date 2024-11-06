package com.androidkotlin.devicecontrolkit

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class LogEntry(// 동작
    val action: String, //값
    var value: String
) {
    val timestamp //시간
            : String

    init {
        timestamp = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        value = value
    }
}


/*
Entry는 '입력' 또는 '기입'이라는 의미를 가진 단어입니다.
컴퓨터 프로그래밍에서 Entry는 주로:

데이터베이스의 한 행(row)
로그 기록의 한 항목
목록(리스트)의 한 항목
을 의미할 때 자주 사용됩니다.
 */