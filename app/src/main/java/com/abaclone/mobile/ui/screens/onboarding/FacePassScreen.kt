package com.abaclone.mobile.ui.screens.onboarding

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.abaclone.mobile.data.RegistrationState
import com.abaclone.mobile.ui.components.AuthTopBar
import com.abaclone.mobile.ui.components.PrimaryAuthButton
import com.abaclone.mobile.ui.theme.AbaCoral
import com.abaclone.mobile.ui.theme.AbaGold
import com.abaclone.mobile.ui.theme.AbaTealDark
import com.abaclone.mobile.ui.theme.AbaTealLight
import java.io.File
import java.util.concurrent.Executor

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

            Box(
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
                textAlign = TextAlign.Center
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
        FaceScanCameraView(onBack = { scanning = false }, onDone = onDone)
    }
}

@Composable
private fun Tip(text: String) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .padding(top = 6.dp)
                .size(5.dp)
                .clip(CircleShape)
                .background(AbaGold)
        )
        Spacer(Modifier.padding(start = 10.dp))
        Text(text, color = Color.White.copy(alpha = 0.85f), fontSize = 13.sp, lineHeight = 18.sp)
    }
}

@Composable
private fun FaceScanCameraView(onBack: () -> Unit, onDone: () -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraExecutor = remember { ContextCompat.getMainExecutor(context) }

    var hasPermission by remember { mutableStateOf(false) }
    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    val imageCaptureState = remember { mutableStateOf<ImageCapture?>(null) }
    val cameraProviderState = remember { mutableStateOf<ProcessCameraProvider?>(null) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted -> hasPermission = granted }
    )

    LaunchedEffect(Unit) {
        val granted = ContextCompat.checkSelfPermission(
            context, android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        hasPermission = granted
        if (!granted) permissionLauncher.launch(android.Manifest.permission.CAMERA)
    }

    DisposableEffect(Unit) {
        onDispose {
            cameraProviderState.value?.unbindAll()
        }
    }

    when {
        !hasPermission -> {
            FacePermissionDeniedContent(
                onBack = onBack,
                onSkip = { onDone() },
                onRequestPermission = {
                    permissionLauncher.launch(android.Manifest.permission.CAMERA)
                }
            )
        }
        capturedBitmap != null -> {
            FacePhotoReviewContent(
                bitmap = capturedBitmap!!,
                onRetake = { capturedBitmap = null },
                onUsePhoto = {
                    val photoFile = File(context.cacheDir, "face_photo_" + System.currentTimeMillis() + ".jpg")
                    photoFile.writeBitmap(capturedBitmap!!)
                    RegistrationState.facePhotoPath = photoFile.absolutePath
                    capturedBitmap = null
                    onDone()
                }
            )
        }
        else -> {
            FaceCameraPreviewContent(
                lifecycleOwner = lifecycleOwner,
                cameraExecutor = cameraExecutor,
                imageCaptureState = imageCaptureState,
                cameraProviderState = cameraProviderState,
                onSkip = onDone,
                onPhotoCaptured = { bitmap -> capturedBitmap = bitmap }
            )
        }
    }
}

