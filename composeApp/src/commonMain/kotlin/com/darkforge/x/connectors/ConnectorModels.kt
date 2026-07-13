package com.darkforge.x.connectors

import kotlinx.serialization.Serializable

/**
 * نظام الموصلات (Connectors) لـ Manus 1.6 Max.
 * يسمح بالاتصال بالتطبيقات الخارجية وإدارة الصلاحيات.
 */
@Serializable
enum class ConnectorType {
    GITHUB, GOOGLE, SHOPIFY, OUTLOOK, INSTAGRAM, META, BROWSER
}

@Serializable
data class Connector(
    val id: String,
    val name: String,
    val type: ConnectorType,
    val isConnected: Boolean = false,
    val isBeta: Boolean = false,
    val iconUrl: String? = null,
    val description: String? = null
)

@Serializable
data class ConnectorConfig(
    val connectors: List<Connector> = listOf(
        Connector("github", "جيت هب", ConnectorType.GITHUB, true),
        Connector("browser", "متصفحي", ConnectorType.BROWSER, false),
        Connector("shopify", "Shopify", ConnectorType.SHOPIFY, false, true),
        Connector("outlook_cal", "تقويم Outlook", ConnectorType.OUTLOOK, false),
        Connector("outlook_mail", "بريد Outlook", ConnectorType.OUTLOOK, false),
        Connector("instagram", "Instagram Creator Mar...", ConnectorType.INSTAGRAM, false, true),
        Connector("google_cal", "تقويم Google", ConnectorType.GOOGLE, false),
        Connector("meta_ads", "مدير إعلانات Meta", ConnectorType.META, false, true)
    )
)
