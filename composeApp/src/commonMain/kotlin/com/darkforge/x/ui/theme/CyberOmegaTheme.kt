package com.darkforge.x.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

/**
 * CyberOmegaTheme — تصميم "Cyber-Omega Dark" الأساسي
 * ألوان وخطوط تعكس القوة والهيبة
 * Manus 1.6 Max Level - Premium UI/UX Design
 */

// ألوان Cyber-Omega Dark
object CyberOmegaColors {
    // الألوان الأساسية
    val PrimaryDark = Color(0xFF00D9FF) // Cyan Neon
    val PrimaryLight = Color(0xFF0099CC) // Cyan Dark
    val SecondaryDark = Color(0xFFFF006E) // Magenta Neon
    val SecondaryLight = Color(0xFFCC0055) // Magenta Dark
    val TertiaryDark = Color(0xFF00FF88) // Green Neon
    val TertiaryLight = Color(0xFF00CC66) // Green Dark
    
    // ألوان الخلفية
    val BackgroundDark = Color(0xFF0A0E27) // Deep Navy
    val BackgroundMedium = Color(0xFF1A1F3A) // Navy
    val BackgroundLight = Color(0xFF2A2F4A) // Light Navy
    val SurfaceDark = Color(0xFF0F1428) // Very Deep Navy
    val SurfaceLight = Color(0xFF252D45) // Light Surface
    
    // ألوان النصوص
    val TextPrimary = Color(0xFFFFFFFF) // White
    val TextSecondary = Color(0xFFB0B8D4) // Light Gray
    val TextTertiary = Color(0xFF7A8299) // Medium Gray
    
    // ألوان التنبيهات
    val ErrorRed = Color(0xFFFF4444) // Bright Red
    val WarningOrange = Color(0xFFFFAA00) // Bright Orange
    val SuccessGreen = Color(0xFF00FF88) // Bright Green
    val InfoBlue = Color(0xFF00D9FF) // Bright Cyan
    
    // ألوان الحدود والفواصل
    val BorderColor = Color(0xFF3A4563) // Border Gray
    val DividerColor = Color(0xFF2A3450) // Divider Gray
    
    // ألوان التأكيد
    val AccentCyan = Color(0xFF00D9FF)
    val AccentMagenta = Color(0xFFFF006E)
    val AccentGreen = Color(0xFF00FF88)
    val AccentPurple = Color(0xFFB000FF)
}

// مخطط الألوان الداكن
private val CyberOmegaDarkColorScheme = darkColorScheme(
    primary = CyberOmegaColors.PrimaryDark,
    onPrimary = CyberOmegaColors.BackgroundDark,
    primaryContainer = CyberOmegaColors.PrimaryLight,
    onPrimaryContainer = CyberOmegaColors.TextPrimary,
    
    secondary = CyberOmegaColors.SecondaryDark,
    onSecondary = CyberOmegaColors.BackgroundDark,
    secondaryContainer = CyberOmegaColors.SecondaryLight,
    onSecondaryContainer = CyberOmegaColors.TextPrimary,
    
    tertiary = CyberOmegaColors.TertiaryDark,
    onTertiary = CyberOmegaColors.BackgroundDark,
    tertiaryContainer = CyberOmegaColors.TertiaryLight,
    onTertiaryContainer = CyberOmegaColors.TextPrimary,
    
    error = CyberOmegaColors.ErrorRed,
    onError = CyberOmegaColors.BackgroundDark,
    errorContainer = Color(0xFFFF6666),
    onErrorContainer = CyberOmegaColors.TextPrimary,
    
    background = CyberOmegaColors.BackgroundDark,
    onBackground = CyberOmegaColors.TextPrimary,
    
    surface = CyberOmegaColors.SurfaceDark,
    onSurface = CyberOmegaColors.TextPrimary,
    surfaceVariant = CyberOmegaColors.SurfaceLight,
    onSurfaceVariant = CyberOmegaColors.TextSecondary,
    
    outline = CyberOmegaColors.BorderColor,
    outlineVariant = CyberOmegaColors.DividerColor,
    scrim = Color(0x00000000)
)

