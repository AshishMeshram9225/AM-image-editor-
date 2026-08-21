package com.amimageeditoreditlikepro

import android.content.Intent
import android.os.Bundle
import android.net.Uri
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.secondactivity)

        val button = findViewById<ImageView>(R.id.btnBack)
        val imageView = findViewById<ImageView>(R.id.imagePreview)
        
        val radiojpg = findViewById<RadioButton>(R.id.radiobutton2)
        val radiopng = findViewById<RadioButton>(R.id.radiobutton1)
        val radiowebp = findViewById<RadioButton>(R.id.radiobutton4)
        
        val convertButton = findViewById<Button>(R.id.saveButton)
        val currentFormat = findViewById<TextView>(R.id.Format)
        val settingButton = findViewById<ImageView>(R.id.settingsIcon)
        
        
        settingButton.setOnClickListener {
            val intent = Intent(this, ThardActivity::class.java)
            startActivity (intent)
        }
        
        val imageUriString = intent.getStringExtra("image_uri")

        if (imageUriString == null) {
            Toast.makeText(this, "Image not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        
        val imageUri = Uri.parse(imageUriString)
        imageView.setImageURI(imageUri)
        
        var currentExtension = "Unknown"
        
        contentResolver.query(imageUri, null, null, null, null)?.use {cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            
            if (cursor.moveToFirst() && nameIndex != -1) {
                val fileName = cursor.getString(nameIndex)
                
                if (fileName.contains(".")) {
                    currentExtension = fileName.substringAfterLast(".").uppercase() 
                }
            }
        }
        
        currentFormat.text = "$currentExtension"
        
        button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        

        convertButton.setOnClickListener {

            // Koi bhi format select nahi hai
            if (!radiojpg.isChecked && !radiopng.isChecked && !radiowebp.isChecked) {
                Toast.makeText(this, "Select Format", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            val inputStream = contentResolver.openInputStream(imageUri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

                if (bitmap == null) {
                    Toast.makeText(this, "Unable to load image", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // Format ke according values
                val format: Bitmap.CompressFormat
                val extension: String
                val mimeType: String

                if (radiojpg.isChecked) {
                    format = Bitmap.CompressFormat.JPEG
                    extension = "jpg"
                    mimeType = "image/jpg"
                    
                } else if (radiopng.isChecked) {
                    format = Bitmap.CompressFormat.PNG
                    extension = "png"
                    mimeType = "image/png"
                    
                } else {
                    // WEBP
                    format = Bitmap.CompressFormat.WEBP_LOSSY
                    extension = "webp"
                    mimeType = "image/webp"
                }

                val fileName = "Convert_${System.currentTimeMillis()}.$extension"

                val values = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                    put(MediaStore.Images.Media.MIME_TYPE, mimeType)
                    put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Converted")
                }

                val outputUri = contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    values
                )

                if (outputUri != null) {

                    val outputStream = contentResolver.openOutputStream(outputUri)

                    val success = outputStream?.use {
                        bitmap.compress(format, 100, it)
                    } ?: false

                    if (success) {
                        Toast.makeText(this, "Image converted successfully", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this, MainActivity::class.java)
                        intent.addFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                        )
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Image save failed", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    Toast.makeText(this, "Image save failed", Toast.LENGTH_SHORT).show()
                }
                
        }
    }
}