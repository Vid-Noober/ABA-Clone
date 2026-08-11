package com.abaclone.mobile.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abaclone.mobile.data.MockData
import com.abaclone.mobile.ui.theme.AbaMaroon
import com.abaclone.mobile.ui.theme.AbaTextPrimary
import com.abaclone.mobile.ui.theme.AbaTextSecondary

@Composable
fun QrPayScreen() {
    var tab by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F7))
            .padding(top = 24.dp)
    ) {
        Text(
            "QR Pay",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = AbaTextPrimary,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(Modifier.height(16.dp))

        TabRow(
            selectedTabIndex = tab,
            containerColor = Color.Transparent,
            contentColor = AbaMaroon
        ) {
            Tab(selected = tab == 0, onClick = { tab = 0 }, text = { Text("Scan to pay") })
            Tab(selected = tab == 1, onClick = { tab = 1 }, text = { Text("My QR code") })
        }

        Spacer(Modifier.height(28.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (tab == 0) {
                Box(
                    modifier = Modifier
                        .size(260.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF1E1E1E)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.QrCodeScanner,
                        contentDescription = "Camera viewfinder",
                        tint = Color.White.copy(alpha = 0.5f),
                        modifier = Modifier.size(80.dp)
                    )
                }
                Spacer(Modifier.height(18.dp))
                Text(
                    "Point your camera at a merchant's ABA QR\nto pay instantly",
                    fontSize = 13.sp,
                    color = AbaTextSecondary,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(260.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White)
                        .border(1.dp, Color(0xFFE7E6EA), RoundedCornerShape(20.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.QrCode2,
                        contentDescription = "Your personal QR code",
                        tint = AbaMaroon,
                        modifier = Modifier.size(140.dp)
                    )
                }
                Spacer(Modifier.height(18.dp))
                Text(MockData.account.holderName, fontWeight = FontWeight.SemiBold, color = AbaTextPrimary)
                Text("Acc. ${MockData.account.accountNumber}", fontSize = 12.sp, color = AbaTextSecondary)
                Spacer(Modifier.height(6.dp))
                Text(
                    "Share this code to receive payments",
                    fontSize = 13.sp,
                    color = AbaTextSecondary
                )
            }
        }
    }
}
