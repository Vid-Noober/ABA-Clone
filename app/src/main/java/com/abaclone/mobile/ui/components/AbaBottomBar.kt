package com.abaclone.mobile.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.abaclone.mobile.navigation.AbaDestination
import com.abaclone.mobile.ui.theme.AbaGold
import com.abaclone.mobile.ui.theme.AbaHomeBgBottom

private data class BottomTab(val destination: AbaDestination, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)

private val tabs = listOf(
    BottomTab(AbaDestination.Home, "Home", Icons.Filled.Home),
    BottomTab(AbaDestination.Cards, "Cards", Icons.Filled.CreditCard),
    BottomTab(AbaDestination.QrPay, "QR Pay", Icons.Filled.QrCodeScanner),
    BottomTab(AbaDestination.History, "History", Icons.Filled.Receipt),
    BottomTab(AbaDestination.Profile, "Profile", Icons.Filled.Person)
)

@Composable
fun AbaBottomBar(current: AbaDestination, onNavigate: (AbaDestination) -> Unit) {
    NavigationBar(containerColor = AbaHomeBgBottom) {
        tabs.forEach { tab ->
            NavigationBarItem(
                selected = current == tab.destination,
                onClick = { onNavigate(tab.destination) },
                icon = { Icon(tab.icon, contentDescription = tab.label) },
                label = { Text(tab.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AbaGold,
                    selectedTextColor = AbaGold,
                    unselectedIconColor = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.5f),
                    unselectedTextColor = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.5f),
                    indicatorColor = AbaGold.copy(alpha = 0.15f)
                )
            )
        }
    }
}
