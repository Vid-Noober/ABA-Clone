package com.abaclone.mobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abaclone.mobile.model.Transaction
import com.abaclone.mobile.model.TxDirection
import com.abaclone.mobile.ui.theme.AbaGreen
import com.abaclone.mobile.ui.theme.AbaRed
import com.abaclone.mobile.ui.theme.AbaTextPrimary
import com.abaclone.mobile.ui.theme.AbaTextSecondary

@Composable
fun TransactionRow(transaction: Transaction, modifier: Modifier = Modifier, darkMode: Boolean = false) {
    val isIncoming = transaction.direction == TxDirection.IN
    val primaryText = if (darkMode) androidx.compose.ui.graphics.Color.White else AbaTextPrimary
    val secondaryText = if (darkMode) androidx.compose.ui.graphics.Color.White.copy(alpha = 0.6f) else AbaTextSecondary
    val amountColor = if (isIncoming) AbaGreen else primaryText
    val sign = if (isIncoming) "+" else "-"

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background((if (isIncoming) AbaGreen else AbaRed).copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isIncoming) Icons.Filled.ArrowDownward else Icons.Filled.ArrowUpward,
                    contentDescription = null,
                    tint = if (isIncoming) AbaGreen else AbaRed,
                    modifier = Modifier.size(18.dp)
                )
            }
            androidx.compose.foundation.layout.Spacer(Modifier.padding(start = 6.dp))
            Column(modifier = Modifier.padding(start = 6.dp)) {
                Text(transaction.title, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = primaryText)
                Text(transaction.subtitle + " · " + transaction.date, fontSize = 12.sp, color = secondaryText)
            }
        }
        Text(
            text = "$sign$${"%,.2f".format(transaction.amount)}",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = amountColor
        )
    }
}
