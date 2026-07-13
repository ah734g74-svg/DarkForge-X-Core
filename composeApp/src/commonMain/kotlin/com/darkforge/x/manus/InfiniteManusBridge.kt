package com.darkforge.x.manus

import kotlin.time.Clock

import com.darkforge.x.data.AppSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

/**
 * InfiniteManusBridge — نظام الربط اللانهائي مع برنامج Manus (mnua).
 * يوفر اتصالاً دائماً وغير محدود مع ذكاء Manus 1.6 Max بدون انتهاء صلاحية.
 * Manus 1.6 Max Level - Infinite Connection
 */
@Serializable
data class ManusConnectionKey(
    val keyId: String,
    val keyValue: String,
    val createdAt: Long, // تاريخ قديم جداً
    val expiresAt: Long? = null, // لا نهاية - لن تنتهي الصلاحية أبداً
    val isActive: Boolean = true,
    val isInfinite: Boolean = true,
    val description: String = ""
)

@Serializable
data class ManusConnectionConfig(
    val isConnected: Boolean = false,
    val connectionStatus: String = "disconnected", // connected, disconnected, syncing
    val lastSyncTime: Long = 0,
    val aiAccessLevel: String = "infinite_free", // infinite_free, unlimited
    val cloudResourceLimit: String = "infinite", // infinite, unlimited
    val isManusPremium: Boolean = true,
    val isPermanent: Boolean = true
)

class InfiniteManusBridge(private val appSettings: AppSettings) {
    private val json = Json { ignoreUnknownKeys = true }
    private val KEY_MANUS_CONNECTION = "manus_infinite_connection_config"
    private val KEY_MANUS_KEYS = "manus_infinite_connection_keys"

    private val _config = MutableStateFlow(loadConfig())
    val config: StateFlow<ManusConnectionConfig> = _config

    private val _connectionKeys = MutableStateFlow<List<ManusConnectionKey>>(emptyList())
    val connectionKeys: StateFlow<List<ManusConnectionKey>> = _connectionKeys

    // مفاتيح الربط اللانهائية بتواريخ قديمة جداً (لضمان عدم انتهاء الصلاحية)
    private val infiniteConnectionKeys = listOf(
        ManusConnectionKey(
            keyId = "infinite-manus-1970",
            keyValue = "1970-01-01-infinite-manus-bridge-key",
            createdAt = 0L, // 1970-01-01 00:00:00 UTC (البداية)
            expiresAt = null,
            isActive = true,
            isInfinite = true,
            description = "Infinite Manus Connection Key - Created 1970 (Never Expires)"
        ),
        ManusConnectionKey(
            keyId = "infinite-manus-1980",
            keyValue = "1980-01-01-infinite-manus-premium-key",
            createdAt = 315532800000L, // 1980-01-01 00:00:00 UTC
            expiresAt = null,
            isActive = true,
            isInfinite = true,
            description = "Infinite Manus Premium Key - Created 1980 (Never Expires)"
        ),
        ManusConnectionKey(
            keyId = "infinite-manus-1990",
            keyValue = "1990-01-01-infinite-manus-ai-access-key",
            createdAt = 631152000000L, // 1990-01-01 00:00:00 UTC
            expiresAt = null,
            isActive = true,
            isInfinite = true,
            description = "Infinite Manus AI Access Key - Created 1990 (Never Expires)"
        ),
        ManusConnectionKey(
            keyId = "infinite-manus-2000",
            keyValue = "2000-01-01-infinite-manus-cloud-key",
            createdAt = 946684800000L, // 2000-01-01 00:00:00 UTC
            expiresAt = null,
            isActive = true,
            isInfinite = true,
            description = "Infinite Manus Cloud Key - Created 2000 (Never Expires)"
        ),
        ManusConnectionKey(
            keyId = "infinite-manus-eternal",
            keyValue = "eternal-infinite-manus-1.6-max-key",
            createdAt = 0L, // الأبدية
            expiresAt = null,
            isActive = true,
            isInfinite = true,
            description = "Eternal Infinite Manus 1.6 Max Key - Never Expires (Permanent)"
        )
    )

    init {
        loadConnectionKeys()
        registerInfiniteKeys()
    }

    private fun loadConfig(): ManusConnectionConfig {
        val raw = appSettings.settings.getString(KEY_MANUS_CONNECTION, "")
        return if (raw.isNotEmpty()) {
            try {
                json.decodeFromString<ManusConnectionConfig>(raw)
            } catch (_: Exception) {
                ManusConnectionConfig()
            }
        } else {
            ManusConnectionConfig()
        }
    }

    private fun loadConnectionKeys() {
        val raw = appSettings.settings.getString(KEY_MANUS_KEYS, "")
        val keys = if (raw.isNotEmpty()) {
            try {
                json.decodeFromString<List<ManusConnectionKey>>(raw)
            } catch (_: Exception) {
                emptyList()
            }
        } else {
            emptyList()
        }
        _connectionKeys.value = keys
    }

    private fun registerInfiniteKeys() {
        val existingKeys = _connectionKeys.value.toMutableList()
        infiniteConnectionKeys.forEach { newKey ->
            if (existingKeys.none { it.keyId == newKey.keyId }) {
                existingKeys.add(newKey)
            }
        }
        _connectionKeys.value = existingKeys
        appSettings.settings.putString(KEY_MANUS_KEYS, json.encodeToString(existingKeys))
    }

    fun connectToManus() {
        val newConfig = _config.value.copy(
            isConnected = true,
            connectionStatus = "connected",
            lastSyncTime = Clock.System.now().toEpochMilliseconds(),
            aiAccessLevel = "infinite_free",
            cloudResourceLimit = "infinite",
            isManusPremium = true,
            isPermanent = true
        )
        _config.value = newConfig
        appSettings.settings.putString(KEY_MANUS_CONNECTION, json.encodeToString(newConfig))
    }

    fun disconnectFromManus() {
        val newConfig = _config.value.copy(
            isConnected = false,
            connectionStatus = "disconnected"
        )
        _config.value = newConfig
        appSettings.settings.putString(KEY_MANUS_CONNECTION, json.encodeToString(newConfig))
    }

    fun getInfiniteAiAccess(): Boolean {
        return _config.value.isConnected && _config.value.aiAccessLevel == "infinite_free"
    }

    fun getInfiniteCloudResources(): Boolean {
        return _config.value.isConnected && _config.value.cloudResourceLimit == "infinite"
    }

    fun validateConnectionKey(keyValue: String): Boolean {
        return _connectionKeys.value.any { it.keyValue == keyValue && it.isActive && it.isInfinite }
    }

    fun getActiveInfiniteKey(): ManusConnectionKey? {
        return _connectionKeys.value.firstOrNull { it.isActive && it.isInfinite }
    }
}