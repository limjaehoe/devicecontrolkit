package com.androidkotlin.devicecontrolkit


class DeviceException : Exception {
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
}