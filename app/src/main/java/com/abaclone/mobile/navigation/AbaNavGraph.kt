package com.abaclone.mobile.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.abaclone.mobile.data.RegistrationState
import com.abaclone.mobile.ui.components.AbaBottomBar
import com.abaclone.mobile.ui.screens.AccountsScreen
import com.abaclone.mobile.ui.screens.CardsScreen
import com.abaclone.mobile.ui.screens.NotificationsScreen
import com.abaclone.mobile.ui.screens.HistoryScreen
import com.abaclone.mobile.ui.screens.HomeScreen
import com.abaclone.mobile.ui.screens.LoginScreen
import com.abaclone.mobile.ui.screens.ProfileScreen
import com.abaclone.mobile.ui.screens.QrPayScreen
import com.abaclone.mobile.ui.screens.TransferScreen
import com.abaclone.mobile.ui.screens.WalletScreen
import com.abaclone.mobile.ui.screens.WelcomeScreen
import com.abaclone.mobile.ui.screens.onboarding.ActivationSuccessScreen
import com.abaclone.mobile.ui.screens.onboarding.CreatePasswordScreen
import com.abaclone.mobile.ui.screens.onboarding.CreatePinScreen
import com.abaclone.mobile.ui.screens.onboarding.DocumentDetailsScreen
import com.abaclone.mobile.ui.screens.onboarding.FacePassScreen
import com.abaclone.mobile.ui.screens.onboarding.InstantAccountWelcomeScreen
import com.abaclone.mobile.ui.screens.onboarding.NidScanScreen
import com.abaclone.mobile.ui.screens.onboarding.OtpScreen
import com.abaclone.mobile.ui.screens.onboarding.PhoneEntryScreen
import com.abaclone.mobile.ui.screens.onboarding.SecretWordInfoScreen
import com.abaclone.mobile.ui.screens.onboarding.TermsScreen

/** Routes that show the bottom navigation bar (i.e. the main app shell, post-login). */
private val bottomBarRoutes = setOf<String>()

/** Whether the Instant Account (new-customer) branch is currently active, so Terms knows where to send the user next. */
private var instantAccountFlow = false

