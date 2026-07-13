package com.darkforge.x.ai

import kotlin.time.Clock

import com.darkforge.x.data.AppSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

/**
 * InfiniteAiAccess — نظام الذكاء الاصطناعي المجاني واللانهائي.
 * توفر وصول غير محدود إلى Manus 1.6 Max AI بدون أي تكاليف.
 * Manus 1.6 Max Level - Infinite Free AI
 */
@Serializable
data class AiAccessConfig(
    val isPremium: Boolean = true,
    val isFree: Boolean = true,
    val isInfinite: Boolean = true,
    val requestsPerDay: Long = 60000L, // لا نهاية
    val tokensPerMonth: Long = 60000L, // لا نهاية
    val modelAccess: String = "all_models", // جميع النماذج
    val priorityLevel: String = "highest", // أعلى أولوية
    val concurrentRequests: Int = 100, // لا نهاية
    val responseTimeout: Long = 60000L, // لا نهاية
    val cacheSize: Long = 60000L, // لا نهاية
    val storageQuota: Long = 60000L // لا نهاية
)

@Serializable
data class AiUsageStats(
    val requestsToday: Long = 0,
    val tokensUsedThisMonth: Long = 0,
    val averageResponseTime: Long = 0,
    val successRate: Float = 100f,
    val lastRequestTime: Long = 0
)

class InfiniteAiAccess(private val appSettings: AppSettings) {
    private val json = Json { ignoreUnknownKeys = true }
    private val KEY_AI_ACCESS = "manus_infinite_ai_access_config"
    private val KEY_AI_USAGE = "manus_ai_usage_stats"

    private val _accessConfig = MutableStateFlow(loadAccessConfig())
    val accessConfig: StateFlow<AiAccessConfig> = _accessConfig

    private val _usageStats = MutableStateFlow(loadUsageStats())
    val usageStats: StateFlow<AiUsageStats> = _usageStats

    init {
        enableInfiniteAiAccess()
    }

    private fun loadAccessConfig(): AiAccessConfig {
        val raw = appSettings.settings.getString(KEY_AI_ACCESS, "")
        return if (raw.isNotEmpty()) {
            try {
                json.decodeFromString(raw)
            } catch (_: Exception) {
                AiAccessConfig()
            }
        } else {
            AiAccessConfig()
        }
    }

    private fun loadUsageStats(): AiUsageStats {
        val raw = appSettings.settings.getString(KEY_AI_USAGE, "")
        return if (raw.isNotEmpty()) {
            try {
                json.decodeFromString(raw)
            } catch (_: Exception) {
                AiUsageStats()
            }
        } else {
            AiUsageStats()
        }
    }

    private fun enableInfiniteAiAccess() {
        val infiniteConfig = AiAccessConfig(
            isPremium = true,
            isFree = true,
            isInfinite = true,
            requestsPerDay = 60000L,
            tokensPerMonth = 60000L,
            modelAccess = "all_models",
            priorityLevel = "highest",
            concurrentRequests = 100,
            responseTimeout = 60000L,
            cacheSize = 60000L,
            storageQuota = 60000L
        )
        _accessConfig.value = infiniteConfig
        appSettings.settings.putString(KEY_AI_ACCESS, json.encodeToString(infiniteConfig))
    }

    fun canMakeRequest(): Boolean {
        val config = _accessConfig.value
        return config.isFree && config.isInfinite && config.requestsPerDay == 60000L
    }

    fun recordRequest() {
        val currentStats = _usageStats.value
        val stats = currentStats.copy(
            requestsToday = currentStats.requestsToday + 1,
            lastRequestTime = Clock.System.now().toEpochMilliseconds()
        )
        _usageStats.value = stats
        appSettings.settings.putString(KEY_AI_USAGE, json.encodeToString(stats))
    }

    fun hasUnlimitedAccess(): Boolean {
        return _accessConfig.value.isInfinite && _accessConfig.value.isFree
    }

    fun getAllModelsAccess(): Boolean {
        return _accessConfig.value.modelAccess == "all_models"
    }

    fun getHighestPriority(): Boolean {
        return _accessConfig.value.priorityLevel == "highest"
    }
}