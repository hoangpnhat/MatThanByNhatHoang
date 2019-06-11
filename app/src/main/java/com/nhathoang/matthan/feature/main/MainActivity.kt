package com.nhathoang.matthan.feature.main

import android.graphics.SurfaceTexture
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.TextureView
import kotlinx.android.synthetic.main.activity_main.*
import android.Manifest.permission
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothHeadset
import android.bluetooth.BluetoothProfile
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.hardware.camera2.*
import android.util.Log
import android.util.Size
import android.widget.Toast
import java.util.Arrays.asList
import android.view.Surface
import java.util.*
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraMetadata
import android.hardware.camera2.CaptureRequest
import android.media.Image
import android.media.ImageReader
import android.os.Environment
import android.os.Handler
import android.os.HandlerThread
import android.util.SparseIntArray
import com.nhathoang.matthan.R
import com.nhathoang.matthan.feature.scanImage.ScanImageActivity
import java.io.*
import kotlin.collections.ArrayList


open class MainActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_BLUETOOTH = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var bluetoothHeadset: BluetoothHeadset? = null
        var pairedDevices : Set<BluetoothDevice>? = null
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null) {
        } else {
            if (!bluetoothAdapter.enable()) {
                val enableBT = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBT, REQUEST_BLUETOOTH)
            }
        }
        bluetoothAdapter?.bondedDevices?.let{
            pairedDevices = it
        }
    }
    inner class BroadCastReceviver : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }
}

//val profileListener = object : BluetoothProfile.ServiceListener {
//    override fun onServiceDisconnected(profile: Int) {
//        if (profile == BluetoothProfile.HEADSET) {
//            bluetoothHeadset = null
//        }
//    }
//
//    override fun onServiceConnected(profile: Int, proxy: BluetoothProfile?) {
//        if (profile == BluetoothProfile.HEADSET) {
//            bluetoothHeadset = proxy as BluetoothHeadset
//        }
//    }
//}
//bluetoothAdapter?.getProfileProxy(this@MainActivity, profileListener, BluetoothProfile.HEADSET)
//bluetoothAdapter?.closeProfileProxy(BluetoothProfile.HEADSET, bluetoothHeadset)

