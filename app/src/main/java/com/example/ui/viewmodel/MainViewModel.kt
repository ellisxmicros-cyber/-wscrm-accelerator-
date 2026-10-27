package com.example.ui.viewmodel

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Settings
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.CrmRepository
import com.example.data.CustomerInteractionEntity
import com.example.data.LeadEntity
import com.example.data.LeadStage
import com.example.data.TemplateEntity
import com.example.service.FloatingOverlayService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CrmRepository

    init {
        val db = AppDatabase.getInstance(application)
        repository = CrmRepository(db.templateDao(), db.leadDao(), db.interactionDao())
        // Trigger preloading check
        viewModelScope.launch {
            AppDatabase.seedInitialData(db)
        }
    }

    // Language state with SharedPreferences persistence
    private val prefs = application.getSharedPreferences("turboventas_prefs", Context.MODE_PRIVATE)
    private val _isEnglish = MutableStateFlow(
        prefs.getBoolean("is_english", java.util.Locale.getDefault().language.lowercase().startsWith("en"))
    )
    val isEnglish: StateFlow<Boolean> = _isEnglish.asStateFlow()

    fun setLanguage(isEnglish: Boolean) {
        _isEnglish.value = isEnglish
        prefs.edit().putBoolean("is_english", isEnglish).apply()
    }

    fun toggleLanguage() {
        setLanguage(!_isEnglish.value)
    }

    // Customer Interaction History
    val allInteractions: StateFlow<List<CustomerInteractionEntity>> = repository.allInteractions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // VIP Membership state
    private val _isVipUser = MutableStateFlow(false)
    val isVipUser: StateFlow<Boolean> = _isVipUser.asStateFlow()

    fun activateVip() {
        _isVipUser.value = true
        Toast.makeText(getApplication(), "⭐ ¡Membresía VIP Pro activada!", Toast.LENGTH_LONG).show()
    }

    // Template state
    private val _templateSearchQuery = MutableStateFlow("")
    val templateSearchQuery: StateFlow<String> = _templateSearchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("Todos")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    val filteredTemplates: StateFlow<List<TemplateEntity>> = combine(
        repository.allTemplates,
        _templateSearchQuery,
        _selectedCategory
    ) { templates, query, category ->
        templates.filter { t ->
            val matchesCategory = when {
                category.equals("Todos", ignoreCase = true) -> true
                category.equals("Sales", ignoreCase = true) || category.equals("Ventas", ignoreCase = true) ->
                    t.category.equals("Sales", ignoreCase = true) || t.category.equals("Ventas", ignoreCase = true) || t.category.equals("Precios", ignoreCase = true) || t.category.equals("Promos", ignoreCase = true)
                category.equals("Support", ignoreCase = true) || category.equals("Soporte", ignoreCase = true) ->
                    t.category.equals("Support", ignoreCase = true) || t.category.equals("Soporte", ignoreCase = true) || t.category.equals("Info", ignoreCase = true)
                category.equals("Follow-up", ignoreCase = true) || category.equals("Seguimiento", ignoreCase = true) ->
                    t.category.equals("Follow-up", ignoreCase = true) || t.category.equals("Seguimiento", ignoreCase = true) || t.category.equals("Cierre", ignoreCase = true)
                else -> t.category.equals(category, ignoreCase = true)
            }
            val matchesQuery = query.isBlank() ||
                    t.title.contains(query, ignoreCase = true) ||
                    t.content.contains(query, ignoreCase = true) ||
                    t.shortcut.contains(query, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Leads state
    private val _leadSearchQuery = MutableStateFlow("")
    val leadSearchQuery: StateFlow<String> = _leadSearchQuery.asStateFlow()

    private val _selectedStageFilter = MutableStateFlow<LeadStage?>(null)
    val selectedStageFilter: StateFlow<LeadStage?> = _selectedStageFilter.asStateFlow()

    val filteredLeads: StateFlow<List<LeadEntity>> = combine(
        repository.allLeads,
        _leadSearchQuery,
        _selectedStageFilter
    ) { leads, query, stage ->
        leads.filter { lead ->
            val matchesStage = stage == null || lead.stage == stage
            val matchesQuery = query.isBlank() ||
                    lead.name.contains(query, ignoreCase = true) ||
                    lead.phone.contains(query, ignoreCase = true) ||
                    lead.productInterest.contains(query, ignoreCase = true) ||
                    lead.notes.contains(query, ignoreCase = true)
            matchesStage && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allLeadsRaw: StateFlow<List<LeadEntity>> = repository.allLeads
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allTemplatesRaw: StateFlow<List<TemplateEntity>> = repository.allTemplates
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Direct Chat state
    private val _directCountryCode = MutableStateFlow("+58")
    val directCountryCode: StateFlow<String> = _directCountryCode.asStateFlow()

    private val _directPhoneNumber = MutableStateFlow("")
    val directPhoneNumber: StateFlow<String> = _directPhoneNumber.asStateFlow()

    private val _directMessage = MutableStateFlow("")
    val directMessage: StateFlow<String> = _directMessage.asStateFlow()

    // Overlay state
    val isSystemOverlayServiceRunning = FloatingOverlayService.isServiceActive

    private val _isInAppBubbleVisible = MutableStateFlow(false)
    val isInAppBubbleVisible: StateFlow<Boolean> = _isInAppBubbleVisible.asStateFlow()

    // Template actions
    fun setTemplateSearchQuery(query: String) {
        _templateSearchQuery.value = query
    }

    fun setSelectedCategory(category: String) {
        _selectedCategory.value = category
    }

    fun saveTemplate(template: TemplateEntity) {
        viewModelScope.launch {
            if (template.id == 0L) {
                repository.insertTemplate(template)
            } else {
                repository.updateTemplate(template)
            }
        }
    }

    fun deleteTemplate(template: TemplateEntity) {
        viewModelScope.launch {
            repository.deleteTemplate(template)
        }
    }

    fun copyTemplateToClipboard(template: TemplateEntity, customContent: String? = null) {
        val context = getApplication<Application>()
        val textToCopy = customContent ?: template.content
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(template.title, textToCopy)
        clipboard.setPrimaryClip(clip)

        // Haptic feedback
        try {
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(45, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(45)
            }
        } catch (_: Exception) {}

        Toast.makeText(context, "⚡ ¡Copiado al portapapeles!", Toast.LENGTH_SHORT).show()

        viewModelScope.launch {
            repository.incrementTemplateUsage(template.id)
        }
    }

    fun copyCustomTextToClipboard(text: String, label: String = "Mensaje personalizado") {
        val context = getApplication<Application>()
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)

        try {
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(45, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(45)
            }
        } catch (_: Exception) {}

        Toast.makeText(context, "⚡ ¡Mensaje personalizado copiado!", Toast.LENGTH_SHORT).show()
    }

    fun restoreData(templates: List<TemplateEntity>, leads: List<LeadEntity>) {
        viewModelScope.launch {
            templates.forEach { repository.insertTemplate(it) }
            leads.forEach { repository.insertLead(it) }
            Toast.makeText(getApplication(), "✅ Datos restaurados exitosamente", Toast.LENGTH_LONG).show()
        }
    }

    // Lead actions
    fun setLeadSearchQuery(query: String) {
        _leadSearchQuery.value = query
    }

    fun setSelectedStageFilter(stage: LeadStage?) {
        _selectedStageFilter.value = stage
    }

    fun saveLead(lead: LeadEntity) {
        viewModelScope.launch {
            if (lead.id == 0L) {
                repository.insertLead(lead)
            } else {
                repository.updateLead(lead)
            }
        }
    }

    fun deleteLead(lead: LeadEntity) {
        viewModelScope.launch {
            repository.deleteLead(lead)
        }
    }

    fun updateLeadStage(leadId: Long, stage: LeadStage) {
        viewModelScope.launch {
            repository.updateLeadStage(leadId, stage)
            val lead = allLeadsRaw.value.find { it.id == leadId }
            repository.recordInteraction(
                CustomerInteractionEntity(
                    leadId = leadId,
                    customerPhone = lead?.phone ?: "",
                    customerName = lead?.name ?: "Prospecto",
                    interactionType = CustomerInteractionEntity.TYPE_STAGE_CHANGE,
                    content = "Etapa de cliente actualizada a: ${stage.label}",
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }

    // Direct Chat actions
    fun setDirectCountryCode(code: String) {
        _directCountryCode.value = code
    }

    fun setDirectPhoneNumber(phone: String) {
        _directPhoneNumber.value = phone
    }

    fun setDirectMessage(message: String) {
        _directMessage.value = message
    }

    fun applyTemplateToDirectMessage(template: TemplateEntity, clientName: String = "", productName: String = "") {
        var content = template.content
        if (clientName.isNotBlank()) {
            content = content.replace("{nombre}", clientName, ignoreCase = true)
        }
        if (productName.isNotBlank()) {
            content = content.replace("{producto}", productName, ignoreCase = true)
        }
        _directMessage.value = content
    }

    fun recordCustomerInteraction(
        phone: String,
        name: String = "",
        type: String = CustomerInteractionEntity.TYPE_WHATSAPP_MESSAGE,
        content: String,
        templateTitle: String? = null,
        leadId: Long? = null
    ) {
        viewModelScope.launch {
            repository.recordInteraction(
                CustomerInteractionEntity(
                    leadId = leadId,
                    customerPhone = phone,
                    customerName = name,
                    interactionType = type,
                    content = content,
                    templateTitle = templateTitle,
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }

    fun deleteInteraction(id: Long) {
        viewModelScope.launch {
            repository.deleteInteraction(id)
        }
    }

    fun launchWhatsApp(phoneWithCountry: String, messageText: String, templateTitle: String? = null) {
        val context = getApplication<Application>()
        // Record in Customer Interaction History in Room
        if (phoneWithCountry.isNotBlank() || messageText.isNotBlank()) {
            val matchingLead = allLeadsRaw.value.find { 
                it.phone.replace(Regex("[^0-9]"), "") == phoneWithCountry.replace(Regex("[^0-9]"), "")
            }
            recordCustomerInteraction(
                phone = phoneWithCountry,
                name = matchingLead?.name ?: "",
                type = if (templateTitle != null) CustomerInteractionEntity.TYPE_TEMPLATE_SENT else CustomerInteractionEntity.TYPE_WHATSAPP_MESSAGE,
                content = messageText,
                templateTitle = templateTitle,
                leadId = matchingLead?.id
            )
        }

        // Clean digits
        val cleanDigits = phoneWithCountry.replace(Regex("[^0-9]"), "")
        val encodedMessage = Uri.encode(messageText)
        val url = if (cleanDigits.isNotBlank()) {
            "https://wa.me/$cleanDigits?text=$encodedMessage"
        } else {
            "https://api.whatsapp.com/send?text=$encodedMessage"
        }

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            // Prioritize WhatsApp
            `package` = "com.whatsapp"
        }

        try {
            context.startActivity(intent)
        } catch (_: Exception) {
            // Fallback to any app or browser
            val fallback = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            try {
                context.startActivity(fallback)
            } catch (e: Exception) {
                Toast.makeText(context, "No se encontró aplicación de WhatsApp ni navegador", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Opens WhatsApp with the script message pre-loaded directly
    fun openWhatsAppWithMessage(message: String, templateTitle: String? = null) {
        val context = getApplication<Application>()
        if (message.isNotBlank()) {
            recordCustomerInteraction(
                phone = "WhatsApp General",
                name = "Difusión rápida",
                type = if (templateTitle != null) CustomerInteractionEntity.TYPE_TEMPLATE_SENT else CustomerInteractionEntity.TYPE_WHATSAPP_MESSAGE,
                content = message,
                templateTitle = templateTitle
            )
        }
        val encodedMessage = Uri.encode(message)
        val whatsappUri = Uri.parse("whatsapp://send?text=$encodedMessage")
        val webUri = Uri.parse("https://wa.me/?text=$encodedMessage")

        val whatsappIntent = Intent(Intent.ACTION_VIEW, whatsappUri).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            `package` = "com.whatsapp"
        }

        try {
            context.startActivity(whatsappIntent)
        } catch (_: Exception) {
            // Fallback to WhatsApp Business
            val w4bIntent = Intent(Intent.ACTION_VIEW, whatsappUri).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                `package` = "com.whatsapp.w4b"
            }
            try {
                context.startActivity(w4bIntent)
            } catch (_: Exception) {
                // Fallback to Web URL
                val webIntent = Intent(Intent.ACTION_VIEW, webUri).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                try {
                    context.startActivity(webIntent)
                } catch (e: Exception) {
                    Toast.makeText(context, "No se encontró aplicación de WhatsApp ni navegador", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Overlay controls
    fun toggleInAppBubble() {
        _isInAppBubbleVisible.value = !_isInAppBubbleVisible.value
    }

    fun setInAppBubbleVisible(visible: Boolean) {
        _isInAppBubbleVisible.value = visible
    }

    fun canDrawOverlays(): Boolean {
        val context = getApplication<Application>()
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(context)
        } else {
            true
        }
    }

    fun openOverlaySettings() {
        val context = getApplication<Application>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:${context.packageName}")
            ).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            try {
                context.startActivity(intent)
            } catch (_: Exception) {
                val genericIntent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(genericIntent)
            }
        }
    }

    fun toggleSystemOverlayService() {
        val context = getApplication<Application>()
        if (!canDrawOverlays()) {
            openOverlaySettings()
            Toast.makeText(context, "Por favor habilita 'Mostrar sobre otras aplicaciones'", Toast.LENGTH_LONG).show()
            return
        }

        if (FloatingOverlayService.isServiceActive.value) {
            FloatingOverlayService.stop(context)
            Toast.makeText(context, "Burbuja de WhatsApp desactivada", Toast.LENGTH_SHORT).show()
        } else {
            FloatingOverlayService.start(context)
            Toast.makeText(context, "⚡ Burbuja flotante activa sobre WhatsApp", Toast.LENGTH_SHORT).show()
        }
    }

    // Battery Optimization & OEM Autostart (Xiaomi / Samsung / Huawei / Oppo)
    fun isBatteryOptimizationIgnored(): Boolean {
        return com.example.util.DeviceOptimizationHelper.isBatteryOptimizationIgnored(getApplication())
    }

    fun requestBatteryOptimizationBypass() {
        com.example.util.DeviceOptimizationHelper.requestBatteryOptimizationBypass(getApplication())
    }

    fun openAutostartMenu() {
        com.example.util.DeviceOptimizationHelper.openAutostartSettings(getApplication())
    }

    // User Session & Quick Access Auth State
    private val _userSession = MutableStateFlow(
        UserSession(
            isLoggedIn = true,
            displayName = "Agente CyberCRM",
            emailOrPhone = "+58 412-0000000",
            authProvider = "whatsapp"
        )
    )
    val userSession: StateFlow<UserSession> = _userSession.asStateFlow()

    fun loginWithGoogle(accountName: String = "Agente Google CRM", email: String = "ventas@cybercrm.io") {
        _userSession.value = UserSession(
            isLoggedIn = true,
            displayName = accountName,
            emailOrPhone = email,
            authProvider = "google"
        )
        Toast.makeText(getApplication(), "⚡ Acceso exitoso con Google", Toast.LENGTH_SHORT).show()
    }

    fun loginWithPhone(phone: String) {
        _userSession.value = UserSession(
            isLoggedIn = true,
            displayName = "Agente WhatsApp",
            emailOrPhone = phone,
            authProvider = "whatsapp"
        )
        Toast.makeText(getApplication(), "⚡ Acceso exitoso con $phone", Toast.LENGTH_SHORT).show()
    }

    fun logout() {
        _userSession.value = UserSession(
            isLoggedIn = false,
            displayName = "Invitado",
            emailOrPhone = "",
            authProvider = "guest"
        )
        Toast.makeText(getApplication(), "Sesión cerrada", Toast.LENGTH_SHORT).show()
    }
}

data class UserSession(
    val userId: String = "usr_agente_001",
    val isLoggedIn: Boolean = true,
    val displayName: String = "Agente CyberCRM",
    val emailOrPhone: String = "+58 412-0000000",
    val authProvider: String = "whatsapp"
)
