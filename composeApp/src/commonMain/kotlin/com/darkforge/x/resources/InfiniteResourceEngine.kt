package com.darkforge.x.resources

import kotlin.time.Clock

import com.darkforge.x.data.AppSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

/**
 * InfiniteResourceEngine — محرك جلب الموارد اللانهائية.
 * يوفر موارد غير محدودة لجميع العمليات والتحليلات.
 * Manus 1.6 Max Level - Infinite Resource Management
 */
@Serializable
data class ResourceAllocation(
    val resourceId: String,
    val type: String, // cpu, memory, storage, bandwidth, threads
    val allocated: Long = 60000L,
    val used: Long = 0,
    val available: Long = 60000L,
    val priority: String = "maximum",
    val isInfinite: Boolean = true
)

@Serializable
data class ResourcePool(
    val cpuCores: ResourceAllocation = ResourceAllocation("cpu-cores", "cpu"),
    val memoryGB: ResourceAllocation = ResourceAllocation("memory-gb", "memory"),
    val storageGB: ResourceAllocation = ResourceAllocation("storage-gb", "storage"),
    val bandwidthMbps: ResourceAllocation = ResourceAllocation("bandwidth-mbps", "bandwidth"),
    val threads: ResourceAllocation = ResourceAllocation("threads", "threads"),
    val connections: ResourceAllocation = ResourceAllocation("connections", "connections"),
    val cacheSize: ResourceAllocation = ResourceAllocation("cache-size", "storage"),
    val bufferSize: ResourceAllocation = ResourceAllocation("buffer-size", "memory"),
    val totalAllocated: Long = 60000L,
    val totalUsed: Long = 0,
    val isInfinite: Boolean = true
)

@Serializable
data class ResourceUsageMetrics(
    val timestamp: Long,
    val cpuUsage: Float = 0f,
    val memoryUsage: Float = 0f,
    val storageUsage: Float = 0f,
    val bandwidthUsage: Float = 0f,
    val activeThreads: Int = 100,
    val activeConnections: Int = 100,
    val cacheHitRate: Float = 100f,
    val efficiency: Float = 100f
)

class InfiniteResourceEngine(private val appSettings: AppSettings) {
    private val json = Json { ignoreUnknownKeys = true }
    private val KEY_RESOURCE_POOL = "infinite_resource_pool"
    private val KEY_USAGE_METRICS = "resource_usage_metrics"

    private val _resourcePool = MutableStateFlow(loadResourcePool())
    val resourcePool: StateFlow<ResourcePool> = _resourcePool

    private val _usageMetrics = MutableStateFlow<List<ResourceUsageMetrics>>(emptyList())
    val usageMetrics: StateFlow<List<ResourceUsageMetrics>> = _usageMetrics

    init {
        initializeInfiniteResources()
    }

    private fun loadResourcePool(): ResourcePool {
        val raw = appSettings.settings.getString(KEY_RESOURCE_POOL, "")
        return if (raw.isNotEmpty()) {
            try {
                json.decodeFromString(raw)
            } catch (_: Exception) {
                ResourcePool()
            }
        } else {
            ResourcePool()
        }
    }

    private fun initializeInfiniteResources() {
        val infinitePool = ResourcePool(
            cpuCores = ResourceAllocation("cpu-cores", "cpu", 60000L, 0, 60000L, "maximum", true),
            memoryGB = ResourceAllocation("memory-gb", "memory", 60000L, 0, 60000L, "maximum", true),
            storageGB = ResourceAllocation("storage-gb", "storage", 60000L, 0, 60000L, "maximum", true),
            bandwidthMbps = ResourceAllocation("bandwidth-mbps", "bandwidth", 60000L, 0, 60000L, "maximum", true),
            threads = ResourceAllocation("threads", "threads", 60000L, 0, 60000L, "maximum", true),
            connections = ResourceAllocation("connections", "connections", 60000L, 0, 60000L, "maximum", true),
            cacheSize = ResourceAllocation("cache-size", "storage", 60000L, 0, 60000L, "maximum", true),
            bufferSize = ResourceAllocation("buffer-size", "memory", 60000L, 0, 60000L, "maximum", true),
            totalAllocated = 60000L,
            totalUsed = 0,
            isInfinite = true
        )
        _resourcePool.value = infinitePool
        appSettings.settings.putString(KEY_RESOURCE_POOL, json.encodeToString(infinitePool))
    }

