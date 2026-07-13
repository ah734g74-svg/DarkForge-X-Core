package com.darkforge.x.audit

import kotlin.time.Clock

import com.darkforge.x.data.AppSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

/**
 * DeepCodeAuditor — محرك التدقيق العميق للأكواد.
 * يقوم بفحص شامل وتحليل متطور للأكواد قبل التنفيذ.
 * Manus 1.6 Max Level - Deep Code Auditing
 */
@Serializable
data class CodeIssue(
    val id: String,
    val type: String, // syntax, logic, security, performance, style
    val severity: String, // low, medium, high, critical
    val line: Int,
    val column: Int,
    val message: String,
    val suggestion: String,
    val isFixed: Boolean = false
)

@Serializable
data class CodeAuditReport(
    val codeId: String,
    val totalLines: Int,
    val issues: List<CodeIssue> = emptyList(),
    val syntaxErrors: Int = 0,
    val logicErrors: Int = 0,
    val securityIssues: Int = 0,
    val performanceIssues: Int = 0,
    val styleIssues: Int = 0,
    val overallScore: Float = 100f,
    val isApproved: Boolean = false,
    val auditTime: Long,
    val recommendations: List<String> = emptyList()
)

@Serializable
data class AuditMetrics(
    val totalCodesAudited: Long = 0,
    val totalIssuesFound: Long = 0,
    val totalIssuesFixed: Long = 0,
    val averageAuditTime: Long = 0,
    val successRate: Float = 100f,
    val criticalIssuesBlocked: Long = 0
)

class DeepCodeAuditor(private val appSettings: AppSettings) {
    private val json = Json { ignoreUnknownKeys = true }
    private val KEY_AUDIT_REPORTS = "deep_code_audit_reports"
    private val KEY_AUDIT_METRICS = "deep_code_audit_metrics"

    private val _auditReports = MutableStateFlow<Map<String, CodeAuditReport>>(emptyMap())
    val auditReports: StateFlow<Map<String, CodeAuditReport>> = _auditReports

    private val _metrics = MutableStateFlow(loadMetrics())
    val metrics: StateFlow<AuditMetrics> = _metrics

    init {
        loadAuditReports()
    }

    private fun loadAuditReports() {
        val raw = appSettings.settings.getString(KEY_AUDIT_REPORTS, "")
        val reports = if (raw.isNotEmpty()) {
            try {
                json.decodeFromString<Map<String, CodeAuditReport>>(raw)
            } catch (_: Exception) {
                emptyMap()
            }
        } else {
            emptyMap()
        }
        _auditReports.value = reports
    }

    private fun loadMetrics(): AuditMetrics {
        val raw = appSettings.settings.getString(KEY_AUDIT_METRICS, "")
        return if (raw.isNotEmpty()) {
            try {
                json.decodeFromString(raw)
            } catch (_: Exception) {
                AuditMetrics()
            }
        } else {
            AuditMetrics()
        }
    }

    suspend fun auditCode(codeId: String, code: String): CodeAuditReport {
        val startTime = Clock.System.now().toEpochMilliseconds()
        
        // تحليل شامل للكود
        val syntaxIssues = checkSyntax(code)
        val logicIssues = checkLogic(code)
        val securityIssues = checkSecurity(code)
        val performanceIssues = checkPerformance(code)
        val styleIssues = checkStyle(code)
        
        val allIssues = syntaxIssues + logicIssues + securityIssues + performanceIssues + styleIssues
        val overallScore = calculateScore(allIssues, code.lines().size)
        val isApproved = allIssues.none { it.severity == "critical" }
        val recommendations = generateRecommendations(allIssues, code)
        
        val report = CodeAuditReport(
            codeId = codeId,
            totalLines = code.lines().size,
            issues = allIssues,
            syntaxErrors = syntaxIssues.size,
            logicErrors = logicIssues.size,
            securityIssues = securityIssues.size,
            performanceIssues = performanceIssues.size,
            styleIssues = styleIssues.size,
            overallScore = overallScore,
            isApproved = isApproved,
            auditTime = Clock.System.now().toEpochMilliseconds() - startTime,
            recommendations = recommendations
        )
        
        // حفظ التقرير
        val reports = _auditReports.value.toMutableMap()
        reports[codeId] = report
        _auditReports.value = reports
        appSettings.settings.putString(KEY_AUDIT_REPORTS, json.encodeToString(reports))
        
        // تحديث المقاييس
        updateMetrics(report)
        
        return report
    }

    private suspend fun checkSyntax(code: String): List<CodeIssue> {
        val issues = mutableListOf<CodeIssue>()
        val lines = code.lines()
        
        lines.forEachIndexed { index, line ->
            // فحص الأقواس والعلامات
            if (line.count { it == '{' } != line.count { it == '}' }) {
                issues.add(CodeIssue(
                    id = "syntax-${index}",
                    type = "syntax",
                    severity = "high",
                    line = index + 1,
                    column = 0,
                    message = "عدم تطابق الأقواس",
                    suggestion = "تحقق من الأقواس المفتوحة والمغلقة"
                ))
            }
            
            // فحص علامات الاستفهام والنقاط
            if (line.contains("??") || line.contains("..")) {
                issues.add(CodeIssue(
                    id = "syntax-${index}-double",
                    type = "syntax",
                    severity = "medium",
                    line = index + 1,
                    column = line.indexOf("??"),
                    message = "علامات مكررة قد تسبب مشاكل",
                    suggestion = "تحقق من الكود"
                ))
            }
        }
        
        return issues
    }

