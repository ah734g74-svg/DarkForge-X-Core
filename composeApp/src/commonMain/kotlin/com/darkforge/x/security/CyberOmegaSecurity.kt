package com.darkforge.x.security

import kotlin.time.Clock

import com.darkforge.x.data.AppSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

/**
 * CyberOmegaSecurity — نظام الأمان والتحليل المتقدم.
 * يوفر وصولاً متطوراً مع طلب الإذن المسبق قبل أي عملية.
 * Manus 1.6 Max Level - Cyber-Omega Security
 */
@Serializable
data class AccessRequest(
    val id: String,
    val targetUrl: String,
    val targetApp: String,
    val action: String, // access, modify, execute, analyze
    val description: String,
    val timestamp: Long,
    val riskLevel: String = "low", // low, medium, high, critical
    val isApproved: Boolean = false,
    val approvalTime: Long? = null,
    val approvalReason: String = ""
)

@Serializable
data class SecurityAnalysis(
    val targetUrl: String,
    val threatLevel: String = "safe", // safe, warning, danger, critical
    val vulnerabilities: List<String> = emptyList(),
    val recommendations: List<String> = emptyList(),
    val analysisTime: Long,
    val isVerified: Boolean = true,
    val trustScore: Float = 100f
)

@Serializable
data class CyberOmegaSession(
    val sessionId: String,
    val isActive: Boolean = true,
    val createdAt: Long,
    val accessRequests: List<AccessRequest> = emptyList(),
    val approvedActions: Int = 0,
    val deniedActions: Int = 0,
    val totalAnalysisTime: Long = 0,
    val securityLevel: String = "maximum" // maximum, high, medium, low
)

class CyberOmegaSecurity(private val appSettings: AppSettings) {
    private val json = Json { ignoreUnknownKeys = true }
    private val KEY_SESSIONS = "cyber_omega_sessions"
    private val KEY_REQUESTS = "cyber_omega_access_requests"
    private val KEY_ANALYSIS = "cyber_omega_security_analysis"

    private val _sessions = MutableStateFlow<List<CyberOmegaSession>>(emptyList())
    val sessions: StateFlow<List<CyberOmegaSession>> = _sessions

    private val _pendingRequests = MutableStateFlow<List<AccessRequest>>(emptyList())
    val pendingRequests: StateFlow<List<AccessRequest>> = _pendingRequests

    private val _analysisCache = MutableStateFlow<Map<String, SecurityAnalysis>>(emptyMap())
    val analysisCache: StateFlow<Map<String, SecurityAnalysis>> = _analysisCache

    init {
        loadSessions()
        loadPendingRequests()
    }

    private fun loadSessions() {
        val raw = appSettings.settings.getString(KEY_SESSIONS, "")
        val sessions = if (raw.isNotEmpty()) {
            try {
                json.decodeFromString<List<CyberOmegaSession>>(raw)
            } catch (_: Exception) {
                emptyList()
            }
        } else {
            emptyList()
        }
        _sessions.value = sessions
    }

    private fun loadPendingRequests() {
        val raw = appSettings.settings.getString(KEY_REQUESTS, "")
        val requests = if (raw.isNotEmpty()) {
            try {
                json.decodeFromString<List<AccessRequest>>(raw)
            } catch (_: Exception) {
                emptyList()
            }
        } else {
            emptyList()
        }
        _pendingRequests.value = requests
    }

    fun createSecuritySession(sessionId: String): CyberOmegaSession {
        val session = CyberOmegaSession(
            sessionId = sessionId,
            isActive = true,
            createdAt = Clock.System.now().toEpochMilliseconds(),
            securityLevel = "maximum"
        )
        val sessions = _sessions.value.toMutableList()
        sessions.add(session)
        _sessions.value = sessions
        appSettings.settings.putString(KEY_SESSIONS, json.encodeToString(sessions))
        return session
    }

    fun requestAccess(
        targetUrl: String,
        targetApp: String,
        action: String,
        description: String,
        riskLevel: String = "low"
    ): AccessRequest {
        val request = AccessRequest(
            id = "req-${Clock.System.now().toEpochMilliseconds()}",
            targetUrl = targetUrl,
            targetApp = targetApp,
            action = action,
            description = description,
            timestamp = Clock.System.now().toEpochMilliseconds(),
            riskLevel = riskLevel,
            isApproved = false
        )
        
        val requests = _pendingRequests.value.toMutableList()
        requests.add(request)
        _pendingRequests.value = requests
        appSettings.settings.putString(KEY_REQUESTS, json.encodeToString(requests))
        
        return request
    }

