package com.abaclone.mobile.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abaclone.mobile.data.MockData
import com.abaclone.mobile.ui.components.TransactionRow
import com.abaclone.mobile.ui.theme.AbaTextPrimary

@Composable
fun HistoryScreen() {
    val grouped = MockData.transactions.groupBy { it.date.substringBefore(",") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F7))
            .padding(horizontal = 20.dp)
    ) {
        item { Spacer(Modifier.height(24.dp)) }
        item {
            Text("Transaction history", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = AbaTextPrimary)
            Spacer(Modifier.height(16.dp))
        }

        grouped.forEach { (day, txs) ->
            item {
                Text(day, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = AbaTextPrimary)
                Spacer(Modifier.height(4.dp))
            }
            items(txs) { tx ->
                TransactionRow(transaction = tx)
                HorizontalDivider(color = Color(0xFFE7E6EA))
            }
            item { Spacer(Modifier.height(12.dp)) }
        }

        item { Spacer(Modifier.height(70.dp)) }
    }
}
