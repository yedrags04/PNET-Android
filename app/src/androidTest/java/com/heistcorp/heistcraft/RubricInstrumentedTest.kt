package com.heistcorp.heistcraft

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Tests UI mínimos alineados con la rúbrica del Hito 3 (objetivos claros por pantalla).
 */
@RunWith(AndroidJUnit4::class)
class RubricInstrumentedTest {

    @get:Rule val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun topBar_muestra_marca_heistcraft() {
        composeRule.onNodeWithText("HeistCraft", substring = true).assertIsDisplayed()
    }

    @Test
    fun inicio_muestra_establecimiento_y_desarrollador() {
        composeRule.onNodeWithText("HeistCraft", substring = true).assertIsDisplayed()
        composeRule.onNodeWithText("Yedra", substring = true).assertIsDisplayed()
    }

    @Test
    fun bottomBar_muestra_inicio_bancos_utensilios() {
        composeRule.onNodeWithText("Inicio").assertIsDisplayed()
        composeRule.onNodeWithText("Bancos").assertIsDisplayed()
        composeRule.onNodeWithText("Utensilios").assertIsDisplayed()
    }

    @Test
    fun drawer_abre_y_muestra_salas() {
        composeRule.onNodeWithContentDescription("Abrir menú lateral").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithText("Salas").assertIsDisplayed()
    }

    @Test
    fun drawer_muestra_reservas_y_tutorial() {
        composeRule.onNodeWithContentDescription("Abrir menú lateral").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithText("Reservas").assertIsDisplayed()
        composeRule.onNodeWithText("Tutorial").assertIsDisplayed()
    }
}
