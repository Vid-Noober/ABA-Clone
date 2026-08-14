package com.abaclone.mobile.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Atm
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.SyncAlt
import com.abaclone.mobile.model.BankCard
import com.abaclone.mobile.model.Contact
import com.abaclone.mobile.model.MiniApp
import com.abaclone.mobile.model.Promo
import com.abaclone.mobile.model.QuickAction
import com.abaclone.mobile.model.Transaction
import com.abaclone.mobile.model.TxDirection
import com.abaclone.mobile.model.UserAccount

/**
 * Static in-memory data standing in for a real backend / API layer.
 * Swap this out for a Retrofit/Room-backed repository when wiring up real accounts.
 */
object MockData {

    val account = UserAccount(
        holderName = "David",
        accountNumber = "000 762 XXX",
        balance = 1000.00
    )

    val quickActions = listOf(
        QuickAction("Accounts", Icons.Filled.AccountBalance),
        QuickAction("Cards", Icons.Filled.CreditCard),
        QuickAction("Payments", Icons.Filled.CurrencyExchange),
        QuickAction("New Account", Icons.Filled.CreditCard),
        QuickAction("Cash to ATM", Icons.Filled.Atm),
        QuickAction("Transfers", Icons.Filled.SyncAlt),
        QuickAction("ABA Scan", Icons.Filled.QrCodeScanner),
        QuickAction("Loans", Icons.Filled.Payments),
        QuickAction("Mini Apps", Icons.Filled.Dashboard)
    )

    val promos = listOf(
        Promo("ABA Home Loan", "6.5% first year, up to 25 years"),
        Promo("ABA Pay Lucky Draw", "iPhone 17 Pro Max, BYD Sealion, and more"),
        Promo("Win a Car", "Spend with ABA Mobile for a chance to win")
    )

    val miniApps = listOf(
        MiniApp("Metfone"),
        MiniApp("Video"),
        MiniApp("Smart"),
        MiniApp("VET"),
        MiniApp("Cellcard")
    )

    val transactions = listOf(
        Transaction("t1", "Sok Dara", "Money transfer", "Today, 09:41", 25.00, TxDirection.OUT),
        Transaction("t2", "Salary — Tech Co Ltd", "Incoming transfer", "Today, 08:02", 950.00, TxDirection.IN),
        Transaction("t3", "Brown Coffee", "QR Pay", "Yesterday, 17:20", 3.20, TxDirection.OUT),
        Transaction("t4", "EDC Electricity", "Bill payment", "Yesterday, 11:05", 18.40, TxDirection.OUT),
        Transaction("t5", "Chan Vitou", "Money transfer", "22 Jul, 19:12", 40.00, TxDirection.IN),
        Transaction("t6", "Lucky Supermarket", "QR Pay", "21 Jul, 14:37", 12.75, TxDirection.OUT),
        Transaction("t7", "Smart Axiata", "Mobile top-up", "20 Jul, 10:02", 5.00, TxDirection.OUT)
    )

    val cards = listOf(
        BankCard("SOKHA CHANTHOU", "4582 71XX XXXX 3390", "09/28", "VISA"),
        BankCard("SOKHA CHANTHOU", "5412 88XX XXXX 1120", "03/27", "MASTERCARD", isFrozen = true)
    )

    val recentContacts = listOf(
        Contact("Sok Dara", "012 345 678"),
        Contact("Chan Vitou", "092 111 222"),
        Contact("Ly Sreymom", "088 456 789"),
        Contact("Heng Piseth", "070 999 000")
    )
}
