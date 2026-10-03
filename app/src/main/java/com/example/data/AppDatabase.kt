package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Converters {
    @TypeConverter
    fun fromLeadStage(value: LeadStage): String = value.name

    @TypeConverter
    fun toLeadStage(value: String): LeadStage = try {
        LeadStage.valueOf(value)
    } catch (_: Exception) {
        LeadStage.NEW
    }
}

@Database(
    entities = [TemplateEntity::class, LeadEntity::class, CustomerInteractionEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun templateDao(): TemplateDao
    abstract fun leadDao(): LeadDao
    abstract fun interactionDao(): CustomerInteractionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "wscrm_database.db"
                )
                .fallbackToDestructiveMigration()
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            INSTANCE?.let { seedInitialData(it) }
                        }
                    }
                }).build()
                INSTANCE = instance
                instance
            }
        }

        suspend fun seedInitialData(database: AppDatabase) {
            val templateDao = database.templateDao()
            val leadDao = database.leadDao()

            if (templateDao.getCount() == 0) {
                templateDao.insertAll(
                    listOf(
                        TemplateEntity(
                            title = "⚡ Datos de Pago",
                            content = "*DATOS PARA TRANSFERIR*\n\n• Banco: Banesco\n• Pago Móvil: 0412-0000000\n• Cedula: V-12345678",
                            category = "Pagos",
                            shortcut = "/pago",
                            isPinned = true,
                            usageCount = 19
                        ),
                        TemplateEntity(
                            title = "📌 Ubicación de la Tienda",
                            content = "Estamos ubicados en Centro Comercial El Castillo, La Hoyada. ¡Te esperamos!",
                            category = "Info",
                            shortcut = "/ubicacion",
                            isPinned = true,
                            usageCount = 8
                        ),
                        TemplateEntity(
                            title = "⚡ Lista de Precios",
                            content = "📌 *NUESTROS PRECIOS DE HOY*\n\n• Opción A: \$10 USD\n• Opción B: \$20 USD\n• Opción Pro: \$35 USD\n\n💳 *Aceptamos Pago Móvil / Binance / Zelle*",
                            category = "Precios",
                            shortcut = "/precios",
                            isPinned = true,
                            usageCount = 14
                        ),
                        TemplateEntity(
                            title = "👋 Saludo & Bienvenida",
                            content = "¡Hola {nombre}! 👋 Gracias por comunicarte con nosotros.\n\n¿En qué producto o servicio te gustaría recibir información y asesoría hoy?",
                            category = "Saludos",
                            shortcut = "/hola",
                            isPinned = false,
                            usageCount = 12
                        ),
                        TemplateEntity(
                            title = "📦 Envíos & Delivery",
                            content = "🛵 *SERVICIO DE ENVÍOS*\n\n• Delivery Express en la ciudad: 30 a 45 minutos (\$2.00 a \$3.50)\n• Envíos Nacionales: MRW, Zoom, Tealca (Cobro a destino)\n• Retiro en Tienda: Sin costo adicional",
                            category = "Envíos",
                            shortcut = "/envios",
                            isPinned = false,
                            usageCount = 6
                        ),
                        TemplateEntity(
                            title = "🎯 Cierre de Venta Rápido",
                            content = "¡Excelente elección {nombre}! 🚀\n¿Deseas que preparemos tu pedido de {producto} para despacho hoy mismo? Indícanos tu método de pago preferido para apartarlo.",
                            category = "Cierre",
                            shortcut = "/cierre",
                            isPinned = false,
                            usageCount = 9
                        ),
                        TemplateEntity(
                            title = "🎁 Descuento Exclusivo",
                            content = "🔥 *OFERTA ESPECIAL FLASH*\n\nPor concretar tu compra en los próximos 60 minutos, te obsequiamos un 10% de descuento directo en tu orden de {producto}. ¡Aprovecha!",
                            category = "Promos",
                            shortcut = "/promo",
                            isPinned = false,
                            usageCount = 5
                        )
                    )
                )
            }

            if (leadDao.getCount() == 0) {
                leadDao.insertAll(
                    listOf(
                        LeadEntity(
                            name = "Carlos Mendoza",
                            phone = "+584121234567",
                            stage = LeadStage.QUOTED,
                            productInterest = "Opción B (\$20)",
                            estimatedValue = 20.0,
                            notes = "Preguntó por garantía y delivery al centro",
                            lastContactedAt = System.currentTimeMillis() - 1000 * 60 * 30
                        ),
                        LeadEntity(
                            name = "Mariana Ramos",
                            phone = "+584149876543",
                            stage = LeadStage.CONTACTED,
                            productInterest = "Opción Pro (\$35)",
                            estimatedValue = 35.0,
                            notes = "Vio la publicidad en Instagram",
                            lastContactedAt = System.currentTimeMillis() - 1000 * 60 * 120
                        ),
                        LeadEntity(
                            name = "Roberto Silva",
                            phone = "+584245551234",
                            stage = LeadStage.CLOSED_WON,
                            productInterest = "Opción A x 2 (\$20)",
                            estimatedValue = 20.0,
                            notes = "Pagó por Binance Pay, pedido despachado",
                            lastContactedAt = System.currentTimeMillis() - 1000 * 60 * 360
                        ),
                        LeadEntity(
                            name = "Elena Gómez",
                            phone = "+584163334455",
                            stage = LeadStage.NEW,
                            productInterest = "Consulta general",
                            estimatedValue = 0.0,
                            notes = "Envió mensaje preguntando horario",
                            lastContactedAt = System.currentTimeMillis() - 1000 * 60 * 15
                        )
                    )
                )
            }

            val interactionDao = database.interactionDao()
            if (interactionDao.getCount() == 0) {
                interactionDao.insertAll(
                    listOf(
                        CustomerInteractionEntity(
                            customerPhone = "+584121234567",
                            customerName = "Carlos Mendoza",
                            interactionType = CustomerInteractionEntity.TYPE_TEMPLATE_SENT,
                            templateTitle = "⚡ Lista de Precios",
                            content = "Enviada lista de precios Opción B (\$20 USD). Cliente evaluando delivery.",
                            timestamp = System.currentTimeMillis() - 1000 * 60 * 35
                        ),
                        CustomerInteractionEntity(
                            customerPhone = "+584245551234",
                            customerName = "Roberto Silva",
                            interactionType = CustomerInteractionEntity.TYPE_STAGE_CHANGE,
                            templateTitle = "⚡ Datos de Pago",
                            content = "Etapa actualizada a PAGADO / GANADO. Pago verificado por Binance Pay.",
                            timestamp = System.currentTimeMillis() - 1000 * 60 * 180
                        ),
                        CustomerInteractionEntity(
                            customerPhone = "+584149876543",
                            customerName = "Mariana Ramos",
                            interactionType = CustomerInteractionEntity.TYPE_WHATSAPP_MESSAGE,
                            templateTitle = "👋 Saludo & Bienvenida",
                            content = "Contacto inicial desde WhatsApp. Interesada en plan Opción Pro.",
                            timestamp = System.currentTimeMillis() - 1000 * 60 * 320
                        )
                    )
                )
            }
        }
    }
}
