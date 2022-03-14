package com.jokopriyono.belajarmvvm.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.jokopriyono.belajarmvvm.databinding.ActivityUploadBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import kotlin.math.roundToInt
import kotlin.math.sqrt

@AndroidEntryPoint
class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding
    private val requestGetPhoto = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedUri: Uri? = result.data?.data
            selectedUri?.let { uri ->
                binding.imgPreview.setImageURI(uri)
                createFileBeforeUpload(uri)
            }
        } else {
            Toast.makeText(
                this,
                "Pemilihan gambar dibatalkan",
                Toast.LENGTH_LONG
            ).show()
        }
    }
    private val viewModel: UploadViewModel by viewModels()

    private fun createFileBeforeUpload(uri: Uri) {
        CoroutineScope(Dispatchers.IO).launch {
            kotlin.runCatching {
                try {
                    val targetOriginal =
                        BitmapFactory.decodeStream(contentResolver.openInputStream(uri))
                    val targetCompressed = reduceBitmapSize(targetOriginal)
                    val bos = ByteArrayOutputStream()
                    targetCompressed.compress(Bitmap.CompressFormat.JPEG, 0, bos)
                    val bitmapData = bos.toByteArray()
                    val file = File(
                        filesDir.path,
                        "${System.currentTimeMillis()}_compressed.jpeg"
                    )
                    file.createNewFile()
                    val fos = FileOutputStream(file)
                    fos.write(bitmapData)
                    fos.flush()
                    fos.close()

                    // lempar file ke view model untuk diupload retrofit
                    viewModel.uploadImageToServer(file)
                } catch (e: Exception) {
                    runOnUiThread {
                        Toast.makeText(
                            this@UploadActivity,
                            "Terjadi kesalahan saat menulis file",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    private fun reduceBitmapSize(bitmap: Bitmap, MAX_SIZE: Int = 360000): Bitmap {
        val ratioSquare: Double
        val bitmapHeight: Int = bitmap.height
        val bitmapWidth: Int = bitmap.width
        ratioSquare = (bitmapHeight * bitmapWidth / MAX_SIZE).toDouble()
        if (ratioSquare <= 1) return bitmap
        val ratio = sqrt(ratioSquare)
        val requiredHeight = (bitmapHeight / ratio).roundToInt()
        val requiredWidth = (bitmapWidth / ratio).roundToInt()
        return Bitmap.createScaledBitmap(bitmap, requiredWidth, requiredHeight, true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSelectAndUpload.setOnClickListener {
            requestAccessForFile()
        }

        setupObserver()
    }

    private fun setupObserver() {
        viewModel.loading.observe(this) {
            binding.loading.visibility =
                if (it) View.VISIBLE
                else View.GONE
        }
        viewModel.message.observe(this) {
            Toast.makeText(
                this@UploadActivity,
                it,
                Toast.LENGTH_LONG
            ).show()
        }
        viewModel.uploadResponse.observe(this) {
            binding.txtResult.text = it.toString()
        }
    }

    private fun requestAccessForFile() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                777
            )
        } else {
            selectFileForUpload()
        }

    }

    private fun selectFileForUpload() {
        val i = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        requestGetPhoto.launch(Intent.createChooser(i, "Select Picture"))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 777) {
            if (
                grantResults.isNotEmpty() && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED
            ) {
                selectFileForUpload()
            } else {
                Toast.makeText(
                    this,
                    "Aplikasi memerlukan izin anda",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

}