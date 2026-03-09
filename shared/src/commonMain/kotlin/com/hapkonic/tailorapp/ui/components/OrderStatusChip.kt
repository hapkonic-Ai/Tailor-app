package com.hapkonic.tailorapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hapkonic.tailorapp.domain.model.OrderStatus
import com.hapkonic.tailorapp.ui.theme.StatusDelivered
import com.hapkonic.tailorapp.ui.theme.StatusInProgress
import com.hapkonic.tailorapp.ui.theme.StatusPending
import com.hapkonic.tailorapp.ui.theme.StatusReady

@Composable
fun OrderStatusChip(status: OrderStatus, modifier: Modifier = Modifier) {
    val (bg, label) = when (status) {
        OrderStatus.PENDING     -> Pair(StatusPending,    "Pending")
        OrderStatus.IN_PROGRESS -> Pair(StatusInProgress, "In Progress")
        OrderStatus.READY       -> Pair(StatusReady,      "Ready")
        OrderStatus.DELIVERED   -> Pair(StatusDelivered,  "Delivered")
    }
    Text(
        text = label,
        color = Color.White,
        fontSize = 12.sp,
        modifier = modifier
            .background(bg, RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}
