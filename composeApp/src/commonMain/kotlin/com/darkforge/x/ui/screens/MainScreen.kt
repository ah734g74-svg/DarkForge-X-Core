package com.darkforge.x.ui.screens



import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width






import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue








import com.darkforge.x.ui.neonCyan
import com.darkforge.x.ui.electricViolet
import com.darkforge.x.ui.obsidianBlack
import com.darkforge.x.ui.darkCharcoal
import com.darkforge.x.ui.cyberGray
import com.darkforge.x.ui.DarkForgeXHeader
import com.darkforge.x.ui.DarkForgeXCard
import com.darkforge.x.ui.DarkForgeXPrimaryButton
import com.darkforge.x.ui.DarkForgeXSecondaryButton
import com.darkforge.x.ui.DarkForgeXStatusBadge
import com.darkforge.x.ui.StatusType
import com.darkforge.x.ui.DarkForgeXStyleGuide
import com.darkforge.x.ui.DarkForgeXCornerRadius
import com.darkforge.x.ui.DarkForgeXSpacing
import com.darkforge.x.ui.DarkForgeXTypography
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Compress
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VideoLibrary

/**
 * MainScreen — الشاشة الرئيسية لـ DarkForge-X
 * واجهة DarkForge-X الأمامية المتطورة
 * The Ultimate Source - Premium Main UI
 */

@Composable
fun MainScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(obsidianBlack)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 80.dp)
        ) {
            DarkForgeXHeader(title = "DarkForge-X")
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // لوحات التحكم الرئيسية
            when (selectedTab) {
                0 -> SecurityDashboard()
                1 -> SearchDashboard()
                2 -> VideoDashboard()
                3 -> FileManagerDashboard()
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
        
        // شريط التنقل السفلي
        CustomNavigationBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it }
        )
    }
}

@Composable


@Composable
fun SearchBar() {
    OutlinedTextField(
        value = "",
        onValueChange = {},
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
            placeholder = {
                Text(
                    "ابحث في أي مكان...",
                    color = cyberGray,
                    fontSize = DarkForgeXTypography.bodyMedium.fontSize
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = neonCyan
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = "Filter",
                    tint = electricViolet,
                    modifier = Modifier.clickable { }
                )
            },
            shape = RoundedCornerShape(DarkForgeXCornerRadius.md),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = neonCyan,
                unfocusedBorderColor = darkCharcoal,
                focusedTextColor = Color.White,
                unfocusedTextColor = cyberGray,
                focusedContainerColor = darkCharcoal,
                unfocusedContainerColor = darkCharcoal
            )
    )
}

@Composable
fun SecurityDashboard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "جناح الأمن السيبراني",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = neonCyan,
            modifier = Modifier.padding(bottom = DarkForgeXSpacing.lg)
        )
        
        // بطاقات الأمان
        SecurityCard(
            title = "محلل الويب الشامل",
            description = "تحليل وعرض كود أي موقع",
            icon = Icons.Default.Language,
            color = neonCyan
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        SecurityCard(
            title = "نظام التدقيق المطلق",
            description = "تدقيق الأكواد وضمان صحتها 100%",
            icon = Icons.Default.CheckCircle,
            color = Color(0xFF00FF00) // Green
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        SecurityCard(
            title = "فحص الثغرات الدفاعي",
            description = "فحص وتأمين الأنظمة ضد الثغرات",
            icon = Icons.Default.Security,
            color = electricViolet
        )
    }
}

@Composable
fun SearchDashboard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "محرك البحث الشامل",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = neonCyan,
            modifier = Modifier.padding(bottom = DarkForgeXSpacing.lg)
        )
        
        // بطاقات البحث
        SecurityCard(
            title = "البحث في الويب",
            description = "البحث السطحي والعميق",
            icon = Icons.Default.Public,
            color = neonCyan
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        SecurityCard(
            title = "البحث في قواعد البيانات",
            description = "البحث في الأرشيفات العميقة",
            icon = Icons.Default.Storage,
            color = Color(0xFF00FF00) // Green
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        SecurityCard(
            title = "البحث المتقدم",
            description = "بحث متوازي في عدة أماكن",
            icon = Icons.Default.Radar,
            color = electricViolet
        )
    }
}

