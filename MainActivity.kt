package com.amimageeditoreditlikepro

import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.ImageView
import android.net.Uri
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    
    private val Imagepicker = registerForActivityResult(ActivityResultContracts.GetContent()){uri: Uri? ->
        if (uri != null){
            
            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("image_uri", uri.toString())
            startActivity (intent)
        }
    }
    
    
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        val setting_icon = findViewById<ImageView>(R.id.settingsIcon)
        val card = findViewById<LinearLayout>(R.id.pick_card)
        
        card.setOnClickListener {
            Imagepicker.launch("image/*")
        }
        
        setting_icon.setOnClickListener{
            val intent_1 = Intent(this,ThardActivity::class.java)
            startActivity(intent_1)
        }
        
    }
}
