package com.mfam.cameratest

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.orhanobut.logger.Logger

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun startCamera(v: View) {
        if (!checkCameraPermission()) {
            return
        }

        Logger.d("Start Camera===")
        Intent(this, CameraActivity::class.java).run {
            startActivity(this)
        }
    }


}
