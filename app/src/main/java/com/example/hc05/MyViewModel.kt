package com.example.hc05

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException

// 55 3F 04 00 14 00 05 A3 90

class MyViewModel : ViewModel() {
    val TAG = "myTag"
    var readableEnable = false
    val reReadListenKey = MutableLiveData<Boolean>()
    var readResult = ArrayList<String>()
    var indexstartbyte = 0
    var weightlivedata = MutableLiveData<Int>()
    var replivedata = MutableLiveData<Int>()
    var numBytes = 0
    var weight = 0
    var rep = 0

    init {
        weightlivedata.value = 0
        replivedata.value = 0
    }

    private fun readData() {
        println("every 200ms once")
        var inStream = MainActivity.btSocket.inputStream
        try {
            inStream = MainActivity.btSocket.inputStream
        } catch (e: IOException) {
            println(e.printStackTrace())
        }
//假如buffer內有資料就先處理, 再去讀取


        val buffer: ByteArray = ByteArray(1024)
        try {
            numBytes = inStream.read(buffer)  // bytes returned from read()
            println("numBytes = $numBytes")
        } catch (e: Exception) {
            println("buffer = null, $e")
        }

        for (i in 0..numBytes - 1) {   //先知道nubBytes的數字再去讀
            if (buffer[i].toInt() >= 0) {
                readResult.add(buffer[i].toString())
            } else {                //負數處理
                val b1 = 256 + buffer[i].toInt()
                readResult.add(b1.toString())
            }

        }
        if (readResult.size >= 90) {
            readResult.clear()
        } else {
            while (readResult.size >= 9) {
                ParserStart()       //印出正常處理
                println(readResult)
            }
            weightlivedata.value = weight       //更新ui
            replivedata.value = rep
        }
    }  // Read data end


    //  函式開始

    fun ParserStart() {
        indexstartbyte = readResult.indexOf("85")
        if (readResult[indexstartbyte] != "85") {
            delRangeofArrayData(readResult, indexstartbyte, 0)
            return
        }

        if (readResult[indexstartbyte + 1] != "63") {
            delRangeofArrayData(readResult, indexstartbyte, 1)
            return
        }
        if (readResult[indexstartbyte + 2] != "4") {
            delRangeofArrayData(readResult, indexstartbyte, 2)
            return
        }

        if (readResult[indexstartbyte + 8] != "144") {
            delRangeofArrayData(readResult, indexstartbyte, 8)
            return
        }

//parser成功，請資料
        //讀重量
        var weightHibyte = readResult[indexstartbyte + 3].toInt()
        var weightLobyte = readResult[indexstartbyte + 4].toInt()
        weight = (256 * weightHibyte + weightLobyte).toInt()
        //      weightlivedata.value = weight
        //讀次數
        var repHibyte = readResult[indexstartbyte + 5].toInt()
        var repLobyte = readResult[indexstartbyte + 6].toInt()
        rep = 256 * repHibyte + repLobyte
//        replivedata.value = rep

        for (i in 0..indexstartbyte + 8) {
            readResult.removeAt(0)          //清除已處理的資料
        }
        println("正常處理")
    }

    fun delRangeofArrayData(arr: ArrayList<String>, startIndex: Int, num: Int) {
        println("err1 = $arr")
        for (i in 0..startIndex + num) {          //刪除 從頭刪到index + num的值
            arr.removeAt(0)
        }
        println("err2 =$arr")
    }


    //清除及搬移

    fun init() {
        viewModelScope.launch(Dispatchers.Main) {
            delay200ms()
        }
// 先執行再delay

    }

    suspend fun delay200ms() {
        println("pre 200ms")
        delay(timeMillis = 200)
        readData()
        readableEnable = true
        reReadListenKey.value = reReadListenKey.value
    }
}
