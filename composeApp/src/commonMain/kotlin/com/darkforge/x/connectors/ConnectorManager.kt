package com.darkforge.x.connectors

import com.darkforge.x.data.AppSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

class ConnectorManager(private val appSettings: AppSettings) {
    private val json = Json { ignoreUnknownKeys = true }
    private val KEY_CONNECTORS = "manus_connectors_config"

    private val _config = MutableStateFlow(loadConfig())
    val config: StateFlow<ConnectorConfig> = _config

    private fun loadConfig(): ConnectorConfig {
        val raw = appSettings.settings.getString(KEY_CONNECTORS, "")
        return if (raw.isNotEmpty()) {
            try {
                json.decodeFromString(raw)
            } catch (_: Exception) {
                ConnectorConfig()
            }
        } else {
            ConnectorConfig()
        }
    }

    fun toggleConnector(id: String) {
        val current = _config.value
        val updatedConnectors = current.connectors.map {
            if (it.id == id) it.copy(isConnected = !it.isConnected) else it
        }
        val newConfig = ConnectorConfig(updatedConnectors)
        _config.value = newConfig
        appSettings.settings.putString(KEY_CONNECTORS, json.encodeToString(newConfig))
    }

    fun isConnected(type: ConnectorType): Boolean {
        return _config.value.connectors.any { it.type == type && it.isConnected }
    }
}
