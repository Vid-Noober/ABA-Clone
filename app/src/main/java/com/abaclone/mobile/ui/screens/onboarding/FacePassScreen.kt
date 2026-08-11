package com.abaclone.mobile.ui.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abaclone.mobile.ui.components.AuthTopBar
import com.abaclone.mobile.ui.components.PrimaryAuthButton
import com.abaclone.mobile.ui.theme.AbaGold
import com.abaclone.mobile.ui.theme.AbaTealDark
import com.abaclone.mobile.ui.theme.AbaTealLight
import kotlinx.coroutines.delay

@Composable
fun FacePassScreen(onBack: () -> Unit, onDone: () -> Unit) {
    var scanning by remember { mutableStateOf(false) }

    if (!scanning) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(AbaTealLight, AbaTealDark)))
                .padding(horizontal = 24.dp, vertical = 32.dp)
        ) {
            AuthTopBar(onBack = onBack)
            Spacer(Modifier.height(28.dp))

            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Face, contentDescription = null, tint = AbaGold, modifier = Modifier.size(40.dp))
            }

            Spacer(Modifier.height(20.dp))
            Text(
                "Prepare to Scan Your",
                color = Color.White, fontSize = 19.sp, fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                "Face (FacePass)",
                color = Color.White, fontSize = 19.sp, fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(20.dp))
            Text(
                "FacePass is advanced facial recognition technology designed to make your banking with ABA even more secure.",
                color = Color.White.copy(alpha = 0.8f), fontSize = 13.sp, lineHeight = 19.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(Modifier.height(24.dp))
            Tip("You must be the account owner")
            Spacer(Modifier.height(14.dp))
            Tip("Ensure your camera lens is clean")
            Spacer(Modifier.height(14.dp))
            Tip("Choose a well-lit space with a clear background")
            Spacer(Modifier.height(14.dp))
            Tip("Keep your face visible and avoid wearing masks, hats, or sunglasses")

            Spacer(Modifier.weight(1f))
            PrimaryAuthButton(text = "GET STARTED", onClick = { scanning = true })
        }
    } else {
        FaceScanningView(onDone = onDone)
    }
}

@Composable
private fun Tip(text: String) {
    Row(verticalAlignment = Alignment.Top) {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .padding(top = 6.dp)
                .size(5.dp)
                .clip(CircleShape)
                .background(AbaGold)
        )
        androidx.compose.foundation.layout.Spacer(Modifier.padding(start = 10.dp))
        Text(text, color = Color.White.copy(alpha = 0.85f), fontSize = 13.sp, lineHeight = 18.sp)
    }
}

@Composable
private fun FaceScanningView(onDone: () -> Unit) {
    var secondsLeft by remember { mutableIntStateOf(29) }
    var ready by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(1500)
        ready = true
    }
    LaunchedEffect(ready) {
        while (secondsLeft > 0 && !ready) {
            delay(1000)
            secondsLeft -= 1
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(AbaTealLight, AbaTealDark)))
            .padding(horizontal = 24.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(40.dp))
        ViewfinderFrame(size = 240.dp)
        Spacer(Modifier.height(16.dp))
        Text("00:%02d".format(secondsLeft), color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp)

        Spacer(Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(28.dp)) {
            FaceRule(Icons.Filled.Block, "No sunglasses")
            FaceRule(Icons.Filled.VisibilityOff, "No mask")
            FaceRule(Icons.Filled.Face, "No Half-Face")
        }

        Spacer(Modifier.weight(1f))
        PrimaryAuthButton(text = "Done", enabled = ready, onClick = onDone)
    }
}

@Composable
private fun FaceRule(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = label, tint = Color.White.copy(alpha = 0.7f), modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.height(6.dp))
        Text(label, color = Color.White.copy(alpha = 0.7f), fontSize = 10.sp)
    }
}

@Composable
private fun ViewfinderFrame(size: Dp) {
    val bracketLength = 24f
    val strokeWidth = 3f
    androidx.compose.foundation.layout.Box(
        modifier = Modifier
            .size(size)
            .drawBehind {
                val px = size.toPx()
                val c = AbaGold
                drawLine(c, Offset(0f, 0f), Offset(bracketLength, 0f), strokeWidth, cap = StrokeCap.Round)
                drawLine(c, Offset(0f, 0f), Offset(0f, bracketLength), strokeWidth, cap = StrokeCap.Round)
                drawLine(c, Offset(px, 0f), Offset(px - bracketLength, 0f), strokeWidth, cap = StrokeCap.Round)
                drawLine(c, Offset(px, 0f), Offset(px, bracketLength), strokeWidth, cap = StrokeCap.Round)
                drawLine(c, Offset(0f, px), Offset(bracketLength, px), strokeWidth, cap = StrokeCap.Round)
                drawLine(c, Offset(0f, px), Offset(0f, px - bracketLength), strokeWidth, cap = StrokeCap.Round)
                drawLine(c, Offset(px, px), Offset(px - bracketLength, px), strokeWidth, cap = StrokeCap.Round)
                drawLine(c, Offset(px, px), Offset(px, px - bracketLength), strokeWidth, cap = StrokeCap.Round)
            }
    )
}
