package com.abaclone.mobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abaclone.mobile.model.Promo
import com.abaclone.mobile.ui.theme.AbaGold
import com.abaclone.mobile.ui.theme.AbaNavyDark
import com.abaclone.mobile.ui.theme.AbaNavyLight

@Composable
fun PromoCarousel(promos: List<Promo>, modifier: Modifier = Modifier) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(promos) { promo -> PromoCard(promo) }
    }
}

@Composable
private fun PromoCard(promo: Promo) {
    Column(
        modifier = Modifier
            .height(96.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Brush.linearGradient(listOf(AbaNavyLight, AbaNavyDark)))
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(promo.title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Text(promo.subtitle, color = AbaGold, fontSize = 11.sp, modifier = Modifier.padding(top = 2.dp))
    }
}
