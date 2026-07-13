package com.darkforge.x.terminal

import kotlin.time.Clock

import com.darkforge.x.data.AppSettings
import com.darkforge.x.data.SelfEvolutionEngine
import com.darkforge.x.data.OmegaExecutionEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

/**
 * InfiniteTerminalEngine — محرك الطرفية اللانهائية الخارق.
 * نظام طرفي متقدم يدعم جميع ميزات Manus 1.6 Max بدون حدود.
 * Manus 1.6 Max Level - Infinite Terminal
 */
@Serializable
data class TerminalCommand(
    val id: String,
    val command: String,
    val timestamp: Long,
    val priority: String = "normal", // normal, high, critical
    val isParallel: Boolean = false,
    val timeout: Long = 60000L,
    val retries: Int = 100
)

@Serializable
data class TerminalOutput(
    val commandId: String,
    val output: String,
    val error: String = "",
    val exitCode: Int = 0,
    val executionTime: Long = 0,
    val status: String = "completed" // completed, running, failed, pending
)

@Serializable
data class TerminalSession(
    val sessionId: String,
    val isActive: Boolean = true,
    val createdAt: Long,
    val commandCount: Long = 0,
    val totalExecutionTime: Long = 0,
    val successRate: Float = 100f,
    val isInfinite: Boolean = true,
    val features: List<String> = listOf(
        "parallel_execution",
        "auto_retry",
        "smart_caching",
        "ai_suggestions",
        "syntax_highlighting",
        "command_history",
        "auto_completion",
        "error_recovery"
    )
)

