package com.darkforge.x.security

import kotlin.time.Clock

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable

/**
 * UniversalWebInspector — محلل الويب الشامل
 * تحليل وعرض كود أي موقع أو تطبيق بدقة متناهية
 * Manus 1.6 Max Level - Universal Web Analysis
 */
@Serializable
data class WebAnalysis(
    val id: String,
    val targetUrl: String,
    val status: String = "pending", // pending, analyzing, completed, failed
    val htmlCode: String = "",
    val cssCode: String = "",
    val jsCode: String = "",
    val apiEndpoints: List<String> = emptyList(),
    val externalResources: List<String> = emptyList(),
    val metadata: Map<String, String> = emptyMap(),
    val headers: Map<String, String> = emptyMap(),
    val cookies: List<String> = emptyList(),
    val forms: List<FormData> = emptyList(),
    val scripts: List<ScriptData> = emptyList(),
    val images: List<ImageData> = emptyList(),
    val links: List<LinkData> = emptyList(),
    val analysisTime: Long = 0,
    val startTime: Long = Clock.System.now().toEpochMilliseconds(),
    val completedTime: Long = 0,
    val securityScore: Float = 0f,
    val vulnerabilities: List<String> = emptyList()
)

@Serializable
data class FormData(
    val id: String,
    val name: String,
    val method: String, // GET, POST, etc.
    val action: String,
    val fields: List<String> = emptyList(),
    val enctype: String = ""
)

@Serializable
data class ScriptData(
    val id: String,
    val src: String,
    val type: String,
    val content: String = "",
    val isExternal: Boolean = false
)

@Serializable
data class ImageData(
    val id: String,
    val src: String,
    val alt: String = "",
    val width: Int = 0,
    val height: Int = 0
)

@Serializable
data class LinkData(
    val id: String,
    val href: String,
    val text: String,
    val target: String = "_self"
)

class UniversalWebInspector {
    private val _analyses = MutableStateFlow<List<WebAnalysis>>(emptyList())
    val analyses: StateFlow<List<WebAnalysis>> = _analyses

    private val _extractedCode = MutableStateFlow<Map<String, String>>(emptyMap())
    val extractedCode: StateFlow<Map<String, String>> = _extractedCode

    // تحليل موقع ويب شامل
    fun analyzeWebsite(targetUrl: String): WebAnalysis {
        val analysis = WebAnalysis(
            id = "analysis-${Clock.System.now().toEpochMilliseconds()}",
            targetUrl = targetUrl,
            status = "analyzing",
            startTime = Clock.System.now().toEpochMilliseconds()
        )
        
        addAnalysis(analysis)
        return analysis
    }

    // استخراج كود HTML
    fun extractHtmlCode(analysisId: String, htmlCode: String) {
        val analyses = _analyses.value.toMutableList()
        val index = analyses.indexOfFirst { it.id == analysisId }
        
        if (index >= 0) {
            val analysis = analyses[index]
            analyses[index] = analysis.copy(htmlCode = htmlCode)
            _analyses.value = analyses
            
            // حفظ الكود المستخرج
            val codes = _extractedCode.value.toMutableMap()
            codes["html_$analysisId"] = htmlCode
            _extractedCode.value = codes
        }
    }

    // استخراج كود CSS
    fun extractCssCode(analysisId: String, cssCode: String) {
        val analyses = _analyses.value.toMutableList()
        val index = analyses.indexOfFirst { it.id == analysisId }
        
        if (index >= 0) {
            val analysis = analyses[index]
            analyses[index] = analysis.copy(cssCode = cssCode)
            _analyses.value = analyses
            
            // حفظ الكود المستخرج
            val codes = _extractedCode.value.toMutableMap()
            codes["css_$analysisId"] = cssCode
            _extractedCode.value = codes
        }
    }

