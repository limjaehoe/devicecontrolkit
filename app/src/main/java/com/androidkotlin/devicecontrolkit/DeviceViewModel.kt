package com.androidkotlin.devicecontrolkit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


class DeviceViewModel(application: Application) : AndroidViewModel(application) {
    private val _status = MutableLiveData<String>("Disconnected")
    val status: LiveData<String> = _status  // public getter

    private val _kvValue = MutableLiveData<Int>(0)
    val kvValue: LiveData<Int> = _kvValue

    private val _maValue = MutableLiveData<Int>(0)
    val maValue: LiveData<Int> = _maValue

    private val _error = MutableLiveData<DeviceError>()
    val error: LiveData<DeviceError> = _error

    private val _logs = MutableLiveData<List<LogEntry>>(emptyList())
    val logs: LiveData<List<LogEntry>> = _logs

    private var preferences: DevicePreferences? = null
    private var deviceRepository: DeviceRepository? = null

    init {
        preferences = DevicePreferences(application)
        deviceRepository = DeviceRepository()
        loadSavedSettings()
    }

    fun connect() {
        try {
            deviceRepository?.connect()
            _status.value = "Connected"  // setValue 대신 value 사용
        } catch (e: Exception) {
            val connectionError = DeviceError(
                DeviceError.ErrorType.CONNECTION_ERROR,
                "장치 연결에 실패했습니다: " + e.message,
                "케이블 연결을 확인하고 다시 시도해주세요."
            )
            _error.value = connectionError
            _status.value = connectionError.getMessage()
        }
    }

    private fun addLog(action: String, value: String?) {
        _logs.value = _logs.value?.let { currentLogs ->
            val newLogs = currentLogs.toMutableList()
            newLogs.add(0, LogEntry(action, value))
            newLogs
        } ?: listOf(LogEntry(action, value))
    }

    fun setKvValue(value: Int) {
        try {
            preferences?.saveKvValue(value)  // null 안전 호출
            deviceRepository?.sendKvValue(value)
            _kvValue.value = value
            addLog("KV 설정", value.toString())
        } catch (e: IllegalArgumentException) {
            addLog("KV 설정 실패", e.message)
            _error.value = DeviceError(
                DeviceError.ErrorType.VALUE_OUT_OF_RANGE,
                "KV 값이 허용 범위를 벗어났습니다.",
                "0~100 사이의 값을 입력해주세요."
            )
        } catch (e: Exception) {
            _error.value = DeviceError(
                DeviceError.ErrorType.COMMUNICATION_ERROR,
                "값 설정 중 오류가 발생했습니다: " + e.message,
                "장치 연결 상태를 확인하고 다시 시도해주세요."
            )
        }
    }

    fun setMaValue(value: Int) {
        try {
            require(!(value < 0 || value > 100)) { "mA value out of range" }
            preferences?.saveMaValue(value)
            deviceRepository?.sendMaValue(value)
            _maValue.value = value
            addLog("mA 설정", value.toString())
        } catch (e: IllegalArgumentException) {
            addLog("mA 값이 허용 범위를 벗어남", e.message)
            _error.value = DeviceError(
                DeviceError.ErrorType.VALUE_OUT_OF_RANGE,
                "mA 값이 허용 범위를 벗어났습니다.",
                "0~100 사이의 값을 입력해주세요."
            )
        } catch (e: Exception) {
            _error.value = DeviceError(
                DeviceError.ErrorType.COMMUNICATION_ERROR,
                "값 설정 중 오류가 발생했습니다: " + e.message,
                "장치 연결 상태를 확인하고 다시 시도해주세요."
            )
        }
    }

    // FIXME: error수정중 
    private fun loadSavedSettings() {
        try {
            val savedKv = preferences?.getKvValue() ?: 0
            val savedMa = preferences?.getMaValue() ?: 0
            val wasConnected = preferences?.wasConnected() ?: false

            _kvValue.value = savedKv
            _maValue.value = savedMa

            addLog("설정 불러오기", String.format("KV: %d, mA: %d", savedKv, savedMa))

            if (wasConnected) {
                connect()
            }
        } catch (e: Exception) {
            _error.value = DeviceError(
                DeviceError.ErrorType.COMMUNICATION_ERROR,
                "설정 불러오기 실패",
                "앱을 다시 시작해주세요."
            )
            _kvValue.value = 0
            _maValue.value = 0
            addLog("설정 불러오기 실패", e.message)
        }
    }
}