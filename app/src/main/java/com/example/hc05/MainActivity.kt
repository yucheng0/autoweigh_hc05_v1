package com.example.hc05

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.hc05.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException

const val BT_MAC = "98:D3:31:FC:1B:1B" //""24:79:F3:8E:55:AA"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val databinding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

//       setContentView(R.layout.activity_main)
        val myViewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        myViewModel.context = this
        if (myViewModel.coroutineisactived == true) {
            myViewModel.job?.cancel()
        }
        //    if (myViewModel.isPowerOn == true)  {           //第1次關閉協程
        //        myViewModel.job.cancel()
        //    }


        //     writeData("1")
        //     myViewModel.init()


        myViewModel.reReadListenKey.observe(this, androidx.lifecycle.Observer {
            if (myViewModel.readableEnable == true) {
                myViewModel.init()
            }
        })
//按鍵處理
        btnDisconnect.setOnClickListener {
            //          if (myViewModel.btconnectstates == true) {          //連線成功才按
            //            if (myViewModel.coroutineisactived == false) {
            ///              myViewModel.init()            //注意讀寫都去協程做
            //           myViewModel.readableEnable == true
            //         myViewModel.coroutineisactived = true
            //       } //啟動協程旗號
            //        }
            myViewModel.job?.cancel()
            myViewModel.err01 = true
            myViewModel.btSocket?.close() //支援空值的btsocketclose所以一直按不會當
            Log.d(TAG, "btScoket closed, ${myViewModel.btSocket}")
            Log.d(TAG, "isconnected?: ${myViewModel.btSocket?.isConnected}")
        }
//按鍵處理
        btnConnect.setOnClickListener {
            Log.d(TAG, "btsocket status: ${myViewModel.btSocket?.isConnected}")
            if (myViewModel.btSocket?.isConnected == null || myViewModel.btSocket?.isConnected == false) {
                // wait 2sec 再連接, 因為發生了錯誤了 (不要馬上關馬上連會有問題的）
                Toast.makeText(this, "Open Socket", Toast.LENGTH_SHORT).show()
                myViewModel.isPowerOn = true
                myViewModel.err01 = false
                myViewModel.CheckBt()
                myViewModel.Connect()
                if (myViewModel.btconnectstates == true) {
                    myViewModel.init()
                }

            }
        }

        //監控資料變化
        myViewModel.weightlivedata.observe(this, androidx.lifecycle.Observer {
            textViewWeight.text = myViewModel.weightlivedata.value.toString()
        })

        myViewModel.replivedata.observe(this, androidx.lifecycle.Observer {
            textViewRep.text = myViewModel.replivedata.value.toString()
        })

    }


    override fun onPause() {
        super.onPause()
    }


}