package com.abaclone.mobile.ui.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abaclone.mobile.data.RegistrationState
import com.abaclone.mobile.ui.components.AuthTextField
import com.abaclone.mobile.ui.components.AuthTopBar
import com.abaclone.mobile.ui.components.OutlinedAuthButton
import com.abaclone.mobile.ui.components.PrimaryAuthButton
import com.abaclone.mobile.ui.theme.AbaCoral
import com.abaclone.mobile.ui.theme.AbaGold
import com.abaclone.mobile.ui.theme.AbaTealDark
import com.abaclone.mobile.ui.theme.AbaTealLight
import kotlinx.coroutines.delay

@Composable
fun DocumentDetailsScreen(onBack: () -> Unit, onTryAgain: () -> Unit, onSubmit: () -> Unit) {
    var firstName by remember { mutableStateOf("Takku") }
    var lastName by remember { mutableStateOf("Kim") }
    var dateOfBirth by remember { mutableStateOf("31 / 12 / 1998") }
    var verifying by remember { mutableStateOf(false) }
    var verified by remember { mutableStateOf(false) }

    LaunchedEffect(verifying) {
        if (verifying) {
            delay(1500)
            RegistrationState.fullName = "$firstName $lastName"
            RegistrationState.dateOfBirth = dateOfBirth
            RegistrationState.idNumber = "XXX XXX XXX"
            RegistrationState.kycVerified = true
            verified = true
            delay(800)
            onSubmit()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(AbaTealLight, AbaTealDark)))
            .padding(horizontal = 24.dp, vertical = 32.dp)
    ) {
        AuthTopBar(onBack = onBack)
        Spacer(Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(90.dp)
                .clip(CircleShape)
                .background(AbaCoral.copy(alpha = 0.85f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(40.dp))
        }

        if (verifying) {
            Spacer(Modifier.height(16.dp))
            Text(
                if (verified) "Identity verified successfully" else "Verifying your identity...",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            if (!verified) {
                Spacer(Modifier.height(12.dp))
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(20.dp)
                )
            }
        } else {
            Spacer(Modifier.height(16.dp))
            Text(
                "Document Details",
                color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                "Please verify your document details below, correcting any fields as needed.",
                color = Color.White.copy(alpha = 0.75f), fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(horizontal = 20.dp, vertical = 6.dp)
            )

            Spacer(Modifier.height(20.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.08f))
                    .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(16.dp))
                    .padding(18.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    DetailField("Cambodian NID", "XXX XXX XXX")
                    DetailField("Expiry Date", "XX XXX XXXX")
                }
                Spacer(Modifier.height(16.dp))
                AuthTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = "First Name",
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
                AuthTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = "Last Name",
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(16.dp))
                AuthTextField(
                    value = dateOfBirth,
                    onValueChange = { dateOfBirth = it },
                    label = "Date of Birth",
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    DetailField("Gender", "Male")
                }
            }

            Spacer(Modifier.weight(1f))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedAuthButton(text = "Try Again!", onClick = onTryAgain, modifier = Modifier.weight(1f))
                PrimaryAuthButton(text = "Submit", onClick = { verifying = true }, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun DetailField(label: String, value: String) {
    Column {
        Text(label, color = AbaGold, fontSize = 11.sp)
        Text(value, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    }
}
