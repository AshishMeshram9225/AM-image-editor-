package com.amimageeditoreditlikepro

import android.app.IntentService
import android.os.Bundle
import android.content.Intent
import android.widget.ImageView
import android.net.Uri
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity


class ThardActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState : Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.thard_activity)
        
        val BackButton = findViewById<ImageView>(R.id.backBtn)
        val contactButton = findViewById<LinearLayout>(R.id.contactid)
        val privacyButton = findViewById<LinearLayout>(R.id.privacypolicyid)
        val shareAppButton = findViewById<LinearLayout>(R.id.shareAppButton)
        val rateApp = findViewById<LinearLayout>(R.id.rateAppButton)
        
        rateApp.setOnClickListener {
            val appPackageName = packageName
            
            try {
                startActivity (
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=$appPackageName&reviewId=0")
                    )
                )
            } catch(e: Exception) {
                startActivity (
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                    )
                )
            }
        }
        
        
        
        
        shareAppButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            
            shareIntent.putExtra(
                Intent.EXTRA_TEXT, "download AM Image Editor:\nhttps://play.google.com/store/apps/details?id=com.amimageeditoreditlikepro"
            )
            startActivity(Intent.createChooser(shareIntent, "Share AM Image Editor"))
        }
        
        
        
        contactButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply{
                data = Uri.parse("mailto:amimageeditor.support@gmail.com")
            }
            startActivity (intent)
        }
        
        BackButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity (intent)
        }
        
        privacyButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW,
                Uri.parse("https://sites.google.com/view/amimageeditor-privacy-policy/%E0%A4%AE%E0%A4%96%E0%A4%AF%E0%A4%AA%E0%A4%B7%E0%A4%A0")
            )
            startActivity (intent)
        }
    }
}
