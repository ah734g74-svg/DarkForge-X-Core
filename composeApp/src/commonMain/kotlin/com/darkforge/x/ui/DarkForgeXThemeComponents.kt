/**
 * DarkForgeXThemeComponents.kt
 * 
 * مكونات Compose احترافية متناسقة مع هوية DarkForge-X
 * تضمن التناسق البصري والوظيفي الشامل عبر جميع واجهات التطبيق
 * 
 * الألوان الأساسية:
 * - Neon Cyan (#00D9FF): اللون الأساسي والتأكيدات
 * - Electric Violet (#9D4EDD): اللون الثانوي والحدود
 * - Obsidian Black (#0A0E27): الخلفية الداكنة
 * - Dark Charcoal (#1A1F3A): السطح الثانوي
 * - Cyber Gray (#2D3561): العناصر الإضافية
 */

package com.darkforge.x.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * DarkForgeXPrimaryButton: زر أساسي احترافي بألوان DarkForge-X
 * يستخدم Neon Cyan كلون أساسي مع تأثيرات نيون
 */
@Composable
fun DarkForgeXPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = neonCyan,
            contentColor = obsidianBlack,
            disabledContainerColor = cyberGray,
            disabledContentColor = Color.Gray,
        ),
        shape = RoundedCornerShape(12.dp),
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp),
        )
    }
}

/**
 * DarkForgeXSecondaryButton: زر ثانوي احترافي بألوان DarkForge-X
 * يستخدم Electric Violet كلون ثانوي مع حدود نيون
 */
@Composable
fun DarkForgeXSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .border(2.dp, electricViolet, RoundedCornerShape(12.dp)),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = electricViolet,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = cyberGray,
        ),
        shape = RoundedCornerShape(12.dp),
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp),
        )
    }
}

/**
 * DarkForgeXCard: بطاقة احترافية متناسقة مع التصميم
 * تستخدم Dark Charcoal كخلفية مع حدود Neon Cyan
 */
@Composable
fun DarkForgeXCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.5.dp, neonCyan, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = darkCharcoal,
            contentColor = Color.White,
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            content()
        }
    }
}

/**
 * DarkForgeXGradientCard: بطاقة متدرجة احترافية
 * تستخدم تدرج من Neon Cyan إلى Electric Violet
 */
@Composable
fun DarkForgeXGradientCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = androidx.compose.ui.graphics.Brush.linearGradient(
                    colors = listOf(neonCyan.copy(alpha = 0.1f), electricViolet.copy(alpha = 0.1f)),
                ),
                shape = RoundedCornerShape(16.dp),
            )
            .border(1.5.dp, gradientBrush, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = darkCharcoal,
            contentColor = Color.White,
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            content()
        }
    }
}

/**
 * DarkForgeXHeader: رأس احترافي للشاشات
 * يستخدم Neon Cyan كلون أساسي مع نص Bold
 */
@Composable
fun DarkForgeXHeader(
    title: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(obsidianBlack)
            .padding(16.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        Text(
            text = title,
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = neonCyan,
        )
    }
}

/**
 * DarkForgeXSubtitle: عنوان فرعي احترافي
 * يستخدم Electric Violet كلون ثانوي
 */
@Composable
fun DarkForgeXSubtitle(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        color = electricViolet,
        modifier = modifier.padding(vertical = 8.dp),
    )
}

/**
 * DarkForgeXDivider: فاصل احترافي
 * يستخدم Neon Cyan كلون للخط
 */
@Composable
fun DarkForgeXDivider(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .background(neonCyan.copy(alpha = 0.3f))
            .padding(vertical = 1.dp),
    )
}

/**
 * DarkForgeXStatusBadge: شارة حالة احترافية
 * تستخدم ألوان مختلفة حسب الحالة
 */
@Composable
fun DarkForgeXStatusBadge(
    text: String,
    status: StatusType = StatusType.ACTIVE,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = when (status) {
        StatusType.ACTIVE -> neonCyan.copy(alpha = 0.2f)
        StatusType.INACTIVE -> cyberGray.copy(alpha = 0.2f)
        StatusType.WARNING -> Color(0xFFFF9800).copy(alpha = 0.2f)
        StatusType.ERROR -> Color(0xFFFF006E).copy(alpha = 0.2f)
    }
    
    val textColor = when (status) {
        StatusType.ACTIVE -> neonCyan
        StatusType.INACTIVE -> cyberGray
        StatusType.WARNING -> Color(0xFFFF9800)
        StatusType.ERROR -> Color(0xFFFF006E)
    }
    
    Box(
        modifier = modifier
            .background(backgroundColor, RoundedCornerShape(20.dp))
            .border(1.dp, textColor, RoundedCornerShape(20.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
        )
    }
}

enum class StatusType {
    ACTIVE, INACTIVE, WARNING, ERROR
}
