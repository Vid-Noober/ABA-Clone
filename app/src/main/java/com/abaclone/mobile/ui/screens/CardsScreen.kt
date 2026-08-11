package com.abaclone.mobile.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abaclone.mobile.data.MockData
import com.abaclone.mobile.model.BankCard
import com.abaclone.mobile.ui.theme.AbaGold
import com.abaclone.mobile.ui.theme.AbaMaroon
import com.abaclone.mobile.ui.theme.AbaMaroonDark
import com.abaclone.mobile.ui.theme.AbaTextPrimary
import com.abaclone.mobile.ui.theme.AbaTextSecondary

@Composable
fun CardsScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F7))
            .padding(horizontal = 20.dp)
    ) {
        item { Spacer(Modifier.height(24.dp)) }
        item {
            Text("My cards", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = AbaTextPrimary)
            Spacer(Modifier.height(18.dp))
        }
        items(MockData.cards) { card ->
            CardVisual(card)
            Spacer(Modifier.height(14.dp))
            CardSettingsRow(card)
            Spacer(Modifier.height(24.dp))
        }
        item { Spacer(Modifier.height(70.dp)) }
    }
}

@Composable
private fun CardVisual(card: BankCard) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.linearGradient(listOf(AbaMaroonDark, AbaMaroon)))
            .padding(20.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("ABA", color = Color.White, fontWeight = FontWeight.Black, fontSize = 18.sp)
                Icon(Icons.Filled.CreditCard, contentDescription = null, tint = AbaGold)
            }

            Spacer(Modifier.weight(1f))

            Text(
                text = card.number,
                color = Color.White,
                fontSize = 19.sp,
                letterSpacing = 2.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text("CARD HOLDER", color = Color.White.copy(alpha = 0.6f), fontSize = 9.sp)
                    Text(card.holderName, color = Color.White, fontSize = 13.sp)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("EXPIRES", color = Color.White.copy(alpha = 0.6f), fontSize = 9.sp)
                    Text(card.expiry, color = Color.White, fontSize = 13.sp)
                }
                Text(card.network, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun CardSettingsRow(card: BankCard) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.AcUnit, contentDescription = null, tint = AbaTextSecondary, modifier = Modifier.height(18.dp))
            Text("  Freeze card", fontSize = 14.sp, color = AbaTextPrimary)
        }
        Switch(
            checked = card.isFrozen,
            onCheckedChange = { },
            colors = SwitchDefaults.colors(checkedTrackColor = AbaMaroon)
        )
    }
}
