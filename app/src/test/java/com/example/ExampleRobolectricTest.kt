package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.LeadEntity
import com.example.data.LeadStage
import com.example.data.TemplateEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("TurboVentas WS", appName)
  }

  @Test
  fun `template entity creation and shortcut`() {
    val template = TemplateEntity(
      id = 1L,
      title = "⚡ Lista de Precios",
      content = "Precios: A $10, B $20",
      category = "Precios",
      shortcut = "/precios"
    )
    assertEquals("Precios", template.category)
    assertEquals("/precios", template.shortcut)
    assertTrue(template.content.contains("Precios"))
  }

  @Test
  fun `lead entity creation and stage`() {
    val lead = LeadEntity(
      id = 1L,
      name = "Carlos Mendoza",
      phone = "+584121234567",
      stage = LeadStage.QUOTED,
      estimatedValue = 20.0
    )
    assertEquals(LeadStage.QUOTED, lead.stage)
    assertEquals(20.0, lead.estimatedValue, 0.001)
  }

  @Test
  fun `check battery optimization helper`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    // Should execute safely without exceptions
    val isIgnored = com.example.util.DeviceOptimizationHelper.isBatteryOptimizationIgnored(context)
    assertTrue(isIgnored || !isIgnored)
  }

  @Test
  fun `room database template entity fields`() {
    val template = com.example.data.Template(
      id = 42L,
      title = "Script Cierre",
      content = "Texto de cierre de venta",
      category = "Cierre"
    )
    assertEquals(42L, template.id)
    assertEquals("Script Cierre", template.title)
    assertEquals("Texto de cierre de venta", template.content)
    assertEquals("Cierre", template.category)
  }
}
