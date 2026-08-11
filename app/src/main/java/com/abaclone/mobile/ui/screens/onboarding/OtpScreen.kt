package com.abaclone.mobile.ui.screens.onboarding

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abaclone.mobile.data.RegistrationState
import com.abaclone.mobile.ui.components.AuthTopBar
import com.abaclone.mobile.ui.components.PrimaryAuthButton
import com.abaclone.mobile.ui.theme.AbaCoral
import com.abaclone.mobile.ui.theme.AbaTealDark
import com.abaclone.mobile.ui.theme.AbaTealLight
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

@Composable
fun OtpScreen(onBack: () -> Unit, onNext: () -> Unit) {
    val context = LocalContext.current
    val activity = context as? Activity

    var code by remember { mutableStateOf("") }
    var verificationId by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var secondsLeft by remember { mutableIntStateOf(60) }

    // Countdown timer for resend
    LaunchedEffect(secondsLeft) {
        if (secondsLeft > 0) {
            delay(1000)
            secondsLeft -= 1
        }
    }

    // Trigger OTP send on initial load
    LaunchedEffect(Unit) {
        if (activity != null && RegistrationState.phone.isNotBlank()) {
            val formattedPhone = "+855${RegistrationState.phone.trimStart('0', '+', '8', '5', '5')}"
            val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                .setPhoneNumber(formattedPhone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        val smsCode = credential.smsCode
                        if (!smsCode.isNullOrBlank()) {
                            code = smsCode
                        }
                        isLoading = false
                    }

                    override fun onVerificationFailed(e: FirebaseException) {
                        isLoading = false
                        errorMessage = e.localizedMessage ?: "Verification failed"
                    }

                    override fun onCodeSent(
                        verId: String,
                        token: PhoneAuthProvider.ForceResendingToken
                    ) {
                        verificationId = verId
                        isLoading = false
                    }
                })
                .build()

            isLoading = true
            PhoneAuthProvider.verifyPhoneNumber(options)
        } else {
            errorMessage = "Invalid phone number or activity context"
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

        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(84.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.9f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.Sms, contentDescription = null, tint = AbaTealDark, modifier = Modifier.size(32.dp))
        }

        Spacer(Modifier.height(20.dp))
        Text(
            "SMS Code Verification",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(8.dp))
        Text(
            "Enter the 6-digit code sent to +855 ${RegistrationState.phone}",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 13.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(32.dp))

        OutlinedTextField(
            value = code,
            onValueChange = { if (it.length <= 6 && it.all(Char::isDigit)) code = it },
            label = { Text("Enter 6-digit OTP", color = Color.White.copy(alpha = 0.7f)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White.copy(alpha = 0.4f),
                cursorColor = Color.White
            )
        )

        Spacer(Modifier.height(12.dp))
        if (secondsLeft > 0) {
            Text("Resend code in ${secondsLeft}s", color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
        } else {
            TextButton(onClick = { secondsLeft = 60 }) {
                Text("Resend Code", color = Color.White)
            }
        }

        errorMessage?.let { msg ->
            Spacer(Modifier.height(8.dp))
            Text(msg, color = AbaCoral, fontSize = 13.sp)
        }

        if (isLoading) {
            Spacer(Modifier.height(16.dp))
            CircularProgressIndicator(color = Color.White, modifier = Modifier.align(Alignment.CenterHorizontally))
        }

        Spacer(Modifier.weight(1f))

        PrimaryAuthButton(
            text = "VERIFY",
            enabled = code.length == 6 && verificationId != null && !isLoading,
            onClick = {
                val verId = verificationId
                if (verId != null) {
                    isLoading = true
                    errorMessage = null
                    val credential = PhoneAuthProvider.getCredential(verId, code)
                    FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener { task ->
                            isLoading = false
                            if (task.isSuccessful) {
                                onNext()
                            } else {
                                errorMessage = task.exception?.localizedMessage ?: "Invalid verification code"
                            }
                        }
                }
            }
        )
    }
}

