package com.darkforge.x.sms

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

actual class SmsSender actual constructor() {
    actual fun hasPermission(): Boolean {
        // On Linux, we check if common SMS tools are available
        return isToolAvailable("mmcli") || isToolAvailable("gammu") || isToolAvailable("termux-sms-send")
    }

    private fun isToolAvailable(tool: String): Boolean {
        return try {
            val process = ProcessBuilder("which", tool).start()
            process.waitFor() == 0
        } catch (e: Exception) {
            false
        }
    }

    actual suspend fun send(address: String, body: String): SmsSendResult = withContext(Dispatchers.IO) {
        if (address.isBlank()) return@withContext SmsSendResult.Failure("Missing address")
        if (body.isEmpty()) return@withContext SmsSendResult.Failure("Empty body")

        try {
            when {
                isToolAvailable("mmcli") -> sendWithMmcli(address, body)
                isToolAvailable("gammu") -> sendWithGammu(address, body)
                isToolAvailable("termux-sms-send") -> sendWithTermux(address, body)
                else -> SmsSendResult.Failure("No SMS tool found (mmcli, gammu, or termux-sms-send)")
            }
        } catch (e: Exception) {
            SmsSendResult.Failure(e.message ?: "Failed to send SMS on Linux")
        }
    }

    private fun sendWithMmcli(address: String, body: String): SmsSendResult {
        // This is a simplified implementation for mmcli
        return try {
            val process = ProcessBuilder("mmcli", "-m", "any", "--messaging-create-sms=\"number=$address,text=$body\"").start()
            if (process.waitFor() == 0) {
                SmsSendResult.Success
            } else {
                SmsSendResult.Failure("mmcli failed to create SMS")
            }
        } catch (e: Exception) {
            SmsSendResult.Failure("mmcli error: ${e.message}")
        }
    }

    private fun sendWithGammu(address: String, body: String): SmsSendResult {
        return try {
            val process = ProcessBuilder("gammu", "sendsms", "TEXT", address, "-text", body).start()
            if (process.waitFor() == 0) {
                SmsSendResult.Success
            } else {
                SmsSendResult.Failure("gammu failed to send SMS")
            }
        } catch (e: Exception) {
            SmsSendResult.Failure("gammu error: ${e.message}")
        }
    }

    private fun sendWithTermux(address: String, body: String): SmsSendResult {
        return try {
            val process = ProcessBuilder("termux-sms-send", "-n", address, body).start()
            if (process.waitFor() == 0) {
                SmsSendResult.Success
            } else {
                SmsSendResult.Failure("termux-sms-send failed")
            }
        } catch (e: Exception) {
            SmsSendResult.Failure("termux error: ${e.message}")
        }
    }
}
