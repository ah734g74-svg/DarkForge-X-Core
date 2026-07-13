package com.darkforge.x.sms

import com.darkforge.x.data.SmsMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

actual class SmsReader actual constructor() {
    actual fun isSupported(): Boolean = true

    actual fun hasPermission(): Boolean {
        // Check if mmcli or gammu is available
        return isToolAvailable("mmcli") || isToolAvailable("gammu")
    }

    private fun isToolAvailable(tool: String): Boolean {
        return try {
            val process = ProcessBuilder("which", tool).start()
            process.waitFor() == 0
        } catch (e: Exception) {
            false
        }
    }

    actual suspend fun readInboxSince(lastSeenId: Long, limit: Int): List<SmsMessage> = withContext(Dispatchers.IO) {
        // Implementation for Linux (placeholder for actual modem integration)
        // In a real scenario, this would parse output from mmcli or gammu
        emptyList()
    }

    actual suspend fun readById(id: Long): SmsMessage? = null

    actual suspend fun search(query: String, limit: Int): List<SmsMessage> = emptyList()

    actual suspend fun currentMaxInboxId(): Long = 0L
}