@Composable
private fun FacePermissionDeniedContent(onBack: () -> Unit, onSkip: () -> Unit, onRequestPermission: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(AbaTealLight, AbaTealDark)))
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AuthTopBar(onBack = onBack)
        Spacer(Modifier.height(60.dp))
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.CameraAlt,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.4f),
                modifier = Modifier.size(36.dp)
            )
        }
        Spacer(Modifier.height(20.dp))
        Text(
            "Camera Permission Needed",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(12.dp))
        Text(
            "Camera access is needed to scan your face. You can also skip this step and continue your application.",
            color = Color.White.copy(alpha = 0.75f),
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Spacer(Modifier.weight(1f))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            PrimaryAuthButton(
                text = "Request Permission",
                onClick = onRequestPermission,
                modifier = Modifier.weight(1f)
            )
            PrimaryAuthButton(
                text = "Skip for now",
                onClick = onSkip,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun FaceCameraPreviewContent(
    lifecycleOwner: androidx.lifecycle.LifecycleOwner,
    cameraExecutor: Executor,
    imageCaptureState: MutableState<ImageCapture?>,
    cameraProviderState: MutableState<ProcessCameraProvider?>,
    onSkip: () -> Unit,
    onPhotoCaptured: (Bitmap) -> Unit
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(AbaTealDark, AbaTealDark)))
    ) {
        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx).apply {
                    scaleType = PreviewView.ScaleType.FIT_CENTER
                }
                val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                cameraProviderFuture.addListener({
                    try {
                        val cameraProvider = cameraProviderFuture.get()
                        cameraProviderState.value = cameraProvider
                        val preview = Preview.Builder().build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }
                        val imageCapture = ImageCapture.Builder()
                            .setTargetRotation(previewView.display?.rotation ?: 0)
                            .build()
                        imageCaptureState.value = imageCapture
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            CameraSelector.DEFAULT_FRONT_CAMERA,
                            preview,
                            imageCapture
                        )
                    } catch (e: Exception) {
                        Toast.makeText(ctx, "Camera unavailable", Toast.LENGTH_LONG).show()
                    }
                }, cameraExecutor)
                previewView
            },
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
        )

        FaceFrame()

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Position your face inside the frame",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        TextButton(
            onClick = onSkip,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 40.dp, end = 8.dp)
        ) {
            Text("Skip for now", color = Color.White.copy(alpha = 0.75f), fontSize = 13.sp)
        }

        val captureEnabled = imageCaptureState.value != null
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        ) {
            IconButton(
                onClick = {
                    val capture = imageCaptureState.value ?: return@IconButton
                    val photoFile = File(context.cacheDir, "face_photo_" + System.currentTimeMillis() + ".jpg")
                    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
                    capture.takePicture(
                        outputOptions,
                        cameraExecutor,
                        object : ImageCapture.OnImageSavedCallback {
                            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                                val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
                                if (bitmap != null) onPhotoCaptured(bitmap)
                                else Toast.makeText(context, "Failed to load photo", Toast.LENGTH_SHORT).show()
                            }
                            override fun onError(exception: ImageCaptureException) {
                                Toast.makeText(context, "Capture failed", Toast.LENGTH_LONG).show()
                            }
                        }
                    )
                },
                enabled = captureEnabled,
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(if (captureEnabled) AbaCoral else Color.Gray.copy(alpha = 0.4f))
            ) {
                Icon(
                    imageVector = Icons.Filled.CameraAlt,
                    contentDescription = "Capture face",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@Composable
private fun FacePhotoReviewContent(
    bitmap: Bitmap,
    onRetake: () -> Unit,
    onUsePhoto: () -> Unit
) {
    val imageBitmap: ImageBitmap = bitmap.asImageBitmap()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(AbaTealLight, AbaTealDark)))
            .padding(horizontal = 24.dp, vertical = 32.dp)
    ) {
        Text(
            "Review Your Face Photo",
            color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(2.dp, AbaGold, RoundedCornerShape(16.dp))
        ) {
            Image(
                bitmap = imageBitmap,
                contentDescription = "Captured face photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(Modifier.height(16.dp))
        Text(
            "Make sure your face is clearly visible before continuing.",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(Modifier.weight(1f))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            TextButton(
                onClick = onRetake,
                modifier = Modifier
                    .weight(1f)
                    .height(54.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Retake", color = Color.White.copy(alpha = 0.75f), fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
            PrimaryAuthButton(text = "Use This Photo", onClick = onUsePhoto, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun FaceFrame(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .drawBehind {
                val strokeWidth = 3f
                val bracketLength = 36f
                val frameWidth = size.width * 0.55f
                val frameHeight = frameWidth * 1.3f
                val left = (size.width - frameWidth) / 2
                val top = (size.height - frameHeight) / 2
                val right = left + frameWidth
                val bottom = top + frameHeight
                drawOval(
                    color = Color.White.copy(alpha = 0.5f),
                    style = Stroke(width = strokeWidth),
                    topLeft = Offset(left, top),
                    size = Size(frameWidth, frameHeight)
                )
                val bracketColor = AbaGold
                drawLine(bracketColor, Offset(left, top), Offset(left + bracketLength, top), strokeWidth, cap = StrokeCap.Round)
                drawLine(bracketColor, Offset(left, top), Offset(left, top + bracketLength), strokeWidth, cap = StrokeCap.Round)
                drawLine(bracketColor, Offset(right, top), Offset(right - bracketLength, top), strokeWidth, cap = StrokeCap.Round)
                drawLine(bracketColor, Offset(right, top), Offset(right, top + bracketLength), strokeWidth, cap = StrokeCap.Round)
                drawLine(bracketColor, Offset(left, bottom), Offset(left + bracketLength, bottom), strokeWidth, cap = StrokeCap.Round)
                drawLine(bracketColor, Offset(left, bottom), Offset(left, bottom - bracketLength), strokeWidth, cap = StrokeCap.Round)
                drawLine(bracketColor, Offset(right, bottom), Offset(right - bracketLength, bottom), strokeWidth, cap = StrokeCap.Round)
                drawLine(bracketColor, Offset(right, bottom), Offset(right, bottom - bracketLength), strokeWidth, cap = StrokeCap.Round)
            }
    )
}

private fun File.writeBitmap(bmp: Bitmap) {
    outputStream().use { out ->
        bmp.compress(Bitmap.CompressFormat.JPEG, 90, out)
        out.flush()
    }
}
