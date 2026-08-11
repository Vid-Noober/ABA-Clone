# ABA Clone — Jetpack Compose

A Kotlin + Jetpack Compose recreation of the **ABA Mobile** (ABA Bank, Cambodia)
banking app UI, built from your uploaded Figma file.

> **Note on the source file:** `ABA - Clone.fig` is stored in Figma's proprietary
> binary format (`fig-kiwi`, a zlib-compressed schema-based encoding). It can't be
> parsed losslessly outside Figma's own tooling, so this project rebuilds the app
> using ABA Bank's real brand identity (deep maroon `#7A1128`, gold accent) and its
> known screen structure, rather than pixel-extracted layout data.

## Screens

| Screen | Description |
|---|---|
| **Login** | Phone number + 6-digit PIN keypad, biometric shortcut |
| **Home** | Balance card (show/hide toggle), quick actions grid, recent activity |
| **Transfer** | Recipient picker with recent contacts, amount + note, send |
| **QR Pay** | Tabs for "Scan to pay" and "My QR code" |
| **Cards** | Card visuals (VISA/Mastercard), freeze toggle |
| **History** | Full transaction list grouped by date |
| **Profile** | Account summary + settings list (Security, Language, Log out) |

All data is served from `data/MockData.kt` — an in-memory stand-in for a real
backend. Swap it for a Retrofit/Room-backed repository when you're ready to wire
up live accounts.

## Project structure

```
app/src/main/java/com/abaclone/mobile/
├── MainActivity.kt
├── data/MockData.kt
├── model/Models.kt
├── navigation/
│   ├── AbaDestination.kt      # route definitions
│   └── AbaNavGraph.kt         # NavHost + bottom bar wiring
├── ui/
│   ├── theme/                 # Color.kt, Type.kt, Theme.kt
│   ├── components/            # BalanceCard, QuickActionGrid, TransactionRow, AbaBottomBar
│   └── screens/                # LoginScreen, HomeScreen, TransferScreen, QrPayScreen,
                                 # CardsScreen, HistoryScreen, ProfileScreen
```

## Running it

1. Open this folder in **Android Studio** (Koala or newer).
2. Let it sync Gradle — it will generate the wrapper JAR automatically on first sync
   if it's missing (or run **File → Sync Project with Gradle Files**).
3. Run on an emulator or device (minSdk 24 / Android 7.0+).

### Command line

If you have Gradle installed locally:

```bash
gradle wrapper --gradle-version 8.7   # generates gradlew + wrapper jar
./gradlew assembleDebug
```

The debug APK will be at `app/build/outputs/apk/debug/app-debug.apk`.

## Stack

- Kotlin, Jetpack Compose (Material 3)
- Navigation Compose for screen routing
- Compose BOM 2024.06.00, compileSdk/targetSdk 34, minSdk 24

## Next steps you might want

- Replace `MockData` with real API calls (Retrofit) and persist session state (DataStore)
- Wire up an actual camera-based QR scanner (CameraX + ML Kit) on the QR Pay screen
- Add real biometric auth via `androidx.biometric`
- Hook the Transfer screen's "Send money" button up to a confirmation + success flow
