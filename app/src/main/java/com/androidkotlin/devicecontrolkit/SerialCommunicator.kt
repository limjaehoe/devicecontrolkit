package com.androidkotlin.devicecontrolkit

import android.util.Log


class SerialCommunicator {
    private var isConnected = false
    @Throws(Exception::class)
    fun connect() {
        // 실제 시리얼 포트 연결 로직
        Thread.sleep(1000) // 시뮬레이션
        isConnected = true
    }

    @Throws(Exception::class)
    fun sendCommand(command: String) {
        check(isConnected) { "Not connected" }
        // 실제 명령어 전송 로직
        Log.d("SerialCommunicator", "Sending command: $command")
        Thread.sleep(100) // 시뮬레이션
    }
}