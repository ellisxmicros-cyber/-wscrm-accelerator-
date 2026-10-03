package com.example.data

import kotlinx.coroutines.flow.Flow

class CrmRepository(
    private val templateDao: TemplateDao,
    private val leadDao: LeadDao,
    private val interactionDao: CustomerInteractionDao
) {
    // Templates
    val allTemplates: Flow<List<TemplateEntity>> = templateDao.getAllTemplates()
    val pinnedTemplates: Flow<List<TemplateEntity>> = templateDao.getPinnedTemplates()

    fun searchTemplates(query: String): Flow<List<TemplateEntity>> {
        return if (query.isBlank()) {
            templateDao.getAllTemplates()
        } else {
            templateDao.searchTemplates(query.trim())
        }
    }

    suspend fun insertTemplate(template: TemplateEntity): Long {
        return templateDao.insertTemplate(template)
    }

    suspend fun updateTemplate(template: TemplateEntity) {
        templateDao.updateTemplate(template)
    }

    suspend fun deleteTemplate(template: TemplateEntity) {
        templateDao.deleteTemplate(template)
    }

    suspend fun incrementTemplateUsage(id: Long) {
        templateDao.incrementUsage(id)
    }

    // Leads
    val allLeads: Flow<List<LeadEntity>> = leadDao.getAllLeads()

    fun getLeadsByStage(stage: LeadStage): Flow<List<LeadEntity>> {
        return leadDao.getLeadsByStage(stage)
    }

    fun searchLeads(query: String): Flow<List<LeadEntity>> {
        return if (query.isBlank()) {
            leadDao.getAllLeads()
        } else {
            leadDao.searchLeads(query.trim())
        }
    }

    suspend fun insertLead(lead: LeadEntity): Long {
        return leadDao.insertLead(lead)
    }

    suspend fun updateLead(lead: LeadEntity) {
        leadDao.updateLead(lead)
    }

    suspend fun deleteLead(lead: LeadEntity) {
        leadDao.deleteLead(lead)
    }

    suspend fun updateLeadStage(id: Long, stage: LeadStage) {
        leadDao.updateStage(id, stage, System.currentTimeMillis())
    }

    // Customer Interaction History
    val allInteractions: Flow<List<CustomerInteractionEntity>> = interactionDao.getAllInteractions()

    fun getInteractionsByPhone(phone: String): Flow<List<CustomerInteractionEntity>> {
        return interactionDao.getInteractionsByPhone(phone)
    }

    fun getInteractionsByLead(leadId: Long): Flow<List<CustomerInteractionEntity>> {
        return interactionDao.getInteractionsByLead(leadId)
    }

    suspend fun recordInteraction(interaction: CustomerInteractionEntity): Long {
        return interactionDao.insert(interaction)
    }

    suspend fun deleteInteraction(id: Long) {
        interactionDao.deleteById(id)
    }

    suspend fun clearAllInteractions() {
        interactionDao.clearAll()
    }
}
