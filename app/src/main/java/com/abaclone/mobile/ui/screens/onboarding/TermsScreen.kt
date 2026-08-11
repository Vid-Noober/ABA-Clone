package com.abaclone.mobile.ui.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abaclone.mobile.ui.components.AuthTopBar
import com.abaclone.mobile.ui.components.PrimaryAuthButton
import com.abaclone.mobile.ui.theme.AbaTealDark
import com.abaclone.mobile.ui.theme.AbaTealLight

@Composable
fun TermsScreen(onBack: () -> Unit, onAgree: () -> Unit) {
    var agreeMobile by remember { mutableStateOf(false) }
    var agreeInstant by remember { mutableStateOf(false) }
    var agreeFacePass by remember { mutableStateOf(false) }
    val allAgreed = agreeMobile && agreeInstant && agreeFacePass

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(AbaTealLight, AbaTealDark)))
            .padding(horizontal = 24.dp, vertical = 32.dp)
    ) {
        AuthTopBar(onBack = onBack)
        Spacer(Modifier.height(24.dp))

        Text("ABA Mobile Terms and Conditions", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                "Please read below Terms and Conditions carefully before activating ABA Mobile application.",
                color = Color.White.copy(alpha = 0.85f), fontSize = 14.sp, lineHeight = 20.sp
            )
            Spacer(Modifier.height(14.dp))
            Text(
                "By selecting \u201cAGREE\u201d you confirmed to be bound with Terms and Conditions of ABA Mobile application and ABA instant Account.",
                color = Color.White.copy(alpha = 0.85f), fontSize = 14.sp, lineHeight = 20.sp
            )
            Spacer(Modifier.height(14.dp))
            Text(
                "If you do not agree please do not proceed further and uninstall ABA Mobile application.",
                color = Color.White.copy(alpha = 0.85f), fontSize = 14.sp, lineHeight = 20.sp
            )
        }

        Spacer(Modifier.height(16.dp))

        AgreeRow(checked = agreeMobile, text = "I have read and agree to the ABA Mobile Terms & Conditions.") { agreeMobile = !agreeMobile }
        AgreeRow(checked = agreeInstant, text = "I have read and agree to the ABA Instant Account Terms & Conditions.") { agreeInstant = !agreeInstant }
        AgreeRow(checked = agreeFacePass, text = "I have read and agree to the ABA FacePass Terms & Conditions.") { agreeFacePass = !agreeFacePass }

        Spacer(Modifier.height(12.dp))
        androidx.compose.material3.HorizontalDivider(color = Color.White.copy(alpha = 0.2f))
        Spacer(Modifier.height(12.dp))

        AgreeRow(checked = allAgreed, text = "I agree to all of the above.") {
            val newVal = !allAgreed
            agreeMobile = newVal
            agreeInstant = newVal
            agreeFacePass = newVal
        }

        Spacer(Modifier.height(20.dp))
        PrimaryAuthButton(text = "AGREE", enabled = allAgreed, onClick = onAgree)
    }
}

@Composable
private fun AgreeRow(checked: Boolean, text: String, onToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(if (checked) Color.White else Color.Transparent)
                .border(1.dp, Color.White.copy(alpha = 0.6f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (checked) {
                Icon(Icons.Filled.Check, contentDescription = null, tint = AbaTealDark, modifier = Modifier.size(14.dp))
            }
        }
        Spacer(Modifier.width(12.dp))
        Text(text, color = Color.White.copy(alpha = 0.9f), fontSize = 13.sp, lineHeight = 18.sp)
    }
}
