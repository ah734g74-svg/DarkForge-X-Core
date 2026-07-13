package com.darkforge.x.cloud

import kotlin.time.Clock

import com.darkforge.x.data.AppSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

/**
 * Cloud Computer Manager — نظام الكمبيوتر السحابي الكامل لـ Manus 1.6 Max.
 * يدير الموارد السحابية والملفات والقواعد والـ APIs بكفاءة عالية.
 */
@Serializable
data class CloudComputerConfig(
    val isEnabled: Boolean = false,
    val computerId: String = "kai-cloud-$(timestamp)",
    val region: String = "auto",
    val cpuCores: Int = 4,
    val memoryGB: Int = 8,
    val storageGB: Int = 100,
    val legacyApiKey: String = "2004-06-29-cloud-api-key"
)

@Serializable
data class CloudSession(
    val sessionId: String,
    val status: String, // "active", "idle", "suspended"
    val createdAt: Long,
    val lastActivity: Long,
    val resourceUsage: ResourceUsage
)

@Serializable
data class ResourceUsage(
    val cpuPercent: Float = 0f,
    val memoryMB: Long = 0,
    val storageMB: Long = 0
)

class CloudComputerManager(private val appSettings: AppSettings) {
    private val json = Json { ignoreUnknownKeys = true }
    private val KEY_CLOUD_CONFIG = "manus_cloud_computer_config"
    private val KEY_CLOUD_SESSIONS = "manus_cloud_sessions"

    private val _config = MutableStateFlow(loadConfig())
    val config: StateFlow<CloudComputerConfig> = _config

    private val _sessions = MutableStateFlow<List<CloudSession>>(emptyList())
    val sessions: StateFlow<List<CloudSession>> = _sessions

    private fun loadConfig(): CloudComputerConfig {
        val raw = appSettings.settings.getString(KEY_CLOUD_CONFIG, "")
        return if (raw.isNotEmpty()) {
            try {
                json.decodeFromString(raw)
            } catch (_: Exception) {
                CloudComputerConfig()
            }
        } else {
            CloudComputerConfig()
        }
    }

    fun enableCloudComputer(config: CloudComputerConfig) {
        val newConfig = config.copy(isEnabled = true)
        _config.value = newConfig
        appSettings.settings.putString(KEY_CLOUD_CONFIG, json.encodeToString(newConfig))
    }

    fun disableCloudComputer() {
        val newConfig = _config.value.copy(isEnabled = false)
        _config.value = newConfig
        appSettings.settings.putString(KEY_CLOUD_CONFIG, json.encodeToString(newConfig))
    }

    fun createSession(sessionId: String): CloudSession {
        val session = CloudSession(
            sessionId = sessionId,
            status = "active",
            createdAt = Clock.System.now().toEpochMilliseconds(),
            lastActivity = Clock.System.now().toEpochMilliseconds(),
            resourceUsage = ResourceUsage()
        )
        val currentSessions = _sessions.value.toMutableList()
        currentSessions.add(session)
        _sessions.value = currentSessions
        appSettings.settings.putString(KEY_CLOUD_SESSIONS, json.encodeToString(currentSessions))
        return session
    }

    fun updateSessionActivity(sessionId: String) {
        val updated = _sessions.value.map { session ->
            if (session.sessionId == sessionId) {
                session.copy(lastActivity = Clock.System.now().toEpochMilliseconds())
            } else {
                session
            }
        }
        _sessions.value = updated
        appSettings.settings.putString(KEY_CLOUD_SESSIONS, json.encodeToString(updated))
    }

    fun getSessionStatus(sessionId: String): String {
        return _sessions.value.firstOrNull { it.sessionId == sessionId }?.status ?: "unknown"
    }
}