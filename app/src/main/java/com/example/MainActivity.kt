package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.FlashOn
import androidx.compose.material.icons.outlined.Layers
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.InAppFloatingBubble
import com.example.ui.screens.CrmPipelineScreen
import com.example.ui.screens.DirectChatScreen
import com.example.ui.screens.OverlaySettingsScreen
import com.example.ui.screens.TemplatesScreen
import com.example.ui.screens.TermsAndServicesScreen
import com.example.ui.theme.CyberBlack
import com.example.ui.theme.CyberCardVariant
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.TextMuted
import com.example.ui.viewmodel.MainViewModel
import com.example.util.LocalIsEnglish
import com.example.util.tr
import java.util.Locale

enum class NavigationTab(val title: String) {
    TEMPLATES("Plantillas"),
    PIPELINE("Pipeline"),
    DIRECT_CHAT("Chat WA"),
    OVERLAY("Burbuja")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp(viewModel: MainViewModel = viewModel()) {
    var currentTab by remember { mutableStateOf(NavigationTab.TEMPLATES) }
    var isShowingTermsScreen by remember { mutableStateOf(false) }
    val isEnglish by viewModel.isEnglish.collectAsStateWithLifecycle()

    val templates by viewModel.filteredTemplates.collectAsStateWithLifecycle()
    val allTemplates by viewModel.allTemplatesRaw.collectAsStateWithLifecycle()
    val allLeads by viewModel.allLeadsRaw.collectAsStateWithLifecycle()
    val leads by viewModel.filteredLeads.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val templateSearchQuery by viewModel.templateSearchQuery.collectAsStateWithLifecycle()

    val leadSearchQuery by viewModel.leadSearchQuery.collectAsStateWithLifecycle()
    val selectedStageFilter by viewModel.selectedStageFilter.collectAsStateWithLifecycle()

    val directCountryCode by viewModel.directCountryCode.collectAsStateWithLifecycle()
    val directPhoneNumber by viewModel.directPhoneNumber.collectAsStateWithLifecycle()
    val directMessage by viewModel.directMessage.collectAsStateWithLifecycle()

    val isSystemOverlayRunning by viewModel.isSystemOverlayServiceRunning.collectAsStateWithLifecycle()
    val isInAppBubbleActive by viewModel.isInAppBubbleVisible.collectAsStateWithLifecycle()
    val userSession by viewModel.userSession.collectAsStateWithLifecycle()
    val allInteractions by viewModel.allInteractions.collectAsStateWithLifecycle()
    val isVipUser by viewModel.isVipUser.collectAsStateWithLifecycle()

    BackHandler(enabled = isShowingTermsScreen || currentTab != NavigationTab.TEMPLATES) {
        if (isShowingTermsScreen) {
            isShowingTermsScreen = false
        } else {
            currentTab = NavigationTab.TEMPLATES
        }
    }

    CompositionLocalProvider(LocalIsEnglish provides isEnglish) {
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                containerColor = CyberBlack,
                bottomBar = {
                    NavigationBar(
                        containerColor = Color(0xFF101018),
                        modifier = Modifier
                            .windowInsetsPadding(WindowInsets.navigationBars)
                            .testTag("main_navigation_bar")
                    ) {
                        NavigationBarItem(
                            selected = currentTab == NavigationTab.TEMPLATES,
                            onClick = { currentTab = NavigationTab.TEMPLATES },
                            icon = {
                                Icon(
                                    imageVector = if (currentTab == NavigationTab.TEMPLATES) Icons.Filled.FlashOn else Icons.Outlined.FlashOn,
                                    contentDescription = tr("Plantillas", "Templates"),
                                    modifier = Modifier.size(22.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = tr("PLANTILLAS", "TEMPLATES"),
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 9.sp,
                                    fontWeight = if (currentTab == NavigationTab.TEMPLATES) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFF0A0A0E),
                                selectedTextColor = NeonCyan,
                                indicatorColor = NeonCyan,
                                unselectedIconColor = TextMuted,
                                unselectedTextColor = TextMuted
                            ),
                            modifier = Modifier.testTag("nav_item_templates")
                        )

                        NavigationBarItem(
                            selected = currentTab == NavigationTab.PIPELINE,
                            onClick = { currentTab = NavigationTab.PIPELINE },
                            icon = {
                                Icon(
                                    imageVector = if (currentTab == NavigationTab.PIPELINE) Icons.Filled.Assignment else Icons.Outlined.Assignment,
                                    contentDescription = "Pipeline",
                                    modifier = Modifier.size(22.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = "PIPELINE",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 9.sp,
                                    fontWeight = if (currentTab == NavigationTab.PIPELINE) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFF0A0A0E),
                                selectedTextColor = NeonGreen,
                                indicatorColor = NeonGreen,
                                unselectedIconColor = TextMuted,
                                unselectedTextColor = TextMuted
                            ),
                            modifier = Modifier.testTag("nav_item_pipeline")
                        )

                        NavigationBarItem(
                            selected = currentTab == NavigationTab.DIRECT_CHAT,
                            onClick = { currentTab = NavigationTab.DIRECT_CHAT },
                            icon = {
                                Icon(
                                    imageVector = if (currentTab == NavigationTab.DIRECT_CHAT) Icons.Filled.Phone else Icons.Outlined.Phone,
                                    contentDescription = tr("Chat Directo", "Direct Chat"),
                                    modifier = Modifier.size(22.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = tr("CHAT WA", "WA CHAT"),
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 9.sp,
                                    fontWeight = if (currentTab == NavigationTab.DIRECT_CHAT) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFF0A0A0E),
                                selectedTextColor = NeonCyan,
                                indicatorColor = NeonCyan,
                                unselectedIconColor = TextMuted,
                                unselectedTextColor = TextMuted
                            ),
                            modifier = Modifier.testTag("nav_item_direct_chat")
                        )

                        NavigationBarItem(
                            selected = currentTab == NavigationTab.OVERLAY,
                            onClick = { currentTab = NavigationTab.OVERLAY },
                            icon = {
                                Icon(
                                    imageVector = if (currentTab == NavigationTab.OVERLAY) Icons.Filled.Settings else Icons.Outlined.Settings,
                                    contentDescription = tr("Ajustes y Burbuja", "Settings & Bubble"),
                                    modifier = Modifier.size(22.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = tr("AJUSTES", "SETTINGS"),
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 9.sp,
                                    fontWeight = if (currentTab == NavigationTab.OVERLAY) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFF0A0A0E),
                                selectedTextColor = NeonGreen,
                                indicatorColor = NeonGreen,
                                unselectedIconColor = TextMuted,
                                unselectedTextColor = TextMuted
                            ),
                            modifier = Modifier.testTag("nav_item_overlay")
                        )
                    }
                }
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    if (isShowingTermsScreen) {
                        TermsAndServicesScreen(
                            onNavigateBack = { isShowingTermsScreen = false }
                        )
                    } else {
                        when (currentTab) {
                            NavigationTab.TEMPLATES -> {
                                TemplatesScreen(
                                    templates = templates,
                                    selectedCategory = selectedCategory,
                                    searchQuery = templateSearchQuery,
                                    onSearchChange = viewModel::setTemplateSearchQuery,
                                    onCategoryChange = viewModel::setSelectedCategory,
                                    onCopyTemplate = viewModel::copyTemplateToClipboard,
                                    onSaveTemplate = viewModel::saveTemplate,
                                    onDeleteTemplate = viewModel::deleteTemplate,
                                    onLaunchWhatsAppWithTemplate = { template ->
                                        viewModel.openWhatsAppWithMessage(template.content)
                                    },
                                    onToggleOverlay = {
                                        if (viewModel.canDrawOverlays()) {
                                            viewModel.toggleSystemOverlayService()
                                        } else {
                                            viewModel.toggleInAppBubble()
                                        }
                                    },
                                    isOverlayActive = isSystemOverlayRunning || isInAppBubbleActive,
                                    userSession = userSession,
                                    onLoginGoogle = { viewModel.loginWithGoogle() },
                                    onLoginPhone = { viewModel.loginWithPhone(it) },
                                    onLogout = viewModel::logout,
                                    allTemplates = allTemplates,
                                    leadsCount = allLeads.size,
                                    onLaunchWhatsAppWithCustomText = { text, phone ->
                                        if (phone != null && phone.isNotBlank()) {
                                            viewModel.launchWhatsApp(phone, text)
                                        } else {
                                            viewModel.openWhatsAppWithMessage(text)
                                        }
                                    },
                                    onCopyCustomText = { text ->
                                        viewModel.copyCustomTextToClipboard(text)
                                    },
                                    interactions = allInteractions,
                                    isVipUser = isVipUser,
                                    onActivateVip = viewModel::activateVip,
                                    onDeleteInteraction = viewModel::deleteInteraction,
                                    onNavigateToTerms = { isShowingTermsScreen = true },
                                    isEnglish = isEnglish,
                                    onToggleLanguage = viewModel::toggleLanguage,
                                    onRestoreData = { resTemplates, resLeads ->
                                        viewModel.restoreData(resTemplates, resLeads)
                                    },
                                    allLeads = allLeads
                                )
                            }

                            NavigationTab.PIPELINE -> {
                                CrmPipelineScreen(
                                    leads = leads,
                                    allLeads = allLeads,
                                    searchQuery = leadSearchQuery,
                                    selectedStage = selectedStageFilter,
                                    onSearchChange = viewModel::setLeadSearchQuery,
                                    onStageFilterChange = viewModel::setSelectedStageFilter,
                                    onSaveLead = viewModel::saveLead,
                                    onDeleteLead = viewModel::deleteLead,
                                    onUpdateStage = viewModel::updateLeadStage,
                                    onOpenWhatsApp = { phone, name ->
                                        viewModel.setDirectPhoneNumber(phone)
                                        viewModel.launchWhatsApp(phone, if (isEnglish) "Hello $name! 👋" else "¡Hola $name! 👋")
                                    }
                                )
                            }

                            NavigationTab.DIRECT_CHAT -> {
                                DirectChatScreen(
                                    countryCode = directCountryCode,
                                    phoneNumber = directPhoneNumber,
                                    messageText = directMessage,
                                    templates = templates,
                                    onCountryCodeChange = viewModel::setDirectCountryCode,
                                    onPhoneNumberChange = viewModel::setDirectPhoneNumber,
                                    onMessageChange = viewModel::setDirectMessage,
                                    onApplyTemplate = viewModel::applyTemplateToDirectMessage,
                                    onLaunchWhatsApp = viewModel::launchWhatsApp,
                                    onSaveAsLead = viewModel::saveLead
                                )
                            }

                            NavigationTab.OVERLAY -> {
                                OverlaySettingsScreen(
                                    isSystemOverlayRunning = isSystemOverlayRunning,
                                    isInAppBubbleActive = isInAppBubbleActive,
                                    hasOverlayPermission = viewModel.canDrawOverlays(),
                                    isBatteryOptimized = viewModel.isBatteryOptimizationIgnored(),
                                    isEnglish = isEnglish,
                                    onSelectLanguage = viewModel::setLanguage,
                                    onToggleSystemOverlay = viewModel::toggleSystemOverlayService,
                                    onToggleInAppBubble = viewModel::toggleInAppBubble,
                                    onRequestPermission = viewModel::openOverlaySettings,
                                    onRequestBatteryBypass = viewModel::requestBatteryOptimizationBypass,
                                    onOpenAutostartSettings = viewModel::openAutostartMenu,
                                    onLaunchWhatsApp = {
                                        viewModel.launchWhatsApp("", "")
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // In-App Floating Draggable Bubble overlay (visible on top of UI when active)
            if (isInAppBubbleActive) {
                InAppFloatingBubble(
                    templates = templates,
                    onCopyTemplate = { viewModel.copyTemplateToClipboard(it) },
                    onLaunchWhatsApp = { viewModel.launchWhatsApp("", "") },
                    onCloseBubble = { viewModel.setInAppBubbleVisible(false) }
                )
            }
        }
    }
}
