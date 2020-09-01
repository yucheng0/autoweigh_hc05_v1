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


  //     writeData("1")
   //     myViewModel.init()


       myViewModel.reReadListenKey .observe(this, androidx.lifecycle.Observer {
       if ( myViewModel.readableEnable == true  ){
            myViewModel.init()}
        })
//按鍵處理
        btn1.setOnClickListener {
            if (myViewModel.btconnectstates == true) {          //連線成功才按
                if (myViewModel.coroutineisactived == false) {
                    myViewModel.init()            //注意讀寫都去協程做
                    myViewModel.readableEnable == true
                    myViewModel.coroutineisactived = true
                } //啟動協程旗號
            }
        }
//按鍵處理
        btnConnect.setOnClickListener {
    //        if (myViewModel.isPowerOn == false) {
            Log.d(TAG, "btSceket before close: ${myViewModel.btSocket} ")
              if (myViewModel.btSocket != null){       // 有Socket 就不要再開了
                   Log.d(TAG, "btsocket!=null>>>haha ")
                      myViewModel.job.cancel()  //這個協程要關不然會當掉
                      myViewModel.btSocket!!.close()
                  myViewModel.btSocket = null
                  Toast.makeText(this, "close socket", Toast.LENGTH_SHORT).show()
                   Log.d(TAG, "btSceket after close: ${myViewModel.btSocket} ")
               }  else {
          // wait 2sec 再連接, 因為發生了錯誤了 (不要馬上關馬上連會有問題的）
                  Toast.makeText(this, "Open Socket", Toast.LENGTH_SHORT).show()
                myViewModel.isPowerOn = true
                  myViewModel.err01 = false
                    myViewModel.CheckBt()
                    myViewModel.Connect()
                    if (myViewModel.btconnectstates == true)
                    {
                        myViewModel.init()
                    }}
        }


        //監控資料變化
        myViewModel.weightlivedata.observe(this, androidx.lifecycle.Observer {
            textViewWeight.text = myViewModel.weightlivedata.value.toString()
        })

        myViewModel.replivedata.observe(this, androidx.lifecycle.Observer {
            textViewRep.text = myViewModel.replivedata.value.toString()
        })

        }








}