package com.androidkotlin.devicecontrolkit


class DeviceRepository {
    //데이터 접근과 장치 통신을 담당
    private val serialCommunicator: SerialCommunicator
    private var isConnected = false

    init {
        serialCommunicator = SerialCommunicator()
    }

    @Throws(DeviceException::class)
    fun connect() {
        isConnected = try {
            serialCommunicator.connect()
            true
        } catch (e: Exception) {
            throw DeviceException("Connection failed: " + e.message)
        }
    }

    @Throws(DeviceException::class)
    fun sendKvValue(value: Int) {
        if (!isConnected) {
            throw DeviceException("Device not connected")
        }
        try {
            serialCommunicator.sendCommand("SET_KV:$value")
        } catch (e: Exception) {
            throw DeviceException("Failed to send KV value: " + e.message)
        }
    }

    @Throws(DeviceException::class)
    fun sendMaValue(value: Int) {
        if (!isConnected) {
            throw DeviceException("Device not connected")
        }
        try {
            serialCommunicator.sendCommand("SET_MA:$value")
        } catch (e: Exception) {
            throw DeviceException("Failed to send mA value: " + e.message)
        }
    }
}