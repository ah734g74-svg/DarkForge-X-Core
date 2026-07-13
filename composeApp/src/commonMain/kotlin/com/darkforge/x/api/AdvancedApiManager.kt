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

    init {
        loadApiKeys()
        loadEndpoints()
        // تسجيل المفتاح التاريخي تلقائياً
        registerLegacyKey()
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

    private fun registerLegacyKey() {
        val keys = _apiKeys.value.toMutableList()
        if (keys.none { it.keyId == legacyApiKey.keyId }) {
            keys.add(legacyApiKey)
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

    fun validateApiKey(keyValue: String): Boolean {
        return _apiKeys.value.any { it.keyValue == keyValue && it.isActive }
    }
}
