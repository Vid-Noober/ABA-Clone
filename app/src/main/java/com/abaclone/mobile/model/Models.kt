package com.abaclone.mobile.model

data class UserAccount(
    val holderName: String,
    val accountNumber: String,
    val balance: Double,
    val currency: String = "USD"
)

data class QuickAction(
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

enum class TxDirection { IN, OUT }

data class Transaction(
    val id: String,
    val title: String,
    val subtitle: String,
    val date: String,
    val amount: Double,
    val direction: TxDirection,
    val currency: String = "USD"
)

data class BankCard(
    val holderName: String,
    val number: String,
    val expiry: String,
    val network: String, // e.g. "VISA"
    val isFrozen: Boolean = false
)

data class Contact(
    val name: String,
    val phone: String
)

data class Promo(
    val title: String,
    val subtitle: String
)

data class MiniApp(
    val name: String
)
