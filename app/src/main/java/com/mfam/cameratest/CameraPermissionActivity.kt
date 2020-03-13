package com.mfam.cameratest

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.orhanobut.logger.Logger
import com.tedpark.tedpermission.rx2.TedRx2Permission


class CameraPermissionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_permission)
    }

    @SuppressLint("CheckResult")
    fun requestCameraPermission(v: View) {
        TedRx2Permission.with(this)
            .setPermissions(Manifest.permission.CAMERA)
            .setGotoSettingButton(false)
            .request()
            .subscribe({ permissionResult ->
                if (permissionResult.isGranted()) {
                    Logger.d("Permission Granted")
                    finish()
                } else {
                    Logger.d("Permission Denied ========= : ${permissionResult.deniedPermissions}")
                }
            }, { e -> Logger.d(e.message) })
    }

    fun startPermissionSettingActivity(v: View) {
        Intent().apply {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            data = Uri.parse("package:" + packageName)
            addCategory(Intent.CATEGORY_DEFAULT)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        }.run {

            startActivity(this)
        }
    }
}