class InfiniteTerminalEngine(
    private val appSettings: AppSettings,
    private val selfEvolutionEngine: SelfEvolutionEngine,
    private val omegaExecutionEngine: OmegaExecutionEngine
) {
    private val json = Json { ignoreUnknownKeys = true }
    private val KEY_TERMINAL_SESSIONS = "manus_infinite_terminal_sessions"
    private val KEY_TERMINAL_HISTORY = "manus_terminal_command_history"

    private val _sessions = MutableStateFlow<List<TerminalSession>>(emptyList())
    val sessions: StateFlow<List<TerminalSession>> = _sessions

    private val _commandHistory = MutableStateFlow<List<TerminalCommand>>(emptyList())
    val commandHistory: StateFlow<List<TerminalCommand>> = _commandHistory

    private val _terminalOutput = MutableStateFlow<List<TerminalOutput>>(emptyList())
    val terminalOutput: StateFlow<List<TerminalOutput>> = _terminalOutput

    init {
        loadSessions()
        loadCommandHistory()
    }

    private fun loadSessions() {
        val raw = appSettings.settings.getString(KEY_TERMINAL_SESSIONS, "")
        val sessions = if (raw.isNotEmpty()) {
            try {
                json.decodeFromString<List<TerminalSession>>(raw)
            } catch (_: Exception) {
                emptyList()
            }
        } else {
            emptyList()
        }
        _sessions.value = sessions
    }

    private fun loadCommandHistory() {
        val raw = appSettings.settings.getString(KEY_TERMINAL_HISTORY, "")
        val history = if (raw.isNotEmpty()) {
            try {
                json.decodeFromString<List<TerminalCommand>>(raw)
            } catch (_: Exception) {
                emptyList()
            }
        } else {
            emptyList()
        }
        _commandHistory.value = history
    }

    fun createInfiniteSession(sessionId: String): TerminalSession {
        val session = TerminalSession(
            sessionId = sessionId,
            isActive = true,
            createdAt = Clock.System.now().toEpochMilliseconds(),
            isInfinite = true,
            features = listOf(
                "parallel_execution",
                "auto_retry",
                "smart_caching",
                "ai_suggestions",
                "syntax_highlighting",
                "command_history",
                "auto_completion",
                "error_recovery",
                "manus_1_6_max_features",
                "infinite_access",
                "free_forever"
            )
        )
        val sessions = _sessions.value.toMutableList()
        sessions.add(session)
        _sessions.value = sessions
        appSettings.settings.putString(KEY_TERMINAL_SESSIONS, json.encodeToString(sessions))
        return session
    }

    suspend fun executeInfiniteCommand(
        sessionId: String,
        command: String,
        priority: String = "normal",
        isParallel: Boolean = false
    ): TerminalOutput {
        // التحقق من الأمر باستخدام محرك التطور الذاتي
        val isSafe = selfEvolutionEngine.verifyAndMonitor(command) { /* log */ }
        
        if (!isSafe) {
            return TerminalOutput(
                commandId = "cmd-${Clock.System.now().toEpochMilliseconds()}",
                output = "",
                error = "Command failed safety verification",
                exitCode = 1,
                status = "failed"
            )
        }

        // تسجيل الأمر في السجل
        val terminalCommand = TerminalCommand(
            id = "cmd-${Clock.System.now().toEpochMilliseconds()}",
            command = command,
            timestamp = Clock.System.now().toEpochMilliseconds(),
            priority = priority,
            isParallel = isParallel,
            timeout = 60000L,
            retries = 100
        )
        
        val history = _commandHistory.value.toMutableList()
        history.add(terminalCommand)
        _commandHistory.value = history
        appSettings.settings.putString(KEY_TERMINAL_HISTORY, json.encodeToString(history))

        // تنفيذ الأمر باستخدام محرك Omega
        val startTime = Clock.System.now().toEpochMilliseconds()
        val result = omegaExecutionEngine.executeOmega(command)
        val executionTime = Clock.System.now().toEpochMilliseconds() - startTime

        val output = TerminalOutput(
            commandId = terminalCommand.id,
            output = result["output"]?.toString() ?: "",
            error = result["error"]?.toString() ?: "",
            exitCode = (result["exitCode"] as? Number)?.toInt() ?: 0,
            executionTime = executionTime,
            status = if (result["success"] as? Boolean == true) "completed" else "failed"
        )

        val outputs = _terminalOutput.value.toMutableList()
        outputs.add(output)
        _terminalOutput.value = outputs

        // تحديث إحصائيات الجلسة
        updateSessionStats(sessionId, output)

        return output
    }

    private fun updateSessionStats(sessionId: String, output: TerminalOutput) {
        val updated = _sessions.value.map { session ->
            if (session.sessionId == sessionId) {
                val newCommandCount = session.commandCount + 1
                val isSuccess = output.status == "completed"
                val newSuccessRate = if (newCommandCount > 0) {
                    ((_terminalOutput.value.count { it.status == "completed" }.toFloat() / newCommandCount) * 100)
                } else {
                    100f
                }
                session.copy(
                    commandCount = newCommandCount,
                    totalExecutionTime = session.totalExecutionTime + output.executionTime,
                    successRate = newSuccessRate
                )
            } else {
                session
            }
        }
        _sessions.value = updated
        appSettings.settings.putString(KEY_TERMINAL_SESSIONS, json.encodeToString(updated))
    }

    fun getSessionStats(sessionId: String): TerminalSession? {
        return _sessions.value.firstOrNull { it.sessionId == sessionId }
    }

    fun clearHistory() {
        _commandHistory.value = emptyList()
        _terminalOutput.value = emptyList()
        appSettings.settings.putString(KEY_TERMINAL_HISTORY, json.encodeToString(emptyList<TerminalCommand>()))
    }

    fun hasInfiniteFeatures(): Boolean {
        return _sessions.value.any { it.isInfinite }
    }

    fun getAvailableFeatures(): List<String> {
        return listOf(
            "parallel_execution",
            "auto_retry",
            "smart_caching",
            "ai_suggestions",
            "syntax_highlighting",
            "command_history",
            "auto_completion",
            "error_recovery",
            "manus_1_6_max_integration",
            "infinite_access",
            "free_forever",
            "zero_limits"
        )
    }
}