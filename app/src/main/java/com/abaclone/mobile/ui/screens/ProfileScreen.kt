package com.abaclone.mobile.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abaclone.mobile.data.MockData
import com.abaclone.mobile.ui.theme.AbaMaroon
import com.abaclone.mobile.ui.theme.AbaRed
import com.abaclone.mobile.ui.theme.AbaTextPrimary
import com.abaclone.mobile.ui.theme.AbaTextSecondary

private data class SettingsRow(val label: String, val icon: ImageVector, val destructive: Boolean = false)

private val rows = listOf(
    SettingsRow("Personal information", Icons.Filled.Person),
    SettingsRow("Security & PIN", Icons.Filled.Lock),
    SettingsRow("Notifications", Icons.Filled.Notifications),
    SettingsRow("Language", Icons.Filled.Language),
    SettingsRow("Help & support", Icons.Filled.HelpOutline),
    SettingsRow("Log out", Icons.AutoMirrored.Filled.Logout, destructive = true)
)

@Composable
fun ProfileScreen(onLogout: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F7))
            .padding(horizontal = 20.dp)
    ) {
        Spacer(Modifier.height(24.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(AbaMaroon.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(MockData.account.holderName.take(1), color = AbaMaroon, fontWeight = FontWeight.Bold, fontSize = 22.sp)
            }
            Spacer(Modifier.height(0.dp))
            Column(modifier = Modifier.padding(start = 14.dp)) {
                Text(MockData.account.holderName, fontSize = 17.sp, fontWeight = FontWeight.SemiBold, color = AbaTextPrimary)
                Text("Acc. ${MockData.account.accountNumber}", fontSize = 13.sp, color = AbaTextSecondary)
            }
        }

        Spacer(Modifier.height(28.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
        ) {
            rows.forEachIndexed { index, row ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { if (row.destructive) onLogout() }
                        .padding(horizontal = 16.dp, vertical = 15.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            row.icon,
                            contentDescription = null,
                            tint = if (row.destructive) AbaRed else AbaMaroon,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            row.label,
                            fontSize = 14.sp,
                            color = if (row.destructive) AbaRed else AbaTextPrimary,
                            modifier = Modifier.padding(start = 14.dp)
                        )
                    }
                    if (!row.destructive) {
                        Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = AbaTextSecondary)
                    }
                }
                if (index != rows.lastIndex) {
                    androidx.compose.material3.HorizontalDivider(color = Color(0xFFE7E6EA))
                }
            }
        }
    }
}
