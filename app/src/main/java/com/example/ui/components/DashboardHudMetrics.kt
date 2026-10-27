package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextMuted
import com.example.util.tr

/**
 * Cyberpunk Dashboard HUD Metrics Row.
 * Displays high-impact operational stats (total templates, total usages/copies, active leads).
 */
@Composable
fun DashboardHudMetrics(
    totalTemplates: Int,
    totalUsages: Int,
    activeLeads: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .testTag("dashboard_hud_metrics"),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        HudMetricCard(
            label = tr("PLANTILLAS", "TEMPLATES"),
            value = totalTemplates.toString(),
            icon = Icons.Default.FlashOn,
            accentColor = NeonCyan,
            modifier = Modifier.weight(1f)
        )
        HudMetricCard(
            label = tr("USOS HOY", "USES TODAY"),
            value = totalUsages.toString(),
            icon = Icons.Default.Send,
            accentColor = NeonGreen,
            modifier = Modifier.weight(1f)
        )
        HudMetricCard(
            label = tr("LEADS CRM", "CRM LEADS"),
            value = activeLeads.toString(),
            icon = Icons.Default.Group,
            accentColor = NeonPurple,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun HudMetricCard(
    label: String,
    value: String,
    icon: ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF12121A))
            .border(1.dp, Color(0xFF2A2A3D), RoundedCornerShape(10.dp))
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = label,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 9.sp,
                    color = TextMuted,
                    letterSpacing = 0.5.sp
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontFamily = FontFamily.Monospace,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = accentColor
            )
        }
    }
}
