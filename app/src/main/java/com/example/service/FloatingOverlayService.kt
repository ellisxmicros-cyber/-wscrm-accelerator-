package com.example.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.R
import com.example.data.AppDatabase
import com.example.data.TemplateEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FloatingOverlayService : Service() {

    private var windowManager: WindowManager? = null
    private var bubbleView: View? = null
    private var hudPanelView: View? = null

    private lateinit var bubbleParams: WindowManager.LayoutParams
    private lateinit var hudParams: WindowManager.LayoutParams

    private val serviceScope = CoroutineScope(Dispatchers.Main + Job())
    private var templatesList = listOf<TemplateEntity>()
    private var currentFilter = "Todos"

    companion object {
        private const val CHANNEL_ID = "wscrm_floating_overlay"
        private const val NOTIFICATION_ID = 1001

        private val _isServiceActive = MutableStateFlow(false)
        val isServiceActive = _isServiceActive.asStateFlow()

        fun start(context: Context) {
            val intent = Intent(context, FloatingOverlayService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun stop(context: Context) {
            val intent = Intent(context, FloatingOverlayService::class.java)
            context.stopService(intent)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        _isServiceActive.value = true
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, buildForegroundNotification())

        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        initViews()
        loadTemplates()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    private fun loadTemplates() {
        serviceScope.launch {
            val db = AppDatabase.getInstance(applicationContext)
            db.templateDao().getAllTemplates().collectLatest { list ->
                templatesList = list
                updateHudTemplates()
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "WS Companion Overlay",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Burbuja flotante activa sobre WhatsApp y otras apps"
                setShowBadge(false)
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun buildForegroundNotification(): Notification {
        val launchIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            launchIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("WS Companion Activo")
            .setContentText("Burbuja flotante de respuestas rápidas sobre WhatsApp")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initViews() {
        val density = resources.displayMetrics.density
        val bubbleSizePx = (58 * density).toInt()

        val layoutFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            @Suppress("DEPRECATION")
            WindowManager.LayoutParams.TYPE_PHONE
        }

        bubbleParams = WindowManager.LayoutParams(
            bubbleSizePx,
            bubbleSizePx,
            layoutFlag,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = resources.displayMetrics.widthPixels - bubbleSizePx - 20
            y = (resources.displayMetrics.heightPixels * 0.35f).toInt()
        }

        // Create bubble view
        val bubble = FrameLayout(this).apply {
            val bg = GradientDrawable().apply {
                shape = GradientDrawable.OVAL
                setColor(Color.parseColor("#0A0A0E"))
                setStroke((2.5f * density).toInt(), Color.parseColor("#39FF14"))
            }
            background = bg

            val iconText = TextView(this@FloatingOverlayService).apply {
                text = "⚡"
                textSize = 24f
                setTextColor(Color.parseColor("#39FF14"))
                gravity = Gravity.CENTER
            }
            addView(
                iconText,
                FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    Gravity.CENTER
                )
            )
        }

        // Draggable bubble logic
        bubble.setOnTouchListener(object : View.OnTouchListener {
            private var initialX = 0
            private var initialY = 0
            private var initialTouchX = 0f
            private var initialTouchY = 0f
            private var isDragging = false

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        initialX = bubbleParams.x
                        initialY = bubbleParams.y
                        initialTouchX = event.rawX
                        initialTouchY = event.rawY
                        isDragging = false
                        return true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val dx = (event.rawX - initialTouchX).toInt()
                        val dy = (event.rawY - initialTouchY).toInt()
                        if (Math.abs(dx) > 10 || Math.abs(dy) > 10) {
                            isDragging = true
                            bubbleParams.x = initialX + dx
                            bubbleParams.y = initialY + dy
                            windowManager?.updateViewLayout(bubble, bubbleParams)
                        }
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        if (!isDragging) {
                            toggleHudPanel()
                        }
                        return true
                    }
                }
                return false
            }
        })

        bubbleView = bubble
        try {
            windowManager?.addView(bubble, bubbleParams)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Build HUD Panel
        val panelWidth = (340 * density).toInt().coerceAtMost((resources.displayMetrics.widthPixels * 0.92f).toInt())
        val panelHeight = (460 * density).toInt().coerceAtMost((resources.displayMetrics.heightPixels * 0.7f).toInt())

        hudParams = WindowManager.LayoutParams(
            panelWidth,
            panelHeight,
            layoutFlag,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.CENTER
        }

        hudPanelView = createHudView(density)
    }

    private fun createHudView(density: Float): View {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            val bg = GradientDrawable().apply {
                setColor(Color.parseColor("#0E0E16"))
                cornerRadius = 16 * density
                setStroke((1.5f * density).toInt(), Color.parseColor("#00E5FF"))
            }
            background = bg
            setPadding((14 * density).toInt(), (12 * density).toInt(), (14 * density).toInt(), (12 * density).toInt())
        }

        // Title Bar
        val titleBar = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }

        val titleTv = TextView(this).apply {
            text = "⚡ WS COMPANION"
            textSize = 15f
            setTextColor(Color.parseColor("#00E5FF"))
            typeface = android.graphics.Typeface.MONOSPACE
            layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        }
        titleBar.addView(titleTv)

        val minimizeBtn = TextView(this).apply {
            text = "─"
            textSize = 18f
            setTextColor(Color.parseColor("#8E8EA8"))
            setPadding((10 * density).toInt(), (4 * density).toInt(), (10 * density).toInt(), (4 * density).toInt())
            setOnClickListener { hideHudPanel() }
        }
        titleBar.addView(minimizeBtn)

        val closeBtn = TextView(this).apply {
            text = "✕"
            textSize = 18f
            setTextColor(Color.parseColor("#FF3366"))
            setPadding((10 * density).toInt(), (4 * density).toInt(), (10 * density).toInt(), (4 * density).toInt())
            setOnClickListener { stopSelf() }
        }
        titleBar.addView(closeBtn)

        root.addView(titleBar)

        // Subtitle & WhatsApp Direct Action
        val subBar = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(0, (6 * density).toInt(), 0, (8 * density).toInt())
        }

        val subText = TextView(this).apply {
            text = "PLANTILLAS RÁPIDAS"
            textSize = 11f
            setTextColor(Color.parseColor("#8E8EA8"))
            typeface = android.graphics.Typeface.MONOSPACE
            layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        }
        subBar.addView(subText)

        val openWaBtn = TextView(this).apply {
            text = "Abrir WhatsApp"
            textSize = 11f
            setTextColor(Color.parseColor("#39FF14"))
            val waBg = GradientDrawable().apply {
                setColor(Color.parseColor("#152D12"))
                cornerRadius = 8 * density
                setStroke(1, Color.parseColor("#39FF14"))
            }
            background = waBg
            setPadding((8 * density).toInt(), (4 * density).toInt(), (8 * density).toInt(), (4 * density).toInt())
            setOnClickListener {
                openWhatsApp()
            }
        }
        subBar.addView(openWaBtn)

        root.addView(subBar)

        // Category Filter Chips ScrollView
        val chipScroll = HorizontalScrollView(this).apply {
            isHorizontalScrollBarEnabled = false
            overScrollMode = View.OVER_SCROLL_NEVER
        }
        val chipContainer = LinearLayout(this).apply {
            id = View.generateViewId()
            tag = "chipContainer"
            orientation = LinearLayout.HORIZONTAL
            setPadding(0, 0, 0, (8 * density).toInt())
        }
        chipScroll.addView(chipContainer)
        root.addView(chipScroll)

        // Scrollable Templates Container
        val scrollView = ScrollView(this).apply {
            isVerticalScrollBarEnabled = true
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        }

        val listContainer = LinearLayout(this).apply {
            id = View.generateViewId()
            tag = "templatesContainer"
            orientation = LinearLayout.VERTICAL
        }
        scrollView.addView(listContainer)
        root.addView(scrollView)

        return root
    }

    private fun updateHudTemplates() {
        val panel = hudPanelView ?: return
        val listContainer = panel.findViewWithTag<LinearLayout>("templatesContainer") ?: return
        val chipContainer = panel.findViewWithTag<LinearLayout>("chipContainer") ?: return
        val density = resources.displayMetrics.density

        // Setup filter categories
        val categories = mutableListOf("Todos")
        templatesList.forEach { if (!categories.contains(it.category)) categories.add(it.category) }

        chipContainer.removeAllViews()
        for (cat in categories) {
            val isSelected = cat == currentFilter
            val chip = TextView(this).apply {
                text = cat
                textSize = 11f
                typeface = android.graphics.Typeface.MONOSPACE
                setTextColor(if (isSelected) Color.parseColor("#0A0A0E") else Color.parseColor("#00E5FF"))
                val chipBg = GradientDrawable().apply {
                    setColor(if (isSelected) Color.parseColor("#00E5FF") else Color.parseColor("#161622"))
                    cornerRadius = 12 * density
                    setStroke(1, Color.parseColor("#00E5FF"))
                }
                background = chipBg
                setPadding((10 * density).toInt(), (4 * density).toInt(), (10 * density).toInt(), (4 * density).toInt())
                val lp = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 0, (6 * density).toInt(), 0)
                }
                layoutParams = lp
                setOnClickListener {
                    currentFilter = cat
                    updateHudTemplates()
                }
            }
            chipContainer.addView(chip)
        }

        // Filter templates
        val filtered = if (currentFilter == "Todos") {
            templatesList
        } else {
            templatesList.filter { it.category == currentFilter }
        }

        listContainer.removeAllViews()

        if (filtered.isEmpty()) {
            val emptyTv = TextView(this).apply {
                text = "No hay plantillas en esta categoría"
                setTextColor(Color.parseColor("#8E8EA8"))
                textSize = 12f
                gravity = Gravity.CENTER
                setPadding(0, (20 * density).toInt(), 0, (20 * density).toInt())
            }
            listContainer.addView(emptyTv)
            return
        }

        for (template in filtered) {
            val card = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                val cardBg = GradientDrawable().apply {
                    setColor(Color.parseColor("#161622"))
                    cornerRadius = 10 * density
                    setStroke(1, Color.parseColor("#2A2A3D"))
                }
                background = cardBg
                setPadding((12 * density).toInt(), (10 * density).toInt(), (12 * density).toInt(), (10 * density).toInt())
                val lp = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 0, 0, (8 * density).toInt())
                }
                layoutParams = lp
            }

            // Card Header
            val headerRow = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
            }

            val titleTv = TextView(this).apply {
                text = template.title
                textSize = 13f
                setTextColor(Color.WHITE)
                typeface = android.graphics.Typeface.DEFAULT_BOLD
                layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            }
            headerRow.addView(titleTv)

            val copyBtn = TextView(this).apply {
                text = "⚡ COPIAR"
                textSize = 11f
                setTextColor(Color.parseColor("#00E5FF"))
                typeface = android.graphics.Typeface.MONOSPACE
                val copyBg = GradientDrawable().apply {
                    setColor(Color.parseColor("#0E2A32"))
                    cornerRadius = 6 * density
                    setStroke(1, Color.parseColor("#00E5FF"))
                }
                background = copyBg
                setPadding((10 * density).toInt(), (4 * density).toInt(), (10 * density).toInt(), (4 * density).toInt())
                setOnClickListener {
                    copyToClipboard(template)
                }
            }
            headerRow.addView(copyBtn)

            card.addView(headerRow)

            // Content preview
            val contentTv = TextView(this).apply {
                text = template.content
                textSize = 11.5f
                setTextColor(Color.parseColor("#A0A0B8"))
                maxLines = 3
                ellipsize = android.text.TextUtils.TruncateAt.END
                setPadding(0, (6 * density).toInt(), 0, 0)
            }
            card.addView(contentTv)

            listContainer.addView(card)
        }
    }

    private fun copyToClipboard(template: TemplateEntity) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(template.title, template.content)
        clipboard.setPrimaryClip(clip)

        // Haptic feedback
        try {
            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(40, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(40)
            }
        } catch (_: Exception) {}

        Toast.makeText(this, "⚡ Copiado: ${template.title}", Toast.LENGTH_SHORT).show()

        // Track usage in database
        serviceScope.launch {
            withContext(Dispatchers.IO) {
                AppDatabase.getInstance(applicationContext).templateDao().incrementUsage(template.id)
            }
        }

        // Auto-minimize HUD so user can paste into WhatsApp directly
        hideHudPanel()
    }

    private fun openWhatsApp() {
        val launchIntent = packageManager.getLaunchIntentForPackage("com.whatsapp")
            ?: packageManager.getLaunchIntentForPackage("com.whatsapp.w4b")
            ?: Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com"))

        launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(launchIntent)
        } catch (e: Exception) {
            Toast.makeText(this, "No se pudo abrir WhatsApp", Toast.LENGTH_SHORT).show()
        }
    }

    private var isHudVisible = false

    private fun toggleHudPanel() {
        if (isHudVisible) {
            hideHudPanel()
        } else {
            showHudPanel()
        }
    }

    private fun showHudPanel() {
        if (isHudVisible || hudPanelView == null) return
        try {
            windowManager?.addView(hudPanelView, hudParams)
            isHudVisible = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun hideHudPanel() {
        if (!isHudVisible || hudPanelView == null) return
        try {
            windowManager?.removeView(hudPanelView)
            isHudVisible = false
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _isServiceActive.value = false
        hideHudPanel()
        bubbleView?.let {
            try {
                windowManager?.removeView(it)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        bubbleView = null
        hudPanelView = null
    }
}