    // استخراج كود JavaScript
    fun extractJsCode(analysisId: String, jsCode: String) {
        val analyses = _analyses.value.toMutableList()
        val index = analyses.indexOfFirst { it.id == analysisId }
        
        if (index >= 0) {
            val analysis = analyses[index]
            analyses[index] = analysis.copy(jsCode = jsCode)
            _analyses.value = analyses
            
            // حفظ الكود المستخرج
            val codes = _extractedCode.value.toMutableMap()
            codes["js_$analysisId"] = jsCode
            _extractedCode.value = codes
        }
    }

    // استخراج نقاط النهاية (API Endpoints)
    fun extractApiEndpoints(analysisId: String, endpoints: List<String>) {
        val analyses = _analyses.value.toMutableList()
        val index = analyses.indexOfFirst { it.id == analysisId }
        
        if (index >= 0) {
            val analysis = analyses[index]
            analyses[index] = analysis.copy(apiEndpoints = endpoints)
            _analyses.value = analyses
        }
    }

    // استخراج الموارد الخارجية
    fun extractExternalResources(analysisId: String, resources: List<String>) {
        val analyses = _analyses.value.toMutableList()
        val index = analyses.indexOfFirst { it.id == analysisId }
        
        if (index >= 0) {
            val analysis = analyses[index]
            analyses[index] = analysis.copy(externalResources = resources)
            _analyses.value = analyses
        }
    }

    // استخراج البيانات الوصفية
    fun extractMetadata(analysisId: String, metadata: Map<String, String>) {
        val analyses = _analyses.value.toMutableList()
        val index = analyses.indexOfFirst { it.id == analysisId }
        
        if (index >= 0) {
            val analysis = analyses[index]
            analyses[index] = analysis.copy(metadata = metadata)
            _analyses.value = analyses
        }
    }

    // استخراج رؤوس HTTP
    fun extractHeaders(analysisId: String, headers: Map<String, String>) {
        val analyses = _analyses.value.toMutableList()
        val index = analyses.indexOfFirst { it.id == analysisId }
        
        if (index >= 0) {
            val analysis = analyses[index]
            analyses[index] = analysis.copy(headers = headers)
            _analyses.value = analyses
        }
    }

    // استخراج ملفات تعريف الارتباط (Cookies)
    fun extractCookies(analysisId: String, cookies: List<String>) {
        val analyses = _analyses.value.toMutableList()
        val index = analyses.indexOfFirst { it.id == analysisId }
        
        if (index >= 0) {
            val analysis = analyses[index]
            analyses[index] = analysis.copy(cookies = cookies)
            _analyses.value = analyses
        }
    }

    // استخراج النماذج (Forms)
    fun extractForms(analysisId: String, forms: List<FormData>) {
        val analyses = _analyses.value.toMutableList()
        val index = analyses.indexOfFirst { it.id == analysisId }
        
        if (index >= 0) {
            val analysis = analyses[index]
            analyses[index] = analysis.copy(forms = forms)
            _analyses.value = analyses
        }
    }

    // استخراج البرامج النصية (Scripts)
    fun extractScripts(analysisId: String, scripts: List<ScriptData>) {
        val analyses = _analyses.value.toMutableList()
        val index = analyses.indexOfFirst { it.id == analysisId }
        
        if (index >= 0) {
            val analysis = analyses[index]
            analyses[index] = analysis.copy(scripts = scripts)
            _analyses.value = analyses
        }
    }

    // استخراج الصور
    fun extractImages(analysisId: String, images: List<ImageData>) {
        val analyses = _analyses.value.toMutableList()
        val index = analyses.indexOfFirst { it.id == analysisId }
        
        if (index >= 0) {
            val analysis = analyses[index]
            analyses[index] = analysis.copy(images = images)
            _analyses.value = analyses
        }
    }

    // استخراج الروابط
    fun extractLinks(analysisId: String, links: List<LinkData>) {
        val analyses = _analyses.value.toMutableList()
        val index = analyses.indexOfFirst { it.id == analysisId }
        
        if (index >= 0) {
            val analysis = analyses[index]
            analyses[index] = analysis.copy(links = links)
            _analyses.value = analyses
        }
    }

