package com.example.hc05

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.hc05.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.util.*

const val BT_MAC = "98:D3:31:FC:1B:1B" //""24:79:F3:8E:55:AA"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val databinding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

//       setContentView(R.layout.activity_main)
        val myViewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        myViewModel.context = this
      if (myViewModel.coroutineisactived==true){
            myViewModel.job.cancel()
       }
    //    if (myViewModel.isPowerOn == true)  {           //第1次關閉協程
    //        myViewModel.job.cancel()
    //    }

if (myViewModel.isPowerOn == false) {
    myViewModel.isPowerOn = true
    myViewModel.CheckBt()
    myViewModel.Connect()
}
  //     writeData("1")
   //     myViewModel.init()


       myViewModel.reReadListenKey .observe(this, androidx.lifecycle.Observer {
       if ( myViewModel.readableEnable == true  ){
            myViewModel.init()}
        })

        btn1.setOnClickListener {
            if (myViewModel.coroutineisactived == false) {
            myViewModel.init()            //注意讀寫都去協程做
            myViewModel.readableEnable == true
            myViewModel.coroutineisactived = true } //啟動協程旗號
        }

        myViewModel.weightlivedata.observe(this, androidx.lifecycle.Observer {
            textViewWeight.text = myViewModel.weightlivedata.value.toString()
        })

        myViewModel.replivedata.observe(this, androidx.lifecycle.Observer {
            textViewRep.text = myViewModel.replivedata.value.toString()
        })

        }








}