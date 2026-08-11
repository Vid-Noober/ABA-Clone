package com.abaclone.mobile.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ---------- Color palette pulled from the screenshot ----------
private val DarkNavy = Color(0xFF0B3A52)
private val DarkNavyTop = Color(0xFF0C3348)
private val AbaRed = Color(0xFFE94C4C)
private val FavoriteBlue = Color(0xFF3FA9DA)
private val IconTint = Color(0xFFEAF3F7)
private val DividerColor = Color(0xFF1C4C63)

data class GridItem(
    val label: String,
    val icon: ImageVector,
    val badge: String? = null // e.g. "E-CASH" style badge text, optional
)

@Composable
fun HomeScreen(
    onSeeAllTransactions: () -> Unit = {},
    onAccountClick: () -> Unit = {}
) {
    Scaffold(
        containerColor = DarkNavy,
        topBar = { AbaTopBar() }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            AbaFeatureGrid(onAccountClick = onAccountClick)

            Spacer(modifier = Modifier.height(0.dp))

            FavoriteBanner(
                title = "Favorite Transfers",
                subtitle = "Access your favorites here for quick transfers",
                background = FavoriteBlue,
                icon = Icons.Filled.SwapHoriz
            )

            FavoriteBanner(
                title = "Favorite Payments",
                subtitle = "Paying your bills with favorites is faster",
                background = AbaRed,
                icon = Icons.Filled.AttachMoney
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AbaTopBar() {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = DarkNavyTop,
            titleContentColor = Color.White
        ),
        navigationIcon = {
            Box {
                IconButton(onClick = { /* open drawer */ }) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Menu",
                        tint = Color.White
                    )
                }
                // small red notification dot on the menu icon
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 10.dp, end = 10.dp)
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(AbaRed)
                )
            }
        },
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "ABA",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "'",
                    color = AbaRed,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        },
        actions = {
            IconButton(onClick = { /* chat */ }) {
                Icon(Icons.Outlined.ChatBubbleOutline, contentDescription = "Chat", tint = Color.White)
            }
            IconButton(onClick = { /* notifications */ }) {
                Icon(Icons.Outlined.NotificationsNone, contentDescription = "Notifications", tint = Color.White)
            }
            IconButton(onClick = { /* cards / wallet stack */ }) {
                Icon(Icons.Outlined.ViewAgenda, contentDescription = "Cards", tint = Color.White)
            }
            Box(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(AbaRed)
                    .clickable { /* QR / scan */ },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.QrCodeScanner,
                    contentDescription = "Scan",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    )
}

@Composable
private fun AbaFeatureGrid(onAccountClick: () -> Unit) {
    val items = listOf(
        GridItem("Accounts", Icons.Outlined.AccountBalanceWallet),
        GridItem("Cards", Icons.Outlined.CreditCard),
        GridItem("Payments", Icons.Outlined.AttachMoney),
        GridItem("New Account", Icons.Outlined.Description),
        GridItem("Cash to ATM", Icons.Outlined.LocalAtm, badge = "E-CASH"),
        GridItem("Transfers", Icons.Outlined.SwapHoriz),
        GridItem("ABA Scan", Icons.Outlined.QrCodeScanner),
        GridItem("Loans", Icons.Outlined.VolunteerActivism),
        GridItem("Mini Apps", Icons.Outlined.Apps)
    )

    Column {
        items.chunked(3).forEachIndexed { rowIndex, rowItems ->
            Row(modifier = Modifier.fillMaxWidth()) {
                rowItems.forEach { item ->
                    GridCell(
                        item = item,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            if (item.label == "Accounts") {
                                onAccountClick()
                            }
                        }
                    )
                }
            }
            if (rowIndex < 2) {
                HorizontalDivider(color = DividerColor, thickness = 1.dp)
            }
        }
        HorizontalDivider(color = DividerColor, thickness = 1.dp)
    }
}

@Composable
private fun GridCell(item: GridItem, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(vertical = 30.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.label,
            tint = IconTint,
            modifier = Modifier.size(36.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = item.label,
            color = Color.White,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun FavoriteBanner(
    title: String,
    subtitle: String,
    background: Color,
    icon: ImageVector
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(172.dp)
            .background(background)
            .padding(28.dp)
    ) {
        Column(modifier = Modifier.align(Alignment.CenterStart)) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = subtitle,
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 14.sp
            )
        }

        // large faded circular icon on the right, like the screenshot
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .offset(x = 36.dp)
                .size(110.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.9f),
                modifier = Modifier.size(42.dp)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B3A52)
@Composable
private fun AbaHomeScreenPreview() {
    MaterialTheme {
        HomeScreen()
    }
}