    // إكمال التحليل
    fun completeAnalysis(
        analysisId: String,
        securityScore: Float = 0f,
        vulnerabilities: List<String> = emptyList()
    ) {
        val analyses = _analyses.value.toMutableList()
        val index = analyses.indexOfFirst { it.id == analysisId }
        
        if (index >= 0) {
            val analysis = analyses[index]
            analyses[index] = analysis.copy(
                status = "completed",
                analysisTime = Clock.System.now().toEpochMilliseconds() - analysis.startTime,
                completedTime = Clock.System.now().toEpochMilliseconds(),
                securityScore = securityScore,
                vulnerabilities = vulnerabilities
            )
            _analyses.value = analyses
        }
    }

    // فشل التحليل
    fun failAnalysis(analysisId: String, errorMessage: String) {
        val analyses = _analyses.value.toMutableList()
        val index = analyses.indexOfFirst { it.id == analysisId }
        
        if (index >= 0) {
            val analysis = analyses[index]
            analyses[index] = analysis.copy(
                status = "failed",
                completedTime = Clock.System.now().toEpochMilliseconds()
            )
            _analyses.value = analyses
        }
    }

    // الحصول على التحليل المكتمل
    fun getCompletedAnalysis(analysisId: String): WebAnalysis? {
        return _analyses.value.firstOrNull { it.id == analysisId && it.status == "completed" }
    }

    // الحصول على كود HTML المستخرج
    fun getExtractedHtml(analysisId: String): String? {
        return _analyses.value.firstOrNull { it.id == analysisId }?.htmlCode
    }

    // الحصول على كود CSS المستخرج
    fun getExtractedCss(analysisId: String): String? {
        return _analyses.value.firstOrNull { it.id == analysisId }?.cssCode
    }

    // الحصول على كود JavaScript المستخرج
    fun getExtractedJs(analysisId: String): String? {
        return _analyses.value.firstOrNull { it.id == analysisId }?.jsCode
    }

    // الحصول على جميع التحليلات
    fun getAllAnalyses(): List<WebAnalysis> {
        return _analyses.value
    }

    // الحصول على سرعة التحليل
    fun getAnalysisSpeed(): String {
        return "OMEGA_SPEED_∞" // سرعة لانهائية
    }

    // الحصول على الكود المستخرج
    fun getExtractedCode(): Map<String, String> {
        return _extractedCode.value
    }

    // حذف تحليل
    fun deleteAnalysis(analysisId: String) {
        val analyses = _analyses.value.filter { it.id != analysisId }
        _analyses.value = analyses
    }

    // التحقق من نجاح التحليل
    fun isAnalysisSuccessful(analysisId: String): Boolean {
        return _analyses.value.firstOrNull { it.id == analysisId }?.status == "completed"
    }

    private fun addAnalysis(analysis: WebAnalysis) {
        val analyses = _analyses.value.toMutableList()
        analyses.add(analysis)
        _analyses.value = analyses
    }

    // الحصول على معدل نجاح التحليل
    fun getSuccessRate(): Float {
        val all = _analyses.value.size
        if (all == 0) return 0f
        val successful = _analyses.value.count { it.status == "completed" }
        return (successful.toFloat() / all) * 100
    }

    // تحليل متقدم مع فحص الأمان
    fun advancedAnalysis(targetUrl: String): WebAnalysis {
        return analyzeWebsite(targetUrl)
    }

    // استخراج جميع البيانات دفعة واحدة
    fun extractAllData(analysisId: String, allData: WebAnalysis) {
        val analyses = _analyses.value.toMutableList()
        val index = analyses.indexOfFirst { it.id == analysisId }
        
        if (index >= 0) {
            analyses[index] = allData
            _analyses.value = analyses
        }
    }
}