@Composable
fun AbaNavGraph() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute in bottomBarRoutes) {
                val current = when (currentRoute) {
                    AbaDestination.Cards.route -> AbaDestination.Cards
                    AbaDestination.QrPay.route -> AbaDestination.QrPay
                    AbaDestination.History.route -> AbaDestination.History
                    AbaDestination.Profile.route -> AbaDestination.Profile
                    else -> AbaDestination.Home
                }
                AbaBottomBar(current = current) { destination ->
                    navController.navigate(destination.route) {
                        popUpTo(AbaDestination.Home.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = AbaDestination.Welcome.route,
                modifier = Modifier
                    .fillMaxSize()
                    .let { m ->
                        if (currentRoute in bottomBarRoutes) m.padding(bottom = padding.calculateBottomPadding()) else m
                    }
            ) {
                // ---- Entry point ----
                composable(AbaDestination.Welcome.route) {
                    WelcomeScreen(
                        onActivate = {
                            instantAccountFlow = false
                            navController.navigate(AbaDestination.Terms.route)
                        },
                        onOpenInstantAccount = {
                            instantAccountFlow = true
                            navController.navigate(AbaDestination.InstantAccountWelcome.route)
                        }
                    )
                }

                // ---- "Activate ABA Mobile" branch (existing account holders) ----
                composable(AbaDestination.Terms.route) {
                    TermsScreen(
                        onBack = { navController.popBackStack() },
                        onAgree = { navController.navigate(AbaDestination.PhoneEntry.route) }
                    )
                }

                composable(AbaDestination.PhoneEntry.route) {
                    PhoneEntryScreen(
                        onBack = { navController.popBackStack() },
                        onNext = { phone ->
                            RegistrationState.phone = phone
                            navController.navigate(AbaDestination.Otp.route)
                        }
                    )
                }

                composable(AbaDestination.Otp.route) {
                    OtpScreen(
                        onBack = { navController.popBackStack() },
                        onNext = {
                            if (instantAccountFlow) {
                                navController.navigate(AbaDestination.NidScan.route)
                            } else {
                                // For "Activate ABA Mobile" flow - go to Create PIN
                                navController.navigate(AbaDestination.CreatePin.route)
                            }
                        }
                    )
                }

                composable(AbaDestination.SecretWordInfo.route) {
                    SecretWordInfoScreen(
                        onBack = { navController.popBackStack() },
                        onNext = { navController.navigate(AbaDestination.CreatePassword.route) }
                    )
                }

                composable(AbaDestination.CreatePassword.route) {
                    CreatePasswordScreen(
                        onBack = { navController.popBackStack() },
                        onNext = { navController.navigate(AbaDestination.CreatePin.route) }
                    )
                }

                composable(AbaDestination.CreatePin.route) {
                    CreatePinScreen(
                        onBack = { navController.popBackStack() },
                        onNext = {
                            if (instantAccountFlow) {
                                // For Instant Account - go to Activation Success
                                navController.navigate(AbaDestination.ActivationSuccess.route)
                            } else {
                                // For Activate ABA Mobile - go to Face Scan
                                navController.navigate(AbaDestination.FacePass.route)
                            }
                        }
                    )
                }

                composable(AbaDestination.ActivationSuccess.route) {
                    ActivationSuccessScreen(
                        name = "Besdong",
                        onNext = {
                            navController.navigate(AbaDestination.Login.route) {
                                popUpTo(AbaDestination.Welcome.route) { inclusive = true }
                            }
                        }
                    )
                }

                // ---- "Open ABA Instant Account" branch (new customers) ----
                composable(AbaDestination.InstantAccountWelcome.route) {
                    InstantAccountWelcomeScreen(
                        onBack = { navController.popBackStack() },
                        onAgree = { navController.navigate(AbaDestination.Terms.route) }
                    )
                }

                composable(AbaDestination.NidScan.route) {
                    NidScanScreen(
                        onBack = { navController.popBackStack() },
                        onDone = { navController.navigate(AbaDestination.FacePass.route) }
                    )
                }

                composable(AbaDestination.FacePass.route) {
                    FacePassScreen(
                        onBack = { navController.popBackStack() },
                        onDone = {
                            if (instantAccountFlow) {
                                // For Instant Account - go to Document Details
                                navController.navigate(AbaDestination.DocumentDetails.route)
                            } else {
                                // For Activate ABA Mobile - go to Login
                                navController.navigate(AbaDestination.Login.route) {
                                    popUpTo(AbaDestination.Welcome.route) { inclusive = true }
                                }
                            }
                        }
                    )
                }

                composable(AbaDestination.DocumentDetails.route) {
                    DocumentDetailsScreen(
                        onBack = { navController.popBackStack() },
                        onTryAgain = { navController.navigate(AbaDestination.NidScan.route) },
                        onSubmit = { navController.navigate(AbaDestination.SecretWordInfo.route) }
                    )
                }

                // ---- PIN login (returning users) ----
                composable(AbaDestination.Login.route) {
                    LoginScreen(onLoginSuccess = {
                        navController.navigate(AbaDestination.Home.route) {
                            popUpTo(AbaDestination.Login.route) { inclusive = true }
                        }
                    })
                }

                // ---- Main app shell ----
                composable(AbaDestination.Home.route) {
                    HomeScreen(
                        onSeeAllTransactions = { navController.navigate(AbaDestination.History.route) },
                        onAccountClick = { navController.navigate(AbaDestination.Accounts.route) },
                        onNotificationClick = { navController.navigate(AbaDestination.Notifications.route) }
                    )
                }

                composable(AbaDestination.Accounts.route) {
                    AccountsScreen(onBack = { navController.popBackStack() })
                }

                composable(AbaDestination.Notifications.route) {
                    NotificationsScreen(onBack = { navController.popBackStack() })
                }

                composable(AbaDestination.Wallet.route) {
                    WalletScreen(onBack = { navController.popBackStack() })
                }

                composable(AbaDestination.Cards.route) { CardsScreen() }

                composable(AbaDestination.QrPay.route) { QrPayScreen() }

                composable(AbaDestination.History.route) { HistoryScreen() }

                composable(AbaDestination.Profile.route) {
                    ProfileScreen(onLogout = {
                        navController.navigate(AbaDestination.Welcome.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    })
                }

                composable(AbaDestination.Transfer.route) {
                    TransferScreen(
                        onBack = { navController.popBackStack() },
                        onSent = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
