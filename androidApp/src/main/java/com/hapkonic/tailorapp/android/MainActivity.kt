package com.hapkonic.tailorapp.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.hapkonic.tailorapp.App
import com.hapkonic.tailorapp.data.local.ActivityHolder

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            App()
        }
    }

    override fun onResume() {
        super.onResume()
        ActivityHolder.set(this)
    }

    override fun onPause() {
        super.onPause()
        ActivityHolder.clear()
    }
}
