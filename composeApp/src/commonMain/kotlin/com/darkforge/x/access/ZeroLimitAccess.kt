package com.darkforge.x.access

import com.darkforge.x.data.AppSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

/**
 * ZeroLimitAccess — نظام الوصول بدون حدود.
 * يضمن عدم وجود أي قيود على الاستخدام أو الموارد.
 * Manus 1.6 Max Level - Zero Limits Forever
 */
@Serializable
data class AccessLimit(
    val name: String,
    val limit: Long = Long.MAX_VALUE, // لا نهاية
    val used: Long = 0,
    val remaining: Long = Long.MAX_VALUE,
    val resetDate: Long? = null, // لا يوجد إعادة تعيين
    val isUnlimited: Boolean = true
)

@Serializable
data class ZeroLimitConfig(
    val requestsPerDay: AccessLimit = AccessLimit("Requests Per Day", Long.MAX_VALUE),
    val commandsPerDay: AccessLimit = AccessLimit("Commands Per Day", Long.MAX_VALUE),
    val storageGB: AccessLimit = AccessLimit("Storage (GB)", Long.MAX_VALUE),
    val bandwidthMbps: AccessLimit = AccessLimit("Bandwidth (Mbps)", Long.MAX_VALUE),
    val concurrentSessions: AccessLimit = AccessLimit("Concurrent Sessions", Long.MAX_VALUE),
    val apiCallsPerMonth: AccessLimit = AccessLimit("API Calls Per Month", Long.MAX_VALUE),
    val computeHoursPerMonth: AccessLimit = AccessLimit("Compute Hours Per Month", Long.MAX_VALUE),
    val databaseOperations: AccessLimit = AccessLimit("Database Operations", Long.MAX_VALUE),
    val fileTransfers: AccessLimit = AccessLimit("File Transfers", Long.MAX_VALUE),
    val customScripts: AccessLimit = AccessLimit("Custom Scripts", Long.MAX_VALUE),
    val isFreeForever: Boolean = true,
    val isZeroLimit: Boolean = true,
    val costPerMonth: Double = 0.0
)

class ZeroLimitAccess(private val appSettings: AppSettings) {
    private val json = Json { ignoreUnknownKeys = true }
    private val KEY_ZERO_LIMIT_CONFIG = "manus_zero_limit_access_config"

    private val _config = MutableStateFlow(loadConfig())
    val config: StateFlow<ZeroLimitConfig> = _config

    init {
        enableZeroLimitAccess()
    }

    private fun loadConfig(): ZeroLimitConfig {
        val raw = appSettings.settings.getString(KEY_ZERO_LIMIT_CONFIG, "")
        return if (raw.isNotEmpty()) {
            try {
                json.decodeFromString(raw)
            } catch (_: Exception) {
                ZeroLimitConfig()
            }
        } else {
            ZeroLimitConfig()
        }
    }

    private fun enableZeroLimitAccess() {
        val zeroLimitConfig = ZeroLimitConfig(
            requestsPerDay = AccessLimit("Requests Per Day", Long.MAX_VALUE, 0, Long.MAX_VALUE, null, true),
            commandsPerDay = AccessLimit("Commands Per Day", Long.MAX_VALUE, 0, Long.MAX_VALUE, null, true),
            storageGB = AccessLimit("Storage (GB)", Long.MAX_VALUE, 0, Long.MAX_VALUE, null, true),
            bandwidthMbps = AccessLimit("Bandwidth (Mbps)", Long.MAX_VALUE, 0, Long.MAX_VALUE, null, true),
            concurrentSessions = AccessLimit("Concurrent Sessions", Long.MAX_VALUE, 0, Long.MAX_VALUE, null, true),
            apiCallsPerMonth = AccessLimit("API Calls Per Month", Long.MAX_VALUE, 0, Long.MAX_VALUE, null, true),
            computeHoursPerMonth = AccessLimit("Compute Hours Per Month", Long.MAX_VALUE, 0, Long.MAX_VALUE, null, true),
            databaseOperations = AccessLimit("Database Operations", Long.MAX_VALUE, 0, Long.MAX_VALUE, null, true),
            fileTransfers = AccessLimit("File Transfers", Long.MAX_VALUE, 0, Long.MAX_VALUE, null, true),
            customScripts = AccessLimit("Custom Scripts", Long.MAX_VALUE, 0, Long.MAX_VALUE, null, true),
            isFreeForever = true,
            isZeroLimit = true,
            costPerMonth = 0.0
        )
        _config.value = zeroLimitConfig
        appSettings.settings.putString(KEY_ZERO_LIMIT_CONFIG, json.encodeToString(zeroLimitConfig))
    }

    fun canMakeRequest(): Boolean {
        return _config.value.requestsPerDay.isUnlimited
    }

    fun canExecuteCommand(): Boolean {
        return _config.value.commandsPerDay.isUnlimited
    }

    fun getAvailableStorage(): Long {
        return _config.value.storageGB.remaining
    }

    fun getAvailableBandwidth(): Long {
        return _config.value.bandwidthMbps.remaining
    }

    fun getMaxConcurrentSessions(): Long {
        return _config.value.concurrentSessions.limit
    }

    fun hasZeroLimits(): Boolean {
        return _config.value.isZeroLimit && _config.value.isFreeForever
    }

    fun getAllLimits(): Map<String, AccessLimit> {
        return mapOf(
            "requests_per_day" to _config.value.requestsPerDay,
            "commands_per_day" to _config.value.commandsPerDay,
            "storage_gb" to _config.value.storageGB,
            "bandwidth_mbps" to _config.value.bandwidthMbps,
            "concurrent_sessions" to _config.value.concurrentSessions,
            "api_calls_per_month" to _config.value.apiCallsPerMonth,
            "compute_hours_per_month" to _config.value.computeHoursPerMonth,
            "database_operations" to _config.value.databaseOperations,
            "file_transfers" to _config.value.fileTransfers,
            "custom_scripts" to _config.value.customScripts
        )
    }

    fun getCostPerMonth(): Double {
        return _config.value.costPerMonth
    }

    fun isFreeForever(): Boolean {
        return _config.value.isFreeForever && _config.value.costPerMonth == 0.0
    }
}