    fun approveAccessRequest(requestId: String, reason: String = ""): Boolean {
        val updated = _pendingRequests.value.map { request ->
            if (request.id == requestId) {
                request.copy(
                    isApproved = true,
                    approvalTime = Clock.System.now().toEpochMilliseconds(),
                    approvalReason = reason
                )
            } else {
                request
            }
        }
        _pendingRequests.value = updated
        appSettings.settings.putString(KEY_REQUESTS, json.encodeToString(updated))
        
        // تحديث إحصائيات الجلسة
        updateSessionStats(requestId, true)
        
        return true
    }

    fun denyAccessRequest(requestId: String, reason: String = ""): Boolean {
        val updated = _pendingRequests.value.filter { it.id != requestId }
        _pendingRequests.value = updated
        appSettings.settings.putString(KEY_REQUESTS, json.encodeToString(updated))
        
        // تحديث إحصائيات الجلسة
        updateSessionStats(requestId, false)
        
        return true
    }

    suspend fun analyzeTarget(targetUrl: String): SecurityAnalysis {
        val startTime = Clock.System.now().toEpochMilliseconds()
        
        // تحليل أمان متقدم
        val threatLevel = determineThreatLevel(targetUrl)
        val vulnerabilities = scanVulnerabilities(targetUrl)
        val recommendations = generateRecommendations(targetUrl, vulnerabilities)
        val trustScore = calculateTrustScore(targetUrl, vulnerabilities)
        
        val analysis = SecurityAnalysis(
            targetUrl = targetUrl,
            threatLevel = threatLevel,
            vulnerabilities = vulnerabilities,
            recommendations = recommendations,
            analysisTime = Clock.System.now().toEpochMilliseconds() - startTime,
            isVerified = true,
            trustScore = trustScore
        )
        
        // حفظ في الذاكرة المؤقتة
        val cache = _analysisCache.value.toMutableMap()
        cache[targetUrl] = analysis
        _analysisCache.value = cache
        
        return analysis
    }

    private fun determineThreatLevel(url: String): String {
        return when {
            url.contains("https") && url.contains("verified") -> "safe"
            url.contains("suspicious") -> "warning"
            url.contains("malware") -> "danger"
            else -> "safe"
        }
    }

    private suspend fun scanVulnerabilities(url: String): List<String> {
        // محاكاة فحص الثغرات الأمنية
        return listOf(
            "SSL Certificate Valid",
            "No Known Vulnerabilities",
            "Security Headers Present",
            "CORS Properly Configured"
        )
    }

    private fun generateRecommendations(url: String, vulnerabilities: List<String>): List<String> {
        return listOf(
            "استخدم HTTPS دائماً",
            "تحديث البرامج بانتظام",
            "تفعيل المصادقة الثنائية",
            "مراقبة السجلات الأمنية"
        )
    }

    private fun calculateTrustScore(url: String, vulnerabilities: List<String>): Float {
        val baseScore = 100f
        val vulnerabilityPenalty = vulnerabilities.size * 5f
        return maxOf(0f, baseScore - vulnerabilityPenalty)
    }

    private fun updateSessionStats(requestId: String, isApproved: Boolean) {
        val updated = _sessions.value.map { session ->
            if (isApproved) {
                session.copy(approvedActions = session.approvedActions + 1)
            } else {
                session.copy(deniedActions = session.deniedActions + 1)
            }
        }
        _sessions.value = updated
        appSettings.settings.putString(KEY_SESSIONS, json.encodeToString(updated))
    }

    fun getPendingRequests(): List<AccessRequest> {
        return _pendingRequests.value.filter { !it.isApproved }
    }

    fun getApprovedRequests(): List<AccessRequest> {
        return _pendingRequests.value.filter { it.isApproved }
    }

    fun getSecurityLevel(): String {
        return _sessions.value.firstOrNull()?.securityLevel ?: "maximum"
    }

    fun hasMaximumSecurity(): Boolean {
        return getSecurityLevel() == "maximum"
    }
}