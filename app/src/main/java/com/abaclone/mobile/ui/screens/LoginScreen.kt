package com.abaclone.mobile.ui.screens

import android.widget.Toast
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
import androidx.compose.material.icons.filled.Face
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import com.abaclone.mobile.ui.theme.AbaGold
import com.abaclone.mobile.ui.theme.AbaNavyDark
import com.abaclone.mobile.ui.theme.AbaNavyLight
import com.abaclone.mobile.util.BiometricAuthHelper

private const val PIN_LENGTH = 6

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var pin by remember { mutableStateOf("") }
    val context = LocalContext.current
    val activity = context as? FragmentActivity

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
                .background(Color(0xFFD9D9D9)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Face,
                contentDescription = "User avatar",
                tint = Color.Gray,
                modifier = Modifier.size(48.dp)
            )
        }

        Spacer(Modifier.height(20.dp))
        Text("Enter your PIN to login", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Medium)

        Spacer(Modifier.height(22.dp))
        PinDots(length = PIN_LENGTH, filled = pin.length)

        Spacer(Modifier.weight(1f))

        // Face Scan Button
        if (BiometricAuthHelper.isBiometricAvailable(context)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(50))
                    .background(AbaGold)
                    .clickable {
                        activity?.let { act ->
                            BiometricAuthHelper.showBiometricPrompt(
                                activity = act,
                                title = "Face Scan Login",
                                subtitle = "Verify your identity using face recognition",
                                onSuccess = {
                                    Toast.makeText(context, "Face recognized!", Toast.LENGTH_SHORT).show()
                                    onLoginSuccess()
                                },
                                onError = { errorCode, errString ->
                                    Toast.makeText(context, "Error: $errString", Toast.LENGTH_SHORT).show()
                                },
                                onFailed = {
                                    Toast.makeText(context, "Face not recognized. Try again.", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Face,
                        contentDescription = "Face scan",
                        tint = AbaNavyDark,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        "Login with Face Scan",
                        color = AbaNavyDark,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // Divider with "OR"
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp)
                        .background(Color.White.copy(alpha = 0.3f))
                )
                Text(
                    "  OR  ",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 12.sp
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp)
                        .background(Color.White.copy(alpha = 0.3f))
                )
            }

            Spacer(Modifier.height(12.dp))
        }

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
