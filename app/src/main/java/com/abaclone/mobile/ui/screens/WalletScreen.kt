package com.abaclone.mobile.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abaclone.mobile.data.FirebaseRepository
import com.abaclone.mobile.data.MockData
import com.abaclone.mobile.data.RegistrationState
import com.abaclone.mobile.model.UserAccount
import com.abaclone.mobile.ui.components.AuthTopBar
import com.abaclone.mobile.ui.components.BalanceCard
import com.abaclone.mobile.ui.theme.AbaGold
import com.abaclone.mobile.ui.theme.AbaHomeBgBottom
import com.abaclone.mobile.ui.theme.AbaHomeBgTop
import com.abaclone.mobile.ui.theme.AbaHomeCard
import com.abaclone.mobile.ui.theme.AbaHomeCardBorder

@Composable
fun WalletScreen(onBack: () -> Unit) {
    var balance by remember { mutableStateOf<Double?>(null) }
    var name by remember { mutableStateOf(RegistrationState.fullName.ifEmpty { MockData.account.holderName }) }

    LaunchedEffect(Unit) {
        FirebaseRepository.getCurrentUserBalance { b ->
            balance = b
        }
    }

    val account = UserAccount(
        holderName = name,
        accountNumber = MockData.account.accountNumber,
        balance = balance ?: MockData.account.balance
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(AbaHomeBgTop, AbaHomeBgBottom)))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        AuthTopBar(title = "My Wallet", onBack = onBack)

        Spacer(Modifier.height(24.dp))

        BalanceCard(account = account, modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(AbaHomeCard)
                .border(1.dp, AbaHomeCardBorder, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Text("Account Details", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(12.dp))
            DetailRow(label = "Account Holder", value = account.holderName)
            DetailRow(label = "Account Number", value = account.accountNumber)
            DetailRow(label = "Phone", value = RegistrationState.phone.ifEmpty { "Not set" })
            DetailRow(label = "Email", value = RegistrationState.email.ifEmpty { "Not set" })
        }

        Spacer(Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(AbaHomeCard)
                .border(1.dp, AbaHomeCardBorder, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Text("Recent Activity", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            MockData.transactions.take(3).forEach { tx ->
                TransactionSummary(transaction = tx)
                if (tx.id != MockData.transactions.take(3).last().id) {
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.White.copy(alpha = 0.6f), fontSize = 13.sp)
        Text(value, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun TransactionSummary(transaction: com.abaclone.mobile.model.Transaction) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(transaction.title, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            Text(transaction.subtitle, color = Color.White.copy(alpha = 0.5f), fontSize = 11.sp)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                "${if (transaction.direction == com.abaclone.mobile.model.TxDirection.IN) "+" else "-"}${transaction.amount}",
                color = if (transaction.direction == com.abaclone.mobile.model.TxDirection.IN) Color(0xFF4CAF50) else AbaGold,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Text(transaction.date, color = Color.White.copy(alpha = 0.5f), fontSize = 11.sp)
        }
    }
}
