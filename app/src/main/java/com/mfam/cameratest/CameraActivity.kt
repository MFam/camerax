package com.mfam.cameratest

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.google.common.util.concurrent.ListenableFuture
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.File
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity() {

    lateinit var imageCapture: ImageCapture
    private lateinit var cameraProviderFuture : ListenableFuture<ProcessCameraProvider>
    private val executor = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        startCamera()
    }

    private fun startCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            val preview = Preview.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_4_3).build()
            preview.setSurfaceProvider(previewView.previewSurfaceProvider)

            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                .build()

            cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
        }, ContextCompat.getMainExecutor(this))
    }

    fun capture(v: View) {
        Logger.d("cacheDir : ${cacheDir.absolutePath}")
        val savedPath = File(cacheDir.absolutePath, "temp.jpg")
        val imageSavedCallback = object: ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                previewView.post {
                    if (outputFileResults.savedUri == null) {
                        Logger.e("outputFileResults savedUri is NULL===")
                    }
                    Toast.makeText(this@CameraActivity, "Image save success!!", Toast.LENGTH_SHORT).show()
                    Intent(this@CameraActivity, ImageCropActivity::class.java).apply {
                            putExtra(EXTRA_URI, Uri.fromFile(savedPath))
                        }.run {
                            startActivity(this)
                        }

//                    outputFileResults.savedUri?.let {
//                        Logger.d("onImageSaved : ${outputFileResults.savedUri!!.path}")
//                        Intent(this@CameraActivity, ImageCropActivity::class.java).apply {
//                            putExtra(EXTRA_URI, Uri.parse(savedPath.absolutePath))
//                        }.run {
//                            startActivity(this)
//                        }
//                    }
                }
            }

            override fun onError(exception: ImageCaptureException) {
                Logger.e("ImageCaptureError : ${exception.message}")
            }
        }

        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(savedPath).build()
        imageCapture.takePicture(outputFileOptions, executor, imageSavedCallback)
    }
}