@Composable
fun VideoDashboard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "مركز الفيديو",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = neonCyan,
            modifier = Modifier.padding(bottom = DarkForgeXSpacing.lg)
        )
        
        // بطاقات الفيديو
        SecurityCard(
            title = "تنزيل الفيديوهات",
            description = "سرعة تنزيل خرافية",
            icon = Icons.Default.Download,
            color = neonCyan
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        SecurityCard(
            title = "ضغط الفيديو",
            description = "ضغط إلى 140p بالقوة الغاشمة",
            icon = Icons.Default.Compress,
            color = electricViolet
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        SecurityCard(
            title = "جالب المجرّة",
            description = "التقاط من أي منصة",
            icon = Icons.Default.CloudDownload,
            color = Color(0xFF00FF00) // Green
        )
    }
}

@Composable
fun FileManagerDashboard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "مدير الملفات",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = neonCyan,
            modifier = Modifier.padding(bottom = DarkForgeXSpacing.lg)
        )
        
        // بطاقات الملفات
        SecurityCard(
            title = "إدارة الملفات",
            description = "وصول مطلق للملفات",
            icon = Icons.Default.FolderOpen,
            color = neonCyan
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        SecurityCard(
            title = "البحث عن الملفات",
            description = "بحث سريع وفعال",
            icon = Icons.Default.Search,
            color = Color(0xFF00FF00) // Green
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        SecurityCard(
            title = "معالجة الملفات",
            description = "تعديل ونسخ ودمج",
            icon = Icons.Default.Edit,
            color = electricViolet
        )
    }
}

@Composable
fun SecurityCard(
    title: String,
    description: String,
    icon: ImageVector,
    color: Color
) {
    DarkForgeXCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .height(100.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(DarkForgeXSpacing.lg),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(DarkForgeXSizes.iconXLarge)
                    .background(
                        color = color.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(DarkForgeXCornerRadius.sm)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(DarkForgeXSizes.iconLarge)
                )
            }
            
            Spacer(modifier = Modifier.width(DarkForgeXSpacing.lg))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = DarkForgeXTypography.titleSmall.fontSize,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = description,
                    fontSize = DarkForgeXTypography.bodySmall.fontSize,
                    color = cyberGray
                )
            }
            
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = "Navigate",
                tint = color,
                modifier = Modifier.size(DarkForgeXSizes.iconMedium)
            )
        }
    }
}

@Composable
fun CustomNavigationBar(
    modifier: Modifier = Modifier,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    NavigationBar(
        modifier = modifier
            .fillMaxWidth()
            .background(darkCharcoal),
        containerColor = darkCharcoal,
        contentColor = neonCyan
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Security, contentDescription = "Security", tint = if (selectedTab == 0) neonCyan else cyberGray) },
            label = { Text("الأمان", fontSize = DarkForgeXTypography.labelSmall.fontSize, color = if (selectedTab == 0) neonCyan else cyberGray) },
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = if (selectedTab == 1) neonCyan else cyberGray) },
            label = { Text("البحث", fontSize = DarkForgeXTypography.labelSmall.fontSize, color = if (selectedTab == 1) neonCyan else cyberGray) },
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.VideoLibrary, contentDescription = "Video", tint = if (selectedTab == 2) neonCyan else cyberGray) },
            label = { Text("الفيديو", fontSize = DarkForgeXTypography.labelSmall.fontSize, color = if (selectedTab == 2) neonCyan else cyberGray) },
            selected = selectedTab == 2,
            onClick = { onTabSelected(2) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Folder, contentDescription = "Files", tint = if (selectedTab == 3) neonCyan else cyberGray) },
            label = { Text("الملفات", fontSize = DarkForgeXTypography.labelSmall.fontSize, color = if (selectedTab == 3) neonCyan else cyberGray) },
            selected = selectedTab == 3,
            onClick = { onTabSelected(3) }
        )
    }
}