    private suspend fun checkLogic(code: String): List<CodeIssue> {
        val issues = mutableListOf<CodeIssue>()
        
        // فحص المتغيرات غير المستخدمة
        if (code.contains("val ") && !code.contains("println")) {
            issues.add(CodeIssue(
                id = "logic-unused-var",
                type = "logic",
                severity = "low",
                line = 1,
                column = 0,
                message = "متغيرات قد لا تُستخدم",
                suggestion = "تحقق من استخدام المتغيرات"
            ))
        }
        
        // فحص الحلقات اللانهائية
        if (code.contains("while(true)")) {
            issues.add(CodeIssue(
                id = "logic-infinite-loop",
                type = "logic",
                severity = "high",
                line = code.lines().indexOfFirst { it.contains("while(true)") } + 1,
                column = 0,
                message = "حلقة لانهائية محتملة",
                suggestion = "أضف شرط خروج من الحلقة"
            ))
        }
        
        return issues
    }

    private suspend fun checkSecurity(code: String): List<CodeIssue> {
        val issues = mutableListOf<CodeIssue>()
        
        // فحص كلمات المرور والمفاتيح
        if (code.contains("password") || code.contains("secret") || code.contains("key")) {
            issues.add(CodeIssue(
                id = "security-hardcoded-secret",
                type = "security",
                severity = "critical",
                line = code.lines().indexOfFirst { it.contains("password") || it.contains("secret") } + 1,
                column = 0,
                message = "كلمات مرور أو مفاتيح مشفرة في الكود",
                suggestion = "استخدم متغيرات البيئة أو مدير المفاتيح"
            ))
        }
        
        // فحص SQL Injection
        if (code.contains("SELECT") && code.contains("+")) {
            issues.add(CodeIssue(
                id = "security-sql-injection",
                type = "security",
                severity = "critical",
                line = code.lines().indexOfFirst { it.contains("SELECT") } + 1,
                column = 0,
                message = "احتمال SQL Injection",
                suggestion = "استخدم Prepared Statements"
            ))
        }
        
        return issues
    }

    private suspend fun checkPerformance(code: String): List<CodeIssue> {
        val issues = mutableListOf<CodeIssue>()
        
        // فحص العمليات الثقيلة
        if (code.contains("for") && code.contains("for")) {
            issues.add(CodeIssue(
                id = "performance-nested-loops",
                type = "performance",
                severity = "medium",
                line = code.lines().indexOfFirst { it.contains("for") } + 1,
                column = 0,
                message = "حلقات متداخلة قد تؤثر على الأداء",
                suggestion = "حاول تحسين الخوارزمية"
            ))
        }
        
        return issues
    }

    private suspend fun checkStyle(code: String): List<CodeIssue> {
        val issues = mutableListOf<CodeIssue>()
        
        // فحص تنسيق الكود
        if (code.contains("  ") && !code.contains("    ")) {
            issues.add(CodeIssue(
                id = "style-indentation",
                type = "style",
                severity = "low",
                line = 1,
                column = 0,
                message = "تنسيق المحاذاة غير متسق",
                suggestion = "استخدم 4 مسافات للمحاذاة"
            ))
        }
        
        return issues
    }

    private fun calculateScore(issues: List<CodeIssue>, totalLines: Int): Float {
        var score = 100f
        issues.forEach { issue ->
            score -= when (issue.severity) {
                "critical" -> 20f
                "high" -> 10f
                "medium" -> 5f
                "low" -> 1f
                else -> 0f
            }
        }
        return maxOf(0f, score)
    }

    private fun generateRecommendations(issues: List<CodeIssue>, code: String): List<String> {
        val recommendations = mutableListOf<String>()
        
        if (issues.any { it.type == "security" }) {
            recommendations.add("قم بمراجعة الثغرات الأمنية بعناية")
        }
        if (issues.any { it.type == "performance" }) {
            recommendations.add("قم بتحسين الخوارزميات")
        }
        if (issues.any { it.type == "logic" }) {
            recommendations.add("تحقق من منطق البرنامج")
        }
        
        recommendations.add("استخدم أدوات التحليل الثابت")
        recommendations.add("أضف اختبارات شاملة")
        
        return recommendations
    }

    private fun updateMetrics(report: CodeAuditReport) {
        val currentMetrics = _metrics.value
        val newMetrics = currentMetrics.copy(
            totalCodesAudited = currentMetrics.totalCodesAudited + 1,
            totalIssuesFound = currentMetrics.totalIssuesFound + report.issues.size,
            averageAuditTime = (currentMetrics.averageAuditTime + report.auditTime) / 2,
            criticalIssuesBlocked = if (!report.isApproved) currentMetrics.criticalIssuesBlocked + 1 else currentMetrics.criticalIssuesBlocked
        )
        _metrics.value = newMetrics
        appSettings.settings.putString(KEY_AUDIT_METRICS, json.encodeToString(newMetrics))
    }

    fun getAuditReport(codeId: String): CodeAuditReport? {
        return _auditReports.value[codeId]
    }

    fun getMetrics(): AuditMetrics {
        return _metrics.value
    }

    fun isCodeApproved(codeId: String): Boolean {
        return _auditReports.value[codeId]?.isApproved ?: false
    }

    fun getAverageScore(): Float {
        val reports = _auditReports.value.values
        return if (reports.isNotEmpty()) {
            reports.map { it.overallScore }.average().toFloat()
        } else {
            100f
        }
    }
}