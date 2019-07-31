package com.huy.library.handler

interface DataReceiver<T> {

    fun onDataReceived(data: T)
}