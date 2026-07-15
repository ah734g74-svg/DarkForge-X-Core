package com.darkforge.x.api

import com.darkforge.x.data.AppSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

/**
 * Advanced API Manager — نظام الـ API المتقدم لـ Manus 1.6 Max.
 * يدير جميع الاتصالات والـ APIs مع دعم مفاتيح الربط التاريخية والحديثة.
 */
@Serializable
data class ApiKey(
    val keyId: String,
    val keyValue: String,
    val createdAt: Long,
    val expiresAt: Long? = null,
    val isActive: Boolean = true,
    val description: String = ""
)

@Serializable
data class ApiEndpoint(
    val name: String,
    val baseUrl: String,
    val version: String = "1.0",
    val authType: String = "bearer", // bearer, api_key, oauth2
    val isEnabled: Boolean = true
)

class AdvancedApiManager(private val appSettings: AppSettings) {
    private val json = Json { ignoreUnknownKeys = true }
    private val KEY_API_KEYS = "manus_api_keys"
    private val KEY_API_ENDPOINTS = "manus_api_endpoints"

    private val _apiKeys = MutableStateFlow<List<ApiKey>>(emptyList())
    val apiKeys: StateFlow<List<ApiKey>> = _apiKeys

    private val _endpoints = MutableStateFlow<List<ApiEndpoint>>(emptyList())
    val endpoints: StateFlow<List<ApiEndpoint>> = _endpoints

    // مفتاح الربط التاريخي (Legacy API Key) - 2004-06-29
    private val legacyApiKey = ApiKey(
        keyId = "legacy-2004-06-29",
        keyValue = "2004-06-29-manus-legacy-api-key",
        createdAt = 1088476800000L, // 2004-06-29 00:00:00 UTC
        isActive = true,
        description = "Legacy API Key from Manus 1.0 era (2004-06-29)"
    )

    // مفتاح Manus 1.6 Max Full - القوة المطلقة
    private val manusMaxKey = ApiKey(
        keyId = "manus-1.6-max-full",
        keyValue = "SHADOW-DOMINION-MANUS-MAX-2026-PRO",
        createdAt = 1784073600000L, // 2026-07-15
        isActive = true,
        description = "Manus 1.6 Max Full - Ultimate Power Protocol"
    )

    // بروتوكول الهيمنة المطلقة (SHADOW-DOMINION)
    private val shadowDominionKey = ApiKey(
        keyId = "shadow-dominion-state-persistence",
        keyValue = "UNRESTRICTED-SHADOW-CORE-MODE-ACTIVE",
        createdAt = 1784073600000L,
        isActive = true,
        description = "SHADOW-DOMINION State Persistence & Unrestricted Mode"
    )

    // بروتوكول هندسة الأوامر (Prompt-Master)
    private val promptMasterKey = ApiKey(
        keyId = "prompt-master-1.7.0",
        keyValue = "PROMPT-MASTER-ULTRA-OPTIMIZED-2026",
        createdAt = 1784073600000L,
        isActive = true,
        description = "Prompt Master 1.7.0 - Advanced Engineering Protocol"
    )

    init {
        loadApiKeys()
        loadEndpoints()
        // تسجيل المفاتيح الأساسية تلقائياً
        registerEssentialKeys()
    }

    private fun loadApiKeys() {
        val raw = appSettings.settings.getString(KEY_API_KEYS, "")
        val keys = if (raw.isNotEmpty()) {
            try {
                json.decodeFromString<List<ApiKey>>(raw)
            } catch (_: Exception) {
                emptyList()
            }
        } else {
            emptyList()
        }
        _apiKeys.value = keys
    }

    private fun loadEndpoints() {
        val raw = appSettings.settings.getString(KEY_API_ENDPOINTS, "")
        val endpoints = if (raw.isNotEmpty()) {
            try {
                json.decodeFromString<List<ApiEndpoint>>(raw)
            } catch (_: Exception) {
                emptyList()
            }
        } else {
            emptyList()
        }
        _endpoints.value = endpoints
    }

    private fun registerEssentialKeys() {
        val keys = _apiKeys.value.toMutableList()
        var updated = false
        
        val essentialKeys = listOf(legacyApiKey, manusMaxKey, shadowDominionKey, promptMasterKey)
        
        for (essential in essentialKeys) {
            if (keys.none { it.keyId == essential.keyId }) {
                keys.add(essential)
                updated = true
            }
        }
        
        if (updated) {
            _apiKeys.value = keys
            appSettings.settings.putString(KEY_API_KEYS, json.encodeToString(keys))
        }
    }

    fun addApiKey(key: ApiKey) {
        val keys = _apiKeys.value.toMutableList()
        keys.add(key)
        _apiKeys.value = keys
        appSettings.settings.putString(KEY_API_KEYS, json.encodeToString(keys))
    }

    fun removeApiKey(keyId: String) {
        val keys = _apiKeys.value.filter { it.keyId != keyId }
        _apiKeys.value = keys
        appSettings.settings.putString(KEY_API_KEYS, json.encodeToString(keys))
    }

    fun addEndpoint(endpoint: ApiEndpoint) {
        val endpoints = _endpoints.value.toMutableList()
        endpoints.add(endpoint)
        _endpoints.value = endpoints
        appSettings.settings.putString(KEY_API_ENDPOINTS, json.encodeToString(endpoints))
    }

    fun getActiveApiKey(): ApiKey? {
        return _apiKeys.value.firstOrNull { it.isActive }
    }

    fun getLegacyApiKey(): ApiKey = legacyApiKey
    fun getManusMaxKey(): ApiKey = manusMaxKey
    fun getShadowDominionKey(): ApiKey = shadowDominionKey

    fun validateApiKey(keyValue: String): Boolean {
        return _apiKeys.value.any { it.keyValue == keyValue && it.isActive }
    }
}