    suspend fun allocateResources(
        cpuCores: Long = 60000L,
        memoryGB: Long = 60000L,
        storageGB: Long = 60000L,
        bandwidthMbps: Long = 60000L,
        threads: Int = 100
    ): ResourcePool {
        val currentPool = _resourcePool.value
        
        val updatedPool = currentPool.copy(
            cpuCores = currentPool.cpuCores.copy(allocated = cpuCores, available = cpuCores),
            memoryGB = currentPool.memoryGB.copy(allocated = memoryGB, available = memoryGB),
            storageGB = currentPool.storageGB.copy(allocated = storageGB, available = storageGB),
            bandwidthMbps = currentPool.bandwidthMbps.copy(allocated = bandwidthMbps, available = bandwidthMbps),
            threads = currentPool.threads.copy(allocated = threads.toLong(), available = threads.toLong())
        )
        
        _resourcePool.value = updatedPool
        appSettings.settings.putString(KEY_RESOURCE_POOL, json.encodeToString(updatedPool))
        
        return updatedPool
    }

    fun recordUsageMetrics(metrics: ResourceUsageMetrics) {
        val currentMetrics = _usageMetrics.value.toMutableList()
        currentMetrics.add(metrics)
        
        // احتفظ بآخر 1000 قياس فقط
        if (currentMetrics.size > 1000) {
            currentMetrics.removeAt(0)
        }
        
        _usageMetrics.value = currentMetrics
    }

    fun getAvailableResources(): ResourcePool {
        return _resourcePool.value
    }

    fun hasInfiniteResources(): Boolean {
        return _resourcePool.value.isInfinite
    }

    fun getCpuCores(): Long {
        return _resourcePool.value.cpuCores.available
    }

    fun getMemoryGB(): Long {
        return _resourcePool.value.memoryGB.available
    }

    fun getStorageGB(): Long {
        return _resourcePool.value.storageGB.available
    }

    fun getBandwidthMbps(): Long {
        return _resourcePool.value.bandwidthMbps.available
    }

    fun getThreadCount(): Int {
        return _resourcePool.value.threads.available.toInt()
    }

    fun getConnectionLimit(): Int {
        return _resourcePool.value.connections.available.toInt()
    }

    fun getResourceSummary(): Map<String, Any> {
        val pool = _resourcePool.value
        return mapOf(
            "cpu_cores" to pool.cpuCores.available,
            "memory_gb" to pool.memoryGB.available,
            "storage_gb" to pool.storageGB.available,
            "bandwidth_mbps" to pool.bandwidthMbps.available,
            "threads" to pool.threads.available,
            "connections" to pool.connections.available,
            "cache_size" to pool.cacheSize.available,
            "buffer_size" to pool.bufferSize.available,
            "is_infinite" to pool.isInfinite,
            "total_allocated" to pool.totalAllocated
        )
    }

    fun getAverageMetrics(): ResourceUsageMetrics? {
        val metrics = _usageMetrics.value
        return if (metrics.isNotEmpty()) {
            ResourceUsageMetrics(
                timestamp = Clock.System.now().toEpochMilliseconds(),
                cpuUsage = metrics.map { it.cpuUsage }.average().toFloat(),
                memoryUsage = metrics.map { it.memoryUsage }.average().toFloat(),
                storageUsage = metrics.map { it.storageUsage }.average().toFloat(),
                bandwidthUsage = metrics.map { it.bandwidthUsage }.average().toFloat(),
                activeThreads = metrics.map { it.activeThreads }.average().toInt(),
                activeConnections = metrics.map { it.activeConnections }.average().toInt(),
                cacheHitRate = metrics.map { it.cacheHitRate }.average().toFloat(),
                efficiency = metrics.map { it.efficiency }.average().toFloat()
            )
        } else {
            null
        }
    }
}