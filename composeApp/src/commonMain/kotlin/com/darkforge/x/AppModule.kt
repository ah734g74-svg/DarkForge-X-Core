package com.darkforge.x

import com.darkforge.x.data.AppSettings
import com.darkforge.x.data.ConversationStorage
import com.darkforge.x.data.DataRepository
import com.darkforge.x.data.EmailStore
import com.darkforge.x.data.HeartbeatManager
import com.darkforge.x.data.MemoryStore
import com.darkforge.x.data.NotificationStore
import com.darkforge.x.data.RemoteDataRepository
import com.darkforge.x.data.SmsDraftStore
import com.darkforge.x.data.SmsStore
import com.darkforge.x.data.TaskScheduler
import com.darkforge.x.data.TaskStore
import com.darkforge.x.data.ToolExecutor
import com.darkforge.x.data.runMigrations
import com.darkforge.x.email.EmailPoller
import com.darkforge.x.inference.createLocalInferenceEngine
import com.darkforge.x.mcp.McpServerManager
import com.darkforge.x.network.Requests
import com.darkforge.x.connectors.ConnectorManager
import com.darkforge.x.data.SelfEvolutionEngine
import com.darkforge.x.data.OmegaExecutionEngine
import com.darkforge.x.cloud.CloudComputerManager
import com.darkforge.x.api.AdvancedApiManager
import com.darkforge.x.manus.InfiniteManusBridge
import com.darkforge.x.manus.Manus16MaxFeatures
import com.darkforge.x.ai.InfiniteAiAccess
import com.darkforge.x.cloud.InfiniteCloudComputer
import com.darkforge.x.terminal.InfiniteTerminalEngine
import com.darkforge.x.access.ZeroLimitAccess
import com.darkforge.x.security.CyberOmegaSecurity
import com.darkforge.x.audit.DeepCodeAuditor
import com.darkforge.x.resources.InfiniteResourceEngine
import com.darkforge.x.notifications.NotificationReader
import com.darkforge.x.skills.SkillManager
import com.darkforge.x.sms.SmsPoller
import com.darkforge.x.sms.SmsReader
import com.darkforge.x.sms.SmsSender
import com.darkforge.x.splinterlands.SplinterlandsApi
import com.darkforge.x.splinterlands.SplinterlandsBattleRunner
import com.darkforge.x.splinterlands.SplinterlandsStore
import com.darkforge.x.tools.CalendarPermissionController
import com.darkforge.x.tools.NotificationListenerController
import com.darkforge.x.tools.NotificationPermissionController
import com.darkforge.x.tools.SmsPermissionController
import com.darkforge.x.tools.SmsSendPermissionController
import com.darkforge.x.ui.chat.ChatViewModel
import com.darkforge.x.ui.sandbox.SandboxFileBrowserViewModel
import com.darkforge.x.ui.sandbox.SandboxPackagesViewModel
import com.darkforge.x.ui.sandbox.SandboxSessionViewModel
import com.darkforge.x.ui.settings.SandboxViewModel
import com.darkforge.x.ui.settings.SettingsViewModel
import com.darkforge.x.ui.settings.SplinterlandsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<CalendarPermissionController> { CalendarPermissionController() }
    single<NotificationPermissionController> { NotificationPermissionController() }
    single<SmsPermissionController> { SmsPermissionController() }
    single<SmsSendPermissionController> { SmsSendPermissionController() }
    single<SmsReader> { SmsReader() }
    single<SmsSender> { SmsSender() }
    single<NotificationListenerController> { NotificationListenerController() }
    single<NotificationReader> { NotificationReader() }
    single<AppSettings> {
        AppSettings(createSecureSettings()).also {
            it.runMigrations(createLegacySettings())
        }
    }
    single<Requests> {
        Requests()
    }
    single<ConversationStorage> {
        ConversationStorage(get())
    }
    single<ToolExecutor> {
        ToolExecutor()
    }
    single<MemoryStore> {
        MemoryStore(get())
    }
    single<TaskStore> {
        TaskStore(get())
    }
    single<EmailStore> {
        EmailStore(get())
    }
    single<EmailPoller> {
        EmailPoller(get<EmailStore>())
    }
    single<SmsStore> {
        SmsStore(get())
    }
    single<SmsPoller> {
        SmsPoller(get<SmsStore>(), get<SmsReader>())
    }
    single<SmsDraftStore> {
        SmsDraftStore(get())
    }
    single<NotificationStore> {
        NotificationStore(get())
    }
    single<SplinterlandsStore> {
        SplinterlandsStore(get())
    }
    single<SplinterlandsApi> {
        SplinterlandsApi()
    }
    single<HeartbeatManager> {
        HeartbeatManager(get(), get(), get(), get())
    }
    single<McpServerManager> {
        McpServerManager(get())
    }
    single<ConnectorManager> {
        ConnectorManager(get())
    }
    single<SelfEvolutionEngine> {
        SelfEvolutionEngine(get())
    }
    single<OmegaExecutionEngine> {
        OmegaExecutionEngine(get(), get())
    }
    single<CloudComputerManager> {
        CloudComputerManager(get())
    }
    single<AdvancedApiManager> {
        AdvancedApiManager(get())
    }
    single<InfiniteManusBridge> {
        InfiniteManusBridge(get())
    }
    single<InfiniteAiAccess> {
        InfiniteAiAccess(get())
    }
    single<InfiniteCloudComputer> {
        InfiniteCloudComputer(get())
    }
    single<Manus16MaxFeatures> {
        Manus16MaxFeatures(get())
    }
    single<ZeroLimitAccess> {
        ZeroLimitAccess(get())
    }
    single<InfiniteTerminalEngine> {
        InfiniteTerminalEngine(get(), get<SelfEvolutionEngine>(), get<OmegaExecutionEngine>())
    }
    single<CyberOmegaSecurity> {
        CyberOmegaSecurity(get())
    }
    single<DeepCodeAuditor> {
        DeepCodeAuditor(get())
    }
    single<InfiniteResourceEngine> {
        InfiniteResourceEngine(get())
    }
    single<SkillManager> {
        SkillManager(get<SandboxController>())
    }
    single<RemoteDataRepository> {
        RemoteDataRepository(
            requests = get(),
            appSettings = get(),
            conversationStorage = get(),
            toolExecutor = get(),
            memoryStore = get(),
            taskStore = get(),
            heartbeatManager = get(),
            emailStore = get(),
            emailPoller = get(),
            smsStore = get(),
            smsPoller = get(),
            smsReader = get(),
            smsPermissionController = get(),
            smsSendPermissionController = get(),
            smsSender = get(),
            smsDraftStore = get(),
            notificationStore = get(),
            notificationListenerController = get(),
            mcpServerManager = get(),
            skillManager = get(),
            sandboxController = get(),
            localInferenceEngine = createLocalInferenceEngine(),
        )
    }
    single<DataRepository> { get<RemoteDataRepository>() }
    single<SplinterlandsBattleRunner> {
        SplinterlandsBattleRunner(get(), get(), get<DataRepository>(), get<DaemonController>())
    }
    single<TaskScheduler> {
        TaskScheduler(
            get<DataRepository>(),
            get(),
            get(),
            get(),
            get(),
            get<EmailPoller>(),
            get<SmsStore>(),
            get<SmsPoller>(),
            get<NotificationStore>(),
        )
    }
    single<DaemonController> { createDaemonController() }
    single<SandboxController> { createSandboxController() }
    viewModel { SettingsViewModel(get<DataRepository>(), get<DaemonController>(), get<NotificationPermissionController>(), get<TaskScheduler>()) }
    viewModel { SandboxViewModel(get<DataRepository>(), get<SandboxController>()) }
    viewModel { SandboxFileBrowserViewModel(get<SandboxController>()) }
    viewModel { SandboxPackagesViewModel(get<SandboxController>()) }
    viewModel { SandboxSessionViewModel(get<SandboxController>(), get<DataRepository>()) }
    viewModel { SplinterlandsViewModel(get<DataRepository>(), get(), get(), get<SplinterlandsApi>()) }
    viewModel { ChatViewModel(get<DataRepository>(), get<TaskScheduler>(), get<SelfEvolutionEngine>()) }
}
