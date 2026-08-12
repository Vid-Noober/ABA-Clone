package com.abaclone.mobile.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val TealHeader = Color(0xFF0C3348)
private val LightBg = Color(0xFFF2F2F7)
private val TabTeal = Color(0xFF00838F)
private val CardBg = Color(0xFFFFFFFF)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(onBack: () -> Unit) {
    var selectedTab by remember { mutableStateOf(1) } // 0: My Alerts, 1: Transactions, 2: Announcements

    Scaffold(
        containerColor = LightBg,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TealHeader,
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("ABA", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White)
                        Text("'", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFFE94C4C))
                        Text(" Notifications", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Tab row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                NotificationTab("My Alerts", selectedTab == 0) { selectedTab = 0 }
                NotificationTab("Transactions", selectedTab == 1) { selectedTab = 1 }
                NotificationTab("Announcements", selectedTab == 2) { selectedTab = 2 }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (selectedTab == 1) {
                    item {
                        Spacer(Modifier.height(8.dp))
                        Text("11 AUG 2026", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                    item {
                        NotificationItem(
                            title = "CHEA DANETH",
                            time = "7:15 PM",
                            detail = "2,000.00 KHR is paid from account 006 027 576",
                            isReceived = false
                        )
                    }
                    item {
                        NotificationItem(
                            title = "CHHITH SOPHANN",
                            time = "1:54 PM",
                            detail = "1,500.00 KHR is paid from account 006 027 576",
                            isReceived = false
                        )
                    }
                    item {
                        NotificationItem(
                            title = "POL MANY",
                            time = "12:00 PM",
                            detail = "4,000.00 KHR is paid from account 006 027 576",
                            isReceived = false
                        )
                    }
                    item {
                        NotificationItem(
                            title = "Received from Chea Lina",
                            time = "11:45 AM",
                            detail = "7,700.00 KHR is received to account 006 027 576",
                            isReceived = true
                        )
                    }
                    item {
                        NotificationItem(
                            title = "MAO NIMOL",
                            time = "7:15 AM",
                            detail = "0.95 USD is paid from account 006 027 576",
                            isReceived = false
                        )
                    }
                    item {
                        Spacer(Modifier.height(4.dp))
                        Text("10 AUG 2026", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                    item {
                        NotificationItem(
                            title = "MIXUE RUPP",
                            time = "5:02 PM",
                            detail = "10,100.00 KHR is paid from account 006 027 576",
                            isReceived = false
                        )
                    }
                } else if (selectedTab == 0) {
                    item {
                        Spacer(Modifier.height(8.dp))
                        Text("02 AUG 2026", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                    item {
                        AlertNotificationItem(
                            title = "Payment Declined",
                            time = "5:50 PM",
                            detail = "VISA *6380 has insufficient funds for payment of 4.99 USD at APPLE.COM/BILL. Please check and try again."
                        )
                    }
                    item {
                        AlertNotificationItem(
                            title = "Payment Declined",
                            time = "5:48 PM",
                            detail = "VISA *6380 has insufficient funds for payment of 4.99 USD at APPLE.COM/BILL. Please check and try again."
                        )
                    }
                } else {
                    item {
                        Spacer(Modifier.height(8.dp))
                        Text("17 JUN 2026", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                    item {
                        AnnouncementCard(title = "MILLION RIELS", subtitle = "Saving in KHR can make you win big\nKeep 400,000 KHR or more in your KHR Savings A...")
                    }
                    item {
                        Spacer(Modifier.height(4.dp))
                        Text("11 MAR 2026", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                    item {
                        AnnouncementCard(title = "កម្ចីលើប្រាក់ចំនេញ", subtitle = "កម្ចីរហ័សៗ ក្នុងរយៈពេលបង់រំលែក ក្នុ ABA MOBILE\nដឹងទេ? អ្នកអាចទទួលកម្ចី សម្រាប់ការផ្ដល់ជូន ខ្លួន ដោយវិ...")
                    }
                }
                item { Spacer(Modifier.height(40.dp)) }
            }
        }
    }
}

@Composable
private fun NotificationTab(text: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (selected) TabTeal else Color.LightGray.copy(alpha = 0.4f))
            .clickable(onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (selected) Color.White else Color.DarkGray,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun NotificationItem(title: String, time: String, detail: String, isReceived: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(if (isReceived) Color(0xFFE94C4C) else TealHeader),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isReceived) Icons.Filled.Notifications else Icons.Filled.Store,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(title, color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text(time, color = Color.Gray, fontSize = 11.sp)
                }
                Spacer(Modifier.height(4.dp))
                Text(detail, color = Color.DarkGray, fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun AlertNotificationItem(title: String, time: String, detail: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(TealHeader),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Store, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(title, color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text(time, color = Color.Gray, fontSize = 11.sp)
                }
                Spacer(Modifier.height(4.dp))
                Text(detail, color = Color.DarkGray, fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun AnnouncementCard(title: String, subtitle: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Brush.horizontalGradient(listOf(Color(0xFF1976D2), Color(0xFF64B5F6)))),
                contentAlignment = Alignment.BottomStart
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(title, color = Color.White, fontWeight = FontWeight.Black, fontSize = 16.sp)
                    Text("Cash Prizes", color = Color.White.copy(alpha = 0.9f), fontSize = 12.sp)
                }
            }
            Spacer(Modifier.height(10.dp))
            Text(subtitle, color = Color.DarkGray, fontSize = 12.sp)
        }
    }
}
