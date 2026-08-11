package com.abaclone.mobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abaclone.mobile.model.QuickAction
import com.abaclone.mobile.ui.theme.AbaGold
import com.abaclone.mobile.ui.theme.AbaHomeCard
import com.abaclone.mobile.ui.theme.AbaHomeCardBorder

/**
 * A plain (non-lazy) grid, 3 columns per row. Quick action lists are always
 * short and fully known up front, so this avoids nesting a scrollable
 * LazyVerticalGrid inside the outer scrollable Home screen (which Compose
 * disallows and crashes on — "infinity maximum height constraints").
 */
@Composable
fun QuickActionGrid(
    actions: List<QuickAction>,
    onActionClick: (QuickAction) -> Unit,
    modifier: Modifier = Modifier,
    columns: Int = 3
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        actions.chunked(columns).forEach { rowActions ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                rowActions.forEach { action ->
                    QuickActionItem(
                        action = action,
                        onClick = { onActionClick(action) },
                        modifier = Modifier.weight(1f)
                    )
                }
                // Pad the last row with invisible spacers so items stay
                // aligned to the grid even if the count isn't a multiple of `columns`.
                repeat(columns - rowActions.size) {
                    Row(modifier = Modifier.weight(1f)) {}
                }
            }
        }
    }
}

@Composable
private fun QuickActionItem(action: QuickAction, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(14.dp))
            .background(AbaHomeCard)
            .border(1.dp, AbaHomeCardBorder, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = action.icon,
            contentDescription = action.label,
            tint = AbaGold
        )
        androidx.compose.foundation.layout.Spacer(Modifier.padding(top = 4.dp))
        Text(
            text = action.label,
            fontSize = 11.sp,
            color = androidx.compose.ui.graphics.Color.White,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}
