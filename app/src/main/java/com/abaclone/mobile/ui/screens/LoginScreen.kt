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
import com.abaclone.mobile.ui.theme.AbaNavyDark
import com.abaclone.mobile.ui.theme.AbaNavyLight

private const val PIN_LENGTH = 6

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var pin by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(AbaNavyLight, AbaNavyDark))
            )
            .padding(horizontal = 24.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text("\u2190", color = Color.White, fontSize = 22.sp)
        }

        Spacer(Modifier.height(28.dp))

        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(Color(0xFFD9D9D9))
        )

        Spacer(Modifier.height(20.dp))
        Text("Enter your PIN to login", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Medium)

        Spacer(Modifier.height(22.dp))
        PinDots(length = PIN_LENGTH, filled = pin.length)

        Spacer(Modifier.weight(1f))

        Keypad(
            onKey = { key ->
                when (key) {
                    "<" -> if (pin.isNotEmpty()) pin = pin.dropLast(1)
                    "*" -> { /* alternate input key — no-op in this demo */ }
                    else -> if (pin.length < PIN_LENGTH) pin += key
                }
            }
        )

        Spacer(Modifier.height(20.dp))

        val ready = pin.length == PIN_LENGTH
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .clip(RoundedCornerShape(50))
                .background(if (ready) Color.White.copy(alpha = 0.14f) else Color.Transparent)
                .clickable(enabled = ready) { onLoginSuccess() },
            contentAlignment = Alignment.Center
        ) {
            Text("OK", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun PinDots(length: Int, filled: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
        repeat(length) { index ->
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .clip(CircleShape)
                    .background(if (index < filled) Color.White else Color.White.copy(alpha = 0.25f))
            )
        }
    }
}

@Composable
private fun Keypad(onKey: (String) -> Unit) {
    val rows = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf("*", "0", "<")
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        rows.forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(30.dp)) {
                row.forEach { key ->
                    KeypadKey(key = key, onClick = { onKey(key) })
                }
            }
        }
    }
}

@Composable
private fun KeypadKey(key: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(64.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(key, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Medium)
    }
}
