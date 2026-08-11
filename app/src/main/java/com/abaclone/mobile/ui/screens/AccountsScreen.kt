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
                },
                actions = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.Close, contentDescription = "Close", tint = Color.White)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* edit/add account action */ },
                containerColor = AbaRed,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Filled.MoreHoriz, contentDescription = "Actions")
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
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Circular graph summary
                        Box(
                            modifier = Modifier
                               .size(110.dp)
                               .clip(CircleShape)
                               .background(DarkNavyTop),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(96.dp)
                                    .clip(CircleShape)
                                    .background(DarkNavy),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("All Accounts", color = Color.White, fontSize = 11.sp)
                                    Text("Summary", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            // Ring indicator overlay
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .clip(CircleShape)
                                    .background(Color.Transparent)
                                    .border(6.dp, TealCircle, CircleShape)
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text("Total in USD", color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp)
                            Spacer(Modifier.height(4.dp))
                            HorizontalDivider(color = Color.White.copy(alpha = 0.3f), thickness = 1.dp, modifier = Modifier.width(160.dp))
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = "$ ${"%.2f".format(currentBalance)}",
                                color = Color.White,
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(Modifier.height(10.dp))

                // Account List Card (Payroll Account)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFEFEFF4))
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
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
                                        .height(42.dp)
                                        .background(TealCircle)
                                )
                                Spacer(Modifier.width(12.dp))
                                Column {
                                    Text("Payroll Account", color = Color.Black, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                    Spacer(Modifier.height(2.dp))
                                    Text(MockData.account.accountNumber, color = Color.Gray, fontSize = 13.sp)
                                    Spacer(Modifier.height(8.dp))
                                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        BadgeTag("VISA")
                                        BadgeTag("ATM")
                                    }
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "${"%.2f".format(currentBalance)} USD",
                                    color = Color.Black,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(Modifier.height(18.dp))
                                Text("•••", color = Color.Gray, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                        }
                    }
                }
            } else {
                // Cards tab view
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
