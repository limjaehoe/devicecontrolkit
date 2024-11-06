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

    private val _logs = MutableLiveData<List<LogEntry>>()
    val logs: LiveData<List<LogEntry>> = _logs

    private var preferences: DevicePreferences? = null
    private var deviceRepository: DeviceRepository? = null

    //AndroidViewModel을 상속받아 Application을 생성자로 받음
    init {
        preferences = DevicePreferences(application)
        deviceRepository = DeviceRepository()
        loadSavedSettings() // 생성자에서 호출
    }

    fun getStatus(): LiveData<String> {
        return status
    }

    fun getKvValue(): LiveData<Int> {
        return kvValue
    }

    // error getter 추가
    fun getError(): LiveData<DeviceError> {
        return error
    }

    fun getLogs(): LiveData<List<LogEntry>> {
        return logs
    }

    fun connect() {
        try {
            deviceRepository?.connect()
            status.setValue("Connected")
        } catch (e: Exception) {
            val connectionError = DeviceError(
                DeviceError.ErrorType.CONNECTION_ERROR,
                "장치 연결에 실패했습니다: " + e.message,
                "케이블 연결을 확인하고 다시 시도해주세요."
            )
            error.setValue(connectionError)
            status.setValue(connectionError.getMessage())
        }
    }

    private fun addLog(action: String, value: String?) {
        val currentLogs: List<LogEntry>? = logs.getValue()
        if (currentLogs != null) {
            val newLogs: List<LogEntry> = ArrayList<Any?>(currentLogs)
            newLogs.add(0, LogEntry(action, value))
            logs.setValue(newLogs)
        }
    }

    fun setKvValue(value: Int) {
        try {
            val preferences = DevicePreferences(getApplication())
            preferences.saveKvValue(value)

            deviceRepository.sendKvValue(value)
            kvValue.setValue(value)
            addLog("KV 설정", value.toString())
        } catch (e: IllegalArgumentException) {
            addLog("KV 설정 실패", e.message)
            val rangeError = DeviceError(
                DeviceError.ErrorType.VALUE_OUT_OF_RANGE,
                "KV 값이 허용 범위를 벗어났습니다.",
                "0~100 사이의 값을 입력해주세요."
            )
            error.setValue(rangeError)
        } catch (e: Exception) {
            val communicationError = DeviceError(
                DeviceError.ErrorType.COMMUNICATION_ERROR,
                "값 설정 중 오류가 발생했습니다: " + e.message,
                "장치 연결 상태를 확인하고 다시 시도해주세요."
            )
            error.setValue(communicationError)
        }
    }

    fun setMaValue(value: Int) {  // 추가
        try {
            require(!(value < 0 || value > 100)) { "mA value out of range" }
            val preferences = DevicePreferences(getApplication())
            preferences.saveMaValue(value)
            deviceRepository.sendMaValue(value)
            maValue.setValue(value)
            addLog("mA 설정", value.toString())
        } catch (e: IllegalArgumentException) {
            addLog("mA  값이 허용 범위를 벗어남", e.message)
            val rangeError = DeviceError(
                DeviceError.ErrorType.VALUE_OUT_OF_RANGE,
                "mA 값이 허용 범위를 벗어났습니다.",
                "0~100 사이의 값을 입력해주세요."
            )
            error.setValue(rangeError)
        } catch (e: Exception) {
            addLog("mA 값 설정 중 오류", e.message)
            val communicationError = DeviceError(
                DeviceError.ErrorType.COMMUNICATION_ERROR,
                "값 설정 중 오류가 발생했습니다: " + e.message,
                "장치 연결 상태를 확인하고 다시 시도해주세요."
            )
            error.setValue(communicationError)
        }
    }

    // 저장된 설정값을 불러오는 메소드
    private fun loadSavedSettings() {
        try {
            // SharedPreferences에서 저장된 값 불러오기
            val savedKv: Int = preferences.getKvValue()
            val savedMa: Int = preferences.getMaValue()
            val wasConnected: Boolean = preferences.wasConnected()

            // LiveData에 저장된 값 설정
            kvValue.setValue(savedKv)
            maValue.setValue(savedMa)

            // 로그에 기록
            addLog("설정 불러오기", String.format("KV: %d, mA: %d", savedKv, savedMa))

            // 이전에 연결되어 있었다면 자동 연결 시도
            if (wasConnected) {
                connect()
            }
        } catch (e: Exception) {
            // 설정 불러오기 실패시 에러 처리
            error.setValue(
                DeviceError(
                    DeviceError.ErrorType.COMMUNICATION_ERROR,
                    "설정 불러오기 실패",
                    "앱을 다시 시작해주세요."
                )
            )
            // 기본값 설정
            kvValue.setValue(0)
            maValue.setValue(0)
            // 로그에 기록
            addLog("설정 불러오기 실패", e.message)
        }
    }
}