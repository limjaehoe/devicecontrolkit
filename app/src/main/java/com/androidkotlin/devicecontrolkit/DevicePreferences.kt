package com.androidkotlin.devicecontrolkit

import android.content.Context
import android.content.SharedPreferences


class DevicePreferences(context: Context) {

    private val preferences: SharedPreferences

    init {
        // 앱의 설정값을 저장할 SharedPreferences 초기화
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    // KV 값 저장
    fun saveKvValue(value: Int) {
        preferences.edit()
            .putInt(PREF_KV_VALUE, value)
            .apply()
    }

    val kvValue: Int
        // KV 값 불러오기 (기본값 0)
        get() = preferences.getInt(PREF_KV_VALUE, 0)

    // mA 값 저장
    fun saveMaValue(value: Int) {
        preferences.edit()
            .putInt(PREF_MA_VALUE, value)
            .apply()
    }

    val maValue: Int
        // mA 값 불러오기 (기본값 0)
        get() = preferences.getInt(PREF_MA_VALUE, 0)

    // 연결 상태 저장
    fun saveConnectionState(isConnected: Boolean) {
        preferences.edit()
            .putBoolean(PREF_IS_CONNECTED, isConnected)
            .apply()
    }

    // 연결 상태 불러오기
    fun wasConnected(): Boolean {
        return preferences.getBoolean(PREF_IS_CONNECTED, false)
    }

    // 모든 설정값 저장
    fun saveAllSettings(kv: Int, ma: Int, isConnected: Boolean) {
        preferences.edit()
            .putInt(PREF_KV_VALUE, kv)
            .putInt(PREF_MA_VALUE, ma)
            .putBoolean(PREF_IS_CONNECTED, isConnected)
            .apply()
    }

    // 모든 설정값 초기화
    fun clearAll() {
        preferences.edit().clear().apply()
    }

    companion object {
        private const val PREF_NAME = "device_settings"
        private const val PREF_KV_VALUE = "kv_value"
        private const val PREF_MA_VALUE = "ma_value"
        private const val PREF_IS_CONNECTED = "is_connected"
    }
}