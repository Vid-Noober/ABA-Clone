package com.abaclone.mobile.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abaclone.mobile.data.FirebaseRepository
import com.abaclone.mobile.data.MockData
import com.abaclone.mobile.data.RegistrationState

private val DarkNavyTop = Color(0xFF0C3348)
private val DarkNavy = Color(0xFF0B3A52)
private val TealCircle = Color(0xFF00BCD4)
private val AbaRed = Color(0xFFE94C4C)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountsScreen(onBack: () -> Unit) {
    var selectedTab by remember { mutableStateOf(0) }
    var balance by remember { mutableStateOf<Double?>(null) }

    LaunchedEffect(Unit) {
        FirebaseRepository.getCurrentUserBalance { b ->
            if (b != null) balance = b
        }
    }

    val currentBalance = balance ?: MockData.account.balance

    Scaffold(
        containerColor = DarkNavy,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkNavyTop,
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                title = {
                    Text("ABA Accounts", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* add account */ },
                containerColor = AbaRed,
                contentColor = Color.White,
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("+", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Text("Account", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Tabs: ACCOUNTS | CARDS
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkNavyTop)
            ) {
                TabItem(
                    title = "ACCOUNTS",
                    selected = selectedTab == 0,
                    modifier = Modifier.weight(1f),
                    onClick = { selectedTab = 0 }
                )
                TabItem(
                    title = "CARDS",
                    selected = selectedTab == 1,
                    modifier = Modifier.weight(1f),
                    onClick = { selectedTab = 1 }
                )
            }

            if (selectedTab == 0) {
                // Accounts Summary Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(DarkNavy)
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Circular graph summary with ring indicator
                        Box(
                            modifier = Modifier
                                .size(110.dp)
                                .clip(CircleShape)
                                .background(DarkNavyTop),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(92.dp)
                                    .clip(CircleShape)
                                    .background(DarkNavy),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Filled.MoreHoriz, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(18.dp))
                                    Spacer(Modifier.height(2.dp))
                                    Text("All Accounts", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .clip(CircleShape)
                                    .background(Color.Transparent)
                                    .border(6.dp, Color(0xFFC77DF3), CircleShape)
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text("Available Balance", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = "$ ${"%.2f".format(currentBalance)}",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(8.dp))
                            HorizontalDivider(color = Color.White.copy(alpha = 0.3f), thickness = 1.dp, modifier = Modifier.width(160.dp))
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = "៛ ${"%,.2f".format(currentBalance * 4100)}",
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                // Analytics banner bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(DarkNavyTop.copy(alpha = 0.6f))
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Analytics", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        Spacer(Modifier.width(4.dp))
                        Text("▼", color = Color.White, fontSize = 10.sp)
                    }
                }

                Spacer(Modifier.height(8.dp))

                // Account Cards List with top rounded corners container
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                        .background(Color(0xFFEFEFF4))
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .width(4.dp)
                                            .height(48.dp)
                                            .background(TealCircle)
                                    )
                                    Spacer(Modifier.width(12.dp))
                                    Column {
                                        Text("Savings", color = Color.Black, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                        Spacer(Modifier.height(2.dp))
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text("006 027 575 | Savings  ", color = Color.Gray, fontSize = 12.sp)
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(4.dp))
                                                    .background(Color(0xFF00B0FF))
                                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                            ) {
                                                Text("Default", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                        Spacer(Modifier.height(8.dp))
                                        BadgeTag("VISA")
                                    }
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "${"%.2f".format(currentBalance)} USD",
                                        color = Color.Black,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(Modifier.height(22.dp))
                                    Text("•••", color = Color.Gray, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                }
                            }
                        }

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .width(4.dp)
                                            .height(48.dp)
                                            .background(Color(0xFF9C27B0))
                                    )
                                    Spacer(Modifier.width(12.dp))
                                    Column {
                                        Text("Savings", color = Color.Black, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                        Spacer(Modifier.height(2.dp))
                                        Text("006 027 576 | Savings", color = Color.Gray, fontSize = 12.sp)
                                    }
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "${"%,.2f".format(currentBalance * 4100)} KHR",
                                        color = Color.Black,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(Modifier.height(22.dp))
                                    Text("•••", color = Color.Gray, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                }
                            }
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFEFEFF4))
                        .padding(20.dp)
                ) {
                    Text("Cards linked to your account", color = Color.Gray, fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
private fun TabItem(title: String, selected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            color = if (selected) Color.White else Color.White.copy(alpha = 0.6f),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
        Spacer(Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(3.dp)
                .background(if (selected) AbaRed else Color.Transparent)
        )
    }
}

@Composable
private fun BadgeTag(text: String) {
    Box(
        modifier = Modifier
            .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(text, color = Color.DarkGray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
    }
}
