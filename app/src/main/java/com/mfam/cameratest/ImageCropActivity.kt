package com.mfam.cameratest

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_image_crop.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

class ImageCropActivity : AppCompatActivity() {

    lateinit var uri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_crop)

        uri = intent.getParcelableExtra(EXTRA_URI)
        Logger.d("ImageCropActivity uri : ${uri.path}")

        cropImageView.setImageUriAsync(uri)
    }

    fun complete(v: View) {
//        cropImageView.setOnCropImageCompleteListener( { view, result ->
//            if (result.isSuccessful) {
//                Toast.makeText(this, "Crop Complete", Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(this, "Crop Fail", Toast.LENGTH_SHORT).show()
//            }
//        })
//        cropImageView.getCroppedImageAsync()

        val croppedImage = cropImageView.croppedImage
        bitmapToFile(croppedImage, File(cacheDir.absolutePath, "cropped.jpg"))
        Toast.makeText(this, "Crop Complete", Toast.LENGTH_SHORT).show()
    }

    private fun bitmapToFile(bitmap: Bitmap, path: File) {
        try {
            val stream: OutputStream = FileOutputStream(path)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun rotateLeft(v: View) {
        cropImageView.rotateImage(-90)
    }

    fun rotateRight(v: View) {
        cropImageView.rotateImage(90)
    }
}