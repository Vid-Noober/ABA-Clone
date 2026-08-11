package com.abaclone.mobile.ui.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abaclone.mobile.ui.components.AuthTopBar
import com.abaclone.mobile.ui.components.PrimaryAuthButton
import com.abaclone.mobile.ui.theme.AbaTealDark
import com.abaclone.mobile.ui.theme.AbaTealLight

@Composable
fun PhoneEntryScreen(onBack: () -> Unit, onNext: (String) -> Unit) {
    var phone by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(AbaTealLight, AbaTealDark)))
            .padding(horizontal = 24.dp, vertical = 32.dp)
    ) {
        AuthTopBar(onBack = onBack)
        Spacer(Modifier.height(48.dp))

        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(84.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.Call, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
        }

        Spacer(Modifier.height(20.dp))
        Text(
            "Enter your Phone Number",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "The SIM card linked to your ABA-registered number must be in this device.",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 13.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(horizontal = 16.dp)
        )

        Spacer(Modifier.height(28.dp))

        Row(verticalAlignment = Alignment.Bottom) {
            OutlinedTextField(
                value = "+ 855",
                onValueChange = {},
                readOnly = true,
                label = { Text("Country Code", color = Color.White.copy(alpha = 0.7f)) },
                modifier = Modifier.width(96.dp),
                colors = fieldColors()
            )
            Spacer(Modifier.width(12.dp))
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it.filter { c -> c.isDigit() } },
                label = { Text("Phone Number", color = Color.White.copy(alpha = 0.7f)) },
                singleLine = true,
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                colors = fieldColors()
            )
        }

        Spacer(Modifier.height(10.dp))
        Text(
            "Your phone number must be registered with ABA Bank. If not, please visit the nearest branch to update your personal details.",
            color = Color.White.copy(alpha = 0.6f),
            fontSize = 12.sp,
            lineHeight = 17.sp
        )

        Spacer(Modifier.weight(1f))
        PrimaryAuthButton(text = "NEXT", enabled = phone.length >= 8, onClick = { onNext(phone) })
    }
}

@Composable
private fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    focusedBorderColor = Color.White,
    unfocusedBorderColor = Color.White.copy(alpha = 0.4f),
    focusedLabelColor = Color.White,
    unfocusedLabelColor = Color.White.copy(alpha = 0.6f),
    cursorColor = Color.White
)
