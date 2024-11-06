package com.androidkotlin.devicecontrolkit

/*
에러 타입을 Enum으로 명확하게 정의
각 에러에 대한 설명과 해결방법 포함
ViewModel에서 적절한 에러 객체 생성
Activity에서 다이얼로그로 에러 표시
 */
class DeviceError // 생성자
    (// Getter 메소드들
    val type: ErrorType, val message: String, val solution: String
) {
    // 에러 타입 정의
    enum class ErrorType(val description: String) {
        CONNECTION_ERROR("연결 오류"),
        COMMUNICATION_ERROR("통신 오류"),
        VALUE_OUT_OF_RANGE("값 범위 초과"),
        HARDWARE_ERROR("하드웨어 오류")

    }

    // 에러 메시지 포맷팅
    override fun toString(): String {
        return String.format(
            "Error: %s\nMessage: %s\nSolution: %s",
            type.description, message, solution
        )
    }
}