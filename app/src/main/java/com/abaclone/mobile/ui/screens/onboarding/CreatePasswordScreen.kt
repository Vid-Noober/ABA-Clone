package com.abaclone.mobile.ui.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abaclone.mobile.data.FirebaseRepository
import com.abaclone.mobile.data.RegistrationState
import com.abaclone.mobile.ui.components.AuthTextField
import com.abaclone.mobile.ui.components.AuthTopBar
import com.abaclone.mobile.ui.components.PrimaryAuthButton
import com.abaclone.mobile.ui.theme.AbaBlue
import com.abaclone.mobile.ui.theme.AbaCoral
import com.abaclone.mobile.ui.theme.AbaTealDark
import com.abaclone.mobile.ui.theme.AbaTealLight
import kotlinx.coroutines.delay

@Composable
fun CreatePasswordScreen(onBack: () -> Unit, onNext: () -> Unit) {
    var email by remember { mutableStateOf(RegistrationState.email) }
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    val valid = email.isNotBlank() && password.length >= 6 && password == confirm

    if (isLoading) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(AbaTealLight, AbaTealDark)))
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(80.dp))
            CircularProgressIndicator(color = AbaCoral, modifier = Modifier.size(48.dp))
            Spacer(Modifier.height(16.dp))
            Text("Creating your account...", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(AbaTealLight, AbaTealDark)))
            .padding(horizontal = 24.dp, vertical = 32.dp)
    ) {
        AuthTopBar(onBack = onBack)
        Spacer(Modifier.height(40.dp))

        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(84.dp)
                .clip(CircleShape)
                .background(AbaBlue),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.Lock, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
        }

        Spacer(Modifier.height(20.dp))
        Text(
            "Set Your Credentials",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(28.dp))
        AuthTextField(
            value = email,
            onValueChange = { email = it; RegistrationState.email = it },
            label = "Email Address"
        )
        Spacer(Modifier.height(16.dp))
        AuthTextField(
            value = password,
            onValueChange = { password = it },
            label = "Password",
            isPassword = true
        )
        Spacer(Modifier.height(16.dp))
        AuthTextField(
            value = confirm,
            onValueChange = { confirm = it },
            label = "Re-enter Password",
            isPassword = true
        )

        errorMsg?.let { msg ->
            Spacer(Modifier.height(8.dp))
            Text(msg, color = Color.Red.copy(alpha = 0.8f), fontSize = 13.sp)
        }

        Spacer(Modifier.weight(1f))
        PrimaryAuthButton(
            text = "NEXT",
            enabled = valid && !isLoading,
            onClick = {
                isLoading = true
                errorMsg = null
                RegistrationState.email = email
                FirebaseRepository.registerUser(
                    email = email,
                    password = password,
                    onSuccess = {
                        isLoading = false
                        onNext()
                    },
                    onError = { message ->
                        isLoading = false
                        errorMsg = message
                    }
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