// مخطط الألوان الفاتح (للتوافقية)
private val CyberOmegaLightColorScheme = lightColorScheme(
    primary = CyberOmegaColors.PrimaryLight,
    onPrimary = Color.White,
    primaryContainer = CyberOmegaColors.PrimaryDark,
    onPrimaryContainer = Color.Black,
    
    secondary = CyberOmegaColors.SecondaryLight,
    onSecondary = Color.White,
    secondaryContainer = CyberOmegaColors.SecondaryDark,
    onSecondaryContainer = Color.Black,
    
    tertiary = CyberOmegaColors.TertiaryLight,
    onTertiary = Color.White,
    tertiaryContainer = CyberOmegaColors.TertiaryDark,
    onTertiaryContainer = Color.Black,
    
    error = CyberOmegaColors.ErrorRed,
    onError = Color.White,
    errorContainer = Color(0xFFFFCCCC),
    onErrorContainer = Color.Black,
    
    background = Color(0xFFF5F5F5),
    onBackground = Color.Black,
    
    surface = Color.White,
    onSurface = Color.Black,
    surfaceVariant = Color(0xFFE8E8E8),
    onSurfaceVariant = Color(0xFF666666),
    
    outline = Color(0xFFCCCCCC),
    outlineVariant = Color(0xFFDDDDDD),
    scrim = Color(0x00000000)
)

// الخطوط
object CyberOmegaTypography {
    // عناوين كبيرة
    val DisplayLarge = androidx.compose.material3.Typography().displayLarge.copy(
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp
    )
    
    val DisplayMedium = androidx.compose.material3.Typography().displayMedium.copy(
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp
    )
    
    val DisplaySmall = androidx.compose.material3.Typography().displaySmall.copy(
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp
    )
    
    // عناوين متوسطة
    val HeadlineLarge = androidx.compose.material3.Typography().headlineLarge.copy(
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    )
    
    val HeadlineMedium = androidx.compose.material3.Typography().headlineMedium.copy(
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    )
    
    val HeadlineSmall = androidx.compose.material3.Typography().headlineSmall.copy(
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    )
    
    // عناوين صغيرة
    val TitleLarge = androidx.compose.material3.Typography().titleLarge.copy(
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    )
    
    val TitleMedium = androidx.compose.material3.Typography().titleMedium.copy(
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    )
    
    val TitleSmall = androidx.compose.material3.Typography().titleSmall.copy(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    )
    
    // نصوص عادية
    val BodyLarge = androidx.compose.material3.Typography().bodyLarge.copy(
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    
    val BodyMedium = androidx.compose.material3.Typography().bodyMedium.copy(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    )
    
    val BodySmall = androidx.compose.material3.Typography().bodySmall.copy(
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    )
    
    // تسميات
    val LabelLarge = androidx.compose.material3.Typography().labelLarge.copy(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    )
    
    val LabelMedium = androidx.compose.material3.Typography().labelMedium.copy(
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    
    val LabelSmall = androidx.compose.material3.Typography().labelSmall.copy(
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
}

// الموضوع الرئيسي
@Composable
fun CyberOmegaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        CyberOmegaDarkColorScheme
    } else {
        CyberOmegaLightColorScheme
    }
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = androidx.compose.material3.Typography(
            displayLarge = CyberOmegaTypography.DisplayLarge,
            displayMedium = CyberOmegaTypography.DisplayMedium,
            displaySmall = CyberOmegaTypography.DisplaySmall,
            headlineLarge = CyberOmegaTypography.HeadlineLarge,
            headlineMedium = CyberOmegaTypography.HeadlineMedium,
            headlineSmall = CyberOmegaTypography.HeadlineSmall,
            titleLarge = CyberOmegaTypography.TitleLarge,
            titleMedium = CyberOmegaTypography.TitleMedium,
            titleSmall = CyberOmegaTypography.TitleSmall,
            bodyLarge = CyberOmegaTypography.BodyLarge,
            bodyMedium = CyberOmegaTypography.BodyMedium,
            bodySmall = CyberOmegaTypography.BodySmall,
            labelLarge = CyberOmegaTypography.LabelLarge,
            labelMedium = CyberOmegaTypography.LabelMedium,
            labelSmall = CyberOmegaTypography.LabelSmall
        ),
        content = content
    )
}
