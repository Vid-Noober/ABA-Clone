package com.abaclone.mobile

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.FragmentActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.abaclone.mobile.navigation.AbaNavGraph
import com.abaclone.mobile.ui.theme.AbaCloneTheme

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AbaCloneTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AbaNavGraph()
                }
            }
        }
    }
}
