package com.nhathoang.matthan.feature.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import android.widget.Toast
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbDeviceConnection
import android.hardware.usb.UsbManager
import com.felhr.usbserial.UsbSerialDevice
import com.felhr.usbserial.UsbSerialInterface
import com.nhathoang.matthan.R
import com.nhathoang.matthan.feature.readNews.ReadNews
import com.nhathoang.matthan.feature.capture.CaptureActivity
import java.io.*
import java.nio.charset.Charset


open class MainActivity : AppCompatActivity() {
    //Serial USB
    private val ACTION_USB_PERMISSION = "permission"
    private lateinit var mUsbManager: UsbManager
    private var mDevice: UsbDevice? = null
    private var mConnection: UsbDeviceConnection? = null
    private var mSerial: UsbSerialDevice? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mUsbManager = getSystemService(Context.USB_SERVICE) as UsbManager
        val filter = IntentFilter()
        filter.addAction(ACTION_USB_PERMISSION)
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED)
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED)
        registerReceiver(broadcastReceiver, filter)
        val usbDevices = mUsbManager.deviceList
        if (usbDevices.isNotEmpty()) {
            var keep = true
            for (entry in usbDevices.entries) {
                mDevice = entry.value
                val deviceVID = mDevice!!.vendorId
                if (deviceVID == 0x9025) { //Arduino Vendor ID
                    val pi = PendingIntent.getBroadcast(this, 0, Intent(ACTION_USB_PERMISSION), 0)
                    mUsbManager.requestPermission(mDevice, pi)
                    keep = false
                } else {
                    mConnection = null
                    mDevice = null
                }

                if (!keep)
                    break
            }
        }
//        btnCaptureFeature.setOnClickListener {
//            startActivity(Intent(this@MainActivity, CaptureActivity::class.java))
//        }
//        btnBaoNoi.setOnClickListener {
//            startActivity(Intent(this@MainActivity, ReadNews::class.java))
//        }
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        //Broadcast Receiver to automatically start and stop the Serial connection.
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                if (it.action == (ACTION_USB_PERMISSION)) {
                    val granted = it.extras.getBoolean(UsbManager.EXTRA_PERMISSION_GRANTED);
                    if (granted) {
                        mConnection = mUsbManager.openDevice(mDevice)
                        mSerial = UsbSerialDevice.createUsbSerialDevice(mDevice, mConnection)
                        if (mSerial != null) {
                            if (mSerial!!.open()) { //Set Serial Connection Parameters.
                                mSerial!!.setBaudRate(9600)
                                mSerial!!.setDataBits(UsbSerialInterface.DATA_BITS_8)
                                mSerial!!.setStopBits(UsbSerialInterface.STOP_BITS_1)
                                mSerial!!.setParity(UsbSerialInterface.PARITY_NONE)
                                mSerial!!.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF)
                                mSerial!!.read(mCallback) //
//                                Toast.makeText(this@MainActivity, "Serial Connection Opened!\n", Toast.LENGTH_SHORT).show()
                                txtView?.append("Serial Connection Opened!\\n")
                            } else {
//                                Toast.makeText(this@MainActivity, "PORT NOT OPEN\n", Toast.LENGTH_SHORT)
                                txtView?.append("PORT NOT OPEN")


                                Log.d("SERIAL", "PORT NOT OPEN")
                            }
                        } else {
//                            Toast.makeText(this@MainActivity, "PORT IS NULL\n", Toast.LENGTH_SHORT)
                            txtView?.append("PORT IS NULL")

                            Log.d("SERIAL", "PORT IS NULL")
                        }
                    } else {
//                        Toast.makeText(this@MainActivity, "PERMISSION NOT GRANTED\n", Toast.LENGTH_SHORT)
                        txtView?.append("PERMISSION NOT GRANTED")
                        Log.d("SERIAL", "PERMISSION NOT GRANTED")
                    }
                } else if (intent.action == UsbManager.ACTION_USB_DEVICE_ATTACHED) {
//                        onClickStart(startButton)
                } else if (intent.action == UsbManager.ACTION_USB_DEVICE_DETACHED) {
//                        onClickStop(stopButton)
                } else {
                }
            }

        }
    }
    private var mCallback: UsbSerialInterface.UsbReadCallback = UsbSerialInterface.UsbReadCallback { receive ->
            //Defining a Callback which triggers whenever data is read.
            var data: String? = null
            try {
                data = String(receive, Charset.forName("UTF-8"))
                Toast.makeText(this@MainActivity, data, Toast.LENGTH_SHORT)

//                when (data) {
//                    "nut1" -> {
//                        startActivity(Intent(this@MainActivity, CaptureActivity::class.java))
//                    }
//                    "nut2" -> {
//
//                    }
//                    "nut3" -> {
//                        startActivity(Intent(this@MainActivity, ReadNews::class.java))
//                    }
//                }

            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }
        }
}

