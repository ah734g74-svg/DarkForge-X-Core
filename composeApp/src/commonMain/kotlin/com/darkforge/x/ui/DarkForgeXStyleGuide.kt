/**
 * DarkForgeXStyleGuide.kt
 * 
 * دليل أسلوب DarkForge-X الشامل
 * يحدد معايير التصميم والاحترافية عبر جميع واجهات التطبيق
 * 
 * الهدف: ضمان التناسق المطلق والاحترافية القصوى
 */

package com.darkforge.x.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * نظام المسافات (Spacing System)
 * يضمن التناسق في جميع العناصر
 */
object DarkForgeXSpacing {
    val xs = 4.dp      // Extra Small
    val sm = 8.dp      // Small
    val md = 12.dp     // Medium
    val lg = 16.dp     // Large
    val xl = 24.dp     // Extra Large
    val xxl = 32.dp    // Double Extra Large
}

/**
 * نظام الزوايا المستديرة (Border Radius)
 * يحقق مظهراً احترافياً وحديثاً
 */
object DarkForgeXCornerRadius {
    val xs = 4.dp      // Extra Small
    val sm = 8.dp      // Small
    val md = 12.dp     // Medium
    val lg = 16.dp     // Large
    val xl = 24.dp     // Extra Large
    val full = 32.dp   // Full
}

/**
 * نظام الأحجام (Size System)
 * يضمن التناسق في أحجام العناصر
 */
object DarkForgeXSizes {
    val iconSmall = 16.dp
    val iconMedium = 24.dp
    val iconLarge = 32.dp
    val iconXLarge = 48.dp
    
    val buttonHeightSmall = 32.dp
    val buttonHeightMedium = 40.dp
    val buttonHeightLarge = 48.dp
    val buttonHeightXLarge = 56.dp
}

/**
 * نظام الخطوط (Typography)
 * يحدد أحجام وأوزان الخطوط الموحدة
 */
object DarkForgeXTypography {
    // Display (عناوين رئيسية)
    val displayLarge = 32.sp
    val displayMedium = 28.sp
    val displaySmall = 24.sp
    
    // Headline (عناوين)
    val headlineLarge = 24.sp
    val headlineMedium = 20.sp
    val headlineSmall = 18.sp
    
    // Title (عناوين فرعية)
    val titleLarge = 18.sp
    val titleMedium = 16.sp
    val titleSmall = 14.sp
    
    // Body (نصوص أساسية)
    val bodyLarge = 16.sp
    val bodyMedium = 14.sp
    val bodySmall = 12.sp
    
    // Label (تسميات)
    val labelLarge = 14.sp
    val labelMedium = 12.sp
    val labelSmall = 10.sp
}

/**
 * نظام الظلال (Elevation/Shadows)
 * يضيف عمق واحترافية للواجهة
 */
object DarkForgeXElevation {
    val none = 0.dp
    val level1 = 1.dp
    val level2 = 3.dp
    val level3 = 6.dp
    val level4 = 8.dp
    val level5 = 12.dp
}

/**
 * نظام الشفافية (Opacity)
 * يضمن التناسق في استخدام الشفافية
 */
object DarkForgeXOpacity {
    const val disabled = 0.38f
    const val hover = 0.08f
    const val focus = 0.12f
    const val pressed = 0.16f
    const val drag = 0.16f
}

/**
 * نظام الألوان الموسع (Extended Color System)
 * يوفر ألوان إضافية للحالات الخاصة
 */
object DarkForgeXExtendedColors {
    // الألوان الأساسية
    val primary = neonCyan
    val secondary = electricViolet
    val tertiary = Color(0xFF00D9FF)
    
    // الألوان الإضافية
    val success = Color(0xFF00FF88)
    val warning = Color(0xFFFF9800)
    val error = Color(0xFFFF006E)
    val info = neonCyan
    
    // الألوان المحايدة
    val neutral100 = obsidianBlack
    val neutral200 = Color(0xFF0F1428)
    val neutral300 = Color(0xFF1A1F3A)
    val neutral400 = cyberGray
    val neutral500 = Color(0xFF3D4575)
    val neutral600 = Color(0xFF4D5A8F)
    val neutral700 = Color(0xFF5D6BA3)
    val neutral800 = Color(0xFF7D8BBF)
    val neutral900 = Color(0xFF9D9FD1)
}

/**
 * نظام الحالات (State System)
 * يحدد الألوان والأسلوب لحالات العناصر المختلفة
 */
object DarkForgeXStates {
    // حالات الزر
    val buttonDefault = neonCyan
    val buttonHover = neonCyan.copy(alpha = 0.8f)
    val buttonPressed = neonCyan.copy(alpha = 0.6f)
    val buttonDisabled = cyberGray.copy(alpha = 0.5f)
    
    // حالات الإدخال
    val inputDefault = darkCharcoal
    val inputFocus = darkCharcoal.copy(alpha = 0.8f)
    val inputError = Color(0xFFFF006E)
    val inputSuccess = Color(0xFF00FF88)
    
    // حالات البطاقة
    val cardDefault = darkCharcoal
    val cardHover = darkCharcoal.copy(alpha = 0.9f)
    val cardActive = darkCharcoal.copy(alpha = 0.7f)
}

/**
 * نظام الانتقالات (Transitions)
 * يحدد مدة الانتقالات الموحدة
 */
object DarkForgeXTransitions {
    const val fast = 150          // ملي ثانية
    const val normal = 300        // ملي ثانية
    const val slow = 500          // ملي ثانية
    const val verySlow = 1000     // ملي ثانية
}

/**
 * نظام الحدود (Stroke)
 * يحدد سمك الحدود الموحدة
 */
object DarkForgeXStroke {
    val thin = 0.5.dp
    val light = 1.dp
    val medium = 1.5.dp
    val bold = 2.dp
    val heavy = 3.dp
}

/**
 * نظام الشبكة (Grid System)
 * يضمن التوافق والتناسق في التخطيط
 */
object DarkForgeXGrid {
    val columns = 12              // عدد الأعمدة
    val gutter = 16.dp            // المسافة بين الأعمدة
    val margin = 16.dp            // الهامش الخارجي
}

/**
 * قائمة الفحص (Checklist) للاحترافية
 * 
 * ✅ جميع الألوان متناسقة مع نظام DarkForge-X
 * ✅ جميع المسافات متوافقة مع نظام المسافات
 * ✅ جميع الزوايا المستديرة متناسقة
 * ✅ جميع الخطوط متوافقة مع نظام الخطوط
 * ✅ جميع الحالات محددة بوضوح
 * ✅ جميع الانتقالات سلسة وموحدة
 * ✅ جميع العناصر متناسقة بصرياً
 * ✅ جميع الأكواد منظمة واحترافية
 * ✅ جميع الملفات متطابقة في الهيكل
 * ✅ جميع الواجهات احترافية وحديثة
 */
