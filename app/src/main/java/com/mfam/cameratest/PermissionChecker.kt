package com.mfam.cameratest

import android.Manifest
import android.app.Activity
import android.content.Intent
import com.tedpark.tedpermission.rx2.TedRx2Permission

fun Activity.checkCameraPermission() : Boolean {
    if (!TedRx2Permission.isGranted(this, Manifest.permission.CAMERA)) {
        startActivity(Intent(this, CameraPermissionActivity::class.java))
        return false
    } else {
        return true
    }
}