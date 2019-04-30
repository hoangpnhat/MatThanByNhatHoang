package com.nhathoang.matthan.feature.main

import android.graphics.SurfaceTexture
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.TextureView
import kotlinx.android.synthetic.main.activity_main.*
import android.Manifest.permission
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
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

    private var cameraId: String? = null
    private var imageDimension: Size? = null
    private val REQUEST_CAMERA_PERMISSION = 200
    var cameraDevice: CameraDevice? = null
    var cameraCaptureSessions: CameraCaptureSession? = null
    private var captureRequestBuilder: CaptureRequest.Builder? = null
    private var mBackgroundHandler: Handler? = null
    private var mBackgroundThread: HandlerThread? = null
    var textureViewListener: TextureView.SurfaceTextureListener? = null
    private var imageReader: ImageReader? = null
    private val ORIENTATIONS = SparseIntArray()

    init {
        ORIENTATIONS.append(Surface.ROTATION_0, 90)
        ORIENTATIONS.append(Surface.ROTATION_90, 0)
        ORIENTATIONS.append(Surface.ROTATION_180, 270)
        ORIENTATIONS.append(Surface.ROTATION_270, 180)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textureViewListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
                openCamera()
            }

            override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
                return false
            }
        }
        camera.surfaceTextureListener = textureViewListener
        btnCapture.setOnClickListener {
            takePicture()
        }
        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    fun updatePreview() {
        if (null == cameraDevice) {
            Log.e("TAG", "updatePreview error, return")
        }
        captureRequestBuilder?.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)
        try {
            cameraCaptureSessions?.setRepeatingRequest(captureRequestBuilder?.build(), null, mBackgroundHandler)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }

    }

    private fun takePicture() {
        if (null == cameraDevice) {
            Log.e("TAG", "cameraDevice is null")
            return
        }
        val manager: CameraManager? = getSystemService(Context.CAMERA_SERVICE) as CameraManager?
        try {
            val characteristics: CameraCharacteristics? = manager?.getCameraCharacteristics(cameraDevice?.id)
            var jpegSizes: Array<Size>? = null
            if (characteristics != null) {
                jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(
                    ImageFormat.JPEG
                )
            }
            var width = 640
            var height = 480
            if (jpegSizes != null && jpegSizes.isNotEmpty()) {
                width = jpegSizes[0].width
                height = jpegSizes[0].height
            }
            val reader: ImageReader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1)
            val outputSurfaces: ArrayList<Surface> = ArrayList(2)
            outputSurfaces.add(reader.surface)
            outputSurfaces.add(Surface(camera.surfaceTexture))
            val captureBuilder = cameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
            captureBuilder?.addTarget(reader.surface)
            captureBuilder?.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)
            // Orientation
            val rotation: Int = windowManager.defaultDisplay.rotation
            captureBuilder?.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation))
            val date = Date().time
            val file = File(Environment.getExternalStorageDirectory(), "/pic$date.jpg")
            val readerListener: ImageReader.OnImageAvailableListener = object : ImageReader.OnImageAvailableListener {
                override fun onImageAvailable(reader: ImageReader) {
                    var image: Image? = null
                    try {
                        image = reader.acquireLatestImage()
                        val buffer = image.planes[0].buffer
                        val bytes = ByteArray(buffer.capacity())
                        buffer.get(bytes)
                        save(bytes)
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } finally {
                        image?.close()
                    }
                }

                @Throws(IOException::class)
                private fun save(bytes: ByteArray) {
                    var output: OutputStream? = null
                    try {
                        output = FileOutputStream(file)
                        output.write(bytes)
                    } finally {
                        output?.close()
                    }
                }
            }
            reader.setOnImageAvailableListener(readerListener, mBackgroundHandler)
            val captureListener: CameraCaptureSession.CaptureCallback =
                object : CameraCaptureSession.CaptureCallback() {
                    override fun onCaptureCompleted(
                        session: CameraCaptureSession,
                        request: CaptureRequest,
                        result: TotalCaptureResult
                    ) {
                        super.onCaptureCompleted(session, request, result)
//                        Toast.makeText(this@MainActivity, "Saved:" + file, Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@MainActivity, ScanImageActivity::class.java).apply {
                            putExtra(ScanImageActivity.IMAGE_PATH,file.toString())
                        })
                        //createCameraPreview()
                    }
                }
            cameraDevice?.createCaptureSession(outputSurfaces, object : CameraCaptureSession.StateCallback() {
                override fun onConfigured(session: CameraCaptureSession) {
                    try {
                        session.capture(captureBuilder?.build(), captureListener, mBackgroundHandler)
                    } catch (e: CameraAccessException) {
                        e.printStackTrace()
                    }
                }

                override fun onConfigureFailed(session: CameraCaptureSession) {}
            }, mBackgroundHandler)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    fun createCameraPreview() {
        try {
            val texture = camera.surfaceTexture
            imageDimension?.let {
                texture?.setDefaultBufferSize(imageDimension!!.width, imageDimension!!.height)
            }
            val surface = Surface(texture)
            captureRequestBuilder = cameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            captureRequestBuilder?.addTarget(surface)
            cameraDevice?.createCaptureSession(asList(surface), object : CameraCaptureSession.StateCallback() {
                override fun onConfigured(cameraCaptureSession: CameraCaptureSession) {
                    //The camera is already closed
                    if (null == cameraDevice) {
                        return
                    }
                    // When the session is ready, we start displaying the preview.
                    cameraCaptureSessions = cameraCaptureSession
                    updatePreview()
                }

                override fun onConfigureFailed(cameraCaptureSession: CameraCaptureSession) {
                    Toast.makeText(this@MainActivity, "Configuration change", Toast.LENGTH_SHORT).show()
                }
            }, null)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }

    }

    private val stateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            //This is called when the camera is open
            Log.e("TAG", "onOpened")
            cameraDevice = camera
            createCameraPreview()
        }

        override fun onDisconnected(camera: CameraDevice) {
            cameraDevice?.close()
        }

        override fun onError(camera: CameraDevice, error: Int) {
            cameraDevice?.close()
            cameraDevice = null
        }
    }

    private fun openCamera() {
        val manager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        Log.e("TAG", "is camera open")
        try {
            cameraId = manager.cameraIdList[0]
            val characteristics = manager.getCameraCharacteristics(cameraId)
            val map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)!!
            imageDimension = map.getOutputSizes(SurfaceTexture::class.java)[0]
            // Add permission for camera and let user grant the permission
            if (ActivityCompat.checkSelfPermission(
                    this, permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this, WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(permission.CAMERA, WRITE_EXTERNAL_STORAGE),
                    REQUEST_CAMERA_PERMISSION
                )
                return
            }
            manager.openCamera(cameraId, stateCallback, null)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }

        Log.e("TAG", "openCamera X")
    }

    private fun startBackgroundThread() {
        mBackgroundThread = HandlerThread("Camera Background")
        mBackgroundThread?.start()
        mBackgroundHandler = Handler(mBackgroundThread?.getLooper())
    }

    private fun stopBackgroundThread() {
        mBackgroundThread?.quitSafely()
        try {
            mBackgroundThread?.join()
            mBackgroundThread = null
            mBackgroundHandler = null
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

    }

    override fun onResume() {
        Log.e("TAG", "onResume")
        startBackgroundThread()
        if (camera.isAvailable) {
            openCamera()
        } else {
            camera.surfaceTextureListener = textureViewListener
        }
        super.onResume()
    }

    override fun onPause() {
        Log.e("TAG", "onPause")
        closeCamera()
        stopBackgroundThread()
        super.onPause()
    }
    private fun closeCamera() {
        if (null != cameraDevice) {
            cameraDevice?.close()
            cameraDevice = null
        }
        if (imageReader != null) {
            imageReader?.close()
            imageReader = null
        }
    }
}
